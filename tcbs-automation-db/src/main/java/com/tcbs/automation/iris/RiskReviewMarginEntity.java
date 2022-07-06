package com.tcbs.automation.iris;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RiskReviewMarginEntity {

  @Step("Delete Data Test")
  public static void deleteData(String ticker) {
    StringBuilder queryBuilder = new StringBuilder();
    Session session = AwsIRis.AwsIRisDbConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    queryBuilder.append("DELETE FROM RISK_ANALYST_MARGIN_REVIEWED_FULL WHERE TICKER = '" + ticker + "'");
    Query query = session.createNativeQuery(queryBuilder.toString());
    query.executeUpdate();
    session.getTransaction().commit();
  }

  @Step("Get Latest Data")
  public static List<HashMap<String, Object>> getLatestData() {
    StringBuilder query = new StringBuilder();
    query.append(
      "SELECT RISK_ANALYST_MARGIN_REVIEWED_FULL.*, TO_CHAR(RISK_ANALYST_MARGIN_REVIEWED_FULL.REVIEWED_DATE, 'YYYY-MM-DD') AS REVIEWEDDATE FROM RISK_ANALYST_MARGIN_REVIEWED_FULL where UPDATED_DATE = (select max(UPDATED_DATE) from RISK_ANALYST_MARGIN_REVIEWED_FULL) ");
    try {
      List<HashMap<String, Object>> resultList = AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get data incase null")
  public static List<HashMap<String, Object>> getDataNull() {
    StringBuilder query = new StringBuilder();
    query.append("SELECT * FROM RISK_ANALYST_MARGIN_REVIEWED_FULL WHERE TICKER = '' OR LOAN_PRICE = '' OR NOTE = ''");
    try {
      List<HashMap<String, Object>> resultList = AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
