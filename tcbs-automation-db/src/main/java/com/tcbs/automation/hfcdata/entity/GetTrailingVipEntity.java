package com.tcbs.automation.hfcdata.entity;

import com.tcbs.automation.tcbsdbapi1.DbApi1;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetTrailingVipEntity {

  @Step("get data")
  public static List<HashMap<String, Object>> getTrailingVipProc(String proc, String tcbsid) {
    StringBuilder query = new StringBuilder();
    query.append(" Exec  ");
    query.append(proc).append(" @tcbsid= :tcbsid ");
    try {
      List<HashMap<String, Object>> resultList = DbApi1.dbApiDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("tcbsid", tcbsid)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step(" update data ")
  public static void updateData(){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" UPDATE smy_tcbsshare_cus_tcb_identification SET REPORTDATE = (SELECT MAX(REPORTDATE) FROM smy_tcbsshare_cus_tcb_identification) ");
    executeQuery(queryBuilder);
  }

  @Step("execute query")
  public static void executeQuery(StringBuilder sql) {
    Session session = DbApi1.dbApiDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = session.createNativeQuery(sql.toString());
    try {
      query.executeUpdate();
      session.getTransaction().commit();
      DbApi1.dbApiDbConnection.closeSession();
    } catch (Exception e) {
      DbApi1.dbApiDbConnection.closeSession();
      throw e;
    }
  }

}
