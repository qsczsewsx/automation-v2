package com.tcbs.automation.config.pricingservice;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import static com.tcbs.automation.config.common.CommonConfig.QE_X_API_KEY;

public class PricingServiceConfig {
  private static final Config conf = new ConfigImpl("pricingservice").getConf();

  public static final String PRICING_SERVICE_DP_INT = conf.getString("pricing-service.datapower-int");
  public static final String PRICING_SERVICE_DP_EXT = conf.getString("pricing-service.datapower-ext");
  public static final String PRICING_SERVICE_IP_INT = conf.getString("pricing-service.ip-int");
  public static final String PRICING_SERVICE_IP_EXT = conf.getString("pricing-service.ip-ext");

  public static final RequestSpecification intPricingServiceTemplate = new RequestSpecBuilder()
    .setPort(80)
    .setBaseUri(PRICING_SERVICE_DP_INT)
    .addHeader("x-api-key", QE_X_API_KEY)
    .build();

  public static final RequestSpecification extPricingServiceTemplate = new RequestSpecBuilder()
    .setPort(80)
    .setBaseUri(PRICING_SERVICE_DP_EXT)
    .build();
}
