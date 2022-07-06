package com.tcbs.automation.config.bondlifecycle;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class BondLifeCycleServiceConfig {
  private static final Config conf = new ConfigImpl("bondlifecycle").getConf();

  public static final String TIMELINE_ENGINE_URL = conf.getString("timeline-engine.url");
  public static final String TIMELINE_ENGINE_TOKEN = conf.getString("timeline-engine.x-api-key");

  public static final String BOND_LIFE_CYCLE_REFERENCE = conf.getString("bond-life-cycle.referenceUrl");
  public static final String BOND_LIFE_CYCLE_BUSINESS = conf.getString("bond-life-cycle.businessUrl");
  public static final String BOND_LIFE_CYCLE_BUSINESS_HISTORY = conf.getString("bond-life-cycle.businessHistoryUrl");
  public static final String BOND_LIFE_CYCLE_PARTICIPANT = conf.getString("bond-life-cycle.participantUrl");
  public static final String BOND_LIFE_CYCLE_PARTICIPANT_HISTORY = conf.getString("bond-life-cycle.participantHistoryUrl");
  public static final String BOND_LIFE_CYCLE_RULE = conf.getString("bond-life-cycle.ruleTemplateUrl");
  public static final String BOND_LIFE_CYCLE_RULE_HISTORY = conf.getString("bond-life-cycle.ruleTemplateHistoryUrl");
  public static final String BOND_LIFE_CYCLE_BOND_TIMELINE = conf.getString("bond-life-cycle.bondTimelineUrl");
  public static final String BOND_LIFE_CYCLE_BOND_TIMELINE_HISTORY = conf.getString("bond-life-cycle.bondTimelineHistoryUrl");
  public static final String GEN_BODY_TLENGINE = conf.getString("bond-life-cycle.genBodyTlengineUrl");
  public static final String BOND_LIFE_CYCLE_BOND_EVENT = conf.getString("bond-life-cycle.bondEventUrl");
  public static final String BOND_LIFE_CYCLE_BOND_EVENT_EXTEND_DETAIL = conf.getString("bond-life-cycle.bondEventExtendUrl");
  public static final String BOND_LIFE_CYCLE_BOND_TIMELINE_GROUP = conf.getString("bond-life-cycle.bondTimelineGroupUrl");
  public static final String BOND_LIFE_CYCLE_BOND_TIMELINE_GROUP_HISTORY = conf.getString("bond-life-cycle.bondTimelineGroupHistoryUrl");
  public static final String BOND_BASE_INFO = conf.getString("bond-life-cycle.bondBaseInfoUrlUrl");

}
