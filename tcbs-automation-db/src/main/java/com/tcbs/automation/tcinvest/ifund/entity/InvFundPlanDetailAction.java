package com.tcbs.automation.tcinvest.ifund.entity;

public enum InvFundPlanDetailAction implements Enumerable {
  BUY(1), SELL(2), BUY_FUTURES(3), CHANGE(4);

  private final int value;

  InvFundPlanDetailAction(int value) {
    this.value = value;
  }

  public int value() {
    return value;
  }

  public static InvFundPlanDetailAction valueOf(int value) {
    for (InvFundPlanDetailAction val : values()) {
      if (val.value == value)
        return val;
    }
    throw new IllegalArgumentException("No enum const InvFundPlanDetailAction with value " + value);
  }

  public static InvFundPlanDetailAction lookUp(String name) {
    for (InvFundPlanDetailAction val : values()) {
      if (val.name().equalsIgnoreCase(name))
        return val;
    }
    throw new IllegalArgumentException("No enum const InvFundPlanDetailAction with name " + name);
  }

  public static InvFundPlanDetailAction fromString(String value) {
    return lookUp(value);
  }
}
