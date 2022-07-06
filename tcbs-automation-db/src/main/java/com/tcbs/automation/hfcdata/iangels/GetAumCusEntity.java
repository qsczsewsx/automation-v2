package com.tcbs.automation.hfcdata.iangels;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class GetAumCusEntity {

  public static final HibernateEdition tcbsDwhDbConnection = Database.TCBS_DWH.getConnection();

  @Step(" Get data ")
  public static List<Object[]> getListAumCus(String iwpInput, String dateInput) {
    String queryBuilder = " api.iag_get_subAUM_foriwp ( '" + iwpInput + "' , '" + dateInput + "' , 'mycursor') ";
    Session session = tcbsDwhDbConnection.getSession();
    session.clear();
    try {
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }
      session.createNativeQuery(queryBuilder).executeUpdate();
      String query = " FETCH ALL FROM mycursor ";
      List<Object[]> res = session.createNativeQuery(query).list();
      return res;
    }  catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    } finally {
      tcbsDwhDbConnection.closeSession();
    }
    return new ArrayList<>();
  }

}
