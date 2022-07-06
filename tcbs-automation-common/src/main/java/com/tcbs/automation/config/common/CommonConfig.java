package com.tcbs.automation.config.common;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class CommonConfig {
  private static final Config conf = new ConfigImpl("common").getConf();
  public static final String LOGIN_API = conf.getString("login.api");
  public static final String QE_X_API_KEY = conf.getString("qe.x-api-key");
  public static final String WSO2_LOGIN_URL = conf.getString("wso2.login");
  public static final String WSO2_LOGIN_CLIENT_ID = conf.getString("wso2.clientId");
  public static final String WSO2_LOGIN_CLIENT_SECRET = conf.getString("wso2.clientSecret");
  public static final String KAFKA_USER = conf.getString("kafka.user");
  public static final String KAFKA_PASS = conf.getString("kafka.pass");
  public static final String KAFKA_GROUP = conf.getString("kafka.group");
  public static final String KAFKA_SERVER = conf.getString("kafka.server");

  public static final RequestSpecification intRequestSpecification = new RequestSpecBuilder()
    .setPort(80)
    .addHeader("x-api-key", QE_X_API_KEY)
    .build();

  public static final String AUTHEN_OTP = conf.getString("authen.otp");
}
