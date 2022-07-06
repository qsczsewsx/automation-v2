package com.tcbs.automation.config.hydra;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class HydraConfig {
  private static final Config conf = new ConfigImpl("hydra").getConf();

  public static final String DOMAIN = conf.getString("hydra.domain");
  public static final String DOMAIN_DATA_POWER = conf.getString("hydra.domainDataPower");
  public static final String HYDRA_ALLACTIVE = conf.getString("hydra.hydraAllActive");
  public static final String STOCK_ALLACTIVE = conf.getString("hydra.stockAllSecurities");
  public static final String ICOPY_WHITELIST = conf.getString("hydra.icopyWhitelist");
  public static final String STOCK_INFO = conf.getString("hydra.stockInfo");

}
