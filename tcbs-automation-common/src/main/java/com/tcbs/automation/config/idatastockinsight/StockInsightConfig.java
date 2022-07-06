package com.tcbs.automation.config.idatastockinsight;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class StockInsightConfig {
  private static final Config conf = new ConfigImpl("idatastockinsight").getConf();


  public static final String COMMON_X_API_KEY = conf.getString("stockinsight.x-api-key");

  // iData - Intraday
  public static final String INTRADAY_DOMAIN_URL = conf.getString("stockinsight.intraday.domain");
  public static final String INTRADAY_PRICE_AND_VOLUME_URL = conf.getString("stockinsight.intraday.intraday.priceVolumeUrl");
  public static final String INTRADAY_PRICE_AND_VOLUME_1_MINUTE_URL = conf.getString("stockinsight.intraday.intraday.priceVolume1MinuteUrl");
  public static final String INTRADAY_TRADING_STATE_URL = conf.getString("stockinsight.intraday.intraday.tradingStateUrl");
  public static final String INTRADAY_SUPPLY_DEMAND_URL = conf.getString("stockinsight.intraday.intraday.supplyDemandUrl");
  public static final String INTRADAY_SUPPLY_DEMAND_EXT_URL = conf.getString("stockinsight.intraday.intraday.supplyDemandExtUrl");
  public static final String INTRADAY_SUPPLY_DEMAND_MONTH_URL = conf.getString("stockinsight.intraday.intraday.supplyDemandMonthUrl");
  public static final String INTRADAY_BID_ASK_URL = conf.getString("stockinsight.intraday.intraday.bidAskUrl");
  public static final String INTRADAY_HISTORY_PAGING_URL = conf.getString("stockinsight.intraday.intraday.historyPagingUrl");
  public static final String CASHFLOW_CARD_URL = conf.getString("stockinsight.intraday.intraday.cashflow");
  public static final String INTRADAY_INVESTOR_HISTORY_PAGING_URL = conf.getString("stockinsight.intraday.intraday.investorHisPagingUrl");
  public static final String INTRADAY_MATCHED_VOLUME_BY_PRICE_URL = conf.getString("stockinsight.intraday.intraday.matchVolByPriceUrl");


  public static final String INTRADAY_VOLUME_URL = conf.getString("stockinsight.intraday.intraday.volumeUrl");
  public static final String SNAPSHOT_INDEX_URL = conf.getString("stockinsight.intraday.pricing.ssIndexUrl");
  public static final String SNAPSHOT_TICKER_URL = conf.getString("stockinsight.intraday.pricing.ssTickerUrl");
  public static final String BUYSELL_TICKER_URL = conf.getString("stockinsight.intraday.pricing.bsTickerUrl");
  public static final String INTRADY_BID_ASK_URL = conf.getString("stockinsight.intraday.intraday.bidAskUrl");
  public static final String INTRADAY_INVESTOR_URL = conf.getString("stockinsight.intraday.intraday.investorUrl");
  public static final String ACC_INVESTOR_URL = conf.getString("stockinsight.intraday.intraday.investorAccUrl");
  public static final String INTRADAY_HORIZONTAL_VOL_URL = conf.getString("stockinsight.intraday.intraday.horizontalVolUrl");

  public static final String INTRADAY_MATCH_VOL_URL = conf.getString("stockinsight.intraday.intraday.matchVolUrl");

  // TransInfo
  public static final String TRANSINFO_FORECAST_VOL_URL = conf.getString("stockinsight.intraday.intraday.transinfoForecastVolUrl");
  public static final String TRANSINFO_TRADING_URL = conf.getString("stockinsight.intraday.intraday.transinfoTradingUrl");
  public static final String NET_VALUE_BUY_SELL_FOEEIGN_URL = conf.getString("stockinsight.intraday.intraday.netvaluebuysellforeign");

  // Indicator
  public static final String INTRADAY_INDICATOR_URL = conf.getString("stockinsight.intraday.intraday.indicatorUrl");
  public static final String DAILY_INDICATOR_URL = conf.getString("stockinsight.intraday.intraday.indicatorDailyUrl");
  public static final String DAILY_TCP_URL = conf.getString("stockinsight.alchemist.alchemist.updateTcpDailyUrl");
  public static final String INTRADAY_TCP_URL = conf.getString("stockinsight.alchemist.alchemist.updateTcpMinutelyUrl");
  public static final String INTRADAY_DW_URL = conf.getString("stockinsight.intraday.intraday.updateDwMinutelyUrl");
  public static final String CW_BACKTEST = conf.getString("stockinsight.intraday.intraday.cwBacktesting");
  public static final String CW_PRICE_STATE = conf.getString("stockinsight.intraday.intraday.cwPriceState");


  // candle bar
  public static final String LONG_TERM_CANDLE_BAR_URL = conf.getString("stockinsight.intraday.longTermCandleBarUrl");
  public static final String SHORT_TERM_CANDLE_BAR_URL = conf.getString("stockinsight.intraday.shortTermCandleBarUrl");

  // intraday market movement

  public static final String INTRADAY_MARKET_VALUE_URL = conf.getString("stockinsight.intraday.intraday.valueUrl");
  public static final String INTRADAY_MARKET_BREADTH_URL = conf.getString("stockinsight.intraday.intraday.marketBreadthUrl");
  public static final String INTRADAY_INVESTOR_CLASSIFY_URL = conf.getString("stockinsight.intraday.intraday.investorClassifyUrl");
  public static final String INTRADAY_INVESTOR_ACC_URL = conf.getString("stockinsight.intraday.intraday.investorClassifyAccUrl");
  //market top leader
  public static final String MARKET_TOP_LEADER_URL = conf.getString("stockinsight.intraday.intraday.marketTopLeaderUrl");
  public static final String MARKET_SUPPLY_DEMAND_URL = conf.getString("stockinsight.intraday.intraday.marketSupplyDemandUrl");
  public static final String MARKET_VOLATILITY_URL = conf.getString("stockinsight.intraday.intraday.marketVolatilityUrl");

  public static final String TRANS_INFO_HIS_URL = conf.getString("stockinsight.intraday.intraday.transInfoHis");

  public static final String RRG_INDUSTRY_URL = conf.getString("stockinsight.intraday.rrg.rrgIndustry");
  public static final String RRG_TICKER_URL = conf.getString("stockinsight.intraday.rrg.rrgTicker");

  public static final String MARKET_BID_ASK_URL = conf.getString("stockinsight.intraday.intraday.marketBidAskUrl");
  // =========================================================== //


  // QUEUE
  public static final String INTRADAY_QUEUE = conf.getString("stockinsight.intraday.queue");

  //thirdparty
  public static final String HIGH_LOW_PRICE_CW = conf.getString("stockinsight.intraday.intraday.highlowpriceUrl");
  public static final String CLOSE_PRICE_STOCK = conf.getString("stockinsight.intraday.intraday.closePriceStockUrl");
  public static final String SHOW_INDUSTRY_LEVEL2 = conf.getString("stockinsight.intraday.orion.showIndustryLevel2Url");
  public static final String LATEST_PRICE_URL = conf.getString("stockinsight.intraday.intraday.latestPriceUrl");

  // future
  public static final String FUTURE_GET_BETA_URL = conf.getString("stockinsight.future.betaUrl");
  public static final String FUTURE_GET_ARBITRAGE_URL = conf.getString("stockinsight.future.arbitrageUrl");

  // secondtcprice
  public static final String SECOND_TC_PRICE_URL = conf.getString("stockinsight.intraday.pricing.secondTcPriceUrl");

  // search Ticker
  public static final String SEARCH_TICKER_COMMON_URL = conf.getString("stockinsight.search.searchTickerCommonUrl");
  public static final String SEARCH_TICKER_TRADING_VIEW_URL = conf.getString("stockinsight.search.searchTickerTradingViewUrl");
  public static final String SEARCH_TICKER_ORION_CW_URL = conf.getString("stockinsight.search.orionCwUrl");
  public static final String SEARCH_TICKER_ORION_DERIVATIVES_URL = conf.getString("stockinsight.search.orionDerivativesUrl");

  // ==================================================================== //
  //saving data
  public static final String SAVING_DATA_URL = conf.getString("stockinsight.savingData.savingDataUrl");
  public static final String GET_ALL_LISTED_BOND_URL = conf.getString("stockinsight.bondOrion.getAllListedBondUrl");

  //industry index
  public static final String INDUSTRY_INDEX_URL = conf.getString("stockinsight.industryIndex.getIndustryIndexUrl");

  public static final String MARKET_FOREIGN_VOL_URL = conf.getString("stockinsight.intraday.intraday.marketForeignVolUrl");

  //flow map
  public static final String INTRADAY_FLOW_MAP_INDUSTRY_INDEX = conf.getString("stockinsight.intraday.flowMap.industryIndexUrl");
  public static final String INTRADAY_FLOW_MAP_MARKET_VALUE_PERCENT_TRADING = conf.getString("stockinsight.intraday.flowMap.marketValuePercentTradingUrl");
  public static final String INTRADAY_FLOW_MAP_BID_ASK_URL = conf.getString("stockinsight.intraday.flowMap.flowMapBidAskUrl");
  public static final String INTRADAY_FLOW_MAP_FOREIGN_VOL_URL = conf.getString("stockinsight.intraday.flowMap.flowForeignVolUrl");
  public static final String INTRADAY_FLOW_MAP_INDICATOR_URL = conf.getString("stockinsight.intraday.flowMap.flowIndicatorUrl");
  public static final String INTRADAY_FLOW_MAP_SUPPLY_DEMAND = conf.getString("stockinsight.intraday.flowMap.supplyDemandUrl");
  public static final String INTRADAY_FLOW_MAP_MARKET_BREADTH = conf.getString("stockinsight.intraday.flowMap.marketBreadthUrl");
  public static final String INTRADAY_FLOW_MAP_INVESTOR_CLASSIFY = conf.getString("stockinsight.intraday.flowMap.investorClassifyUrl");
  public static final String INTRADAY_FLOW_MAP_INVESTOR_CLASSIFY_ACC = conf.getString("stockinsight.intraday.flowMap.investorClassifyAccUrl");
  public static final String INTRADAY_FLOW_MAP_MARKET_TOP_LEADER = conf.getString("stockinsight.intraday.flowMap.marketTopLeaderUrl");
  public static final String INTRADAY_FLOW_MAP_MARKET_VALUE = conf.getString("stockinsight.intraday.flowMap.marketValueUrl");
}
