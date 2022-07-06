package com.tcbs.automation.config.edcm;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class EdcmConstant {
  private static Config conf = new ConfigImpl("edcm").getConf();

  public static final String EDCM_SERVICE_URL = conf.getString("edcm.domain");
  public static final String EDCM_SERVICE_API_KEY = conf.getString("edcm-service.api-key");

  public static final String EDCM_SERVICE_API_KEY_PUBLIC = conf.getString("edcm-service.api-key-public");
  public static final String EDCM_SERVICE_URL_INTERNAL = conf.getString("edcm.domain_internal");

}
