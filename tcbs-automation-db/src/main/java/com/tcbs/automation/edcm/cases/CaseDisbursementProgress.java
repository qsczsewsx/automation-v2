package com.tcbs.automation.edcm.cases;

import com.tcbs.automation.edcm.EdcmConnection;
import com.tcbs.automation.functions.PublicConstant;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.text.ParseException;


@Entity
@Table(name = "CASE_DISBURSEMENT_PROGRESS")
public class CaseDisbursementProgress {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @NotNull
  @Column(name = "CASE_ID")
  private String caseId;
  @NotNull
  @Column(name = "DISBURSEMENT_DATE")
  private Timestamp disbursementDate;
  @Column(name = "QUANTITY")
  private String quantity;
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


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  public String getCaseId() {
    return caseId;
  }

  public void setCaseId(String caseId) {
    this.caseId = caseId;
  }


  public String getDisbursementDate() {
    return disbursementDate == null ? null : PublicConstant.dateTimeFormat.format(disbursementDate);
  }

  public void setDisbursementDate(String disbursementDate) throws ParseException {
    this.disbursementDate = new Timestamp(PublicConstant.dateTimeFormat.parse(disbursementDate).getTime());
  }


  public String getQuantity() {
    return quantity;
  }

  public void setQuantity(String quantity) {
    this.quantity = quantity;
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

  @Step
  public CaseDisbursementProgress getDisbursementByCaseId(String caseId) {
    EdcmConnection.connection.getSession().clear();
    Query<CaseDisbursementProgress> query = EdcmConnection.connection.getSession().createQuery(
      "from CaseDisbursementProgress a WHERE a.caseId= :caseId", CaseDisbursementProgress.class);
    query.setParameter("caseId", caseId);
    return query.getSingleResult();
  }

}
