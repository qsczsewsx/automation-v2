package com.tcbs.automation.config.flexops;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class FlexopsConfig {
  private static final Config conf = new ConfigImpl("flexops").getConf();

  public static final String IMPORT_SEC_BASKET_URL = conf.getString("flexops.import-sec-basket-url");
  public static final String INSERT_BASKET_URL = conf.getString("flexops.create-basket-url");

}
