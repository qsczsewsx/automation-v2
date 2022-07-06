package com.tcbs.automation.enumerable.bond;

public enum OrderMarketStatus {
  WAITING("WAITING"),
  WAITING_FOR_ACTIVED("WAITINGFORACTIVED"),
  CONFIRM("CONFIRM"),
  MATCHING("MATCHING"),
  DONE("DONE"),
  DELETED("DELETED"),
  EXPIRED("EXPIRED"),
  MATCHED("MATCHED"),
  ;

  private String value;

  OrderMarketStatus(String value) {
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
