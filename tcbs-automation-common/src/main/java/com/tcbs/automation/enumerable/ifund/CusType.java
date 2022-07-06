package com.tcbs.automation.enumerable.ifund;

public enum CusType {
  CN(0), DN(1), CUS(2), RM(2);

  private final int value;

  CusType(int value) {
    this.value = value;
  }

  public static CusType valueOf(int value) throws IllegalArgumentException {
    for (CusType val : values()) {
      if (val.value() == value) {
        return val;
      }
    }
    throw new IllegalArgumentException("No enum const Tax with value " + value);
  }

  public int value() {
    return value;
  }
}
