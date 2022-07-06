package com.tcbs.automation.config.dynamicwatchlist;

import java.util.concurrent.ThreadLocalRandom;

public class RememberKeyConstant {
  public static final String ACC_105C310885 = "105C098985";
  public static final String TCBSID_0001310885 = "0001310885";
  public static final String WATCHLIST_NAME = "WL0001";
  public static final String CODE_366006 = "366006";
  public static final String MESSAGE_366006 = "Data does not exist->record not found";
  public static final String CODE_366001 = "366001";
  public static final String CODE_777103 = "777103";
  public static final String MESSAGE_366001 = "invalid format->parameter `id` is invalid value ";
  public static final String SYMBOL = "TUNGKT";
  public static final String CREATED_DYNAMIC_WATCHLIST = "CREATED_DYNAMIC_WATCHLIST";
  public static final String EDITED_DYNAMIC_WATCHLIST = "EDITED_DYNAMIC_WATCHLIST";
  public static final String DELETED_DYNAMIC_WATCHLIST = "DELETED_DYNAMIC_WATCHLIST";
  public static final String PREVIEW_DYNAMIC_WATCHLIST = "PREVIEW_DYNAMIC_WATCHLIST";
  public static final String GET_DATA_BY_TICKER_WATCHLIST = "GET_DATA_BY_TICKER_WATCHLIST";
  public static final String EXPORT_DATA_WATCHLIST = "EXPORT_DATA_WATCHLIST";
  public static final String ALL_DYNAMIC_WATCHLIST = "ALL_DYNAMIC_WATCHLIST";
  public static final String ALL_USER_SETTING_OPTIONS = "ALL_USER_SETTING_OPTIONS";
  public static final String USER_SETTING_OPTIONS_RESPONSE = "USER_SETTING_OPTIONS_RESPONSE";
  public static final String USER_DETAIL_WATCHLIST = "USER_DETAIL_WATCHLIST";
  public static final String ADD_TICKER_TO_POSEIDON = "ADD_TICKER_TO_POSEIDON";
  public static final String COUNT_TICKERS_RESP = "COUNT_TICKERS_RESP";
  public static final String GET_CHART_RESP = "COUNT_TICKERS_RESP";
  public static String symbolRandom() {
    return "TKT" + String.format("%07d", ThreadLocalRandom.current().nextInt(100000, 9000000 + 1));
  }
}