package com.tcbs.automation.config.stockgate;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class StockgateConfigs {
  public static final Config conf = new ConfigImpl("stockgate").getConf();

  // Stock Gate
  public static final String STOCK_GATE_DOMAIN = conf.getString("stockgate.domain");
  public static final String STOCK_GATE_V1 = conf.getString("stockgate.v1");
  public static final String MARKET_ORDERS = conf.getString("stockgate.market_orders");
  public static final String ACTIVATE = conf.getString("stockgate.activate");
  public static final String EDIT = conf.getString("stockgate.edit");
  public static final String UPDATE_PERIODICALLY = conf.getString("stockgate.update_periodically");
  public static final String OTP_AUTHEN = conf.getString("stockgate.otp");

}
