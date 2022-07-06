package com.tcbs.automation.edcm.bond;

import com.tcbs.automation.functions.PublicConstant;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;

@Entity
@Table(name = "BOND_TO_SELL")
public class BondToSell {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "BOND_ID")
  private String bondId;
  @Column(name = "CASE_ID")
  private String caseId;
  @Column(name = "IS_EXPOSE")
  private String isExpose;
  @Column(name = "BUILDBOOK_DEADLINE")
  private Date buildbookDeadline;
  @Column(name = "PRIORITY")
  private String priority;
  @Column(name = "NOTE")
  private String note;
  @Column(name = "AMOUNT_INVESTOR")
  private String amountInvestor;
  @Column(name = "AMOUNT_WAREHOUSE")
  private String amountWarehouse;
  @NotNull
  @Column(name = "CREATED_AT")
  private Timestamp createdAt;
  @NotNull
  @Column(name = "CREATED_BY")
  private String createdBy;
  @Column(name = "UPDATED_AT")
  private Timestamp updatedAt;
  @Column(name = "UPDATED_BY")
  private String updatedBy;
  @Column(name = "IS_EXPOSE_PREV_STATE")
  private String isExposePrevState;


  public String getBondId() {
    return bondId;
  }

  public void setBondId(String bondId) {
    this.bondId = bondId;
  }


  public String getCaseId() {
    return caseId;
  }

  public void setCaseId(String caseId) {
    this.caseId = caseId;
  }


  public String getIsExpose() {
    return isExpose;
  }

  public void setIsExpose(String isExpose) {
    this.isExpose = isExpose;
  }


  public Date getBuildbookDeadline() {
    return buildbookDeadline;
  }

  public void setBuildbookDeadline(Date buildbookDeadline) {
    this.buildbookDeadline = buildbookDeadline;
  }


  public String getPriority() {
    return priority;
  }

  public void setPriority(String priority) {
    this.priority = priority;
  }


  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }


  public String getAmountInvestor() {
    return amountInvestor;
  }

  public void setAmountInvestor(String amountInvestor) {
    this.amountInvestor = amountInvestor;
  }


  public String getAmountWarehouse() {
    return amountWarehouse;
  }

  public void setAmountWarehouse(String amountWarehouse) {
    this.amountWarehouse = amountWarehouse;
  }


  public String getCreatedAt() {
    return createdAt == null ? null : PublicConstant.dateTimeFormat.format(createdAt);
  }

  public void setCreatedAt(String createdAt) throws ParseException {
    this.createdAt = new Timestamp(PublicConstant.dateTimeFormat.parse(createdAt).getTime());
  }


  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }


  public String getUpdatedAt() {
    return updatedAt == null ? null : PublicConstant.dateTimeFormat.format(updatedAt);
  }

  public void setUpdatedAt(String updatedAt) throws ParseException {
    this.updatedAt = new Timestamp(PublicConstant.dateTimeFormat.parse(updatedAt).getTime());
  }


  public String getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }


  public String getIsExposePrevState() {
    return isExposePrevState;
  }

  public void setIsExposePrevState(String isExposePrevState) {
    this.isExposePrevState = isExposePrevState;
  }

}
