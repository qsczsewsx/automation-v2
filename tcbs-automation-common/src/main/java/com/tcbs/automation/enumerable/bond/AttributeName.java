package com.tcbs.automation.enumerable.bond;

public enum AttributeName {
  MARKET_ATTRIBUTE("Market"),
  DEADLINE_PAYMENT_ATTRIBUTE("DeadlinePayment"),
  LIMITED_DAY_ORDER_IBOND_ATTRIBUTE("LimitedDayOrderIbond"),
  LIMITED_DAY_ORDER_ICONNECT_ATTRIBUTE("LimitedDayOrderIconnect"),
  MARKET_DEADLINE_PAYMENT("MarketDeadlinePayment"),
  STATUS_ATTRIBUTE("Status"),
  ;

  private String value;

  AttributeName(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
