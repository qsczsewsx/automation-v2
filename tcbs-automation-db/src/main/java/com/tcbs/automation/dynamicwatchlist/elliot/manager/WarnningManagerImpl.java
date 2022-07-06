package com.tcbs.automation.dynamicwatchlist.elliot.manager;

import com.mongodb.MongoClient;
import com.tcbs.automation.dynamicwatchlist.elliot.model.Warnning;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.List;

public class WarnningManagerImpl implements WarnningManager {
  private Datastore database;

  public WarnningManagerImpl(MongoClient client, String databaseName) {
    Morphia morphia = new Morphia();
    this.database = morphia.createDatastore(client, databaseName);
  }

  @Override
  public void insertWarnning(Warnning info) {
    this.database.save(info);
  }

  @Override
  public List<Warnning> getWarnningByTcbsid(String tcbsid) throws Exception {
    List<Warnning> res = this.database.createQuery(Warnning.class).field("tcbsid").equal(tcbsid).asList();
    if (res == null || res.size() == 0) {
      throw new Exception("database must have only one document for each ticker");
    }
    return res;
  }

  @Override
  public void deleteWarnningByTcbsid(String tcbsid) {
    this.database.delete(this.database.createQuery(Warnning.class).filter("tcbsid =", tcbsid));
  }
}
