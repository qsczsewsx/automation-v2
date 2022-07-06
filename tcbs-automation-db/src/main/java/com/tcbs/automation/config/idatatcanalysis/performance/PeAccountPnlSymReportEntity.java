package com.tcbs.automation.config.idatatcanalysis.performance;

import com.tcbs.automation.config.idatatcanalysis.IData;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PeAccountPnlSymReportEntity {
  public static String P_ACCOUNT_ID = "p_account_id";
  public static String V_MIN_ACCOUNT_DATE = "v_min_account_date";
  public static String V_CUSTODY_ID = "v_custody_id";
  public static String V_CURRENT_DATE = "V_CURRENT_DATE";

  public static List<HashMap<String, Object>> getSymReportPerformanceByAccountNo(String accountNo) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT * " +
      " FROM PE_ACCOUNT_PNL_SYM_REPORT  WHERE ACCOUNT_NO = :ACCOUNT_NO");
    try {
      return IData.idataDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("ACCOUNT_NO", accountNo)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }


  @Step("Truncate table")
  public static void truncateTable() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("TRUNCATE TABLE PE_ACCOUNT_PNL_SYM_REPORT");
    executeQuery(queryBuilder);
  }

  @Step(" Call proc ")
  public static void callProc(String proc, String date) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("CALL " + proc + "('" + date + "')");
    executeQuery(queryBuilder);
  }

  @Step("execute query")
  public static void executeQuery(StringBuilder sql) {
    Session session = IData.idataDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = session.createNativeQuery(sql.toString());
    query.executeUpdate();
    session.getTransaction().commit();
    IData.idataDbConnection.closeSession();
  }


  public static List<HashMap<String, Object>> getAllPerformanceByAccountNo(String accountNo) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT ACCOUNT_ID," +
      "USER_ID," +
      "TO_CHAR(T1W_REPORT_DATE, 'yyyy-mm-dd') as T1W_REPORT_DATE," +
      "T1W_RETURN_VALUE," +
      "T1W_RETURN_PCT," +
      "TO_CHAR(T1M_REPORT_DATE, 'yyyy-mm-dd') as T1M_REPORT_DATE," +
      "T1M_RETURN_VALUE," +
      "T1M_RETURN_PCT," +
      "TO_CHAR(T3M_REPORT_DATE, 'yyyy-mm-dd') as T3M_REPORT_DATE," +
      "T3M_RETURN_VALUE," +
      "T3M_RETURN_PCT," +
      "TO_CHAR(T6M_REPORT_DATE, 'yyyy-mm-dd') as T6M_REPORT_DATE," +
      "T6M_RETURN_VALUE," +
      "T6M_RETURN_PCT," +
      "TO_CHAR(T1Y_REPORT_DATE, 'yyyy-mm-dd') as T1Y_REPORT_DATE," +
      "T1Y_RETURN_VALUE," +
      "T1Y_RETURN_PCT," +
      "TO_CHAR(T3Y_REPORT_DATE, 'yyyy-mm-dd') as T3Y_REPORT_DATE," +
      "T3Y_RETURN_VALUE," +
      "T3Y_RETURN_PCT," +
      "TO_CHAR(ALL_REPORT_DATE, 'yyyy-mm-dd') as ALL_REPORT_DATE," +
      "ALL_RETURN_VALUE," +
      "ALL_RETURN_PCT," +
      "TOTAL_ESTIMATED_REVENUE FROM PE_ICA_PNL_SYM WHERE ACCOUNT_ID = :ACCOUNT_ID");
    try {
      return IData.idataDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("ACCOUNT_ID", accountNo)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }


  public static List<HashMap<String, Object>> getListAccountId() {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT" +
      "           TRUNC(COALESCE(MIN(" +
      "               CASE WHEN api.CREATED_TIME > sia.CREATED_TIME THEN sia.CREATED_TIME" +
      "               ELSE api.CREATED_TIME END" +
      "            ), CURRENT_DATE)) AS v_min_account_date," +
      "           api.CUSTODY_ID AS v_custody_id, api.CUSTODY_ID as USER_ID," +
      "           ACCNO AS p_account_id," +
      "         ACCNO AS ACCOUNT_ID" +
      "        FROM ACCOUNT_PUBLIC_INFO api" +
      "        LEFT JOIN STG_ICOPY_ACCOUNT sia on " +
      "            api.ACCNO = sia.ACCOUNT_NO" +
      " WHERE api.PUBLIC_IN = 'TCI3' AND ACCNO != 'ALL'" +
      "        GROUP BY api.CUSTODY_ID,api.ACCNO");

    List list = IData.idataDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
      .getResultList();
    return (List<HashMap<String, Object>>) list;
  }


  public static void oneWeekMonthYear(HashMap<String, Object> originalMap, String date, String minDate, String one, String two, String three) {
    Timestamp v_min_account_date = (Timestamp) originalMap.get(V_MIN_ACCOUNT_DATE.toUpperCase());
    String p_account_id = (String) originalMap.get(P_ACCOUNT_ID.toUpperCase());
    String v_custody_id = (String) originalMap.get(V_CUSTODY_ID.toUpperCase());

    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT PORTF_INDEX as " + one + " ,ACC_PROFIT as " + two + ", REPORT_DATE as " + three +
      "        FROM (\n" +
      "            SELECT pps.PORTF_INDEX, pps.ACC_PROFIT, pps.REPORT_DATE\n" +
      "            FROM PE_ICA_PERFORMANCE_SNAPSHOT pps\n" +
      "            WHERE pps.ACCOUNT_ID = :p_account_id\n" +
      "                AND REPORT_DATE >= :v_min_account_date\n" +
      "            UNION ALL\n" +
      "            SELECT pps.PORTF_INDEX, pps.ACC_PROFIT, pps.REPORT_DATE\n" +
      "            FROM PE_WL_PERFORMANCE_SNAPSHOT pps\n" +
      "            WHERE pps.USER_ID = :v_custody_id\n" +
      "                AND REPORT_DATE < :v_min_account_date\n" +
      "        )\n" +
      "        RIGHT JOIN dual\n" +
      "        ON REPORT_DATE >= TO_DATE(:MIN_DATE, 'yyyy-MM-dd') AND REPORT_DATE <=  TO_DATE(:V_CURRENT_DATE, 'yyyy-MM-dd')" +
      "        ORDER BY REPORT_DATE ASC\n" +
      "        FETCH NEXT 1 ROWS ONLY");

    List list = (List<HashMap<String, Object>>) IData.idataDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
      .setParameter(P_ACCOUNT_ID, p_account_id)
      .setParameter(V_MIN_ACCOUNT_DATE, v_min_account_date)
      .setParameter(V_CUSTODY_ID, v_custody_id)
      .setParameter("MIN_DATE", minDate)
      .setParameter(V_CURRENT_DATE, date)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
      .getResultList();
    if (list != null && list.size() > 0) {
      HashMap<String, Object> item = (HashMap<String, Object>) list.get(0);
      originalMap.putAll(item);
    }
  }


  public static void startIndex(HashMap<String, Object> originalMap, String date) {
    Timestamp v_min_account_date = (Timestamp) originalMap.get(V_MIN_ACCOUNT_DATE.toUpperCase());
    String p_account_id = (String) originalMap.get(P_ACCOUNT_ID.toUpperCase());
    String v_custody_id = (String) originalMap.get(V_CUSTODY_ID.toUpperCase());

    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT PORTF_INDEX as v_start_index, ACC_PROFIT as v_start_profit, REPORT_DATE as start_report_date " +
      " FROM (" +
      "    SELECT pps.PORTF_INDEX, pps.ACC_PROFIT, pps.REPORT_DATE" +
      "            FROM PE_ICA_PERFORMANCE_SNAPSHOT pps" +
      "            WHERE pps.ACCOUNT_ID = :p_account_id" +
      "                AND REPORT_DATE >= :v_min_account_date" +
      "            UNION ALL" +
      "            SELECT pps.PORTF_INDEX, pps.ACC_PROFIT, pps.REPORT_DATE" +
      "            FROM PE_WL_PERFORMANCE_SNAPSHOT pps " +
      "            WHERE pps.USER_ID = :v_custody_id " +
      "                AND REPORT_DATE < :v_min_account_date" +
      "        ) " +
      " RIGHT JOIN dual" +
      " ON REPORT_DATE <= TO_DATE(:V_CURRENT_DATE, 'yyyy-MM-dd') " +
      " ORDER BY REPORT_DATE ASC " +
      " FETCH NEXT 1 ROWS ONLY");

    List list = (List<HashMap<String, Object>>) IData.idataDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
      .setParameter(P_ACCOUNT_ID, p_account_id)
      .setParameter(V_MIN_ACCOUNT_DATE, v_min_account_date)
      .setParameter(V_CUSTODY_ID, v_custody_id)
      .setParameter(V_CURRENT_DATE, date)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
      .getResultList();
    if (list != null && list.size() > 0) {
      HashMap<String, Object> item = (HashMap<String, Object>) list.get(0);
      originalMap.putAll(item);
    }
  }


  public static void newestIndex(HashMap<String, Object> originalMap, String date) {
    Timestamp v_min_account_date = (Timestamp) originalMap.get(V_MIN_ACCOUNT_DATE.toUpperCase());
    String p_account_id = (String) originalMap.get(P_ACCOUNT_ID.toUpperCase());
    String v_custody_id = (String) originalMap.get(V_CUSTODY_ID.toUpperCase());

    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT PORTF_INDEX as v_newest_index, ACC_PROFIT as v_newest_profit, TOTAL_ESTIMATED_REVENUE, REPORT_DATE as newest_report_date " +
      "\t\tFROM (\n" +
      "\t\t     SELECT pps.PORTF_INDEX, pps.ACC_PROFIT, pps.REPORT_DATE, pps.TOTAL_ESTIMATED_REVENUE \n" +
      "            FROM PE_ICA_PERFORMANCE_SNAPSHOT pps\n" +
      "            WHERE pps.ACCOUNT_ID = :p_account_id\n" +
      "                AND REPORT_DATE >= :v_min_account_date\n" +
      "            UNION ALL\n" +
      "           SELECT pps.PORTF_INDEX, pps.ACC_PROFIT, pps.REPORT_DATE, pps.TOTAL_ESTIMATED_REVENUE \n" +
      "            FROM PE_WL_PERFORMANCE_SNAPSHOT pps\n" +
      "            WHERE pps.USER_ID = :v_custody_id\n" +
      "                AND REPORT_DATE < :v_min_account_date\n" +
      "        )\n" +
      "\t\tRIGHT JOIN dual\n" +
      "\t\tON REPORT_DATE <= TO_DATE(:V_CURRENT_DATE, 'yyyy-MM-dd')\n" +
      "\t\tORDER BY REPORT_DATE DESC\n" +
      "\t\tFETCH NEXT 1 ROWS ONLY");

    List list = (List<HashMap<String, Object>>) IData.idataDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
      .setParameter(P_ACCOUNT_ID, p_account_id)
      .setParameter(V_MIN_ACCOUNT_DATE, v_min_account_date)
      .setParameter(V_CUSTODY_ID, v_custody_id)
      .setParameter(V_CURRENT_DATE, date)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
      .getResultList();
    if (list != null && list.size() > 0) {
      HashMap<String, Object> item = (HashMap<String, Object>) list.get(0);
      originalMap.putAll(item);
    }
  }
}
