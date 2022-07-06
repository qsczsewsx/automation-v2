package com.tcbs.automation.tcbond;

import net.thucydides.core.annotations.Step;
import org.hibernate.annotations.Proxy;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "INV_PLAN_DATA")
@Proxy(lazy = true)
public class InvPlanData implements Serializable {
  @Id
  @Column(name = "PLAN_INSTANCE_ID")
  private Integer planInstanceId;
  @Column(name = "CUSTOMER_ID")
  private String customerId;
  @Column(name = "START_DATE")
  private String startDate;
  @Column(name = "PLAN_CODE")
  private String planCode;
  @Column(name = "ACTIVE")
  private Integer active;
  @Column(name = "PLAN_DATA")
  private String planData;
  @Column(name = "syncStatus", columnDefinition = "BIT")
  private Boolean syncStatus;

  public InvPlanData(Integer planInstanceId, String customerId, String startDate, String planCode, Integer active,
                     String planData, Boolean syncStatus) {
    super();
    this.planInstanceId = planInstanceId;
    this.customerId = customerId;
    this.startDate = startDate;
    this.planCode = planCode;
    this.active = active;
    this.planData = planData;
    this.syncStatus = syncStatus;
  }

  public InvPlanData() {
  }

  public Integer getPlanInstanceId() {
    return planInstanceId;
  }

  public void setPlanInstanceId(Integer planInstanceId) {
    this.planInstanceId = planInstanceId;
  }

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getPlanCode() {
    return planCode;
  }

  public void setPlanCode(String planCode) {
    this.planCode = planCode;
  }

  public Integer getActive() {
    return active;
  }

  public void setActive(Integer active) {
    this.active = active;
  }

  public String getPlanData() {
    return planData;
  }

  public void setPlanData(String planData) {
    this.planData = planData;
  }

  public Boolean getSyncStatus() {
    return syncStatus;
  }

  public void setSyncStatus(Boolean syncStatus) {
    this.syncStatus = syncStatus;
  }

  @SuppressWarnings("unchecked")
  public List<InvPlanData> getAllData() throws Exception {
    Query<InvPlanData> query = TcBond.tcBondDbConnection.getSession().createQuery("from InvPlanData", InvPlanData.class);
    List<InvPlanData> result = query.getResultList();
    System.out.println("number data: " + result.size());
    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }

  public InvPlanData getPlanDataById(Integer id) throws Exception {
    Query<InvPlanData> query = TcBond.tcBondDbConnection.getSession().createQuery(
      "from InvPlanData where planInstanceId=:id", InvPlanData.class);
    query.setParameter("id", id);
    List<InvPlanData> result = query.getResultList();
    if (result.size() > 0) {
      return result.get(0);
    } else {
      return null;
    }
  }

  public List<Integer> getListPlanDataIds() {
    Query<Integer> query = TcBond.tcBondDbConnection.getSession().createQuery(
      "select ipd.planInstanceId from InvPlanData ipd order by ipd.planInstanceId asc", Integer.class);
    List<Integer> result = query.getResultList();
    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }

  @SuppressWarnings({"unchecked", "deprecation"})
  @Step
  public Map<String, Object> getPlanDataFromTcbond(String planInstanceId) throws Exception {
    String query = "SELECT   b.code as bondcode, bt.TradingCode, bt.TradingDate, bt.id as bondOrderId,  bp.Code as bondProductCode, CAST(attr.VALUE as VARCHAR) as planInstanceId,\r\n"
      + "bt.AgencyId as agencyId, bt.CustomerId as customerId FROM   trading bt\r\n"
      + "        inner JOIN (SELECT ioa.*, ipd.syncStatus\r\n"
      + "                     FROM   inv_order_attr ioa\r\n"
      + "                            JOIN inv_plan_data ipd\r\n"
      + "                              ON ioa.value = cast(ipd.plan_instance_id as nvarchar)\r\n"
      + "                     WHERE  ioa.NAME = 'PlanInstanceID'\r\n"
      + "                           AND plan_code = 'CBF') attr\r\n"
      + "                on attr.ORDER_ID = bt.ID\r\n"
      + "        inner join BondProduct BP on bt.BondProductId = BP.ID\r\n"
      + "        inner join Bond B on BP.BondID = B.ID\r\n"
      + "    where attr.VALUE = :planInstanceId";
    List<Map<String, Object>> result = TcBond.tcBondDbConnection.getSession().createNativeQuery(query)
      .setParameter("planInstanceId", planInstanceId)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    if (result.size() == 1) {
      return result.get(0);
    } else {
      return null;
    }

  }

  @SuppressWarnings({"unchecked", "deprecation"})
  @Step
  public String getExpiredDate(String bondCode) throws Exception {
    String query = "select ExpiredDate from bond where code = :bondCode";
    List<Map<String, Object>> result = TcBond.tcBondDbConnection.getSession().createNativeQuery(query).setParameter("bondCode", bondCode)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    if (result.size() == 1) {
      return result.get(0).get("ExpiredDate").toString();
    } else {
      return null;
    }
  }

}
