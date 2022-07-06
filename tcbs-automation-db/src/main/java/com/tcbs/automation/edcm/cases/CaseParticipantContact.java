package com.tcbs.automation.edcm.cases;

import com.tcbs.automation.edcm.EdcmConnection;
import com.tcbs.automation.functions.PublicConstant;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

@Entity
@Table(name = "CASE_PARTICIPANT_CONTACT")
public class CaseParticipantContact {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @Column(name = "CREATED_AT")
  private Timestamp createdAt;
  @Column(name = "CREATED_BY")
  private String createdBy;
  @Column(name = "UPDATED_AT")
  private Timestamp updatedAt;
  @Column(name = "UPDATED_BY")
  private String updatedBy;
  @Column(name = "CASE_ROLE_ID")
  private String caseRoleId;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "EMPLOYEE_ID")
  private String employeeId;
  @Column(name = "CASE_PARTICIPANT_ID")
  private String caseParticipantId;
  @Column(name = "EMPLOYEE_BPM_NAME")
  private String employeeBpmName;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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


  public String getCaseRoleId() {
    return caseRoleId;
  }

  public void setCaseRoleId(String caseRoleId) {
    this.caseRoleId = caseRoleId;
  }


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  public String getEmployeeId() {
    return employeeId;
  }

  public void setEmployeeId(String employeeId) {
    this.employeeId = employeeId;
  }


  public String getCaseParticipantId() {
    return caseParticipantId;
  }

  public void setCaseParticipantId(String caseParticipantId) {
    this.caseParticipantId = caseParticipantId;
  }


  public String getEmployeeBpmName() {
    return employeeBpmName;
  }

  public void setEmployeeBpmName(String employeeBpmName) {
    this.employeeBpmName = employeeBpmName;
  }

  @Step
  public CaseParticipantContact getContactByParticipantId(String caseParticipantId) {
    EdcmConnection.connection.getSession().clear();
    Query<CaseParticipantContact> query = EdcmConnection.connection.getSession().createQuery(
      "from CaseParticipantContact a WHERE a.caseParticipantId =:caseParticipantId", CaseParticipantContact.class);
    query.setParameter("caseParticipantId", caseParticipantId);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new CaseParticipantContact();
    }
  }

  @Step
  public List<CaseParticipantContact> listContactByParticipantId(String caseParticipantId) {
    EdcmConnection.connection.getSession().clear();
    Query<CaseParticipantContact> query = EdcmConnection.connection.getSession().createQuery(
      "from CaseParticipantContact a WHERE a.caseParticipantId =:caseParticipantId", CaseParticipantContact.class);
    query.setParameter("caseParticipantId", caseParticipantId);
    return query.getResultList();
  }
}