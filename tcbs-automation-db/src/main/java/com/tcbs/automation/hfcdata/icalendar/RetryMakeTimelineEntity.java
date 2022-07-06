package com.tcbs.automation.hfcdata.icalendar;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class RetryMakeTimelineEntity {

  public static final HibernateEdition hfc = Database.HFC_DATA.getConnection();

  @Step("get data")
  public static void updateDataTest(String currentDate){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("UPDATE ICAL_MTL_QUEUE SET STATUS = 'ERROR_NEED_RETRY' WHERE TO_CHAR(PROCESSING_DATE, 'yyyy-mm-dd') = '" + currentDate + "'");
    executeQuery(queryBuilder);
  }

  public static void executeQuery(StringBuilder sql) {
    Session session = hfc.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = session.createNativeQuery(sql.toString());
    try {
      query.executeUpdate();
      session.getTransaction().commit();
      hfc.closeSession();
    } catch (Exception e) {
      hfc.closeSession();
      throw e;
    }
  }

}
