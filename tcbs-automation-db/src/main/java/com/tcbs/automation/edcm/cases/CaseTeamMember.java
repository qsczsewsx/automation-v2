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
@Table(name = "CASE_TEAM_MEMBER")
public class CaseTeamMember {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "CASE_ID")
  private String caseId;
  @NotNull
  @Column(name = "DEAL_SUPERVISOR")
  private String dealSupervisor;
  @NotNull
  @Column(name = "DEAL_OWNER")
  private String dealOwner;
  @Column(name = "FOLLOW_UP")
  private String followUp;
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


  public String getCaseId() {
    return caseId;
  }

  public void setCaseId(String caseId) {
    this.caseId = caseId;
  }


  public String getDealSupervisor() {
    return dealSupervisor;
  }

  public void setDealSupervisor(String dealSupervisor) {
    this.dealSupervisor = dealSupervisor;
  }


  public String getDealOwner() {
    return dealOwner;
  }

  public void setDealOwner(String dealOwner) {
    this.dealOwner = dealOwner;
  }


  public String getFollowUp() {
    return followUp;
  }

  public void setFollowUp(String followUp) {
    this.followUp = followUp;
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
  public CaseTeamMember getTeamMemberByCaseId(String caseId) {
    EdcmConnection.connection.getSession().clear();
    Query<CaseTeamMember> query = EdcmConnection.connection.getSession().createQuery(
      "from CaseTeamMember a WHERE a.caseId= :caseId", CaseTeamMember.class);
    query.setParameter("caseId", caseId);
    return query.getSingleResult();
  }

}
