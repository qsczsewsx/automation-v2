package com.tcbs.automation.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum Env {
  LOCAL("local"), TEST("test"), SIT("sit"), UAT("uat"),
  CORETEST("coretest");

  // {{start:logger}}
  private static final Logger log = LoggerFactory.getLogger(Env.class);
  private static final Env currentEnv;

  static {
    String env = SIT.getName();
    // This comes from -Denv={environment}
    if (Configs.systemProperties().hasPath("env")) {
      env = Configs.systemProperties().getString("env");
    }
    currentEnv = Env.valueOf(env.toUpperCase());
    log.info("Current Env: {}", currentEnv.getName());
  }

  private final String name;

  Env(String name) {
    this.name = name;
  }

  public static Env get() {
    return currentEnv;
  }

  public String getName() {
    return name;
  }
}
