package com.tcbs.automation.config.marketservice;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import static com.tcbs.automation.config.common.CommonConfig.QE_X_API_KEY;

public class MarketServiceConfig {
  private static final Config conf = new ConfigImpl("marketservice").getConf();

  public static final String MARKET_SERVICE_DP_INT = conf.getString("market-service.datapower-int");
  public static final String MARKET_SERVICE_DP_EXT = conf.getString("market-service.datapower-ext");
  public static final String MARKET_SERVICE_IP_INT = conf.getString("market-service.ip-int");
  public static final String MARKET_SERVICE_IP_EXT = conf.getString("market-service.ip-ext");
  public static final RequestSpecification intMarketServiceTemplate = new RequestSpecBuilder()
    .setPort(80)
    .setBaseUri(MARKET_SERVICE_DP_INT)
    .addHeader("x-api-key", QE_X_API_KEY)
    .build();

  public static final RequestSpecification extMarketServiceTemplate = new RequestSpecBuilder()
    .setPort(80)
    .setBaseUri(MARKET_SERVICE_DP_EXT)
    .build();
}
