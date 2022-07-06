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
@Table(name = "CASE_ISSUANCE_PARTICIPANT")
public class CaseIssuanceParticipant {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "PARTICIPANT_ID")
  private String participantId;
  @NotNull
  @Column(name = "CASE_ID")
  private String caseId;
  @NotNull
  @Column(name = "COMPANY_GROUP_TYPE_ID")
  private String companyGroupTypeId;
  @NotNull
  @Column(name = "PARTICIPANT_TYPE_ID")
  private String participantTypeId;
  @NotNull
  @Column(name = "COMPANY_ID")
  private String companyId;
  @NotNull
  @Column(name = "CREATED_AT")
  private Timestamp createdAt;
  @Column(name = "CREATED_BY")
  private String createdBy;
  @Column(name = "UPDATED_AT")
  private Timestamp updatedAt;
  @Column(name = "UPDATED_BY")
  private String updatedBy;


  public String getParticipantId() {
    return participantId;
  }

  public void setParticipantId(String participantId) {
    this.participantId = participantId;
  }


  public String getCaseId() {
    return caseId;
  }

  public void setCaseId(String caseId) {
    this.caseId = caseId;
  }


  public String getCompanyGroupTypeId() {
    return companyGroupTypeId;
  }

  public void setCompanyGroupTypeId(String companyGroupTypeId) {
    this.companyGroupTypeId = companyGroupTypeId;
  }


  public String getParticipantTypeId() {
    return participantTypeId;
  }

  public void setParticipantTypeId(String participantTypeId) {
    this.participantTypeId = participantTypeId;
  }


  public String getCompanyId() {
    return companyId;
  }

  public void setCompanyId(String companyId) {
    this.companyId = companyId;
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
  public CaseIssuanceParticipant getParticipantByCaseId(String caseId) {
    EdcmConnection.connection.getSession().clear();
    Query<CaseIssuanceParticipant> query = EdcmConnection.connection.getSession().createQuery(
      "from CaseIssuanceParticipant a WHERE a.caseId =:caseId", CaseIssuanceParticipant.class);
    query.setParameter("caseId", caseId);
    return query.getSingleResult();
  }
}
