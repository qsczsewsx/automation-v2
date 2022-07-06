package com.tcbs.automation.config.devmana;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class DevmanaConfig {

  private static final Config conf = new ConfigImpl("devmana").getConf();

  public static final String DEVMANA_UNREGISTER_PATH = conf.getString("devmana.un-register");
  public static final String DEVMANA_EXTERNAL_ENDPOINT = conf.getString("devmana.external");
  public static final String DEVMANA_INTERNAL_ENDPOINT = conf.getString("devmana.internal");
  public static final String DEVMANA_REGISTER_PATH = conf.getString("devmana.register");
  public static final String DEVMANA_FIREBASE_GROUP = conf.getString("devmana.firebase.group");
  public static final String DEVMANA_FIREBASE_TOKEN = conf.getString("devmana.firebase.token");
  public static final String DEVMANA_UNICAST_PATH = conf.getString("devmana.unicast");
  public static final String DEVMANA_MULTICAST_PATH = conf.getString("devmana.multicast");
  public static final String DEVMANA_BROADCAST_PATH = conf.getString("devmana.broadcast");
  public static final String DEVMANA_CHECK_PATH = conf.getString("devmana.check-register");
  public static final String DEVMANA_HISTORY_MESSAGE_PATH = conf.getString("devmana.message-history");
  public static final String DEVMANA_REGISTER_INFO_PATH = conf.getString("devmana.register-info");
  public static final String DEVMANA_RETRY_REGISTER_PATH = conf.getString("devmana.retry-register");
  public static final String DEVMANA_SUBSCRIBE_PATH = conf.getString("devmana.subscribe");
  public static final String DEVMANA_UNSUBSCRIBE_PATH = conf.getString("devmana.unsubscribe");
  public static final String DEVMANA_RESEND_PATH = conf.getString("devmana.resend");
  public static final String X_API_KEY = conf.getString("devmana.xApiKey");
  public static final String DEVMANA_APITOOL = conf.getString("devmana.apitool");
  public static final String DEVMANA_UNREGISTERALL = conf.getString("devmana.unregisterall");
  public static final String DEVMANA_EXTERNAL_REGISTER = conf.getString("devmana.external-register");
  public static final String DEVMANA_EXTERNAL_UNREGISTER = conf.getString("devmana.external-unregister");
}
