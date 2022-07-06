package com.tcbs.automation.config.tcbstrading;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class TcbsTradingConfig {
  private static final Config conf = new ConfigImpl("tcbstrading").getConf();

  public static final String TRADING_BFF_CUST_PLACE_BOND_ORDER = conf.getString("trading.bondBff.custPlaceOrderFullPath");
  public static final String TRADING_COMBINATION_ORDERS = conf.getString("trading.bondBff.rmsCombinationOrders");
  public static final String TRADING_NORMALIZED_ORDERS = conf.getString("trading.bondBff.normalizedOrders");
  public static final String CONFIRM_NORMALIZED_ORDERS = conf.getString("trading.bondBff.confirmNormalizedOrders");
  public static final String PLACE_ORDER_BOND_API_X_KEY = conf.getString("trading.api-key");
  public static final String BFF_BOND_FOR_105P = conf.getString("trading.bondBff.for105p");
  public static final String OTP_AUTHEN_TRADING = conf.getString("trading.otp");

  public static final String PLACE_PATH = conf.getString("trading.place");
  public static final String CONFIRM_PATH = conf.getString("trading.confirm");
  public static final String SUBMIT_BULK_PATH = conf.getString("trading.submit_bulk");
  public static final String DOMAIN = conf.getString("trading.datapower");
  public static final String TRADINGS_PATH = conf.getString("trading.basePath");
  public static final String V1 = conf.getString("trading.version");
}
