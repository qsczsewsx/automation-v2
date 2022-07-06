package com.tcbs.automation.config.perfngin;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class PerfNginConfig {
  private static final Config conf = new ConfigImpl("perfngin").getConf();
  //
  public static final String PES_DOMAIN = conf.getString("perf_ngin.domain");
  public static final String PES_DOMAIN_BACK_URL = conf.getString("perf_ngin.backUrl");
  public static final String PES_API_KEY = conf.getString("perf_ngin.x-api-key");
  // Perf
  public static final String PES_SUMUP_VALID_DATA = conf.getString("perf_ngin.trans_sum_valid_data");
  public static final String PES_SUMUP_TRANS_4FUN_STATION = conf.getString("perf_ngin.trans_sum_4fun_station");
  public static final String PES_SUMUP_MAKE_PORTFOLIO = conf.getString("perf_ngin.make_portf");
  public static final String PES_SUMUP_GET_PRICE_MARKET = conf.getString("perf_ngin.get_price_market");
  public static final String PES_SUMUP_CALC_PERFORMANCE = conf.getString("perf_ngin.calc_perf");
  public static final String PES_SUMUP_TAKE_PORTF_SS = conf.getString("perf_ngin.take_portf_snapshot");
  public static final String PES_SUMUP_TAKE_PERF_SS = conf.getString("perf_ngin.take_perf_snapshot");
  public static final String PES_SUMUP_TAKE_INDIES_SS = conf.getString("perf_ngin.take_indies_snapshot");
  public static final String PERF_CONTRACT_HISTORY_URL = conf.getString("perf_ngin.getContractHistoryCustomerUrl");
  public static final String PERF_STATEMENT_GENERAL_URL = conf.getString("perf_ngin.getStatementGeneralUrl");

  public static final String PES_GET_PERF_HIS_URL = conf.getString("perf_ngin.getPerfHistoryUrl");

  // Indices
  public static final String PES_INDICATORS_GET_INDICATORS = conf.getString("perf_ngin.get_indicators");

  // 3rd party
  public static final String PES_3RD_GET_PERF_HIS_URL = conf.getString("perf_ngin.3rd.getPerfHistoryUrl");
  public static final String PES_3RD_X_API_KEY = conf.getString("perf_ngin.3rd.x-api-key");

  //I wealth club
  public static final String PUBLIC_STATUS_ENABLE_URL = conf.getString("perf_ngin.i_wealth_club.publicStatusEnableUrl");
  public static final String PUBLIC_STATUS_DISABLE_URL = conf.getString("perf_ngin.i_wealth_club.publicStatusDisableUrl");
  public static final String PUBLIC_STATUS_CHECK_URL = conf.getString("perf_ngin.i_wealth_club.publicStatusCheckUrl");
  public static final String SHARE_PERFORMANCE_PNL_URL = conf.getString("perf_ngin.i_wealth_club.sharePerformanceUrl");
  public static final String SHARE_PERFORMANCE_RETURN_URL = conf.getString("perf_ngin.i_wealth_club.sharePerformanceReturnUrl");
  public static final String SHARE_PERFORMANCE_RISK_URL = conf.getString("perf_ngin.i_wealth_club.sharePerformanceRiskUrl");
  public static final String SHARE_PERFORMANCE_HISTORY_URL = conf.getString("perf_ngin.i_wealth_club.sharePerformanceHistoryUrl");

}
