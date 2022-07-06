package com.tcbs.automation.tasks;

import com.tcbs.automation.config.redis.RedisSentinelInfo;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import redis.clients.jedis.Jedis;

import static com.tcbs.automation.config.redis.RedisSentinelConfig.getSentinelConfig;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class CallRedisKeyRemoval implements Task {

  private String key;

  private static Jedis redisClient;

  public CallRedisKeyRemoval(String key) {
    RedisSentinelInfo sentinelInfo = getSentinelConfig();
    redisClient = sentinelInfo.getDbConnection();
    this.key = key;
  }

  public static CallRedisKeyRemoval with(String key) {
    return instrumented(CallRedisKeyRemoval.class, key);
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    redisClient.del(key);
    redisClient.close();
  }
}
