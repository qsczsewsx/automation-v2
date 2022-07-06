package com.tcbs.automation.hfcdata.icalendar;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MakeTimelineInQueueEntity {

  public static final HibernateEdition hfc = Database.HFC_DATA.getConnection();
  public static final HibernateEdition timelineConnection = Database.ICALENDAR_TIMELINE.getConnection();

  @Step("get data for kafka")
  public static List<HashMap<String, Object>> getDataForKafka(List<String> defId){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select * from timeline_def td where id in :defId ");
    try {
      List<HashMap<String, Object>> resultList = timelineConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("defId", defId)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get event to push noti")
  public static List<HashMap<String, Object>> getEventFromMakeTimeline(String currentDate){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" Select * from ICAL_MTL_TIMELINE where TO_CHAR(REMINDER_DATE, 'yyyy-MM-dd') = :currentDate and DEF_STATUS = 'ACTIVE' ");
    try {
      List<HashMap<String, Object>> resultList = hfc.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("currentDate", currentDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data after update")
  public static List<HashMap<String, Object>> getDataAfterUpdate(String objCode, String eventCode, String newDate){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT * FROM ICAL_EV_MARKET WHERE MARKET_CODE  = :objCode AND EVENT_CODE = :eventCode AND TO_CHAR(EVENT_DATE , 'yyyy-MM-dd') = :newDate  ");
    try {
      List<HashMap<String, Object>> resultList = hfc.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("objCode", objCode)
        .setParameter("eventCode", eventCode)
        .setParameter("newDate", newDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data after update")
  public static List<HashMap<String, Object>> getDataAfterUpdateInTimeline(String objCode, String eventCode, String newDate){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT * FROM ICAL_MTL_TIMELINE WHERE OBJ_CODE  = :objCode AND EVENT_CODE = :eventCode AND TO_CHAR(EVENT_DATE , 'yyyy-MM-dd') = :newDate  ");
    try {
      List<HashMap<String, Object>> resultList = hfc.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("objCode", objCode)
        .setParameter("eventCode", eventCode)
        .setParameter("newDate", newDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data after update")
  public static List<HashMap<String, Object>> getOldDataAfterUpdateInTimeline(String objCode, String eventCode, String oldDate){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT * FROM ICAL_MTL_TIMELINE WHERE OBJ_CODE  = :objCode AND EVENT_CODE = :eventCode AND TO_CHAR(EVENT_DATE , 'yyyy-MM-dd') = :oldDate  ");
    try {
      List<HashMap<String, Object>> resultList = hfc.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("objCode", objCode)
        .setParameter("eventCode", eventCode)
        .setParameter("oldDate", oldDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get first row make timeline")
  public static List<HashMap<String, Object>> getFirstRowTimeline(String currentDate){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT * FROM ICAL_MTL_TIMELINE WHERE TO_CHAR(LAST_UPDATE, 'yyyy-MM-dd') = :currentDate FETCH FIRST ROW ONLY ");
    try {
      List<HashMap<String, Object>> resultList = hfc.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("currentDate", currentDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get event make timeline")
  public static List<HashMap<String, Object>> getEventMakeTimeline(){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT * FROM ICAL_EV_MARKET iem WHERE EVENT_DATE BETWEEN TRUNC(SYSDATE) AND TRUNC(SYSDATE) + 10 ")
      .append(" AND CONF_ID IN (SELECT DISTINCT EV_CONF_ID FROM ICAL_NOTI_CONFIG inc WHERE ACTIVED = 1 AND ACTIVED_MTL = 1) ");
    try {
      List<HashMap<String, Object>> resultList = hfc.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get event retry make timeline")
  public static List<HashMap<String, Object>> getEventMakeTimelineRetry(){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT * FROM ICAL_MTL_TIMELINE WHERE TRUNC(LAST_UPDATE) = TRUNC(SYSDATE)");
    try {
      List<HashMap<String, Object>> resultList = hfc.getSession().createNativeQuery(queryBuilder.toString())
              .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
              .getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("delete data in queue")
  public static void deleteDataInQueue(String currentDate){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" DELETE FROM ICAL_MTL_QUEUE WHERE TO_CHAR(PROCESSING_DATE, 'yyyy-mm-dd') = '").append(currentDate).append("'");
    executeQuery(queryBuilder);
  }

  @Step("delete data in timeline")
  public static void deleteDataInTimeline(String currentDate){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" DELETE FROM ICAL_MTL_TIMELINE WHERE TO_CHAR(LAST_UPDATE, 'yyyy-MM-dd') = '").append(currentDate).append("'");
    executeQuery(queryBuilder);
  }

  @Step("get data")
  public static List<HashMap<String, Object>> getDataMakeTimelineTbl(String currentDate){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT * FROM ICAL_MTL_TIMELINE WHERE TO_CHAR(LAST_UPDATE, 'yyyy-MM-dd') = :currentDate");
    try {
      List<HashMap<String, Object>> resultList = hfc.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("currentDate", currentDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static List<HashMap<String, Object>> getDataMakeTimelineActive(String currentDate){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT * FROM ICAL_MTL_TIMELINE WHERE TO_CHAR(LAST_UPDATE, 'yyyy-MM-dd') = :currentDate AND DEF_STATUS = 'ACTIVE' ");
    try {
      List<HashMap<String, Object>> resultList = hfc.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("currentDate", currentDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Update data")
  public static void updateDataTest(String eventCode, String marketCode, String eventDate, String currentDate){
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(" UPDATE ICAL_EV_MARKET SET EVENT_DATE = TO_DATE('").append(eventDate).append("', 'yyyy-mm-dd') ").append(" WHERE MARKET_CODE  = '").append(marketCode).append(
      "' AND EVENT_CODE = '").append(eventCode).append("' AND TO_CHAR(EVENT_DATE , 'yyyy-MM-dd') = '").append(currentDate).append("' ");
    executeQuery(stringBuilder);
  }

  @Step("execute query")
  public static void executeQuery(StringBuilder sql) {
    Session session = hfc.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = session.createNativeQuery(sql.toString());
    try {
      query.executeUpdate();
      session.getTransaction().commit();
      hfc.closeSession();
    } catch (Exception e) {
      hfc.closeSession();
      throw e;
    }
  }

}
