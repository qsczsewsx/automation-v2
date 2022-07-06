package com.tcbs.automation.config.idatatcanalysis.futureperf;

import com.tcbs.automation.config.idatatcanalysis.IData;
import net.thucydides.core.annotations.Step;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.HashMap;
import java.util.List;

public class FuturePerfEntity {

  @Step
  public static List<HashMap<String, Object>> getPerfHis(String userId, String date) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT " +
      " TO_CHAR(REPORT_DATE,'yyyy-MM-dd') as REPORT_DATE" +
      " , NULL AS NUMBER_CONTRACT  " +
      " , PORTF_INDEX AS \"INDEX\"" +
      " , ACC_PROFIT" +
      " , DAILY_PROFIT AS PROFIT" +
      " , TSR " +
      " FROM PE_FS_PERFORMANCE_SNAPSHOT" +
      " WHERE USER_ID = :p_user_id" +
      " AND REPORT_DATE >= TO_DATE(:V_REPORT_DATE,'yyyy-MM-dd')" +
      " ORDER BY REPORT_DATE ASC");

    List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) IData.idataDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
      .setParameter("p_user_id", userId)
      .setParameter("V_REPORT_DATE", date)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
      .getResultList();
    IData.idataDbConnection.closeSession();
    return list;
  }


  public static String getPerfReturnMaxDate(String custodyCd, String timeFrame) {

    StringBuilder queryStringBuilder = new StringBuilder();
    if ("D".equalsIgnoreCase(timeFrame)) {
      queryStringBuilder.append("SELECT TO_CHAR(MAX(REPORT_DATE),'yyyy-MM-dd') as MAX_DATE " +
        " FROM PE_FS_PERFORMANCE_SNAPSHOT " +
        " WHERE USER_ID = :p_user_id");
    } else {
      queryStringBuilder.append("SELECT TO_CHAR(MAX(EOMONTH),'yyyy-MM-dd') as MAX_DATE" +
        " FROM PE_FS_INDICES_SNAPSHOT " +
        " WHERE USER_ID = :p_user_id");
    }

    List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) IData.idataDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
      .setParameter("p_user_id", custodyCd)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
      .getResultList();
    if (list != null && list.size() == 1) {
      return (String) list.get(0).get("MAX_DATE");
    }
    return null;
  }

  public static List<HashMap<String, Object>> getPerfReturn(String userId, String timeFrame, String maxDate) {
    StringBuilder queryStringBuilder = new StringBuilder();
    if ("D".equalsIgnoreCase(timeFrame)) {
      queryStringBuilder.append("SELECT " +
        " TO_CHAR(REPORT_DATE,'yyyy-MM-dd') AS TIMESTAMP," +
        " DAILY_RETURN AS PORTF_RETURN" +
        " FROM PE_FS_PERFORMANCE_SNAPSHOT " +
        " WHERE REPORT_DATE > TO_DATE(:V_MAX_DATE,'yyyy-MM-dd') -30" +
        " AND USER_ID = :p_user_id" +
        " ORDER BY REPORT_DATE ASC");
    } else {
      queryStringBuilder.append("SELECT " +
        " TO_CHAR(EOMONTH,'yyyy-MM-dd') as TIMESTAMP," +
        " PORTF_MONRET_FACTOR - 1 AS PORTF_RETURN" +
        " FROM PE_FS_INDICES_SNAPSHOT " +
        " WHERE trunc(EOMONTH,'month') > trunc(ADD_MONTHS(TO_DATE(:V_MAX_DATE,'yyyy-MM-dd'), -12), 'MONTH') " +
        " AND USER_ID = :p_user_id" +
        " ORDER BY EOMONTH ASC");
    }
    List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) IData.idataDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
      .setParameter("p_user_id", userId)
      .setParameter("V_MAX_DATE", maxDate)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
      .getResultList();
    return list;
  }


  public static String getPerfRiskMaxDate(String custodyCd) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT TO_CHAR(MAX(EOMONTH),'yyyy-MM-dd') as MAX_DATE" +
      " FROM PE_FS_INDICES_SNAPSHOT " +
      " WHERE USER_ID = :p_user_id");

    List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) IData.idataDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
      .setParameter("p_user_id", custodyCd)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
      .getResultList();
    if (list != null && list.size() == 1) {
      return (String) list.get(0).get("MAX_DATE");
    }
    return null;
  }

  public static List<HashMap<String, Object>> getPerfRisk(String userId, String maxDate) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT" +
      " TO_CHAR(EOMONTH,'yyyy-MM-dd') as EOMONTH," +
      " CASE" +
      " WHEN abs(VALUE_AT_RISK) <= 0.005" +
      " THEN 1" +
      " WHEN abs(VALUE_AT_RISK) > 0.005 AND abs(VALUE_AT_RISK) <= 0.012" +
      " THEN 2" +
      " WHEN abs(VALUE_AT_RISK) > 0.012 AND abs(VALUE_AT_RISK) <= 0.02" +
      " THEN 3" +
      " WHEN abs(VALUE_AT_RISK) > 0.02 AND abs(VALUE_AT_RISK) <= 0.027" +
      " THEN 4" +
      " WHEN abs(VALUE_AT_RISK) >0.027AND abs(VALUE_AT_RISK) <= 0.039" +
      " THEN 5" +
      " WHEN abs(VALUE_AT_RISK) > 0.039 AND abs(VALUE_AT_RISK) <= 0.054" +
      " THEN 6" +
      " WHEN abs(VALUE_AT_RISK) > 0.054 AND abs(VALUE_AT_RISK) <= 0.077" +
      " THEN 7" +
      " WHEN abs(VALUE_AT_RISK) > 0.077 AND abs(VALUE_AT_RISK) <= 0.155" +
      " THEN 8" +
      " WHEN abs(VALUE_AT_RISK) > 0.155 AND abs(VALUE_AT_RISK) <= 0.233" +
      " THEN 9" +
      " WHEN abs(VALUE_AT_RISK) > 0.233" +
      " THEN 10" +
      " ELSE 10" +
      " END AS RISK_SCORE" +
      " FROM PE_USER_INDICES_SNAPSHOT" +
      " WHERE trunc(EOMONTH,'month') > ADD_MONTHS(TO_DATE(:V_MAX_DATE,'yyyy-MM-dd'), -12)" +
      " AND EOMONTH <= TO_DATE(:V_MAX_DATE,'yyyy-MM-dd')" +
      " AND USER_ID = :p_user_id" +
      " ORDER BY EOMONTH ASC");

    List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) IData.idataDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
      .setParameter("p_user_id", userId)
      .setParameter("V_MAX_DATE", maxDate)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
      .getResultList();
    return list;
  }

}
