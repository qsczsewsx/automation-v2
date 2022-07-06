package com.tcbs.automation.hfcdata;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class GetTcbfSmyEntity {
  public static final HibernateEdition redShiftDb = Database.TCBS_DWH.getConnection();

  @Step(" Get data ")
  public static List<Object[]> getTcbfData() {
    String queryBuilder = new StringBuilder()
      .append(" call api.getdatafund ('mycursor') ").toString();

    redShiftDb.closeSession();
    redShiftDb.openSession();

    Session session = redShiftDb.getSession();
    session.clear();
    try {
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }
      session.createNativeQuery(queryBuilder).executeUpdate();
      String query = " FETCH ALL FROM mycursor ";
      return session.createNativeQuery(query).list();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    } finally {
      redShiftDb.closeSession();
    }
    return new ArrayList<>();
  }
}
