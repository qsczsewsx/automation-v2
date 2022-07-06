package com.tcbs.automation.config.istock;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class IStockConfig {
  private static final Config conf = new ConfigImpl("istock").getConf();
  public static final String REST_API_ENDPOINT = conf.getString("istock.rest-api-endpoint");
}
