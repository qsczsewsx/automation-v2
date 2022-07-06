package com.tcbs.automation.iris.bond;

import com.tcbs.automation.iris.AwsIRis;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetGroupInfoEntity {

  @Step("Get data from iRis")
  public static List<HashMap<String, Object>> getIssuer(String issuerGaId) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select *  ");
    queryBuilder.append(" from RISK_CONFIG_LIMIT  ");
    queryBuilder.append(" WHERE ISSUER_GA_GROUP_ID =  :issuerGaId ");

    try {
      List<HashMap<String, Object>> resultList = AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("issuerGaId", issuerGaId)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
