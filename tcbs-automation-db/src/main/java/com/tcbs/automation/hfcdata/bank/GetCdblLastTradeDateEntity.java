package com.tcbs.automation.hfcdata.bank;

import com.tcbs.automation.tcbsdwh.Tcbsdwh;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;

import java.util.List;

public class GetCdblLastTradeDateEntity {

  @Step(" Get data ")
  public static List<Object[]> getCdblLastTradeDate(String cusId, String cdId) {
    String queryBuilder = " call api.cdbl_get_cust_latest_trading ( '" + cusId + "' , '" + cdId + "' , 'mycursor') ";
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

}
