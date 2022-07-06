package com.tcbs.automation.enumerable.bond;

public enum CustomerType {
  PERSONAL_CUSTOMER("PERSONAL"),
  CORPORATE_CUSTOMER("CORPORATE"),
  ;

  private String value;

  CustomerType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }


  @Override
  public String toString() {
    return this.toString();
  }
}
