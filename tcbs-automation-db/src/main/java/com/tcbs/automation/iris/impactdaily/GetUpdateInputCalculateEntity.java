package com.tcbs.automation.iris.impactdaily;

import com.tcbs.automation.iris.AwsIRis;
import com.tcbs.automation.stockmarket.Stockmarket;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class GetUpdateInputCalculateEntity {
  @Step("Get ticker change")
  public static List<HashMap<String, Object>> getTickerDropDown(String fromDate, String toDate) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select * from ( ");
    queryBuilder.append(" SELECT CONVERT_TZ(from_unixtime(i_seq_time), '+00:00', '+07:00') as time, ");
    queryBuilder.append(" t_ticker                                                     ticker, ");
    queryBuilder.append(" f_close_price, ");
    queryBuilder.append(" f_open_price - f_open_price_change f_open_price ");
    queryBuilder.append(" FROM intraday_by_1min where LENGTH(t_ticker) = 3 AND t_ticker != 'TCB' ");
    queryBuilder.append(" ) as a where time >= :fromDate and time < :toDate ");
    try {

      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

  @Step("Get ticker change")
  public static List<HashMap<String, Object>> getFullList() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select * from RISK_ANALYST_MARGIN_REVIEWED_FULL where LOAN_TYPE = 'LOAN' ");
    try {

      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

}
