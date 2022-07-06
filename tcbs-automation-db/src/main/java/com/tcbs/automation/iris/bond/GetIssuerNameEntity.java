package com.tcbs.automation.iris.bond;

import com.tcbs.automation.iris.AwsIRis;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class GetIssuerNameEntity {

  @Step("Get data from iRis")
  public static List<HashMap<String, Object>> getIssuer(String issuerName) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select distinct ISSUER_ID,GA_ID,ISSUER_NAME, ISSUER_SHORT_NAME  ");
    queryBuilder.append(" from RISK_BOND_INFO  ");
    queryBuilder.append(" WHERE UPPER (ISSUER_NAME)  LIKE :issuerName  ");
    queryBuilder.append(" and UPDATED_DATE = (select max(UPDATED_DATE) from RISK_BOND_INFO)  and GA_ID <> 0  ");

    try {
      List<HashMap<String, Object>> resultList = AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("issuerName", issuerName)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static List<HashMap<String, Object>> getAllIssuer() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select distinct ISSUER_ID,GA_ID,ISSUER_NAME, ISSUER_SHORT_NAME  ");
    queryBuilder.append(" from RISK_BOND_INFO  ");
    queryBuilder.append(" where UPDATED_DATE = (select max(UPDATED_DATE) from RISK_BOND_INFO) and GA_ID <> 0  ");

    try {
      List<HashMap<String, Object>> resultList = AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
