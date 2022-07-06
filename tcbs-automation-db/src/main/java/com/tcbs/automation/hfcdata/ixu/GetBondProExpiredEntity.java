package com.tcbs.automation.hfcdata.ixu;

import com.tcbs.automation.tcbsdwh.Tcbsdwh;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class GetBondProExpiredEntity {

  @Step(" Get data ")
  public static List<Object[]> getListBondProExpired(String date, Integer page, Integer size) {
    String queryBuilder = new StringBuilder()
      .append(" call api.ixu_getbondproexpired ( '")
      .append(page).append("' , '")
      .append(size).append("', '")
      .append(date)
      .append("' , 'mycursor') ").toString();
    Session session = Tcbsdwh.tcbsDwhDbConnection.getSession();
    session.clear();
    try {
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }
      session.createNativeQuery(queryBuilder).executeUpdate();
      String query = " FETCH ALL FROM mycursor ";
      List<Object[]> res = session.createNativeQuery(query).list();
      return res;
    } catch (Exception ex) {
      throw ex;
    } finally {
      Tcbsdwh.tcbsDwhDbConnection.closeSession();
    }
  }

  @Step("update Data before test")
  public static void updateEtlRunDateTime() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" update dwh.smy_dwh_tcb_bond_trading_details set etlrundatetime = getdate() ");
    executeQuery(queryBuilder);
  }

  @Step("update Data before test")
  public static void updateCreatedDate() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" update dwh.smy_dwh_tcb_AllBondProExpiredDate set createddate = getdate() ");
    executeQuery(queryBuilder);
  }

  public static void executeQuery(StringBuilder sql) {
    Session session = Tcbsdwh.tcbsDwhDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = session.createNativeQuery(sql.toString());
    try {
      query.executeUpdate();
      session.getTransaction().commit();
      Tcbsdwh.tcbsDwhDbConnection.closeSession();
    } catch (Exception e) {
      Tcbsdwh.tcbsDwhDbConnection.closeSession();
      throw e;
    }
  }

}
