package com.tcbs.automation.config.docportal;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class DocPortalServiceConfig {
  private static final Config conf = new ConfigImpl("docportal").getConf();
  public static final String DOCUMENT_PORTAL_BASE_PATH = conf.getString("document-portal.baseUrl");
}
