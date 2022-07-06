package com.tcbs.automation.config.fundtrading;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class FundTradingConf {
  private static final Config conf = new ConfigImpl("fundtrading").getConf();
  public static final String API_TOOL_INTERNAL = conf.getString("api-internal");
  public static final String API_TOOL_EXTERNAL = conf.getString("api-external");
  public static final String API_PRICING_INTERNAL = conf.getString("pricing-in");
  public static final String API_PRICING_EXTERNAL = conf.getString("pricing-ex");

  // fund
  public static final String FT_BUY = conf.getString("fund.orders");
  public static final String FT_MATCHEDDATE = conf.getString("fund.matchedDate");
  public static final String FT_RELOAD = conf.getString("fund.reload-cache");
  public static final String FT_PROCESSDRAFT = conf.getString("fund.process-draft");
  public static final String FT_REBALANCE = conf.getString("fund.rebalance");
  public static final String FT_CHANGEPRODUCT = conf.getString("fund.changeproduct");
  public static final String FT_MULTIPLEORDER = conf.getString("fund.multiple");
  public static final String FT_RMORDERS = conf.getString("fund.rmorder");
  public static final String FT_UPDATEORDER = conf.getString("fund.updateorder");
  public static final String FT_CANCELORDER = conf.getString("fund.cancelorder");
  public static final String FT_STOPSIPORDER = conf.getString("fund.stopsiporder");
  public static final String FT_RMCHANGEPRODUCT = conf.getString("fund.rmchangeproduct");
  public static final String FT_RMCANCELORDER = conf.getString("fund.rmcancelorder");
  public static final String FT_RMMULTIPLEORDER = conf.getString("fund.rmmultipleorder");
  public static final String FT_RMGETORDERS = conf.getString("fund.rmgetorder");
  public static final String FT_RMGETSIP = conf.getString("fund.rmgetsip");
  public static final String FT_GETORDERS = conf.getString("fund.getorder");
  public static final String FT_GETSIP = conf.getString("fund.getsip");
  public static final String FT_ALLOCATIONORDER = conf.getString("fund.allocation");
  public static final String FT_RMALLOCATIONORDER = conf.getString("fund.rmallocation");
  public static final String FT_CHANGEDATE = conf.getString("fund.changedate");
  public static final String FT_CANCELGROUPORDER = conf.getString("fund.cancelgrouporder");
  public static final String FT_RMCANCELGROUPORDER = conf.getString("fund.rmcancelgrouporder");
  public static final String FT_UPDATEBYGROUPORDERID = conf.getString("fund.updatebygrouporderid");

  //pricing
  public static final String FT_CALCULATESELLING = conf.getString("pricing.calculateSellingQuantity");

}
