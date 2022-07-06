package com.tcbs.automation.tcinvest;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.thucydides.core.annotations.Step;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "INV_PLAN_INSTANCE")
public class InvPlanInstance {
  @Id
  @Column(name = "ID")
  private String id;
  @Column(name = "PLAN_INSTANCE_ID")
  private String planInstanceId;
  @Column(name = "PLAN_ID")
  private Integer planId;
  @Column(name = "CUSTOMER_ID")
  private String customerId;
  @Column(name = "PLAN_INSTANCE_DATA")
  private String planInstanceData;
  @Column(name = "AGENCY_ID")
  private String agencyId;
  @Column(name = "STATE")
  private String state;
  @Column(name = "ACTIVE")
  private Integer active;
  @Column(name = "START_DATE")
  private String startDate;
  @Column(name = "END_DATE")
  private String endDate;
  @Column(name = "CREATED_DATE")
  private String createDate;
  @Column(name = "UPDATED_DATE")
  private String updateDate;
  @Column(name = "TIMELINE_ID")
  private String timelineId;

  public InvPlanInstance(String id, String planInstanceId, Integer planId, String customerId, String planInstanceData,
                         String agencyId, String state, Integer active, String startDate, String endDate, String createDate,
                         String updateDate, String timelineId) {
    super();
    this.id = id;
    this.planInstanceId = planInstanceId;
    this.planId = planId;
    this.customerId = customerId;
    this.planInstanceData = planInstanceData;
    this.agencyId = agencyId;
    this.state = state;
    this.active = active;
    this.startDate = startDate;
    this.endDate = endDate;
    this.createDate = createDate;
    this.updateDate = updateDate;
    this.timelineId = timelineId;
  }

  public InvPlanInstance() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getPlanInstanceId() {
    return planInstanceId;
  }

  public void setPlanInstanceId(String planInstanceId) {
    this.planInstanceId = planInstanceId;
  }

  public Integer getPlanId() {
    return planId;
  }

  public void setPlanId(Integer planId) {
    this.planId = planId;
  }

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public String getPlanInstanceData() {
    return planInstanceData;
  }

  public void setPlanInstanceData(String planInstanceData) {
    this.planInstanceData = planInstanceData;
  }

  public String getAgencyId() {
    return agencyId;
  }

  public void setAgencyId(String agencyId) {
    this.agencyId = agencyId;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public Integer getActive() {
    return active;
  }

  public void setActive(Integer active) {
    this.active = active;
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

  public String getCreateDate() {
    return createDate;
  }

  public void setCreateDate(String createDate) {
    this.createDate = createDate;
  }

  public String getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(String updateDate) {
    this.updateDate = updateDate;
  }

  public String getTimelineId() {
    return timelineId;
  }

  public void setTimelineId(String timelineId) {
    this.timelineId = timelineId;
  }

  @Step
  public InvPlanInstance getDataById(Integer id) {
    TcInvest.tcInvestDbConnection.getSession().clear();
    Query<InvPlanInstance> query = TcInvest.tcInvestDbConnection.getSession().createQuery(
      "from InvPlanInstance where id=:id", InvPlanInstance.class);
    query.setParameter("id", id);
    return query.getSingleResult();
  }

  @Step
  public InvPlanInstance getDataByCustomerId(String customerId) {
    InvPlanInstance result = null;
    TcInvest.tcInvestDbConnection.getSession().clear();
    Query<InvPlanInstance> query = TcInvest.tcInvestDbConnection.getSession().createQuery(
      "from InvPlanInstance where customerId=:customerId and active=:active and state='RUNNING' and planId=:planId", InvPlanInstance.class);
    query.setParameter("customerId", customerId);
    query.setParameter("active", 1);
    query.setParameter("planId", 8);
    return query.getSingleResult();
  }

  @Step
  public InvPlanInstance getByCustomerId(String customerId) {
    TcInvest.tcInvestDbConnection.getSession().clear();

    Query<InvPlanInstance> query = TcInvest.tcInvestDbConnection.getSession().createQuery(
      "from InvPlanInstance where customerId=:customerId and planId=:planId and state='RUNNING'", InvPlanInstance.class);
    query.setParameter("customerId", customerId);
    query.setParameter("planId", 8);
    return query.getSingleResult();

  }


  @Step
  public InvPlanInstance getDataByPlanInstanceId(String planInstanceId) {
    InvPlanInstance result = null;
    TcInvest.tcInvestDbConnection.getSession().clear();
    Query<InvPlanInstance> query = TcInvest.tcInvestDbConnection.getSession().createQuery(
      "from InvPlanInstance where planInstanceId=:planInstanceId and active=:active ", InvPlanInstance.class);
    query.setParameter("planInstanceId", planInstanceId);
    query.setParameter("active", 1);
    List<InvPlanInstance> listResult = query.getResultList();

    if (listResult.size() == 1) {
      result = listResult.get(0);
    }

    return new InvPlanInstance();
  }

  @Step
  public InvPlanInstance getDataByIdActive(String id) {
    InvPlanInstance result = null;
    TcInvest.tcInvestDbConnection.getSession().clear();
    Query<InvPlanInstance> query = TcInvest.tcInvestDbConnection.getSession().createQuery(
      "from InvPlanInstance where id=:id and active=:active", InvPlanInstance.class);
    query.setParameter("id", id);
    query.setParameter("active", 1);
    List<InvPlanInstance> listResult = query.getResultList();

    if (listResult.size() == 1) {
      result = listResult.get(0);
    }

    return result;
  }

  @Step
  public InvPlanInstance getDataByIdInactive(String id) {
    InvPlanInstance result = null;
    TcInvest.tcInvestDbConnection.getSession().clear();
    Query<InvPlanInstance> query = TcInvest.tcInvestDbConnection.getSession().createQuery(
      "from InvPlanInstance where id=:id and active=:active", InvPlanInstance.class);
    query.setParameter("id", id);
    query.setParameter("active", 0);
    List<InvPlanInstance> listResult = query.getResultList();

    if (listResult.size() == 1) {
      result = listResult.get(0);
    }

    return result;
  }


  @SuppressWarnings({"unchecked", "deprecation"})
  @Step
  public Integer getPlanIdOnInvPlan(String planCode) {
    int i = -1;
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

  @Step("lay tong dau tu cho cac ke hoach TNOD va VLAG co trang thai dang thuc hien")
  public Double getAvailableAmount(String tcbsId) {
    TcInvest.tcInvestDbConnection.getSession().clear();
    Query<InvPlanInstance> query = TcInvest.tcInvestDbConnection.getSession().createQuery(
      "from InvPlanInstance where customerId=:customerId and planId in (5,7) and state = 'RUNNING' and active=1", InvPlanInstance.class);
    query.setParameter("customerId", tcbsId);
    List<InvPlanInstance> list = query.getResultList();
    double availableAmount = 0D;
    ObjectMapper mapper = new ObjectMapper();
    for (InvPlanInstance planInstance : list) {
      String planData = planInstance.getPlanInstanceData();
      HashMap hm = null;
      try {
        hm = mapper.readValue(planData, HashMap.class);
      } catch (IOException e) {
        e.printStackTrace();
      }
      availableAmount += Double.parseDouble(hm.get("totalInvestAmount") == null ? "0" : hm.get("totalInvestAmount").toString());
    }
    return availableAmount;
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
