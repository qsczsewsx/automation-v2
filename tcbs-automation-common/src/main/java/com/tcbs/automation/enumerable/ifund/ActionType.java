package com.tcbs.automation.enumerable.ifund;

public enum ActionType {
  BUY(1), SELL(2), CHANGE_PRODUCT(4);

  private final int value;

  ActionType(int value) {
    this.value = value;
  }

  public static ActionType valueOf(int value) throws IllegalArgumentException {
    for (ActionType val : values()) {
      if (val.value() == value) {
        return val;
      }
    }
    throw new IllegalArgumentException("No enum const ActionType with value " + value);
  }

  public int value() {
    return value;
  }
}
