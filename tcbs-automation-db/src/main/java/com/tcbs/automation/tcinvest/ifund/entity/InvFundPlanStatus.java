package com.tcbs.automation.tcinvest.ifund.entity;

public enum InvFundPlanStatus implements Enumerable {
  DRAFT(0), INVESTING(1), STOPPED(2), CANCELLED(3), REJECTED(4);

  private final int value;

  InvFundPlanStatus(int value) {
    this.value = value;
  }

  public int value() {
    return value;
  }

  public static InvFundPlanStatus valueOf(int value) {
    for (InvFundPlanStatus val : values()) {
      if (val.value == value)
        return val;
    }
    throw new IllegalArgumentException("No enum const InvFundPlanStatus with value " + value);
  }

  public static InvFundPlanStatus lookUp(String name) {
    for (InvFundPlanStatus val : values()) {
      if (val.name().equalsIgnoreCase(name))
        return val;
    }
    throw new IllegalArgumentException("No enum const InvFundPlanStatus with name " + name);
  }

  public static InvFundPlanStatus fromString(String value) {
    return lookUp(value);
  }
}