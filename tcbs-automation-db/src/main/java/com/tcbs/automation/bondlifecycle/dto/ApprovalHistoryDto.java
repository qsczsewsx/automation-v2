package com.tcbs.automation.bondlifecycle.dto;

import java.util.Date;

public class ApprovalHistoryDto {
  private Integer id;
  private String objectType;
  private String objectId;
  private String approvalStatus;
  private String content;
  private String note;
  private Date approvalTimeDB = null;
  private String approvalTime = null;
  private String approvalBy = null;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getObjectType() {
    return objectType;
  }

  public void setObjectType(String objectType) {
    this.objectType = objectType;
  }

  public String getObjectId() {
    return objectId;
  }

  public void setObjectId(String objectId) {
    this.objectId = objectId;
  }

  public String getApprovalStatus() {
    return approvalStatus;
  }

  public void setApprovalStatus(String approvalStatus) {
    this.approvalStatus = approvalStatus;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public Date getApprovalTimeDB() {
    return approvalTimeDB;
  }

  public void setApprovalTimeDB(Date approvalTimeDB) {
    this.approvalTimeDB = approvalTimeDB;
  }

  public String getApprovalTime() {
    return approvalTime;
  }

  public void setApprovalTime(String approvalTime) {
    this.approvalTime = approvalTime;
  }

  public String getApprovalBy() {
    return approvalBy;
  }

  public void setApprovalBy(String approvalBy) {
    this.approvalBy = approvalBy;
  }
}
