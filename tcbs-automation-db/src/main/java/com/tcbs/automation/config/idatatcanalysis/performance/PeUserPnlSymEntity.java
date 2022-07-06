package com.tcbs.automation.config.idatatcanalysis.performance;

import com.tcbs.automation.config.idatatcanalysis.IData;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class PeUserPnlSymEntity {

  @Step("Truncate table")
  public static void truncateTable() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("TRUNCATE TABLE PE_USER_PNL_SYM");
    executeQuery(queryBuilder);
  }

  @Step(" Call proc ")
  public static void callProc(String proc, String date) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("CALL " + proc + "('" + date + "')");
    executeQuery(queryBuilder);
  }

  @Step("execute query")
  public static void executeQuery(StringBuilder sql) {
    Session session = IData.idataDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = session.createNativeQuery(sql.toString());
    query.executeUpdate();
    session.getTransaction().commit();
    IData.idataDbConnection.closeSession();
  }
}
