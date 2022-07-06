package com.tcbs.automation.config.idatafutureinsight;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class FutureInsightConfig {
  private static final Config conf = new ConfigImpl("idatafutureinsight").getConf();


  public static final String COMMON_X_API_KEY = conf.getString("futureinsight.x-api-key");

  // iData - Intraday
  public static final String FUTURE_DOMAIN_URL = conf.getString("futureinsight.intraday.intradayUrlV1");
  public static final String FUTURE_PRICE_AND_VOLUME_URL = conf.getString("futureinsight.intraday.listChart.priceVolumeUrl");
  public static final String FUTURE_PRICE_AND_VOLUME_1_MINUTE_URL = conf.getString("futureinsight.intraday.listChart.priceVolume1MinuteUrl");
  public static final String FUTURE_SUPPLY_DEMAND_URL = conf.getString("futureinsight.intraday.listChart.supplyDemandUrl");
  public static final String FUTURE_SUPPLY_DEMAND_EXT_URL = conf.getString("futureinsight.intraday.listChart.supplyDemandExtUrl");
  public static final String FUTURE_SUPPLY_DEMAND_MONTH_URL = conf.getString("futureinsight.intraday.listChart.supplyDemandMonthUrl");
  public static final String FUTURE_BID_ASK_URL = conf.getString("futureinsight.intraday.listChart.bidAskUrl");
  public static final String FUTURE_INTRADAY_HISTORY_PAGING_URL = conf.getString("futureinsight.intraday.listChart.historyPagingUrl");
  public static final String FUTURE_INVESTOR_HISTORY_PAGING_URL = conf.getString("futureinsight.intraday.listChart.investorHisPagingUrl");
  public static final String FUTURE_MATCHED_VOLUME_BY_PRICE_URL = conf.getString("futureinsight.intraday.listChart.matchVolByPriceUrl");
  public static final String FUTURE_VOLUME_URL = conf.getString("futureinsight.intraday.listChart.volumeUrl");
  public static final String FUTURE_INVESTOR_URL = conf.getString("futureinsight.intraday.listChart.investorUrl");
  public static final String FUTURE_ACC_INVESTOR_URL = conf.getString("futureinsight.intraday.listChart.investorAccUrl");
  public static final String FUTURE_MATCH_VOL_URL = conf.getString("futureinsight.intraday.listChart.matchVolUrl");
  public static final String FUTURE_BID_ASK_SPREAD_URL = conf.getString("futureinsight.intraday.listChart.bidAsksSpreadUrl");
  public static final String FUTURE_VN30_DEVIATION_URL = conf.getString("futureinsight.intraday.listChart.futureVN30Url");
  public static final String FUTURE_INTRADAY_PNL_URL = conf.getString("futureinsight.pnl.intradayPnlUrl");

  // candle bar
  public static final String FUTURE_LONG_TERM_CANDLE_BAR_URL = conf.getString("futureinsight.candleBar.longTermCandleBarUrl");
  public static final String FUTURE_SHORT_TERM_CANDLE_BAR_URL = conf.getString("futureinsight.candleBar.shortTermCandleBarUrl");

}
