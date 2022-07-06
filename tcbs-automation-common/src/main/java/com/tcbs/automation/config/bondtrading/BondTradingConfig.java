package com.tcbs.automation.config.bondtrading;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class BondTradingConfig {
  private static final Config conf = new ConfigImpl("bondtrading").getConf();

  public static final String X_API_KEY = conf.getString("bond-trading.x-api-key");
  public static final String BOND_CATEGORIES = conf.getString("bond-trading.categories");
  public static final String BOND_ORDERS = conf.getString("bond-trading.orders");
  public static final String BOND_CD_ORDERS = conf.getString("bond-trading.cd-orders");
  public static final String BOND_ORDERS_V2 = conf.getString("bond-trading.orders-v2");
  public static final String BOND_BOOKINGS = conf.getString("bond-trading.bookings");
  public static final String BOND_CUSTOMERS = conf.getString("bond-trading.customers");
  public static final String BOND_RMS = conf.getString("bond-trading.rms");
  public static final String BOND_ISSUERS = conf.getString("bond-trading.issuers");
  public static final String BOND_BONDS = conf.getString("bond-trading.bonds");
  public static final String BOND_PRODUCTS = conf.getString("bond-trading.products");
  public static final String BOND_TRADING_SUGGEST = conf.getString("bond-trading.suggest");
  public static final String BOND_CACHES = conf.getString("bond-trading.caches");
  public static final String VALIDATION_PLACE_ORDER = conf.getString("bond-trading.validation");
  public static final String BOND_ASSETS = conf.getString("bond-trading.assets");
}
