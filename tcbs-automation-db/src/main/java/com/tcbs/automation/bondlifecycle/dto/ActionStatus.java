package com.tcbs.automation.bondlifecycle.dto;

public enum ActionStatus {
  CREATE(ApprovalStatusCode.CREATE, "Create"), UPDATE(ApprovalStatusCode.UPDATE, "Update"),
  DELETE(ApprovalStatusCode.DELETE, "Delete");

  private final String code;
  private final String message;

  ActionStatus(String code, String message) {
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
    public static final String CREATE = "CREATE";
    public static final String UPDATE = "UPDATE";
    public static final String DELETE = "DELETE";
  }
}
