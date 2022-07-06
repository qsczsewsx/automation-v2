package com.tcbs.automation.config.tradingdynalog;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class TradingDynalog {
  private static final Config conf = new ConfigImpl("tradingdynalog").getConf();

  public static final String TRADING_DYNALOG_URL = conf.getString("tradingdynalog.url");
  public static final String TRADING_DYNALOG_DYNALOG = conf.getString("tradingdynalog.dynalog");
  public static final String TRADING_DYNALOG_PERFORM = conf.getString("tradingdynalog.perform");
  public static final String TRADING_DYNALOG_QUERY = conf.getString("tradingdynalog.query");
  public static final String TRADING_DYNALOG_POLICY = conf.getString("tradingdynalog.policy");
  public static final String TRADING_VERSION = conf.getString("trading.version");
  //  public static final String OTP_API_KEY = conf.getString("tradingdynalog.api-key");
  public static final String TRADING_BFF_PLACE_PARTNER_ORDER = conf.getString("trading.placePartnerOrder");
}
