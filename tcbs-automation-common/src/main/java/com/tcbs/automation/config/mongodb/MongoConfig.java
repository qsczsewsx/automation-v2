package com.tcbs.automation.config.mongodb;

import com.mongodb.MongoClient;
import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class MongoConfig {
  private static final Config conf = new ConfigImpl("mongodb").getConf();

  //RabbitMQ
  public static MongoClient getMongoClient(String serviceName) {
    if (serviceName.contains("warning") || serviceName.contains("coco")) {
      return ClientFactory.newClient(
        conf.getString(String.format("mongodb.%s.endpoint", serviceName)),
        conf.getString(String.format("mongodb.%s.username", serviceName)),
        conf.getString(String.format("mongodb.%s.password", serviceName))
      );
    }
    return ClientFactory.newClient(
      conf.getString(String.format("mongodb.%s.endpoint", serviceName)),
      conf.getString(String.format("mongodb.%s.username", serviceName)),
      conf.getString(String.format("mongodb.%s.password", serviceName)),
      conf.getString(String.format("mongodb.%s.trustStore", serviceName)),
      conf.getString(String.format("mongodb.%s.trustStorePassword", serviceName)),
      conf.getString(String.format("mongodb.%s.readPreference", serviceName)),
      conf.getString(String.format("mongodb.%s.retryWrites", serviceName))
    );
  }
}
