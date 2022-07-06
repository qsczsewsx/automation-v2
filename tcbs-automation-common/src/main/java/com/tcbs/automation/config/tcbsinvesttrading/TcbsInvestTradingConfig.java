package com.tcbs.automation.config.tcbsinvesttrading;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class TcbsInvestTradingConfig {
  private static final Config conf = new ConfigImpl("tcbsinvesttrading").getConf();

  public static final String INVEST_TRADING_URL = conf.getString("invest-trading.product-attribute");
  public static final String BOND_ATTRIBUTE = conf.getString("invest-trading.bond-attribute");
}
