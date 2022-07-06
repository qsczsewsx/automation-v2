package com.tcbs.automation.config.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class RedisSentinelInfo {
  private Set<String> sentinels;
  private String masterName;
  private String password;
  private int db;

  public Jedis getDbConnection() {
    JedisSentinelPool pool = new JedisSentinelPool(masterName, sentinels);
    Jedis client = pool.getResource();
    client.auth(password);

    return client;
  }

  public Jedis getDbConnectionWithDbIdx() {
    JedisSentinelPool pool = new JedisSentinelPool(masterName, sentinels, new GenericObjectPoolConfig(), 2000, password,  db);
    Jedis client = pool.getResource();
    client.auth(password);
    return client;
  }

  public RedisSentinelInfo(Set<String> sentinels, String masterName, String password) {
    this.sentinels = sentinels;
    this.masterName = masterName;
    this.password = password;
  }
}
