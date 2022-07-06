package com.tcbs.automation.dwh.fin5;

import com.tcbs.automation.tcbsdwh.Tcbsdwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import java.util.ArrayList;
import java.util.List;

public class GetFeeDailyBondEntity {

  @Step(" Get data ")
  public static List<Object[]> getListFeeDailyBond(Integer page, Integer size, String from, String to) {
    String queryBuilder = " call api.getdailyicopy (" + page + "," + size + ",'" + from + "' , '" + to + "' , 'mycursor') ";
    Tcbsdwh.tcbsDwhDbConnection.openSession();
    Session session = Tcbsdwh.tcbsDwhDbConnection.getSession();
    session.clear();
    try {
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }
      session.createNativeQuery(queryBuilder).executeUpdate();
      String query = " FETCH ALL FROM mycursor ";
      List res = session.createNativeQuery(query).getResultList();
      return res;
    }  catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    } finally {
      Tcbsdwh.tcbsDwhDbConnection.closeSession();
    }
    return new ArrayList<>();
  }

}
