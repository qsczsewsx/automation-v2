package com.tcbs.automation.config.portfolio;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class PortfolioConfig {
  private static final Config conf = new ConfigImpl("portfolio").getConf();

  //Portfolio
  public static final String PORTFOLIO_ORDER_QUEUE = conf.getString("portfolio.order-queue");
  public static final String PORTFOLIO_DOMAIN = conf.getString("portfolio.domain");
  public static final String PORTFOLIO_TCBS_ORDER_BACK_API_INT_SIT = conf.getString("portfolio.tcbs_order_back_apiintsit");
  public static final String PORTFOLIO_HOLIDAY_MARKET = conf.getString("portfolio.holiday_market");
  public static final String PORTFOLIO_HOLIDAY = conf.getString("portfolio.holiday");
  public static final String PORTFOLIO_RETAIL_AFM = conf.getString("portfolio.sub-paths.retail_afm");
  public static final String PORTFOLIO_V2 = conf.getString("portfolio.v2");
  public static final String PORTFOLIO_V1 = conf.getString("portfolio.v1");
  public static final String PORTFOLIO_SNAPSHOT = conf.getString("portfolio.snapshot");
  public static final String PORTFOLIO_SUBPATHS_GET_ACCOUNTINGS = conf.getString("portfolio.sub-paths.get-accountings");
  public static final String PORTFOLIO_SUBPATHS_GET_ACCOUNTS = conf.getString("portfolio.sub-paths.accounts");
  public static final String PORTFOLIO_REPLY_EXCHANGE = conf.getString("portfolio.reply.exchange");
  public static final String PORTFOLIO_SUCCESSFUL_ROUTING_KEY = conf.getString("portfolio.reply.portfolio-event-order-processing-successful");
  public static final String PORTFOLIO_FAIL_ROUTING_KEY = conf.getString("portfolio.reply.portfolio-event-order-processing-failed");
  public static final String PORTFOLIO_PRODUCTS = conf.getString("portfolio.sub-paths.products");
  public static final String PORTFOLIO_AFC = conf.getString("portfolio.sub-paths.afc");
  public static final String PORTFOLIO_BONDS = conf.getString("portfolio.sub-paths.bonds");
  public static final String PORTFOLIO_VOLUME = conf.getString("portfolio.sub-paths.volume");
  public static final String PORTFOLIO_AFM = conf.getString("portfolio.sub-paths.afm");
  public static final String OTP_API_KEY = conf.getString("otp.api-key");
  public static final String PORTFOLIO_CONFIGURATION = conf.getString("portfolio.sub-paths.configuration");
  public static final String PORTFOLIO_DOMAIN_TCBS_ORDER_BACK = conf.getString("portfolio.domain_tcbs_order_back");
  public static final String PORTFOLIO_ALERT = conf.getString("portfolio.retail_abnormal");


  public static final String BAU_HISTORY_IP = conf.getString("portfolio.bau_history");

  public static final String GOODS_IP = conf.getString("portfolio.goods_afc");
  public static final String SHELF_AFC = conf.getString("portfolio.sub-paths.shelf_afc");
  public static final String RETAIL_AFM = conf.getString("portfolio.sub-paths.retail_afm");
  public static final String PORTFOLIO_SUBPATHS_RETAIL_RETAIL = conf.getString("portfolio.sub-paths.retail_retail");

  public static final String PRODUCT_DOMAIN = conf.getString("portfolio.tcbs-product");
  public static final String CD_ALL_CD = conf.getString("portfolio.all-cd");


  public static final String TCS_ASSET = conf.getString("portfolio.sub-paths.tcs_asset");
  public static final String IMPORT_ORDERS = conf.getString("portfolio.sub-paths.import_orders");

}
