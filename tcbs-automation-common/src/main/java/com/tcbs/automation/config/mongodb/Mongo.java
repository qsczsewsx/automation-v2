package com.tcbs.automation.config.mongodb;

import com.mongodb.MongoClient;

public enum Mongo {
  DYNAMIC_WATCH_LIST("dynamic-watch-list"),
  WARNNING_ANATTA("warning"),
  COCO("coco");

  private String configName;

  Mongo(String configName) {
    this.configName = configName;
  }

  public MongoClient newClient() {
    return MongoConfig.getMongoClient(this.configName);
  }
}