package com.tcbs.automation.config.idataalchemist;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class AlchemistConfig {
  private static final Config conf = new ConfigImpl("idataalchemist").getConf();


  public static final String COMMON_X_API_KEY = conf.getString("alchemist.x-api-key");

  // Indicator
  public static final String DAILY_TCP_URL = conf.getString("alchemist.alchemist.updateTcpDailyUrl");
  public static final String INTRADAY_TCP_URL = conf.getString("alchemist.alchemist.updateTcpMinutelyUrl");
  public static final String INTRADAY_DW_URL = conf.getString("alchemist.alchemist.updateDwMinutelyUrl");

  //Second TcPrice
  public static final String SECOND_TCPRICE_DAILY_URL = conf.getString("alchemist.alchemist.updateSecondTcPriceDailyUrl");
  public static final String SECOND_TCPRICE_MINUTELY_URL = conf.getString("alchemist.alchemist.updateSecondTcPriceMinutelyUrl");
}
