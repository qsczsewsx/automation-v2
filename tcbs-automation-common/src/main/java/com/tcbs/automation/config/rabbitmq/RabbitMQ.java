package com.tcbs.automation.config.rabbitmq;

import com.rabbitmq.client.Connection;

public enum RabbitMQ {
  NOTIFICATION("notification"),
  PORTFOLIO("portfolio"),
  SAMSARA("samsara"),
  TCBS_BOND("tcbs.bond"),
  TCBS_ODIN("tcbs.odin"),
  TCBS_FUND("tcbs.fund"),
  TCBS_ODIN_ISTOCK("tcbs.odin.istock"),
  TCBS_ODIN_BOND("tcbs.odin.bond"),
  CASH_SERVICE("casflex"),
  INTRADAY_SERVICE("intraday"),
  ISTOCK("istock"),
  INBOX("inbox"),
  XOBNI("xobni"),
  XOBNI2("xobni2"),
  MOKSHA("moksha"),
  LIGO_CW_CHANGE("ligo-cw-change"),
  LIGO_WATCHLIST_CHANGE("ligo-watchlist-change"),
  DEROSIE_UPDATE("derosie"),
  DYNAMIC_WATCH_LIST("dynamic-watch-list"),
  WARNING("warning"),
  STOCK_PRICE("stockprice"),
  CONDITIONAL_RESULT("conditional_result"),
  ANICCA("anicca");

  private String configName;

  RabbitMQ(String configName) {
    this.configName = configName;
  }

  public Connection newConnection() {
    ConnectionConfig connectionConfig = RabbitMqConfig.getRabbitConnConfig(this.configName);
    return connectionConfig.newConnection();
  }
}
