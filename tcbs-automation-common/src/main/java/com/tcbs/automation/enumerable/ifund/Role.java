package com.tcbs.automation.enumerable.ifund;

public enum Role {
  CUS("customer"), RM("rm"), IWP("wp");

  private final String value;

  Role(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
