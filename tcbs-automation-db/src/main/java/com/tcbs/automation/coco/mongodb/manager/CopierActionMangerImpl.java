package com.tcbs.automation.coco.mongodb.manager;

import com.mongodb.MongoClient;
import com.tcbs.automation.coco.mongodb.model.CopierActionInfo;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Sort;

import java.util.List;

import static com.tcbs.automation.config.coco.CocoServiceConfig.MONGO_DB_NAME;

public class CopierActionMangerImpl implements CopierActionManger {
  private Datastore database;

  public CopierActionMangerImpl(MongoClient client) {
    Morphia morphia = new Morphia();
    this.database = morphia.createDatastore(client, MONGO_DB_NAME);
  }

  @Override
  public List<CopierActionInfo> findByTcbsIdOrderByActionDateDesc(String tcbsId, int page, int size) {
    return this.database.createQuery(CopierActionInfo.class)
      .field("tcbsId")
      .equal(tcbsId)
      .field("parentActionType")
      .doesNotExist()
      .order(Sort.descending("actionDate"))
      .offset(page * size)
      .limit(size)
      .asList();
  }
}
