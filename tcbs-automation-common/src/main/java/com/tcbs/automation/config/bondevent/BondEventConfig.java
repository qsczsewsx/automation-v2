package com.tcbs.automation.config.bondevent;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class BondEventConfig {
  private static final Config conf = new ConfigImpl("bondevent").getConf();
  public static final String SYNCS_TRANSACTION = conf.getString("bond-event.syncs-transaction");
  public static final String VIEW_HISTORY = conf.getString("bond-event.view-history");
  public static final String VIEW_HISTORY_CUSTOMER = conf.getString("bond-event.view-history-customers");
  public static final String VIEW_HISTORY_RM = conf.getString("bond-event.view-history-rms");
  public static final String SEND_EVENTS = conf.getString("bond-event.events");
}
