package com.tcbs.automation.config.idatafutureinsight;

public class FutureInsightKey {

  // intraday
  public static final String FUTURE_PRICE_VOLUME_RESOLUTION = "5,15,30,60,1440";
  public static final String FUTURE_PRICE_VOLUME_DATA = "INTRADAY_PRICE_VOLUME_DATA";
  public static final String INTRADAY_BID_ASK_DATA = "INTRADAY_BID_ASK_DATA";
  public static final String INTRADAY_INVESTOR_CLASSIFIER_WSIZE = "1d,1w,1M";
  public static final String INTRADAY_CASH_FLOW_WSIZE = "1d,1w,1M,1y";
  public static final String ACC_VOL = "accVol";
  public static final String FOREIGN_VOL = "foreignVol";
  public static final String RATE_VOL = "volRate";

  // candleBar
  public static final String FUTURE_INTRADAY_SHORT_TERM_RESOLUTION = "1,5,10,15,30,60";
  public static final String FUTURE_INTRADAY_LONG_TERM_RESOLUTION = "D,W,M";
  public static final String INTRADAY_HORIZONTAL_VOL_WSIZE = "1d,1w,1M,3M,1y,5y,all";

  public static final Integer INTRADAY_BUY_SELL_PRICEBOARD_NUM = 8;

  public static final String DATA_FROM_API = "DATA_FROM_API";
  public static final String DATA_FROM_DB = "DATA_FROM_DB";
  public static final String INTRADAY_VN30_FUTURE = "1d,1M";
}
