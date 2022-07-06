package com.tcbs.automation.config.pluto;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class PlutoConfig {
  private static final Config conf = new ConfigImpl("pluto").getConf();

  public static final String GET_ROOM_INFO = conf.getString("athena.room-info");
  public static final String LISTED_SEARCH = conf.getString("pluto.listed-search");
  public static final String UNLISTED_SEARCH = conf.getString("pluto.unlisted-search");
  public static final String LISTED_INFO = conf.getString("pluto.listed-info");
  public static final String UNLISTED_INFO = conf.getString("pluto.unlisted-info");
  public static final String SAVE_VALUATION = conf.getString("pluto.save-valuation");
}
