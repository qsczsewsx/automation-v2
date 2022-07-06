package com.tcbs.automation.config.xobni;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class XobniConfig {
  public static final String AUTHEN_ACTION = "authenticate";
  public static final String SUBSCRIBE_ACTION = "subscribe";
  public static final String UNSUBSCRIBE_ACTION = "unsubscribe";
  private static final Config conf = new ConfigImpl("xobni").getConf();
  //XOBNI
  public static final String XOBNI_SOCKET_ENTRY_POINT = conf.getString("xobni.socket-entry-point");
  public static final String XOBNI_SOCKET_INTERNAL = conf.getString("xobni.socket-internal");
  public static final String XOBNI_SERVICE_DOMAIN = conf.getString("xobni.domain");
  public static final String XOBNI_SERVICE_UNICAST_SUBPATH = conf.getString("xobni.sub-paths.unicast");
  public static final String XOBNI_SERVICE_TOPICS_SUBPATH = conf.getString("xobni.sub-paths.topics");
}
