package com.tcbs.automation.hfcdata.amlock;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class GetAmLockInfoEntity {
  public static final HibernateEdition redShiftDb = Database.TCBS_DWH.getConnection();
  @Step("Get data")
  public static List<Object[]> getAmLockInfo(String idNumber, String birthDate, String name) {
    String queryBuilder = new StringBuilder()
      .append(" call api.getamlocklist ( ")
      .append(idNumber).append(",")
      .append(birthDate).append(",")
      .append(name).append(", 'mycursor') ").toString();
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
      List<Object[]> res = session.createNativeQuery(query).list();
      return res;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }finally {
      redShiftDb.closeSession();
    }
    return new ArrayList<>();
  }
}
