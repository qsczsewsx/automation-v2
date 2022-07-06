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

public class PeIcaPnlSymEntity {
  public static String ACCNO = "ACCNO";
  public static String USER_ID = "USER_ID";
  public static String V_CURRENT_DATE = "V_CURRENT_DATE";

  @Step("get list performance from final table")
  public static List<HashMap<String, Object>> getPerformanceIcaPnlSym() {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT * FROM PE_ICA_PNL_SYM");
    try {
      return IData.idataDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
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
    queryBuilder.append("TRUNCATE TABLE PE_ICA_PNL_SYM");
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

  public static List<HashMap<String, Object>> getUserPerformancePnlSymByAccountNo(String userId) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT " +
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
      "TOTAL_ESTIMATED_REVENUE FROM PE_USER_PNL_SYM WHERE USER_ID = :USER_ID");
    try {
      return IData.idataDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter(USER_ID, userId)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static void oneWeekMonthYear(HashMap<String, Object> originalMap, String date, String minDate, String one, String two, String three) {
    String p_user_id = (String) originalMap.get(USER_ID.toUpperCase());

    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT PORTF_INDEX as " + one + " ,ACC_PROFIT as " + two + ", REPORT_DATE as " + three);
    queryStringBuilder.append(" FROM PE_USER_PERFORMANCE_SNAPSHOT " +
      "        RIGHT JOIN dual  " +
      "        ON USER_ID = :p_user_id AND REPORT_DATE >= TO_DATE(:MIN_DATE, 'yyyy-MM-dd') AND REPORT_DATE <= TO_DATE(:V_CURRENT_DATE, 'yyyy-MM-dd') " +
      "        ORDER BY REPORT_DATE ASC " +
      "        FETCH NEXT 1 ROWS ONLY ");


    List list = (List<HashMap<String, Object>>) IData.idataDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
      .setParameter("p_user_id", p_user_id)
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
    String p_user_id = (String) originalMap.get(USER_ID.toUpperCase());

    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT PORTF_INDEX as v_start_index,ACC_PROFIT as v_start_profit,REPORT_DATE as start_report_date");
    queryStringBuilder.append(" FROM PE_USER_PERFORMANCE_SNAPSHOT " +
      "        RIGHT JOIN dual " +
      "        ON USER_ID = :p_user_id AND REPORT_DATE <= TO_DATE(:V_CURRENT_DATE, 'yyyy-MM-dd') " +
      "        ORDER BY REPORT_DATE ASC " +
      "        FETCH NEXT 1 ROWS ONLY");

    List list = (List<HashMap<String, Object>>) IData.idataDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
      .setParameter("p_user_id", p_user_id)
      .setParameter(V_CURRENT_DATE, date)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
      .getResultList();
    if (list != null && list.size() > 0) {
      HashMap<String, Object> item = (HashMap<String, Object>) list.get(0);
      originalMap.putAll(item);
    }
  }


  public static void newestIndex(HashMap<String, Object> originalMap, String date) {
    String p_user_id = (String) originalMap.get(USER_ID.toUpperCase());

    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT PORTF_INDEX as v_newest_index,ACC_PROFIT as v_newest_profit,TOTAL_ESTIMATED_REVENUE,REPORT_DATE as newest_report_date");
    queryStringBuilder.append(" FROM PE_USER_PERFORMANCE_SNAPSHOT " +
      "        RIGHT JOIN dual " +
      "        ON USER_ID = :p_user_id AND REPORT_DATE <= TO_DATE(:V_CURRENT_DATE, 'yyyy-MM-dd') " +
      "        ORDER BY REPORT_DATE DESC " +
      "        FETCH NEXT 1 ROWS ONLY");

    List list = (List<HashMap<String, Object>>) IData.idataDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
      .setParameter("p_user_id", p_user_id)
      .setParameter(V_CURRENT_DATE, date)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
      .getResultList();
    if (list != null && list.size() > 0) {
      HashMap<String, Object> item = (HashMap<String, Object>) list.get(0);
      originalMap.putAll(item);
    }
  }

  public static List<HashMap<String, Object>> getAllPerformancePnl(String accountNo) {
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

  @Step("get list performance from final table")
  public static List<HashMap<String, Object>> getAllPerformanceByUser() {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT * FROM PE_USER_PNL_SYM");
    try {
      return IData.idataDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }


  @Step("cal list performance each account id")
  public static List<HashMap<String, Object>> culPerformanceTable(String date) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT * FROM PE_ICA_PNL_SYM");

    try {
      return IData.idataDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static List<HashMap<String, Object>> getListUserId(String date) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT DISTINCT USER_ID " +
      " FROM PE_USER_PERFORMANCE_SNAPSHOT " +
      " WHERE REPORT_DATE = TO_DATE(:P_REPORT_DATE, 'yyyy-MM-dd')");

    List list = IData.idataDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
      .setParameter("P_REPORT_DATE", date)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
      .getResultList();
    return (List<HashMap<String, Object>>) list;
  }

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

  public static List<HashMap<String, Object>> calperformnace(HashMap<String, Object> originalMap, String currentDate) {
    String p_account_ids = (String) originalMap.get(ACCNO);
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("WITH cus_account_info AS " +
      "(SELECT Trunc(Coalesce(Min(CASE" +
      "                                     WHEN api.created_time > sia.created_time" +
      "                                   THEN" +
      "                                     sia.created_time" +
      "                                     ELSE api.created_time" +
      "                                   END), current_date)) AS CREATE_ACCOUNT_DATE," +
      "                api.custody_id," +
      "                api.accno" +
      "         FROM   account_public_info api" +
      "                left join stg_icopy_account sia" +
      "                       ON api.accno = sia.account_no" +
      "         WHERE  api.accno = :p_account_ids" +
      "                AND api.public_in = 'TCI3'" +
      "         GROUP  BY api.accno," +
      "                   api.custody_id)," +
      "     cus_performnace" +
      "     AS (SELECT pips.portf_index," +
      "                pips.report_date," +
      "                pips.acc_profit," +
      "                pips.numerator," +
      " api.accno AS ACCOUNT_ID" +
      "         FROM   pe_ica_performance_snapshot pips" +
      "                inner join cus_account_info api " +
      "                        ON pips.account_id = api.accno" +
      "                           AND pips.report_date >= api.create_account_date" +
      "         WHERE  pips.report_date <= TO_DATE(:V_CURRENT_DATE, 'yyyy-MM-dd')" +
      "         UNION ALL" +
      "         SELECT pwps.portf_index," +
      "                pwps.report_date," +
      "                pwps.acc_profit," +
      "                pwps.numerator," +
      " api.accno AS ACCOUNT_ID" +
      "         FROM   pe_wl_performance_snapshot pwps" +
      "                 inner join cus_account_info api" +
      "                        ON pwps.user_id = api.custody_id" +
      "                           AND pwps.report_date < api.create_account_date" +
      "         WHERE  pwps.report_date <= TO_DATE(:V_CURRENT_DATE, 'yyyy-MM-dd'))," +
      "     cus_indeces" +
      "     AS (SELECT piis.value_at_risk," +
      "                piis.eomonth," +
      "                api.accno AS ACCOUNT_ID" +
      "         FROM   pe_ica_indices_snapshot piis" +
      "                inner join cus_account_info api" +
      "                        ON piis.account_id = api.accno" +
      "                           AND piis.eomonth >= api.create_account_date" +
      "         WHERE  piis.eomonth <= TO_DATE(:V_CURRENT_DATE, 'yyyy-MM-dd')" +
      "         UNION ALL" +
      "         SELECT pwis.value_at_risk," +
      "                pwis.eomonth," +
      "                api.accno AS ACCOUNT_ID" +
      "         FROM   pe_wl_indices_snapshot pwis" +
      "                inner join cus_account_info api" +
      "                        ON pwis.user_id = api.custody_id" +
      "                           AND pwis.eomonth < api.create_account_date" +
      "         WHERE  pwis.eomonth <= TO_DATE(:V_CURRENT_DATE, 'yyyy-MM-dd'))," +
      "     cus_start_end_report" +
      "     AS (SELECT Coalesce(Min(pps.report_date), TO_DATE(:V_CURRENT_DATE, 'yyyy-MM-dd')) AS" +
      "                START_REPORT_DATE," +
      "                Coalesce(Max(pps.report_date), TO_DATE(:V_CURRENT_DATE, 'yyyy-MM-dd')) AS" +
      "                NEWST_REPORT_DATE," +
      " pps.account_id" +
      "         FROM   cus_performnace pps " +
      "         GROUP  BY pps.account_id ), " +
      "     cus_min_30days" +
      "     AS (SELECT Min(pps.report_date) AS REPORT_DATE, " +
      "  pps.account_id" +
      "         FROM cus_performnace pps" +
      "         WHERE  pps.report_date >= Add_months(TO_DATE(:V_CURRENT_DATE, 'yyyy-MM-dd'), -1)" +
      " GROUP  BY pps.account_id)," +
      "     cus_min_6months" +
      "     AS (SELECT Min(pps.report_date) AS REPORT_DATE," +
      "   pps.account_id" +
      "  from cus_performnace pps" +
      "         WHERE  pps.report_date >= Add_months(TO_DATE(:V_CURRENT_DATE, 'yyyy-MM-dd'), -6)" +
      "  GROUP  BY pps.account_id)," +
      "     cus_min_12months" +
      "     AS (SELECT Min(pps.report_date) AS REPORT_DATE," +
      "    pps.account_id" +
      "    FROM cus_performnace pps" +
      "         WHERE  pps.report_date >= Add_months(TO_DATE(:V_CURRENT_DATE, 'yyyy-MM-dd'), -12)" +
      "         GROUP  BY pps.account_id)," +
      "     cus_max_risk_date" +
      "     AS (SELECT Max(eomonth) AS MAX_RISK_DATE," +
      "                pis.account_id" +
      "         FROM   cus_indeces pis" +
      "         WHERE  pis.eomonth <= TO_DATE(:V_CURRENT_DATE, 'yyyy-MM-dd')" +
      "         GROUP  BY pis.account_id)," +
      "     cus_start_performance" +
      "     AS (SELECT pps.portf_index, " +
      " pps.acc_profit," +
      " pps.account_id," +
      " pps.report_date" +
      "  FROM   cus_performnace pps" +
      "                inner join cus_start_end_report" +
      "                        ON pps.account_id = cus_start_end_report.account_id" +
      "                           AND pps.report_date =" +
      "                               cus_start_end_report.start_report_date)," +
      "     cus_new_performance" +
      " AS (SELECT pps.portf_index," +
      "  pps.acc_profit," +
      " pps.account_id," +
      " pps.report_date" +
      " FROM   cus_performnace pps" +
      "                inner join cus_start_end_report" +
      "                        ON pps.account_id = cus_start_end_report.account_id" +
      "                           AND pps.report_date =" +
      "                               cus_start_end_report.newst_report_date)," +
      "     cus_30days_performance" +
      " AS (SELECT pps.portf_index," +
      " pps.acc_profit," +
      "  pps.account_id," +
      "  pps.report_date" +
      " FROM   cus_performnace pps" +
      "                inner join cus_min_30days" +
      "                        ON pps.account_id = cus_min_30days.account_id" +
      "                           AND pps.report_date = cus_min_30days.report_date)," +
      "     cus_6months_performance" +
      "     AS (SELECT pps.portf_index," +
      "  pps.acc_profit," +
      "                pps.account_id," +
      "                pps.report_date" +
      "  FROM   cus_performnace pps" +
      "                inner join cus_min_6months" +
      "                        ON pps.account_id = cus_min_6months.account_id" +
      "                           AND pps.report_date = cus_min_6months.report_date)," +
      "     cus_12months_performance" +
      "     AS (SELECT pps.portf_index," +
      "                pps.acc_profit," +
      "                pps.account_id," +
      "                pps.report_date" +
      "         FROM   cus_performnace pps" +
      "                inner join cus_min_12months" +
      "                        ON pps.account_id = cus_min_12months.account_id" +
      "                           AND pps.report_date = cus_min_12months.report_date)," +
      "     cus_risk" +
      "     AS (SELECT CASE" +
      "                  WHEN Abs(pis.value_at_risk) <= 0.005 THEN 1" +
      "                  WHEN Abs(pis.value_at_risk) > 0.005" +
      "                       AND Abs(pis.value_at_risk) <= 0.012 THEN 2" +
      "                  WHEN Abs(pis.value_at_risk) > 0.012" +
      "                       AND Abs(pis.value_at_risk) <= 0.02 THEN 3" +
      "                  WHEN Abs(pis.value_at_risk) > 0.02" +
      "                       AND Abs(pis.value_at_risk) <= 0.027 THEN 4" +
      "                  WHEN Abs(pis.value_at_risk) > 0.027" +
      "                       AND Abs(pis.value_at_risk) <= 0.039 THEN 5" +
      "                  WHEN Abs(pis.value_at_risk) > 0.039" +
      "                       AND Abs(pis.value_at_risk) <= 0.054 THEN 6" +
      "                  WHEN Abs(pis.value_at_risk) > 0.054" +
      "                       AND Abs(pis.value_at_risk) <= 0.077 THEN 7" +
      "                  WHEN Abs(pis.value_at_risk) > 0.077" +
      "                       AND Abs(pis.value_at_risk) <= 0.155 THEN 8" +
      "                  WHEN Abs(pis.value_at_risk) > 0.155" +
      "                       AND Abs(pis.value_at_risk) <= 0.233 THEN 9" +
      "                  WHEN Abs(pis.value_at_risk) > 0.233 THEN 10" +
      "                  ELSE 10" +
      "                END AS RISK_SCORE," +
      "                pis.account_id," +
      "                pis.eomonth" +
      "         FROM   cus_indeces pis" +
      "                inner join cus_max_risk_date" +
      "                        ON pis.eomonth = cus_max_risk_date.max_risk_date" +
      "                           AND pis.account_id = cus_max_risk_date.account_id)," +
      "     cus_avg_30days_performance" +
      "     AS (SELECT Avg(pps.numerator) AS AVG_NUMERATOR," +
      "                pps.account_id" +
      "         FROM   cus_performnace pps" +
      "         WHERE  pps.report_date <= TO_DATE(:V_CURRENT_DATE, 'yyyy-MM-dd')" +
      "                AND pps.report_date > TO_DATE(:V_CURRENT_DATE, 'yyyy-MM-dd') - 30" +
      "         GROUP  BY pps.account_id)" +
      "SELECT account_nos.account_no," +
      "       c30dp.report_date                                         AS" +
      "       PNL_30DAYS_REPORT_DATE," +
      " CASE" +
      "         WHEN c30dp.acc_profit IS NULL THEN 0" +
      "         ELSE Coalesce(cnp.acc_profit, 0) - Coalesce(c30dp.acc_profit, 0)" +
      "       END                                      AS" +
      "       PNL_30DAYS_RETURN_VALUE," +
      "  CASE" +
      "         WHEN Coalesce(c30dp.portf_index, 0) = 0 THEN 0" +
      "         ELSE ( Coalesce(cnp.portf_index, 0) - Coalesce(c30dp.portf_index, 0) )" +
      "              /" +
      "              Coalesce(" +
      "              c30dp.portf_index, 0)" +
      "       END AS" +
      "       PNL_30DAYS_RETURN_PCT," +
      "       c6mp.report_date                                          AS" +
      "       PNL_6MONTHS_REPORT_DATE," +
      "   CASE" +
      "         WHEN c6mp.acc_profit IS NULL THEN 0" +
      "         ELSE Coalesce(cnp.acc_profit, 0) - Coalesce(c6mp.acc_profit, 0)" +
      "       END  AS" +
      "       PNL_6MONTHS_RETURN_VALUE," +
      "    CASE" +
      "         WHEN Coalesce(c6mp.portf_index, 0) = 0 THEN 0" +
      "         ELSE ( Coalesce(cnp.portf_index, 0) - Coalesce(c6mp.portf_index, 0) ) /" +
      "              Coalesce(c6mp.portf_index, 0)" +
      "       END   AS" +
      "       PNL_6MONTHS_RETURN_PCT," +
      "       c12mp.report_date                                         AS" +
      "       PNL_12MONTHS_REPORT_DATE," +
      "     CASE" +
      "         WHEN c12mp.acc_profit IS NULL THEN 0" +
      "         ELSE Coalesce(cnp.acc_profit, 0) - Coalesce(c12mp.acc_profit, 0)" +
      "       END     AS" +
      "       PNL_12MONTHS_RETURN_VALUE," +
      "      CASE" +
      "         WHEN Coalesce(c12mp.portf_index, 0) = 0 THEN 0" +
      "         ELSE ( Coalesce(cnp.portf_index, 0) - Coalesce(c12mp.portf_index, 0) )" +
      "              /" +
      "              Coalesce(" +
      "              c12mp.portf_index, 0)" +
      "       END                                                       AS" +
      "       PNL_12MONTHS_RETURN_PCT," +
      "       csp.report_date                                           AS" +
      "       ALL_TIME_REPORT_DATE," +
      "       Coalesce(cnp.acc_profit, 0) - Coalesce(csp.acc_profit, 0) AS" +
      "       ALL_TIME_RETURN_VALUE," +
      "       CASE" +
      "         WHEN Coalesce(cnp.portf_index, 0) = 0 THEN 0" +
      "         ELSE ( Coalesce(cnp.portf_index, 0) - Coalesce(csp.portf_index, 0) ) /" +
      "              Coalesce(cnp.portf_index, 0)" +
      "       END                                                       AS" +
      "       ALL_TIME_RETURN_PCT," +
      "       cus_risk.eomonth" +
      "       LASTED_RISK_DATE," +
      "       cus_risk.risk_score," +
      "       Coalesce(cus_avg_30days_performance.avg_numerator, 0)     AS" +
      "       AVG_30DAYS_NUMERATOR" +
      " FROM   (SELECT :p_account_ids AS ACCOUNT_NO" +
      "        FROM   dual)" +
      "       account_nos" +
      "       left join cus_start_performance csp" +
      "              ON account_nos.account_no = csp.account_id" +
      "       left join cus_new_performance cnp" +
      "              ON account_nos.account_no = cnp.account_id" +
      "       left join cus_30days_performance c30dp" +
      "              ON account_nos.account_no = c30dp.account_id" +
      "       left join cus_6months_performance c6mp" +
      "              ON account_nos.account_no = c6mp.account_id" +
      "       left join cus_12months_performance c12mp" +
      "              ON account_nos.account_no = c12mp.account_id" +
      "       left join cus_risk" +
      "              ON account_nos.account_no = cus_risk.account_id" +
      "       left join cus_avg_30days_performance" +
      "              ON account_nos.account_no = cus_avg_30days_performance.account_id");

    List list = (List<HashMap<String, Object>>) IData.idataDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
      .setParameter("p_account_ids", p_account_ids)
      .setParameter(V_CURRENT_DATE, currentDate)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
      .getResultList();
    return list;
  }

  public static List<HashMap<String, Object>> cus_indeces(HashMap<String, Object> originalMap, String currentDate) {
    String accountNo = (String) originalMap.get(ACCNO);
    String custodyId = (String) originalMap.get("CUSTODY_ID");
    Timestamp createAccountDate = (Timestamp) originalMap.get("CREATE_ACCOUNT_DATE");

    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(
      "SELECT" +
        "            piis.VALUE_AT_RISK," +
        "            piis.EOMONTH," +
        "            api.ACCNO AS ACCOUNT_ID" +
        "        FROM PE_ICA_INDICES_SNAPSHOT piis" +
        "            ON piis.ACCOUNT_ID = :ACCNO" +
        "            AND piis.EOMONTH >= :CREATE_ACCOUNT_DATE" +
        "        WHERE piis.EOMONTH <= :V_CURRENT_DATE" +
        "        UNION ALL" +
        "        SELECT" +
        "            pwis.VALUE_AT_RISK," +
        "            pwis.EOMONTH," +
        "            api.ACCNO AS ACCOUNT_ID" +
        "        FROM PE_WL_INDICES_SNAPSHOT pwis" +
        "            ON pwis.USER_ID = :CUSTODY_ID" +
        "            AND pwis.EOMONTH < :CREATE_ACCOUNT_DATE" +
        "        WHERE pwis.EOMONTH <= :V_CURRENT_DATE");

    List list = (List<HashMap<String, Object>>) IData.idataDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
      .setParameter(ACCNO, accountNo)
      .setParameter("CUSTODY_ID", custodyId)
      .setParameter("CREATE_ACCOUNT_DATE", createAccountDate)
      .setParameter(V_CURRENT_DATE, currentDate)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
      .getResultList();
    return list;
  }

  public static List<HashMap<String, Object>> getListAccountId2() {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT " +
      "        TRUNC(COALESCE(MIN( " +
      "               CASE WHEN api.CREATED_TIME > sia.CREATED_TIME THEN sia.CREATED_TIME " +
      "               ELSE api.CREATED_TIME END " +
      "            ), CURRENT_DATE)) AS CREATE_ACCOUNT_DATE, " +
      "         api.CUSTODY_ID, " +
      "         api.ACCNO " +
      "        FROM ACCOUNT_PUBLIC_INFO api " +
      "        LEFT JOIN STG_ICOPY_ACCOUNT sia " +
      "            on api.ACCNO = sia.ACCOUNT_NO " +
      "        WHERE " +
      "         api.PUBLIC_IN = 'TCI3' AND api.ACCNO != 'ALL'" +
      "        GROUP BY api.ACCNO, api.CUSTODY_ID");

    List list = IData.idataDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
      .getResultList();
    return (List<HashMap<String, Object>>) list;
  }

  @Step("get list performance from final table")
  public static List<HashMap<String, Object>> getAllPerformanceByAccountNo2() {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT * FROM PE_ACCOUNT_PNL_SYM_REPORT");
    try {
      return IData.idataDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
