package com.tcbs.automation.config.bpmcomposite;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class BpmCompositeServiceConfig {

  private static final Config conf = new ConfigImpl("bpmcomposite").getConf();
  public static final String PORTFOLIO_BASE_URL = conf.getString("portfolio.baseUrl");
  public static final String PORTFOLIO_TOKEN = conf.getString("portfolio.x-api-key");
  public static final String PORTFOLIO_GET_BOND_BALANCE_DATA = conf.getString("portfolio.getBondBalanceReport");
  public static final String PRODUCT_BASE_URL = conf.getString("product.baseUrl");
  public static final String PRODUCT_TOKEN = conf.getString("product.x-api-key");
  public static final String PRODUCT_GET_BOND_DETAIL = conf.getString("product.getBondDetail");
  public static final String BPM_COMPOSITE_BASE_URL = conf.getString("bpm-composite.baseUrl");
  public static final String BPM_COMPOSITE_TOKEN = conf.getString("bpm-composite.x-api-key");
  public static final String BPM_COMPOSITE_BBR_GET_REPORT = conf.getString("bpm-composite.getBondBalanceReportUrl");

  private BpmCompositeServiceConfig() {

  }

}
