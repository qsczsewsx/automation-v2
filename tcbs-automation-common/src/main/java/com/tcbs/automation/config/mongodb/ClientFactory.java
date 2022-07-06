package com.tcbs.automation.config.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.apache.commons.lang3.StringUtils;

public class ClientFactory {
  public static MongoClient newClient(String clusterEndpoint, String user, String password, String trustStore,
                                      String trustStorePassword, String readPreference, String retryWrites) {
    String template = "mongodb://%s:%s@%s/?ssl=true&replicaSet=rs0&readpreference=%s&retryWrites=%s";
    String connectionString = String.format(template, user, password, clusterEndpoint, readPreference, retryWrites);
    System.setProperty("javax.net.ssl.trustStore", trustStore);

    System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);
    MongoClientURI clientURI = new MongoClientURI(connectionString);
    MongoClient mongoClient = new MongoClient(clientURI);
    return mongoClient;
  }

  public static MongoClient newClient(String clusterEndpoint, String user, String password, String readPreference) {
    String template = "mongodb://%s:%s@%s/sample-database?ssl=true&replicaSet=rs0&readpreference=%s";
    String connectionString = String.format(template, user, password, clusterEndpoint, readPreference);
    MongoClientURI clientURI = new MongoClientURI(connectionString);
    MongoClient mongoClient = new MongoClient(clientURI);
    return mongoClient;
  }

  public static MongoClient newClient(String clusterEndpoint, String user, String password) {
    String connectionString;
    if(StringUtils.isEmpty(user)) {
      connectionString = String.format("mongodb://%s", clusterEndpoint);
    } else {
      connectionString = String.format("mongodb://%s:%s@%s", user, password, clusterEndpoint);
    }
    MongoClientURI clientURI = new MongoClientURI(connectionString);
    MongoClient mongoClient = new MongoClient(clientURI);
    return mongoClient;
  }
}
