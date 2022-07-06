package com.tcbs.automation.dwh.icopy;

import com.tcbs.automation.tcbsdwh.Tcbsdwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class GetSimilarTraderEntity {

  @Step("get data")
  public static List<Object[]> getListSimilarTrader(String traderId) {
    StringBuilder queryBuilder = new StringBuilder().append(" call api.coco_getsimilartrader ( '").append(traderId).append("' , 'mycursor') ");
    Tcbsdwh.tcbsDwhDbConnection.openSession();
    Session session = Tcbsdwh.tcbsDwhDbConnection.getSession();
    session.clear();
    try {
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }
      session.createNativeQuery(queryBuilder.toString()).executeUpdate();
      String query = " FETCH ALL FROM mycursor ";
      return session.createNativeQuery(query).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    } finally {
      session.close();
      Tcbsdwh.tcbsDwhDbConnection.closeSession();
    }
    return new ArrayList<>();
  }
}
