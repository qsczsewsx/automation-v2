package com.tcbs.automation.iris;

import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class MarginListEnity {
  @Step("Delete Data")
  public static void deleteData(String ticker, String analystId) {
    StringBuilder queryBuilder = new StringBuilder();
    Session session = AwsIRis.AwsIRisDbConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    queryBuilder.append("delete from risk_assignment_info where ticker = '" + ticker + "' and analyst_id = '" + analystId + "'");
    Query query = session.createNativeQuery(queryBuilder.toString());
    query.executeUpdate();
    session.getTransaction().commit();
  }
}
