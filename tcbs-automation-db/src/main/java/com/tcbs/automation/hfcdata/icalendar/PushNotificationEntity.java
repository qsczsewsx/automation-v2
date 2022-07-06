package com.tcbs.automation.hfcdata.icalendar;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PushNotificationEntity {

  public static final HibernateEdition hfc = Database.HFC_DATA.getConnection();

  @Step("get event to push kafka")
  public static List<HashMap<String, Object>> getEventToPushKafka(){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT * FROM ICAL_EV_MARKET iem WHERE TRUNC(EVENT_DATE) IN (SELECT TRUNC(SYSDATE)+t1.REMINDER_DAYS FROM DUAL LEFT JOIN (SELECT DISTINCT REMINDER_DAYS FROM ICAL_NOTI_CONFIG WHERE ACTIVED =1) t1 ON 1 =1) ")
      .append(" AND CONF_ID IN (SELECT EV_CONF_ID FROM ICAL_NOTI_CONFIG inc WHERE ACTIVED = 1 AND ACTIVED_MTL = 1) ");
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

  @Step("get customer to push notification")
  public static List<HashMap<String, Object>> getListCustomer(String yesterday){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT * FROM ICAL_AS_STOCK_LATEST iass WHERE TO_CHAR(DATEREPORT , 'yyyy-mm-dd') = :yesterday ");
    try {
      List<HashMap<String, Object>> resultList = hfc.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("yesterday", yesterday)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get customer information")
  public static List<HashMap<String, Object>> getCusInfo(String cusId){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT * FROM ICAL_AS_STOCK_LATEST iass WHERE ETLCURDATE = (SELECT max(ETLCURDATE) FROM ICAL_AS_STOCK_SNAPSHOT ) AND TCBSID = :cusId ")
      .append(" AND TICKER IN (SELECT MARKET_CODE FROM ICAL_EV_MARKET iem WHERE EVENT_DATE BETWEEN TRUNC(SYSDATE) AND TRUNC(SYSDATE) + 10 AND EVENT_TYPE = 'STOCK' ")
      .append(" AND CONF_ID IN (SELECT DISTINCT EV_CONF_ID FROM ICAL_NOTI_CONFIG inc WHERE ACTIVED = 1 AND ACTIVED_MTL = 1)) ");
    try {
      List<HashMap<String, Object>> resultList = hfc.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("cusId", cusId)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get ticker details")
  public static List<HashMap<String, Object>> getTickerDetail(List<String> ticker){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT * FROM ICAL_EVM_EXT_STOCK WHERE TICKER in :ticker AND ((TRUNC(EXRIGHT_DATE) = TRUNC(SYSDATE) + 1) OR (TRUNC(EXRIGHT_DATE) = TRUNC(SYSDATE) + 7)) ");
    try {
      List<HashMap<String, Object>> resultList = hfc.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("ticker", ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data inbox")
  public static List<HashMap<String, Object>> getDataNotification(List<String> ticker){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT * FROM ICAL_NOTI_CONFIG_INBOX inci ")
      .append(" WHERE ID IN (SELECT INBOX_ID FROM (SELECT * FROM ICAL_NOTI_CONFIG WHERE ACTIVED = 1 AND ACTIVED_MTL = 1) ")
      .append(" WHERE EV_CONF_ID IN (SELECT iec.ID FROM ICAL_EV_CONFIG iec WHERE  ")
      .append(" EVENT_CODE  IN (SELECT DISTINCT EVENT_CODE FROM ICAL_EVM_EXT_STOCK iees WHERE TICKER IN  :ticker AND ((TRUNC(EXRIGHT_DATE) = TRUNC(SYSDATE) + 1) OR (TRUNC(EXRIGHT_DATE) = TRUNC(SYSDATE) + 7)) ))) ");
    try {
      List<HashMap<String, Object>> resultList = hfc.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("ticker", ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get event config")
  public static List<HashMap<String, Object>> getEventConfig(){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT TITLE, EVENT_CODE  FROM ICAL_NOTI_CONFIG_INBOX inci INNER JOIN ICAL_NOTI_CONFIG inc ON INCI.ID = inc.INBOX_ID INNER JOIN ICAL_EV_CONFIG iec ON inc.EV_CONF_ID = iec.ID ");
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

}
