package com.tcbs.automation.config.caffeine;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class CaffeineConfig {
  private static final Config conf = new ConfigImpl("caffeine").getConf();

  public static final String TCI3CAFFEINE_DOMAIN = conf.getString("tci3caffeine.domain");
  public static final String TCI3CAFFEINE_CURRENT_DOMAIN = conf
    .getString("tci3caffeine.current.domain");

  public static final String CAFFEINE_SE_PATH = conf.getString("tci3caffeine.se");
}
