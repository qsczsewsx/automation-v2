package com.tcbs.automation.enumerable.bond;

public enum AttributeValue {
  FROZEN("FROZEN"),
  OTC("OTC"),
  LISTED("LISTED"),
  ;

  private String value;

  AttributeValue(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
