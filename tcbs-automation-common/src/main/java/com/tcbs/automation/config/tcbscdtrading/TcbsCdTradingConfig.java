package com.tcbs.automation.config.tcbscdtrading;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import static com.tcbs.automation.config.common.CommonConfig.QE_X_API_KEY;

public class TcbsCdTradingConfig {
  private static final Config conf = new ConfigImpl("tcbscdtrading").getConf();

  public static final String TCBS_CD_TRADING_DP_INT = conf.getString("tcbs-cd-trading.datapower-int");
  public static final String TCBS_CD_TRADING_DP_EXT = conf.getString("tcbs-cd-trading.datapower-ext");
  public static final String TCBS_CD_TRADING_IP_INT = conf.getString("tcbs-cd-trading.ip-int");
  public static final String TCBS_CD_TRADING_IP_EXT = conf.getString("tcbs-cd-trading.ip-ext");
  public static final RequestSpecification intTcsbCdTradingTemplate = new RequestSpecBuilder()
    .setPort(80)
    .setBaseUri(TCBS_CD_TRADING_DP_INT)
    .addHeader("x-api-key", QE_X_API_KEY)
    .build();

  public static final RequestSpecification extTcsbCdTradingTemplate = new RequestSpecBuilder()
    .setPort(80)
    .setBaseUri(TCBS_CD_TRADING_DP_EXT)
    .build();
}
