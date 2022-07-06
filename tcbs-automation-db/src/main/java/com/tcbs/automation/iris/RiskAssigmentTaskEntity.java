package com.tcbs.automation.iris;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RiskAssigmentTaskEntity {

  @Step("Get Latest assigment task")
  public static List<HashMap<String, Object>> getLatestAssigment(List<String> ticker) {
    StringBuilder query = new StringBuilder();
    query.append("SELECT * FROM risk_assignment_info where ticker in :ticker and create_date = (select max(create_date) from risk_assignment_info)");

    try {
      List<HashMap<String, Object>> resultList = AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("ticker", ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }
}
