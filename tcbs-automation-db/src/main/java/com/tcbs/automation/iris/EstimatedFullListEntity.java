package com.tcbs.automation.iris;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class EstimatedFullListEntity {

  @Step("Get data from IRISK")
  public static List<HashMap<String, Object>> getFullListMarginReviewed() {
    StringBuilder query = new StringBuilder();
    query.append(
      "select distinct TICKER, LOAN_PRICE, ROOM_FINAL, AFMAXAMT,AFMAXAMTT3  from RISK_ANALYST_MARGIN_REVIEWED_FULL where LOAN_TYPE = 'LOAN'  or (LOAN_TYPE = 'BLACK_LIST' and ROOM_USED >0 ) order by ticker ");
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
