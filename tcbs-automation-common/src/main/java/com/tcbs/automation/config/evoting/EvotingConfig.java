package com.tcbs.automation.config.evoting;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class EvotingConfig {
  private static final Config conf = new ConfigImpl("evoting").getConf();

  // EVOTING
  public static final String EVOTING_DOMAIN = conf.getString("evoting.domain");
  public static final String EVOTING_GET_VOTE = conf.getString("evoting.get-vote");
  public static final String EVOTING_CREATE_VOTE = conf.getString("evoting.create-vote");
  public static final String EVOTING_USER_VOTE = conf.getString("evoting.user-vote");
  public static final String EVOTING_IMPORT_USER = conf.getString("evoting.user-import");
  public static final String EVOTING_API_KEY = conf.getString("evoting.api-key");
}
