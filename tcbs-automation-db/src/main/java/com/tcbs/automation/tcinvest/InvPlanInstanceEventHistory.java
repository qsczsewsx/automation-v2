package com.tcbs.automation.tcinvest;

import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "INV_PLAN_INSTANCE_EVENT_HISTORY")
public class InvPlanInstanceEventHistory {
  @Id
  @Column(name = "ID")
  private int id;
  @Column(name = "EVENT_ID")
  private String eventId;
  @Column(name = "PLAN_INSTANCE_ID")
  private String planInstanceId;
  @Column(name = "EVENT_DATA")
  private String eventData;
  @Column(name = "CREATED_DATE")
  private String createDate;
  @Column(name = "UPDATED_DATE")
  private String updateDate;
  @Column(name = "EVENT_DATE")
  private String eventDate;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "ROOT_EVENT_ID")
  private String rootEventId;

  public InvPlanInstanceEventHistory() {
  }

  public InvPlanInstanceEventHistory(int id, String eventId, String planInstanceId, String eventData,
                                     String createDate,
                                     String updateDate, String eventDate, String status, String rootEventId) {
    super();
    this.id = id;
    this.eventId = eventId;
    this.planInstanceId = planInstanceId;
    this.eventData = eventData;
    this.createDate = createDate;
    this.updateDate = updateDate;
    this.eventDate = eventDate;
    this.status = status;
    this.rootEventId = rootEventId;
  }

  public String getRootEventId() {
    return rootEventId;
  }

  public void setRootEventId(String rootEventId) {
    this.rootEventId = rootEventId;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getEventId() {
    return eventId;
  }

  public void setEventId(String eventId) {
    this.eventId = eventId;
  }

  public String getPlanInstanceId() {
    return planInstanceId;
  }

  public void setPlanInstanceId(String planInstanceId) {
    this.planInstanceId = planInstanceId;
  }

  public String getEventData() {
    return eventData;
  }

  public void setEventData(String eventData) {
    this.eventData = eventData;
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

  public String getEventDate() {
    return eventDate;
  }

  public void setEventDate(String eventDate) {
    this.eventDate = eventDate;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @Step
  public InvPlanInstanceEventHistory getDataById(String id, String eventDate) throws Exception {
    InvPlanInstanceEventHistory result = null;
    Query<InvPlanInstanceEventHistory> query = TcInvest.tcInvestDbConnection.getSession().createQuery(
      "from InvPlanInstanceEventHistory where planInstanceId=:id and eventDate=:eventDate", InvPlanInstanceEventHistory.class);
    query.setParameter("id", id);
    query.setParameter("eventDate", eventDate);
    List<InvPlanInstanceEventHistory> listResult = query.getResultList();
    if (listResult.size() == 1) {
      result = listResult.get(0);
    }
    return result;
  }

  @Step
  public List<HashMap<String, Object>> getPlanInstanceIdByEventDate(String eventDate, String bondCode) throws Exception {
    Query<HashMap<String, Object>> query = TcInvest.tcInvestDbConnection.getSession().createNativeQuery(
      " select a.CUSTOMER_ID as  customerId, a.PLAN_INSTANCE_DATA as planData, b.EVENT_DATA as eventData, b.EVENT_DATE as eventDate from INV_PLAN_INSTANCE a\n" +
        "    join INV_Plan_Instance_Event_History  b on a.PLAN_INSTANCE_ID =b.PLAN_INSTANCE_ID\n" +
        "                     where a.PLAN_INSTANCE_ID in\n" +
        "                    (select PLAN_INSTANCE_ID from INV_Plan_Instance_Event_History\n" +
        "                    where EVENT_DATE=:eventDate) and a.PLAN_INSTANCE_DATA like '%\"bondCode\":\":bondCode\"%';");
    query.setParameter("eventDate", eventDate);
    query.setParameter("bondCode", bondCode);
    return query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
  }

  @SuppressWarnings({"deprecation", "unchecked"})
  @Step
  public Map<String, Object> getDataFromInvOrderAttr(String orderId, String name) throws ParseException {
    String query = "select att.VALUE from INV_ORDER_ATTR att "
      + "    where att.ORDER_ID = :orderId "
      + "      and att.NAME = :name";
    List<Map<String, Object>> result = TcInvest.tcInvestDbConnection.getSession().createNativeQuery(query)
      .setParameter("name", name)
      .setParameter("orderId", orderId)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    if (result.size() > 0) {
      return result.get(0);
    } else {
      return null;
    }
  }

  @SuppressWarnings({"deprecation", "unchecked"})
  @Step
  public Map<String, Object> getDataFromInvOrder(String planId, String couponDate) throws ParseException {
    InvHoliday invHoliday = new InvHoliday();
    String query = "select ORDER_ID, TOTAL_VALUE, VOLUME, STATUS from INV_ORDER where PLAN_INS_ID = :planId "
      + " and ORDER_TIMESTAMP BETWEEN :startDate AND :endDate"
      + " order by ORDER_ID desc";
    List<Map<String, Object>> result = TcInvest.tcInvestDbConnection.getSession().createNativeQuery(query)
      .setParameter("planId", planId)
      .setParameter("startDate", invHoliday.getMatchedDateByWorkingDate(couponDate, -3))
      .setParameter("endDate", invHoliday.getMatchedDateByWorkingDate(couponDate, 30))
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    if (result.size() > 0) {
      return result.get(0);
    } else {
      return null;
    }
  }

  @SuppressWarnings({"deprecation", "unchecked"})
  @Step
  public List<HashMap<String, Object>> getDataFromViewEventAuditLog(String nameParam, String inputValue) {
    String query = "select evh.ID, evh.PLAN_INSTANCE_ID, evh.EVENT_ID, evh.IS_MANUAL_RETRY, evh.ACTOR_ID, evh.PRODUCT_CODE, evh.STATUS, evh.EVENT_DATE, evh.EVENT_DATA, evh.TYPE, evh.GOAL_ID, \n" +
      "evh.ROOT_EVENT_ID, evh.CREATED_DATE, ipi.CUSTOMER_ID, ip.PLAN_NAME, ip.PLAN_CODE, ipi.PLAN_INSTANCE_DATA\n" +
      " from EVENT_AUDIT_LOG evh inner join INV_PLAN_INSTANCE ipi on evh.PLAN_INSTANCE_ID = ipi.PLAN_INSTANCE_ID join INV_PLAN ip on ip.PLAN_ID = ipi.PLAN_ID \n" +
      "and " + nameParam + "= :inputValue order by CREATED_DATE desc";
    List<HashMap<String, Object>> result = TcInvest.tcInvestDbConnection.getSession().createNativeQuery(query)
      .setParameter("inputValue", inputValue)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }

}
