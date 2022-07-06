package com.tcbs.automation.config.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import redis.clients.jedis.Jedis;

@Getter
@Setter
@AllArgsConstructor
public class RedisInfo {
  private String host;
  private String port;
  private String password;
  private String dbIndex;

  public Jedis getDbConnection() {
    return new Jedis(this.host);
  }
}