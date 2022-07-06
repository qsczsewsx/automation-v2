package com.tcbs.automation.bondlifecycle.dto;

public enum ApprovalStatus {
  INPROGRESS(ApprovalStatusCode.INPROGRESS, "In progress"),
  WAITING(ApprovalStatusCode.WAITING, "Wating for approval"), APPROVED(ApprovalStatusCode.APPROVED, "Approved"),
  REJECTED(ApprovalStatusCode.REJECTED, "Rejected");

  private final String code;
  private final String message;

  ApprovalStatus(String code, String message) {
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
    public static final String INPROGRESS = "INPROGRESS";
    public static final String WAITING = "WAITING";
    public static final String APPROVED = "APPROVED";
    public static final String REJECTED = "REJECTED";
  }
}
