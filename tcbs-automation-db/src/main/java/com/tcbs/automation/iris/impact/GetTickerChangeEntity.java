package com.tcbs.automation.iris.impact;

import com.tcbs.automation.iris.AwsIRis;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetTickerChangeEntity {
  @Step("Get ticker change")
  public static List<HashMap<String, Object>> getTickerChange(String updatedBy) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select * from RISK_RTT_TICKER_CHANGED where UPDATED_DATE = (select max(UPDATED_DATE) from RISK_RTT_TICKER_CHANGED where UPDATED_BY = :updatedBy) and UPDATED_BY = :updatedBy   ");
    try {

      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("updatedBy",updatedBy)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

  @Step("Get ticker change")
  public static List<HashMap<String, Object>> getFullList(String ticker) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select * from RISK_ANALYST_MARGIN_REVIEWED_FULL where ticker = :ticker   ");
    try {

      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("ticker", ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

  @Step("Get ticker change")
  public static List<HashMap<String, Object>> deleteData(String ticker) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" DELETE  FROM  RISK_ANALYST_MARGIN_REVIEWED_FULL where ticker = :ticker   ");
    try {

      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("ticker", ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

  @Step("Get ticker change")
  public static List<HashMap<String, Object>> insertData(String ticker) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" INSERT INTO RISK_ANALYST_MARGIN_REVIEWED_FULL set LOAN_TYPE = 'LOAN',PRICE_TYPE = 'MARKET_PRICE',LOAN_RATIO = 50, LOAN_PRICE = '10000'\n" +
      "where ticker = :symbol   ");
    try {

      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("symbol", ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

}