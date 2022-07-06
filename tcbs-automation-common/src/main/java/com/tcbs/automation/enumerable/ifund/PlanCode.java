package com.tcbs.automation.enumerable.ifund;

public enum PlanCode {
  VLAG("VLAG"), TNOD("SI"), TTSL("CBF");

  private final String value;

  PlanCode(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
