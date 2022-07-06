package com.tcbs.automation.enumerable.bond;

public enum ServedType {
  RM("rms"),
  CUS("cus");

  private String value;

  ServedType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return value;
  }
}