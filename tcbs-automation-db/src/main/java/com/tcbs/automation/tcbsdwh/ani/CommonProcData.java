package com.tcbs.automation.tcbsdwh.ani;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;
import com.tcbs.automation.tcbsdwh.Tcbsdwh;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.DataBaseUtils.beginTransaction;

public class CommonProcData {

  public static final HibernateEdition redShiftDb = Database.TCBS_DWH.getConnection();
  public static final HibernateEdition stagingRedShiftDb = Database.TCBS_DWH_STAGING.getConnection();
  public static final String ERROR = "error";

  @Step(" Get data ")
  public static List<HashMap<String, Object>> getDataFromRedShiftProc(String proc, String param) {
    String callProcQuery = new StringBuilder()
      .append(" CALL  ")
      .append(proc).append(" ( ")
      .append(param)
      .append(" , 'mycursor') ; ")
      .toString();
    String queryFetchData = new StringBuilder()
      .append(" FETCH ALL FROM mycursor ; ").toString();
    CommonProcData.redShiftDb.closeSession();
    CommonProcData.redShiftDb.openSession();
    Session session = CommonProcData.redShiftDb.getSession();
    String exMess = null;
    List<HashMap<String, Object>> errorMess = new ArrayList<>();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    try {
      session.createNativeQuery(callProcQuery).executeUpdate();
    } catch (Exception ex) {
      HashMap<String, Object> errorItem = new HashMap<>();
      exMess = ex.getCause().getCause().getLocalizedMessage();
      errorItem.put(ERROR, exMess);
      errorMess.add(errorItem);
      return errorMess;
    }
    if (exMess == null) {
      try {
        List<HashMap<String, Object>> res = session.createNativeQuery(queryFetchData)
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
          .getResultList();
        session.close();
        return res;
      } catch (Exception ex) {
        HashMap<String, Object> errorItem = new HashMap<>();
        exMess = ex.getCause().getCause().toString();
        errorItem.put(ERROR, exMess);
        errorMess.add(errorItem);
        return errorMess;
      } finally {
        CommonProcData.redShiftDb.closeSession();

      }
    }
    return errorMess;
  }


  @Step(" Get data ")
  public static List<HashMap<String, Object>> callStagingRedShiftProc(String proc, String param, Boolean isProcTruncated) {
    StringBuilder callProcQuery = new StringBuilder();
    if (isProcTruncated == true) {
      callProcQuery.append("commit; ");
    }
    callProcQuery
      .append(" CALL  ")
      .append(proc).append(" ( ")
      .append(param)
      .append(" ) ; ");

    CommonProcData.stagingRedShiftDb.closeSession();
    CommonProcData.stagingRedShiftDb.openSession();
    Session session = CommonProcData.stagingRedShiftDb.getSession();
    String exMess = null;
    List<HashMap<String, Object>> errorMess = new ArrayList<>();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    try {
      session.createNativeQuery(callProcQuery.toString()).executeUpdate();
    } catch (Exception ex) {
      HashMap<String, Object> errorItem = new HashMap<>();
      exMess = ex.getCause().getCause().getLocalizedMessage();
      errorItem.put(ERROR, exMess);
      errorMess.add(errorItem);
      return errorMess;
    }

    return errorMess;
  }

  @Step("update etlDate data by key")
  public static void updateEtlDate(String table, String etldate, String etlRunDatetime) {
    Session session = Tcbsdwh.tcbsStagingDwhDbConnection.getSession();
    session.clear();
    beginTransaction(session);
    String querySql = new StringBuilder()
      .append("UPDATE ")
      .append(table)
      .append(" SET etlcurdate = ")
      .append(etldate)
      .append(" , etlrundatetime= '")
      .append(etlRunDatetime)
      .append("'").toString();
    Query<?> query = session.createNativeQuery(querySql);
    query.executeUpdate();
    session.getTransaction().commit();
    Tcbsdwh.tcbsStagingDwhDbConnection.closeSession();
  }

}

