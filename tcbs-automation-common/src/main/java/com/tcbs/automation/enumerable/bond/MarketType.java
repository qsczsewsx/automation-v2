package com.tcbs.automation.enumerable.bond;

public enum MarketType {
  ICONNECT("iconnect"),
  IBOND("IBOND"),
  PO_PRIMARY("po_primary"),
  PRE_ORDER("pre_order");
  private String value;

  MarketType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return this.getValue();
  }
}