package com.automation;

import com.automation.config.Configs;
import com.automation.config.Env;
import com.typesafe.config.Config;

public class DbConstant {

  private static Config conf;

  static {
    try {
      conf = Configs.newBuilder()
        .withResource("database." + Env.get().getName() + ".conf")
        .build();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public DbConstant() {
  }

  public static DbInfo getDbInfo(String dbValue) {
    return new DbInfo(
      conf.getString(String.format("hibernate.%s.username", dbValue)),
      conf.getString(String.format("hibernate.%s.pass", dbValue)),
      conf.getString(String.format("hibernate.%s.url", dbValue)),
      conf.getString(String.format("hibernate.%s.config", dbValue)));
  }


}
