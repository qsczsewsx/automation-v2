package com.tcbs.automation.bondlifecycle;

import com.tcbs.automation.bondlifecycle.dto.ActionStatus;
import com.tcbs.automation.bondlifecycle.dto.ApprovalStatus;
import com.tcbs.automation.bondlifecycle.dto.RecordStatus;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class ApprovalEntity extends BaseEntity {
  @Column(name = "ACTION")
  private String action = null;

  @Column(name = "STATUS")
  private String status = null;

  @Column(name = "APPROVAL_STATUS")
  private String approvalStatus = null;

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getApprovalStatus() {
    return approvalStatus;
  }

  public void setApprovalStatus(String approvalStatus) {
    this.approvalStatus = approvalStatus;
  }

  public void create() {
    this.action = ActionStatus.CREATE.getCode();
    this.status = RecordStatus.DRAFT.getCode();
    this.approvalStatus = ApprovalStatus.INPROGRESS.getCode();
  }

  public void update(ApprovalEntity old) {
    this.action = old.getAction();
    this.status = old.getStatus();
    this.approvalStatus = ApprovalStatus.INPROGRESS.getCode();
  }

  public void deactive() {
    this.action = ActionStatus.DELETE.getCode();
    this.status = RecordStatus.INACTIVE.getCode();
    this.approvalStatus = ApprovalStatus.APPROVED.getCode();
  }

  public void approved() {
    this.status = RecordStatus.ACTIVE.getCode();
    this.approvalStatus = ApprovalStatus.APPROVED.getCode();
  }

  public void rejected() {
    this.approvalStatus = ApprovalStatus.REJECTED.getCode();
  }
}
