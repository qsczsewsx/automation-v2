package com.tcbs.automation.iris;

import com.tcbs.automation.config.idatatcanalysis.IData;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RiskConfConditionsEnity {

  @Step("get conf conditions")
  public static List<HashMap<String, Object>> getAllConfConditons() {
    StringBuilder query = new StringBuilder();
    query.append("SELECT * FROM ( SELECT rc.*, ROW_NUMBER() OVER (PARTITION BY NAME ORDER BY ID DESC) AS rn " +
      "FROM IDATASIT.RISK_CONDITION rc) tm WHERE rn = 1");
    try {
      return IData.idataDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList();
  }
}
