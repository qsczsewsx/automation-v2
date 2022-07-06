package com.tcbs.automation.config;

import com.typesafe.config.Config;

public class ConfigImpl {

  private final Config conf;

  public ConfigImpl(String serviceName) {
    Configs.Builder builder = Configs.newBuilder()
      .withResource(serviceName + "/" + serviceName + "." + Env.get().getName() + ".conf")
      .withResource(serviceName + "/" + serviceName + ".conf");
    this.conf = builder.build();
  }

  public Config getConf() {
    return this.conf;
  }
}
