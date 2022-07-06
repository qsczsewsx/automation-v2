package com.tcbs.automation.config.wrapper;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class WrapperConfig {
  private static final Config conf = new ConfigImpl("wrapper").getConf();

  public static final String DOMAIN = conf.getString("wrapper.domain");
  public static final String PRODUCT_BOND = conf.getString("wrapper.product");
  public static final String BY_CODE = conf.getString("wrapper.bycode");

}
