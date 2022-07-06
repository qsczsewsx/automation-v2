package com.tcbs.automation.config.rabbitmq;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConnectionConfig {

  private static final Logger logger = LoggerFactory.getLogger(ConnectionConfig.class);

  private ConnectionFactory connectionFactory;

  public ConnectionConfig(String userName, String password, String virtualHost, String host, int port) {

    logger.info("connect to rabbit-mq with virtualHost: {}, host: {}, port {}", virtualHost, host, port);

    this.connectionFactory = new ConnectionFactory();
    this.connectionFactory.setUsername(userName);
    this.connectionFactory.setPassword(password);
    this.connectionFactory.setVirtualHost(virtualHost);
    this.connectionFactory.setHost(host);
    this.connectionFactory.setPort(port);

  }

  public Connection newConnection() {
    Connection conn = null;
    try {
      conn = this.connectionFactory.newConnection();
    } catch (IOException e) {
      logger.error("error when create new rabbitMQ connection: {}", e.getMessage());
      logger.error("stack trace {}", e.getStackTrace());
    } catch (TimeoutException e) {
      logger.error("timeout when create new rabbitMQ connection: {}", e.getMessage());
      logger.error("stack trace {}", e.getStackTrace());
    }
    return conn;
  }
}
