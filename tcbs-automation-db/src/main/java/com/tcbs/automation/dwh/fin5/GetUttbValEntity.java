package com.tcbs.automation.dwh.fin5;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class GetUttbValEntity {
  private final static Logger logger = LoggerFactory.getLogger(GetUttbValEntity.class);

  public static final HibernateEdition redShiftDb = Database.TCBS_DWH.getConnection();
  @Step("Get data")
  public static List<Object[]> getUttbVal(String from, String to) {
    String queryBuilder = new StringBuilder()
      .append(" call api.getuttbvalue ( '")
      .append(from).append("' , '")
      .append(to).append("' , 'mycursor') ")
      .toString();
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
      List<Object[]> res = session.createNativeQuery(query).getResultList();
      return res;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    } finally {
      redShiftDb.closeSession();
    }
    return new ArrayList<>();
  }
}
