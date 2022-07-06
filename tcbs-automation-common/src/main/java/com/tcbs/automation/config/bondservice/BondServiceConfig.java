package com.tcbs.automation.config.bondservice;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class BondServiceConfig {
  private static final Config conf = new ConfigImpl("bondservice").getConf();
  public static final String BONDBACK_TRADING = conf.getString("services-trading");
}
