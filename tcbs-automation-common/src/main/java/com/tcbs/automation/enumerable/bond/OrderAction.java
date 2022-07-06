package com.tcbs.automation.enumerable.bond;

public enum OrderAction {
  BUY("BUY"),
  SELL("SELL");

  private String value;

  OrderAction(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return this.getValue();
  }
}