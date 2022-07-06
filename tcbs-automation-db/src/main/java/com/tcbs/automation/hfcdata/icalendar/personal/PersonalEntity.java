package com.tcbs.automation.hfcdata.icalendar.personal;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PersonalEntity {
  private static final Logger logger = LoggerFactory.getLogger(PersonalEntity.class);
  public static final HibernateEdition hfc = Database.HFC_DATA.getConnection();

  @Step("Get data personal event list")
  public static List<HashMap<String, Object>> getEventPersonalShort(String tcbsId, String lang,
                                                                    String fromDate, String toDate,
                                                                    String fromYear, String toYear) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("  WITH PER AS ( ");
    queryBuilder.append("     SELECT DEF_TYPE, EVENT_DATE AS ORG_DATE, TCBSID, ");
    queryBuilder.append("     TO_DATE(''||:fromYear||SUBSTR(TO_CHAR(EVENT_DATE,'YYYYMMDD'),5,4),'YYYYMMDD') AS EVENT_DATE ");
    queryBuilder.append("     FROM ICAL_EV_PERSONAL ");
    queryBuilder.append("     WHERE tcbsid = :tcbsId ");
    queryBuilder.append("     AND TO_DATE(''||:fromYear||SUBSTR(TO_CHAR(EVENT_DATE,'YYYYMMDD'),5,4),'YYYYMMDD') ");
    queryBuilder.append("     BETWEEN TO_DATE(:fromDate, 'yyyy-MM-dd') AND TO_DATE(:toDate, 'yyyy-MM-dd') ");
    queryBuilder.append("     UNION ");
    queryBuilder.append("     SELECT DEF_TYPE, EVENT_DATE AS ORG_DATE, TCBSID, ");
    queryBuilder.append("     TO_DATE(''||:toYear||SUBSTR(TO_CHAR(EVENT_DATE,'YYYYMMDD'),5,4),'YYYYMMDD') AS EVENT_DATE ");
    queryBuilder.append("     FROM ICAL_EV_PERSONAL ");
    queryBuilder.append("     WHERE tcbsid = :tcbsId ");
    queryBuilder.append("     AND TO_DATE(''||:toYear||SUBSTR(TO_CHAR(EVENT_DATE,'YYYYMMDD'),5,4),'YYYYMMDD') ");
    queryBuilder.append("     BETWEEN TO_DATE(:fromDate, 'yyyy-MM-dd') AND TO_DATE(:toDate, 'yyyy-MM-dd')) ");
    queryBuilder.append(" SELECT PER.DEF_TYPE, TCBSID, TO_CHAR(EVENT_DATE,'YYYY-MM-DD') AS EVENT_DATE, ");
    queryBuilder.append(" EXTRACT(YEAR FROM EVENT_DATE) - EXTRACT(YEAR FROM ORG_DATE) AS MILE_STONE , ");
    queryBuilder.append(" (CASE  WHEN upper(:lang) = 'EN' THEN CONF.EVENT_NAME_EN   ELSE CONF.EVENT_NAME_VI  END ) AS EVENT_NAME ");
//    queryBuilder.append(" (CASE  WHEN upper(:lang) = 'EN' THEN CONF.EVENT_DESC_EN   ELSE CONF.EVENT_DESC_VI  END ) AS EVENT_DESC ");
    queryBuilder.append(" FROM PER ");
    queryBuilder.append(" INNER JOIN HFC_DATA_SIT.ICAL_EV_CONFIG CONF ");
    queryBuilder.append(" ON PER.DEF_TYPE = CONF.DEF_TYPE ");
    queryBuilder.append(" AND CONF.ACTIVE = 1 ");
    try {
      List<HashMap<String, Object>> resultList = hfc.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("lang", lang)
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setParameter("fromYear", fromYear)
        .setParameter("toYear", toYear)
        .setParameter("tcbsId", tcbsId)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
      ex.printStackTrace();
    }
    return new ArrayList<>();
  }

  @Step("Get data personal event list")
  public static List<HashMap<String, Object>> getEventPersonalEventOption(String role, String tcbsId,
                                                                          String fromDate, String toDate, int page, String lang) throws ParseException {

    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT PER.DEF_TYPE " +
      ",TO_CHAR(PER.EVENT_DATE,'yyyy-MM-dd') AS EVENT_DATE " +
      ", (EXTRACT(YEAR FROM CURRENT_DATE) - EXTRACT(YEAR FROM PER.EVENT_DATE) + 1) AS MILE_STONE " +
      ", PER.TCBSID AS TCBS_ID" +
      ",( CASE  WHEN upper(:p_lang) = 'EN' THEN CONF.EVENT_NAME_EN   ELSE CONF.EVENT_NAME_VI  END ) AS EVENT_NAME " +
      ",( CASE  WHEN upper(:p_lang) = 'EN' THEN CONF.EVENT_DESC_EN   ELSE CONF.EVENT_DESC_VI  END ) AS EVENT_DESC " +
      ",CONF.EVENT_TYPE " +
      ",PER.TCBSID " +
      " FROM HFC_DATA_SIT.ICAL_EV_PERSONAL PER " +
      " INNER JOIN HFC_DATA_SIT.ICAL_EV_CONFIG CONF " +
      " ON PER.DEF_TYPE = CONF.DEF_TYPE " +
      " AND PER.TCBSID IN (:p_listTcbId) " +
      " AND CONF.ACTIVE = 1 ");
    logger.info(queryBuilder.toString());
    try {
      List<String> listTcbsId;
      if (role.equalsIgnoreCase("RM")) {
        listTcbsId = getListTcbsIdFromDB(tcbsId);

      } else {
        listTcbsId = new ArrayList<>();
        listTcbsId.add(tcbsId);
      }
      String[] listTcbsIdString = listTcbsId.toArray(new String[0]);
      logger.info("listTcbsIdString : {}", listTcbsIdString);
      List<HashMap<String, Object>> resultList = hfc.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("p_lang", lang)
        .setParameterList("p_listTcbId", listTcbsIdString)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
      ex.printStackTrace();
    }
    return new ArrayList<>();
  }

  public static List<String> getListTcbsIdFromDB(String tcbsId) {
    List list = new ArrayList();
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT CUS_TCBSID FROM HFC_DATA_SIT.ICAL_EVM_FIL_RM_INFO WHERE RM_TCBSID = :p_tcbsId");
    try {
      List<HashMap<String, Object>> resultList = hfc.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("p_tcbsId", tcbsId)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();

      if (resultList == null || resultList.size() == 0) {
        resultList = new ArrayList<>();
        HashMap<String, Object> item = new HashMap<>();
        item.put("CUS_TCBSID", tcbsId);
        list.add(item.get("CUS_TCBSID").toString());
        return list;
      }


      for (HashMap<String, Object> item : resultList) {
        list.add(item.get("CUS_TCBSID").toString());
      }
      return list;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
