package com.tcbs.automation.tcinvest;

import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;

@Entity
@Table(name = "INV_PLAN_AUDIT_LOG")
public class InvPlanAuditLog {

  @Id
  @GeneratedValue
  @Column(name = "ID")
  private Long id;
  @Column(name = "ACTOR_ID")
  private String actorId;
  @Column(name = "ACTION")
  private String action;
  @Column(name = "CREATED_DATE")
  private String createdDate;
  @Column(name = "PLAN_INSTANCE_ID")
  private String planInstanceId;
  @Column(name = "CONTENT")
  private String content;


  public InvPlanAuditLog(Long id, String actorId, String action, String createdDate, String planInstanceId, String content) {
    this.id = id;
    this.actorId = actorId;
    this.action = action;
    this.createdDate = createdDate;
    this.planInstanceId = planInstanceId;
    this.content = content;
  }

  public InvPlanAuditLog() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getActorId() {
    return actorId;
  }

  public void setActorId(String actorId) {
    this.actorId = actorId;
  }


  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }


  public String getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(String createdDate) {
    this.createdDate = createdDate;
  }


  public String getPlanInstanceId() {
    return planInstanceId;
  }

  public void setPlanInstanceId(String planInstanceId) {
    this.planInstanceId = planInstanceId;
  }


  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Step
  public List<InvPlanAuditLog> getDataByPlanInstanceId(String id) {
    Query<InvPlanAuditLog> query = TcInvest.tcInvestDbConnection.getSession().createQuery(
      "from InvPlanAuditLog where planInstanceId=:id", InvPlanAuditLog.class);
    query.setParameter("id", id);
    List<InvPlanAuditLog> listResult = query.getResultList();

    return listResult;
  }

  @Step
  public List<InvPlanAuditLog> getDataByActorAndActionAndPlanInstanceId(String id, String action) {
    Query<InvPlanAuditLog> query = TcInvest.tcInvestDbConnection.getSession().createQuery(
      "from InvPlanAuditLog where planInstanceId=:id  and action=:action", InvPlanAuditLog.class);
    query.setParameter("id", id)
      .setParameter("action", action);
    return query.getResultList();
  }

  @SuppressWarnings({"deprecation", "unchecked"})
  @Step
  public List<HashMap<String, Object>> getDataByColumnNameAndInputValue(String column, String value) {
    String queryString = "select ID, ACTOR_ID, \"ACTION\", CREATED_DATE, PLAN_INSTANCE_ID, \"CONTENT\" from INV_PLAN_AUDIT_LOG";
    if (column.equals("") == false) {
      queryString = queryString + " where " + column + "='" + value + "'";
    }
    queryString = queryString + " order by CREATED_DATE desc";
    List<HashMap<String, Object>> result = TcInvest.tcInvestDbConnection.getSession().createNativeQuery(queryString)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
      .getResultList();
    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }

}
