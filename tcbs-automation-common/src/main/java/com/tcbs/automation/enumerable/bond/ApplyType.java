package com.tcbs.automation.enumerable.bond;

public enum ApplyType {
  BUY("BUY"),
  SELL("SELL"),
  BOTH("BOTH"),
  ;

  private String value;

  ApplyType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
