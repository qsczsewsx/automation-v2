package com.tcbs.automation.config.redis;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

import java.util.HashSet;

public class RedisSentinelConfig {
  private static final Config conf = new ConfigImpl("redis").getConf();

  // Redis
  public static RedisSentinelInfo getSentinelConfig() {
    return new RedisSentinelInfo(
      new HashSet<String>(conf.getStringList(String.format("redis-sentinel.sentinels"))),
      conf.getString(String.format("redis-sentinel.mastername")),
      conf.getString(String.format("redis-sentinel.pass"))
    );
  }

}