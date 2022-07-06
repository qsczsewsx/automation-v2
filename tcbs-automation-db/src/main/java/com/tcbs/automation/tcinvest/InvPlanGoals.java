package com.tcbs.automation.tcinvest;

import net.thucydides.core.annotations.Step;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;


@Entity
@Table(name = "INV_PLAN_GOAL")
public class InvPlanGoals {


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "id")
  private Integer id;
  @Column(name = "plan_instance_id")
  private String planInstanceId;
  @Column(name = "initial_amount")
  private Double initialAmount;
  @Column(name = "regular_amount")
  private Double regularAmount;
  @Column(name = "start_date")
  private String startDate;
  @Column(name = "end_date")
  private String endDate;
  @Column(name = "frequency")
  private String frequency;
  @Column(name = "is_auto")
  private Integer isAuto;
  @Column(name = "created_date")
  private String createdDate;
  @Column(name = "updated_date")
  private String updatedDate;
  @Column(name = "timeline_id")
  private String timelineId;
  @Column(name = "name")
  private String name;
  @Column(name = "type")
  private String type;
  @Column(name = "status")
  private String status;
  @Column(name = "asset_type")
  private String assetType;
  @Column(name = "duration")
  private Integer duration;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getPlanInstanceId() {
    return planInstanceId;
  }

  public void setPlanInstanceId(String planInstanceId) {
    this.planInstanceId = planInstanceId;
  }

  public Double getInitialAmount() {
    return initialAmount;
  }

  public void setInitialAmount(Double initialAmount) {
    this.initialAmount = initialAmount;
  }

  public Double getRegularAmount() {
    return regularAmount;
  }

  public void setRegularAmount(Double regularAmount) {
    this.regularAmount = regularAmount;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public String getFrequency() {
    return frequency;
  }

  public void setFrequency(String frequency) {
    this.frequency = frequency;
  }

  public Integer getDuration() {
    return duration;
  }

  public void setDuration(Integer duration) {
    this.duration = duration;
  }

  public Integer getIsAuto() {
    return isAuto;
  }

  public void setIsAuto(Integer isAuto) {
    this.isAuto = isAuto;
  }

  public String getAssetType() {
    return assetType;
  }

  public void setAssetType(String assetType) {
    this.assetType = assetType;
  }

  public String getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(String createdDate) {
    this.createdDate = createdDate;
  }

  public String getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(String updatedDate) {
    this.updatedDate = updatedDate;
  }

  public String getTimelineId() {
    return timelineId;
  }

  public void setTimelineId(String timelineId) {
    this.timelineId = timelineId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @Step
  public InvPlanGoals getDataById(Integer id) {
    TcInvest.tcInvestDbConnection.getSession().clear();
    Query<InvPlanGoals> query = TcInvest.tcInvestDbConnection.getSession().createQuery(
      "from InvPlanInstance where id=:id", InvPlanGoals.class);
    query.setParameter("id", id);
    return query.getSingleResult();
  }

  @Step
  public List<InvPlanGoals> getDataGoalsByPlanInstanceId(String planInstanceId) {
    TcInvest.tcInvestDbConnection.getSession().clear();
    Query<InvPlanGoals> query = TcInvest.tcInvestDbConnection.getSession().createQuery(
      "from InvPlanGoals where planInstanceId=:planInstanceId", InvPlanGoals.class);
    query.setParameter("planInstanceId", planInstanceId);
    List<InvPlanGoals> listResult = query.getResultList();

    if (listResult.size() > 0) {
      return listResult;
    } else {
      return null;
    }
  }


  @Step
  public InvPlanGoals getDataByIdActive(String id) {
    InvPlanGoals result = null;
    TcInvest.tcInvestDbConnection.getSession().clear();
    Query<InvPlanGoals> query = TcInvest.tcInvestDbConnection.getSession().createQuery(
      "from InvPlanInstance where id=:id and active=:active", InvPlanGoals.class);
    query.setParameter("id", id);
    query.setParameter("active", 1);
    List<InvPlanGoals> listResult = query.getResultList();

    if (listResult.size() == 1) {
      result = listResult.get(0);
    }

    return result;
  }

  @Step
  public InvPlanGoals getDataByIdInactive(String id) {
    InvPlanGoals result = null;
    TcInvest.tcInvestDbConnection.getSession().clear();
    Query<InvPlanGoals> query = TcInvest.tcInvestDbConnection.getSession().createQuery(
      "from InvPlanInstance where id=:id and active=:active", InvPlanGoals.class);
    query.setParameter("id", id);
    query.setParameter("active", 0);
    List<InvPlanGoals> listResult = query.getResultList();

    if (listResult.size() == 1) {
      result = listResult.get(0);
    }

    return result;
  }


  @SuppressWarnings({"unchecked", "deprecation"})
  @Step
  public Integer getPlanIdOnInvPlan(String planCode) {
    Integer i = -1;
    Query<Map<String, Object>> query = TcInvest.tcInvestDbConnection.getSession().createNativeQuery(
      "select a.PLAN_ID from INV_PLAN a where a.PLAN_CODE=:planCode");
    query.setParameter("planCode", planCode);
    List<Map<String, Object>> result = query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
      .getResultList();
    if (result.size() == 1) {
      i = Integer.parseInt(result.get(0).get("PLAN_ID").toString());
    }
    return i;
  }

  @Step
  public void removeAllPlanOfCustomer(String customerId) {
    TcInvest.tcInvestDbConnection.getSession().clear();
    TcInvest.tcInvestDbConnection.getSession().beginTransaction();
    Query query = TcInvest.tcInvestDbConnection.getSession().createQuery(
      "delete from InvPlanInstance where customerId=:customerId");
    query.setParameter("customerId", customerId);
    int numberRowDeleted = query.executeUpdate();
    TcInvest.tcInvestDbConnection.getSession().getTransaction().commit();
    System.out.println("so data bi xoa: " + numberRowDeleted);
  }

  @Step
  public void removeAllPlanOfCustomerById(String id) {
    TcInvest.tcInvestDbConnection.getSession().clear();
    TcInvest.tcInvestDbConnection.getSession().beginTransaction();
    Query query = TcInvest.tcInvestDbConnection.getSession().createQuery(
      "delete from InvPlanInstance where id=:id");
    query.setParameter("id", id);
    int numberRowDeleted = query.executeUpdate();
    TcInvest.tcInvestDbConnection.getSession().getTransaction().commit();
    System.out.println("so data bi xoa: " + numberRowDeleted);
  }

  @Step
  public void removeAllPlanOfRm(String agencyId) {
    TcInvest.tcInvestDbConnection.getSession().clear();
    TcInvest.tcInvestDbConnection.getSession().beginTransaction();
    Query query = TcInvest.tcInvestDbConnection.getSession().createQuery(
      "delete from InvPlanInstance where agencyId=:agencyId");
    query.setParameter("agencyId", agencyId);
    int numberRowDeleted = query.executeUpdate();
    TcInvest.tcInvestDbConnection.getSession().getTransaction().commit();
    System.out.println("so data bi xoa: " + numberRowDeleted);
  }

  @Step
  public void updateStateOfPlan(String planInstanceId, String state) {
    TcInvest.tcInvestDbConnection.getSession().clear();
    TcInvest.tcInvestDbConnection.getSession().beginTransaction();
    Query query = TcInvest.tcInvestDbConnection.getSession().createQuery(
      "update InvPlanInstance i set i.state=:state  where i.planInstanceId=:planInstanceId");
    query.setParameter("planInstanceId", planInstanceId);
    query.setParameter("state", state);
    int numberRowDeleted = query.executeUpdate();
    TcInvest.tcInvestDbConnection.getSession().getTransaction().commit();
    System.out.println("so data da update: " + numberRowDeleted);
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}