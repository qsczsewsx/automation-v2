package com.tcbs.automation.config.wso2;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class Wso2Config {
  private static final Config conf = new ConfigImpl("wso2").getConf();

  //WSO2
  public static final String WSO2_DOMAIN = conf.getString("wso2.domain");
  public static final String WSO2_SERVICES = conf.getString("wso2.services");
  public static final String WSO2_STORE_MANAGER = conf.getString("wso2.store-manager");
}
