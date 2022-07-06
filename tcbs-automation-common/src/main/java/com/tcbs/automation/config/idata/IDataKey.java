package com.tcbs.automation.config.idata;

public class IDataKey {
  /*===============================DWH T SERVICE===============================*/
  public static final String INBOX_GET_BY_TCBS_ID = "INBOX_GET_BY_TCBS_ID";
  public static final String STAGING_CUS_BIRTHDAY = "customer birthday";
  public static final String BANK_DWH_TOKENS = "BANK_DWH_TOKENS";
  public static final String IDATA_COMMON_LANGUAGE = "vi,en";
  public static final String IDATA_COMMON_SPEED = "1,5";

  // intraday
  public static final String INTRADAY_PRICE_VOLUME_RESOLUTION = "5,15,30,60,1440";
  public static final String INTRADAY_PRICE_VOLUME_DATA = "INTRADAY_PRICE_VOLUME_DATA";
  public static final String INTRADAY_BID_ASK_DATA = "INTRADAY_BID_ASK_DATA";
  public static final String INTRADAY_INVESTOR_CLASSIFIER_WSIZE = "1d,1w,1M";
  public static final String INTRADAY_CASH_FLOW_WSIZE = "1d,1w,1M,1y";
  public static final String INTRADAY_NDT_TYPE = ",all,sheep,wolf,shark";
  public static final String INTRADAY_BSA_EXT_RESOLUTION = "5,15,30";

  // candleBar
  public static final String INTRADAY_SHORT_TERM_RESOLUTION = "1,5,10,15,30,60";
  public static final String INTRADAY_LONG_TERM_RESOLUTION = "D,W,M";
  public static final String INTRADAY_HORIZONTAL_VOL_WSIZE = "1d,1w,1M,3M,1y,5y,all";

  // api response
  public static final String DATA_FROM_API = "DATA_FROM_API";

  public static final String IDATA_COMMON_RRG_TYPE = "1M,3M,1y";


  public static final String DATA_FROM_PORTFOLIO_API = "DATA_FROM_PORTFOLIO_API";

}
