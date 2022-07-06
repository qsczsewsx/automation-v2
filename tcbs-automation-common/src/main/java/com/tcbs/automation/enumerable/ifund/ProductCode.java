package com.tcbs.automation.enumerable.ifund;

public enum ProductCode {
  TCBF("TCBF"), TCEF("TCBF"), TCFF("TCBF");

  private final String value;

  ProductCode(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
