package com.tcbs.automation.config.bondfeemanagement;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class BondFeeManagementServiceConfig {
  private static final Config conf = new ConfigImpl("bondfeemanagement").getConf();

  public static final String TIMELINE_ENGINE_URL = conf.getString("timeline-engine.url");
  public static final String TIMELINE_ENGINE_TOKEN = conf.getString("timeline-engine.x-api-key");

  public static final String REFERENCE_URL = conf.getString("bond-fee-management.referenceUrl");
  public static final String BOND_GROUP_URL = conf.getString("bond-fee-management.bondGroupUrl");
  public static final String BOND_FEE_URL = conf.getString("bond-fee-management.bondFeeUrl");
  public static final String BOND_GROUP_HISTORY_URL = conf.getString("bond-fee-management.bondGroupHistoryUrl");
  public static final String GEN_BODY_TLENGINE_URL = conf.getString("bond-fee-management.genBodyTlengineUrl");
  public static final String FEE_EVENT_URL = conf.getString("bond-fee-management.feeEventUrl");
//  public static final String FEE_EVENT_URL = "http://localhost:8211/b/v1/fee-events";
  public static final String FEE_EVENT_EXTEND_DETAIL = conf.getString("bond-fee-management.feeEventExtendUrl");
  public static final String INTERNAL_URL = conf.getString("bond-fee-management.internalUrl");
}
