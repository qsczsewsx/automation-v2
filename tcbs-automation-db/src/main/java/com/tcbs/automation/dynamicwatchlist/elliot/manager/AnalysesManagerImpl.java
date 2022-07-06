package com.tcbs.automation.dynamicwatchlist.elliot.manager;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.tcbs.automation.dynamicwatchlist.elliot.model.Analyses;
import com.tcbs.automation.dynamicwatchlist.elliot.model.Chart;
import com.tcbs.automation.dynamicwatchlist.elliot.model.CountTickers;
import org.bson.Document;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Projections.computed;
import static com.mongodb.client.model.Projections.fields;

import java.util.*;

public class AnalysesManagerImpl implements AnalysesManager {
  private Datastore database;
  private MongoCollection<Document> collection;

  public AnalysesManagerImpl(MongoClient client, String databaseName) {
    Morphia morphia = new Morphia();
    this.database = morphia.createDatastore(client, databaseName);
    this.collection = client.getDatabase(databaseName).getCollection("stock_statistics");
  }

  @Override
  public void insertAnalyses(Analyses info) {
    this.database.save(info);
  }

  @Override
  public Analyses getAnalysesByTicker(String ticker) throws Exception {
    List<Analyses> res = this.database.createQuery(Analyses.class).field("ticker").equal(ticker).asList();
    if (res == null || res.size() != 1) {
      throw new Exception("database must have only one document for each ticker");
    }
    return res.get(0);
  }

  @Override
  public void deleteAnalysesByTicker(String ticker) {
    this.database.findAndDelete(this.database.createQuery(Analyses.class).field("ticker").equal(ticker));
  }

  @Override
  public List<Chart> getChartData(String indicator, Double max, Double min, Integer numberOfColumn) {
    List<Chart> result = new ArrayList<>();

    String condition = String.format("{ $subtract: [{ $multiply: [ '$%s', %d ] }, { $mod: [ { $multiply: [ '$%s',  %d] }, %f ] }]  }",indicator,numberOfColumn,
      indicator,numberOfColumn,(max - min) / numberOfColumn * 100);
    MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(
      match(Filters.ne(indicator,null)),
      project(fields(computed("valueBucket",Document.parse(condition)))),
      group("$valueBucket", Accumulators.sum("count",1)),
      sort(new BasicDBObject("_id",1))
    )).iterator();
    while (cursor.hasNext()) {
      Document doc = cursor.next();
      Chart chart = Chart.builder().valueBucKet(doc.getDouble("_id")/100).totalTicker(doc.getInteger("count")).build();
      result.add(chart);
    }
    return result;
  }

  @Override
  public Double getMinMax(String indicator, String type) {
    Document doc = collection.aggregate(Collections.singletonList(
      group(null, Accumulators.max("value", "$" + indicator))
    )).first();
    if (type.equals("MIN")) {
      doc = collection.aggregate(Collections.singletonList(
        group(null, Accumulators.min("value", "$" + indicator))
      )).first();
    }
    return Double.parseDouble(doc.get("value").toString()) ;
  }

  @Override
  public CountTickers getCount(String indicator,String transformIndicator, Double max, Double min) {
    Document doc = collection.aggregate(Arrays.asList(
      match(Filters.and(Filters.gte(transformIndicator,min),Filters.lte(transformIndicator,max),
        Filters.in("exchangeName",Arrays.asList("HOSE","HNX","UPCOM")))),
      count("total")
    )).first();
    Integer count = doc.getInteger("total");
    return CountTickers.builder().indicator(indicator).total(count).build();
  }
}
