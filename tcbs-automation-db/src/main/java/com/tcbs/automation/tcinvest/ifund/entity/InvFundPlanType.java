package com.tcbs.automation.tcinvest.ifund.entity;

public enum InvFundPlanType implements Enumerable {
  NEW(0), REUSE(1), MERGE(3);

  private final int value;

  InvFundPlanType(int value) {
    this.value = value;
  }

  public int value() {
    return value;
  }

  public static InvFundPlanType valueOf(int value) {
    for (InvFundPlanType val : values()) {
      if (val.value == value)
        return val;
    }
    throw new IllegalArgumentException("No enum const InvFundPlanType with value " + value);
  }

  public static InvFundPlanType lookUp(String name) {
    for (InvFundPlanType val : values()) {
      if (val.name().equalsIgnoreCase(name))
        return val;
    }
    throw new IllegalArgumentException("No enum const InvFundPlanType with name " + name);
  }

  public static InvFundPlanType fromString(String value) {
    return lookUp(value);
  }
}

