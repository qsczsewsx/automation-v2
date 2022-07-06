package com.tcbs.automation.coco.mongodb.manager;

import com.mongodb.MongoClient;
import com.tcbs.automation.coco.mongodb.model.CustomerRatingSummary;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import static com.tcbs.automation.config.coco.CocoServiceConfig.MONGO_DB_NAME;

public class CustomerRatingSummaryImpl implements CustomerRatingSummaryManager {
  private Datastore database;

  public CustomerRatingSummaryImpl(MongoClient client) {
    Morphia morphia = new Morphia();
    this.database = morphia.createDatastore(client, MONGO_DB_NAME);
  }

  @Override
  public CustomerRatingSummary findOneTraderByTcbsId(String tcbsId) {
    return this.database.createQuery(CustomerRatingSummary.class)
      .field("tcbsID")
      .equal(tcbsId)
      .get();
  }
}

