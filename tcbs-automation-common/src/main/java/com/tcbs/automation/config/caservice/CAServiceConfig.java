package com.tcbs.automation.config.caservice;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class CAServiceConfig {
  private static final Config conf = new ConfigImpl("caservice").getConf();

  //CA Service
  public static final String CASERVICE_URL = conf.getString("caservice.domain");
  public static final String CASERVICE_V1 = conf.getString("caservice.caV1");
  public static final String CASERVICE_REGISTER = conf.getString("caservice.register");
  public static final String CASERVICE_GET_INFO = conf.getString("caservice.getinfo");
  public static final String CASERVICE_UNREGISTER = conf.getString("caservice.unregister");

  public static final String VALID_BASE64_KEY = conf.getString("caservice.valid-base64-key");
  public static final String EXPIRED_BASE64_KEY = conf.getString("caservice.expired-base64-key");
  public static final String REVOKED_BASE64_KEY = conf.getString("caservice.revoked-base64-key");
  public static final String NOT_TRUSTED_BASE64_KEY = conf.getString("caservice.not-trusted-base64-key");
}
