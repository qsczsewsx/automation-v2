package com.tcbs.automation.config.pricingrules;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import static com.tcbs.automation.config.common.CommonConfig.QE_X_API_KEY;

public class PricingRulesConfig {
  private static final Config conf = new ConfigImpl("pricingrules").getConf();

  public static final String PRICING_RULES_DP_INT = conf.getString("pricing-rules.datapower-int");
  public static final String PRICING_RULES_DP_EXT = conf.getString("pricing-rules.datapower-ext");
  public static final String PRICING_RULES_IP_INT = conf.getString("pricing-rules.ip-int");
  public static final String PRICING_RULES_IP_EXT = conf.getString("pricing-rules.ip-ext");

  public static final RequestSpecification intPricingRulesTemplate = new RequestSpecBuilder()
    .setPort(80)
    .setBaseUri(PRICING_RULES_DP_INT)
    .addHeader("x-api-key", QE_X_API_KEY)
    .build();

  public static final RequestSpecification extPricingRulesTemplate = new RequestSpecBuilder()
    .setPort(80)
    .setBaseUri(PRICING_RULES_DP_EXT)
    .build();
}
