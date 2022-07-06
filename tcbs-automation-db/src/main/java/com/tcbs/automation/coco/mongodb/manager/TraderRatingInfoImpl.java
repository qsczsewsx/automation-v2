package com.tcbs.automation.coco.mongodb.manager;

import com.mongodb.MongoClient;
import com.tcbs.automation.coco.mongodb.model.TraderRatingInfo;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.List;

import static com.tcbs.automation.config.coco.CocoServiceConfig.MONGO_DB_NAME;

public class TraderRatingInfoImpl implements TraderRatingInfoManager {
  private Datastore database;

  public TraderRatingInfoImpl(MongoClient client) {
    Morphia morphia = new Morphia();
    this.database = morphia.createDatastore(client, MONGO_DB_NAME);
  }

  @Override
  public List<TraderRatingInfo> findByTcbsId(String tcbsId) {
    return this.database.createQuery(TraderRatingInfo.class)
      .field("tcbsID")
      .equal(tcbsId)
      .asList();
  }
}
