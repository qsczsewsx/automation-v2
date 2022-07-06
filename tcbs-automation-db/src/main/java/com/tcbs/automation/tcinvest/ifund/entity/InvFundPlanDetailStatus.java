package com.tcbs.automation.tcinvest.ifund.entity;

public enum InvFundPlanDetailStatus implements Enumerable {

  WAITING_SELL_FUND(0), DRAFT(1), PLANNING(2), PROCESSING(3), DONE(4), FAILED(5), CANCELED(6), REJECTED(7);

  private final int value;

  InvFundPlanDetailStatus(int value) {
    this.value = value;
  }

  public int value() {
    return value;
  }

  public static InvFundPlanDetailStatus valueOf(int value) {
    for (InvFundPlanDetailStatus val : values()) {
      if (val.value == value)
        return val;
    }
    throw new IllegalArgumentException("No enum const InvFundPlanDetailStatus with value " + value);
  }

  public static InvFundPlanDetailStatus lookUp(String name) {
    for (InvFundPlanDetailStatus val : values()) {
      if (val.name().equalsIgnoreCase(name))
        return val;
    }
    throw new IllegalArgumentException("No enum const InvFundPlanDetailStatus with name " + name);
  }

  public static InvFundPlanDetailStatus fromString(String value) {
    return lookUp(value);
  }
}
