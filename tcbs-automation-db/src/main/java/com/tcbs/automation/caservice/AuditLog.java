package com.tcbs.automation.caservice;

import com.tcbs.automation.functions.PublicConstant;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.text.ParseException;

;

@Entity
@Table(name = "AUDIT_LOG")
public class AuditLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "AUDIT_LOG_ID")
  private String auditLogId;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "LAST_UPDATED_DATE")
  private Timestamp lastUpdatedDate;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "ENCODED_DATA")
  private String encodedData;
  @Column(name = "ORIGINAL_DATA")
  private String originalData;
  @Column(name = "RESULT")
  private String result;


  public String getAuditLogId() {
    return auditLogId;
  }

  public void setAuditLogId(String auditLogId) {
    this.auditLogId = auditLogId;
  }


  public String getTcbsId() {
    return tcbsId;
  }

  public void setTcbsId(String tcbsId) {
    this.tcbsId = tcbsId;
  }


  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }


  public String getLastUpdatedDate() {
    return lastUpdatedDate == null ? null : PublicConstant.dateTimeFormat.format(lastUpdatedDate);
  }

  public void setLastUpdatedDate(String lastUpdatedDate) throws ParseException {
    this.lastUpdatedDate = new Timestamp(PublicConstant.dateTimeFormat.parse(lastUpdatedDate).getTime());
  }


  public String getCreatedDate() {
    return createdDate == null ? null : PublicConstant.dateTimeFormat.format(createdDate);
  }

  public void setCreatedDate(String createdDate) throws ParseException {
    this.createdDate = new Timestamp(PublicConstant.dateTimeFormat.parse(createdDate).getTime());
  }


  public String getEncodedData() {
    return encodedData;
  }

  public void setEncodedData(String encodedData) {
    this.encodedData = encodedData;
  }


  public String getOriginalData() {
    return originalData;
  }

  public void setOriginalData(String originalData) {
    this.originalData = originalData;
  }


  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

}
