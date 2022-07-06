package com.tcbs.automation.config.timeline;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class Timeline {
  private static final Config conf = new ConfigImpl("timeline").getConf();
  public static final String IP_TIMELINE = conf.getString("timeline.ip_timeline");
  public static final String DOMAIN = conf.getString("timeline.domain");
  public static final String EMIT_EVENTS = conf.getString("timeline.emit_events");
  public static final String V1_JOBS = conf.getString("timeline.v1_jobs");
  public static final String COMBO_DEFS = conf.getString("timeline.combo_defs");
  public static final String DEFS = conf.getString("timeline.defs");
  public static final String EVENTS = conf.getString("timeline.events");
  public static final String BOTTOM_DELETE = conf.getString("timeline.bottom_delete");
}
