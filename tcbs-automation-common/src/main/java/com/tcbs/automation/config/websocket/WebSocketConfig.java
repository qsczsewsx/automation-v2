package com.tcbs.automation.config.websocket;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class WebSocketConfig {
  private static final Config conf = new ConfigImpl("websocket").getConf();

  //Conditional Order
  public static final String INTRADAY_WEBSOCKET_URL = conf.getString("websocket.intradayUrl");
}
