package com.tcbs.automation.config.rabbitmq;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class RabbitMqConfig {
  private static final Config conf = new ConfigImpl("rabbitmq").getConf();

  //RabbitMQ
  public static ConnectionConfig getRabbitConnConfig(String configName) {
    return new ConnectionConfig(
      conf.getString(String.format("rabbit-mq.%s.username", configName)),
      conf.getString(String.format("rabbit-mq.%s.password", configName)),
      conf.getString(String.format("rabbit-mq.%s.virtual-host", configName)),
      conf.getString(String.format("rabbit-mq.%s.host", configName)),
      conf.getInt(String.format("rabbit-mq.%s.port", configName))
    );
  }
}
