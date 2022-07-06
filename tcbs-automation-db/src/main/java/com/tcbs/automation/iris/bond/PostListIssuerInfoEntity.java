package com.tcbs.automation.iris.bond;

import com.tcbs.automation.iris.AwsIRis;
import lombok.extern.slf4j.Slf4j;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Slf4j
public class PostListIssuerInfoEntity {
  private static final String ISSUER_GA_ID = "ISSUER_GA_ID";
  private static final String ISSUER_GA_NAME = "ISSUER_GA_NAME";
  private static final String ISSUER_GA_GROUP_NAME = "ISSUER_GA_GROUP_NAME";
  private static final String ISSUER_GA_GROUP_ID = "ISSUER_GA_GROUP_ID";

  @SuppressWarnings("unchecked")
  @Step("Get list issuer info from risk issuer info table")
  public static List<HashMap<String, Object>> getListIssuerInfoFromBondInfo() {
    Session session = AwsIRis.AwsIRisDbConnection.getSession();
    StringBuilder query = new StringBuilder();
    query.append(" SELECT DISTINCT (GA_ID) AS \"ISSUER_GA_ID\", ISSUER_NAME AS \"ISSUER_GA_NAME\" FROM (  ");
    query.append(" SELECT distinct GA_ID , ISSUER_NAME  from RISK_BOND_INFO ");
    query.append(" WHERE UPDATED_DATE = (SELECT max (UPDATED_DATE) FROM RISK_BOND_INFO) ");
    query.append(" and GA_ID IS NOT NULL UNION ");
    query.append(" SELECT distinct GROUP_ISSUER_ID, GROUP_ISSUER_NAME from RISK_BOND_INFO ");
    query.append("  WHERE UPDATED_DATE = (SELECT max (UPDATED_DATE) FROM RISK_BOND_INFO) ");
    query.append(" and GROUP_ISSUER_ID IS NOT NULL) ");
    try {
      List<HashMap<String, Object>> test = session.createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      session.clear();
      return test;
    } catch (Exception ex) {
      session.clear();
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @SuppressWarnings("unchecked")
  @Step("Get list issuer info from risk bond info table")
  public static List<HashMap<String, Object>> getListIssuerInfoFromRiskIssuerInfo() {
    StringBuilder query = new StringBuilder();
    query.append(" select * from RISK_ISSUER_INFO where UPDATED_DATE  = (select max(UPDATED_DATE) from RISK_ISSUER_INFO)  ");
    try {
      List<HashMap<String, Object>> result = AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      List<HashMap<String, Object>> resultFinal = new ArrayList<>();
      result.forEach(v -> {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put(ISSUER_GA_ID, Double.parseDouble(v.get(ISSUER_GA_ID).toString()));
        hashMap.put(ISSUER_GA_NAME, v.get(ISSUER_GA_NAME));
        hashMap.put(ISSUER_GA_GROUP_ID, v.get(ISSUER_GA_GROUP_ID) == null? null: Double.parseDouble(v.get(ISSUER_GA_GROUP_ID).toString()));
        hashMap.put(ISSUER_GA_GROUP_NAME, v.get(ISSUER_GA_GROUP_NAME));
        resultFinal.add(hashMap);
      });
      return resultFinal;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @SuppressWarnings("unchecked")
  @Step("Get group info from risk bond info table")
  public static HashMap<String, Object> getGroupInfoByGaId(String gaId) {
    StringBuilder query = new StringBuilder();
    query.append(" select * from RISK_BOND_INFO where UPDATED_DATE  = (select max(UPDATED_DATE) from RISK_BOND_INFO) AND GA_ID = :gaId  ");
    try {
      List<HashMap<String, Object>> result = AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("gaId", gaId)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      if(result.isEmpty()) {
        return new HashMap<>();
      } else {
        return result.get(0);
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new HashMap<>();
  }

}
