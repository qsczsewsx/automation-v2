package com.tcbs.automation.bondlifecycle.dto;

public enum UserAction {
  CREATE(ApprovalStatusCode.CREATE, "Create Action"), UPDATE(ApprovalStatusCode.UPDATE, "Update Action"),
  DELETE(ApprovalStatusCode.DELETE, "Delete Action"), APPROVAL(ApprovalStatusCode.APPROVAL, "Approval Action"),
  GET_APPROVAL_HISTORY(ApprovalStatusCode.GET_APPROVAL_HISTORY, "Get approval history action");

  private final String code;
  private final String message;

  UserAction(String code, String message) {
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
    public static final String APPROVAL = "APPROVAL";
    public static final String GET_APPROVAL_HISTORY = "GET_APPROVAL_HISTORY";
  }
}
