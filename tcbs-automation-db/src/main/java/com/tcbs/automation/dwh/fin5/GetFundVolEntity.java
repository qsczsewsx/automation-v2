package com.tcbs.automation.dwh.fin5;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class GetFundVolEntity {
  public static final HibernateEdition redShiftDb = Database.TCBS_DWH.getConnection();
  @Step(" Get data ")
  public static List<Object[]> getListFundVol(Integer page, Integer size, String from, String to) {
    String queryBuilder = " call api.getdailyfundvolume (" + page + "," + size + ",'" + from + "' , '" + to + "' , 'mycursor') ";
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
      List res = session.createNativeQuery(query).getResultList();
      return res;
    }  catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    } finally {
      redShiftDb.closeSession();
    }
    return new ArrayList<>();
  }

}
