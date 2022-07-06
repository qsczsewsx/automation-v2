package com.tcbs.automation.bondlifecycle.dto;

public enum RecordStatus {
  DRAFT(ApprovalStatusCode.DRAFT, "Draft"), ACTIVE(ApprovalStatusCode.ACTIVE, "Active"),
  INACTIVE(ApprovalStatusCode.INACTIVE, "Inactive");

  private final String code;
  private final String message;

  RecordStatus(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  private static class ApprovalStatusCode {
    public static final String DRAFT = "DRAFT";
    public static final String ACTIVE = "ACTIVE";
    public static final String INACTIVE = "INACTIVE";
  }
}
