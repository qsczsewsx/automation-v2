package com.tcbs.automation.config.ifund;

public enum TestType {
  NORMAL(1), ORCL(2), DOCKER(3);

  private final int value;

  TestType(int value) {
    this.value = value;
  }

  public int value() {
    return value;
  }
}
