package com.tcbs.automation.config.idatachartstorage;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class IDataChartStorageConfig {
  private static final Config conf = new ConfigImpl("idatachartstorage").getConf();

  // fengshui
  public static final String CHART_STORAGE_DOMAIN_PUBLIC_URL = conf.getString("idatachartstorage.public-domain");

  public static final String CHART_STORAGE_STUDY_TEMPLATE_URL = conf.getString("idatachartstorage.studyTemplateUrl");

}
