package com.tcbs.automation.questions;

import com.tcbs.automation.config.redis.RedisSentinelInfo;
import net.serenitybdd.screenplay.Question;
import redis.clients.jedis.Jedis;

import static com.tcbs.automation.config.redis.RedisSentinelConfig.getSentinelConfig;

public class CallRedisQuestion{

  public static Jedis getRedisClient() {
    RedisSentinelInfo sentinelInfo = getSentinelConfig();
    return sentinelInfo.getDbConnection();
  }

  public static Question<Boolean> keyExists(String key) {
    return Question.about("check redis key exists").answeredBy(actor -> {
      Jedis client = getRedisClient();
      Boolean result = client.exists(key);
      client.close();
      return result;
    });
  }
}
