package com.tcbs.automation.config.idatabiz;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class IDataBizConfig {
  private static final Config conf = new ConfigImpl("idatabiz").getConf();

  // iData - reconcile service
  public static final String IDATA_BIZ_LOGIC_X_API_KEY = conf.getString("idatabiz.api-key");
  public static final String IDATA_BIZ_LOGIC_RECONCILE_TCS_URL = conf.getString("idatabiz.url.reconcileTcsUrl");
  public static final String IDATA_BIZ_LOGIC_RECONCILE_HB_URL = conf.getString("idatabiz.url.reconcileHbUrl");
  public static final String IDATA_BIZ_LOGIC_RETRY_TCS_URL = conf.getString("idatabiz.url.retryTcsUrl");
  public static final String IDATA_BIZ_LOGIC_RETRY_HB_URL = conf.getString("idatabiz.url.retryHbUrl");

  public static final String IDATA_BIZ_LOGIC_RECONCILE_FEE_TAX_URL = conf.getString("idatabiz.url.reconcileFeeTaxUrl");
  public static final String IDATA_BIZ_LOGIC_RETRY_FEE_TAX_URL = conf.getString("idatabiz.url.retryFeeTaxUrl");

  //h2h - transfer and retry service

  public static final String H2H_X_API_KEY = conf.getString("idatabiz.h2h-api.api-key");
  public static final String H2H_GET_TRANSACTION_LIST_INQUIRY = conf.getString("idatabiz.h2h-api.inquiryUrl");
  //bond - summary and detail service

  public static final String IDATA_BIZ_LOGIC_BOND_TRANSACTION_URL = conf.getString("idatabiz.bond-api.getFeeTaxDetailUrl");


  public static final String KAFKA_USER = conf.getString("kafka.user");
  public static final String KAFKA_PASS = conf.getString("kafka.pass");
  public static final String KAFKA_GROUP = conf.getString("kafka.group");
  public static final String KAFKA_SERVER = conf.getString("kafka.server");

}
