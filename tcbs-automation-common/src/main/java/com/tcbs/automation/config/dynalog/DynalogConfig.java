package com.tcbs.automation.config.dynalog;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class DynalogConfig {
  private static final Config conf = new ConfigImpl("dynalog").getConf();
  public static final String TRADING_DYNALOG_URL = conf.getString("tradingDynalog.url");
  public static final String TRADING_DYNALOG_DYNALOG = conf.getString("tradingDynalog.dynalog");
  public static final String TRADING_DYNALOG_PERFORM = conf.getString("tradingDynalog.perform");
  public static final String TRADING_DYNALOG_QUERY = conf.getString("tradingDynalog.query");
  public static final String TRADING_DYNALOG_POLICY = conf.getString("tradingDynalog.policy");
  public static final String OTP_API_KEY = conf.getString("otp.api-key");
  public static final String BOND_VALIDATE_TRADINGDATE = conf.getString("ibond.validate");
  public static final String BOND_VALIDATE_MAX_TRADINGDATE = conf.getString("ibond.validate-MaxTradingDate");
  public static final String BOND_VALIDATE_MINTRADINGAMOUNT = conf.getString("ibond.validate-minTradingAmt");
  public static final String BOND_VALIDATE_POPRIMARY_AMOUNT = conf.getString("ibond.validate-poprimary_amount");
  public static final String BOND_VALIDATE_CUSTOMER = conf.getString("ibond.validate-customer");
  public static final String BOND_VALIDATE_PRODUCT_ONLINE = conf.getString("ibond.validate-product_online");
  public static final String BOND_VALIDATE_PRODUCT_LOCK = conf.getString("ibond.validate-product_lock");
  public static final String BOND_VALIDATE_RM = conf.getString("ibond.validate-rm");
  public static final String BOND_VALIDATE_COUPON = conf.getString("ibond.validate-coupon");
  public static final String BOND_COUPON = conf.getString("ibond.coupon");
  public static final String ICONNECT_VALIDATE_MONEY_FLEX = conf.getString("ibond.validate_money_flex");
  public static final String BOND_VALIDATE_PO_SOCAP = conf.getString("ibond.validate_po_socap");
  public static final String PLACE_ORDER_BOND_API_X_KEY = conf.getString("api-key");
  public static final String BOND_PRODUCTS = conf.getString("ibond.products");


}
