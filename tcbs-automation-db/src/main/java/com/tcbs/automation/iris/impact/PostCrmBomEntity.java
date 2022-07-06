package com.tcbs.automation.iris.impact;

import com.tcbs.automation.iris.AwsIRis;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostCrmBomEntity {

  @Step("Get max Etl")
  public static List<HashMap<String, Object>> getData(String etl) {

    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("  select * from RISK_RTT_CALCULATE_RESULT where ETL_UPDATE_DATE = :etl and STATUS_BONUS <> 'Normal' ");
    try {
      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("etl", etl)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }
}
