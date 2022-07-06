package com.tcbs.automation.config.autologin;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class AutoLoginConfig {
  private static final Config conf = new ConfigImpl("autologin").getConf();

  //AUTHEN
  public static final String AUTHEN_GET_LOGINKEY = conf.getString("authen.getloginkey");
  public static final String AUTHEN_LOGIN = conf.getString("authen.login");
  public static final String AUTHEN_LOGIN_DOMAIN = conf.getString("authen.domain");

}
