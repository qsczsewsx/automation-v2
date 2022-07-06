package com.tcbs.automation.config.redis;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class RedisConfig {
  private static final Config conf = new ConfigImpl("redis").getConf();

  // Redis
  public static RedisInfo getRedisConfig() {
    return new RedisInfo(
      conf.getString(String.format("redis.host")),
      conf.getString(String.format("redis.port")),
      conf.getString(String.format("redis.db")),
      conf.getString(String.format("redis.pass"))
    );
  }

}
