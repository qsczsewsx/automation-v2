package com.tcbs.automation.config.isquare;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class ISquareConfig {
  private static final Config conf = new ConfigImpl("isquare").getConf();

  public static final String ISQUARE_GET_JWT_API_URL = conf.getString("isquare.api.login.get-jwt");
  public static final String ISQUARE_ASSIGN_ROLE_API_URL = conf.getString("isquare.api.login.assign-role");
}
