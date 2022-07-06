package com.tcbs.automation.config.intermana;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class IntermanaConfig {

  private static final Config conf = new ConfigImpl("intermana").getConf();

  public static final String INTERMANA_CREATE_PATH = conf.getString("intermana.create");
  public static final String INTERMANA_DOMAIN = conf.getString("intermana.domain");
  public static final String INTERMANA_GET_USER_LIST_PATH = conf.getString("intermana.user-list");
  public static final String INTERMANA_UPDATE_USER_PATH = conf.getString("intermana.update");
  public static final String INTERMANA_USER_DETAIL_PATH = conf.getString("intermana.detail");
  public static final String INTERMANA_GET_SUB_USER_LIST_PATH = conf.getString("intermana.sub-user-list");
  public static final String INTERMANA_SUB_USER_DETAIL_PATH = conf.getString("intermana.sub-detail");
  public static final String INTERMANA_UPDATE_PERIODICALLY_PATH = conf.getString("intermana.update-periodically");


}
