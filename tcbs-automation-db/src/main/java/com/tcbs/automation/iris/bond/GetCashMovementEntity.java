package com.tcbs.automation.iris.bond;

import com.tcbs.automation.staging.AwsStagingDwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetCashMovementEntity {

  @Step("Get data from iRis")
  public static List<HashMap<String, Object>> getData(String currentdate) {
    StringBuilder query = new StringBuilder();
    query.append(" exec proc_iris_flex_tcbs_cash_mvmt @currentdate= :currentdate  ");
    try {
      List<HashMap<String, Object>> resultList = AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("currentdate", currentdate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get data from iRis")
  public static List<HashMap<String, Object>> getCashMovementFromIRis() {
    StringBuilder query = new StringBuilder();
    query.append(" select * from iris_cash_movement  ");
    try {
      List<HashMap<String, Object>> resultList = AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
