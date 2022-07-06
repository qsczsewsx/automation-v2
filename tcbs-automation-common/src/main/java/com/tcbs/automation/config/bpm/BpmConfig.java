package com.tcbs.automation.config.bpm;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class BpmConfig {

  private static final Config conf = new ConfigImpl("bpm").getConf();

  public static final String BPM_SERVICE_DOMAIN = conf.getString("bpmservice.domain");
  public static final String BPM_TRACKING_CUSTOMER_UPDATE_IDENTIFY_CARD = conf.getString("bmpapi.trackingCustomerUpdateIdentifyCard");
}
