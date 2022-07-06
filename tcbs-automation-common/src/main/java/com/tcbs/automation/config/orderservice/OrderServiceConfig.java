package com.tcbs.automation.config.orderservice;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class OrderServiceConfig {
  private static final Config conf = new ConfigImpl("orderservice").getConf();

  public static final String ORDER_GET_BY_ID = conf.getString("orderservice.getById");
  public static final String ORDER_DOMAIN = conf.getString("orderservice.domain");
  public static final String ORDER_X_API_KEY = conf.getString("orderservice.xApiKey");
  public static final String ORDER_SERVICE_V2 = conf.getString("orderservice.version");
  public static final String SERVICE_ORDERS = conf.getString("orderservice.basePath");
  public static final String ORDER_SERVICE_BULK = conf.getString("orderservice.bulk");
  public static final String ORDER_SERVICE_SEARCH = conf.getString("orderservice.search");
  public static final String BRSW_ORDER_DOMAIN = conf.getString("brsw-orders.domain");
  public static final String ATTRIBUTES_PATH = conf.getString("orderservice.attributes");
  public static final String ORDER_SERVICE_V1 = conf.getString("orderservice.v1");
}
