package com.tcbs.automation.iris.bond;

import com.tcbs.automation.iris.AwsIRis;
import com.tcbs.automation.staging.AwsStagingDwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostSyncBondLimitEntity {

  @SuppressWarnings("unchecked")
  @Step("Get list bond limit expose")
  public static List<HashMap<String, Object>> getCorpBondLimitExpose() {
    StringBuilder query = new StringBuilder();
    query.append(" select * from Stg_risk_Bond_Limit_Expose where Updated_Date = (select max(Updated_Date) from Stg_risk_Bond_Limit_Expose)  ");
    try {
      return AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @SuppressWarnings("unchecked")
  @Step("Get list issuer from risk bond info")
  public static List<HashMap<String, Object>> getCorpBondIssuerFromRiskBondInfo() {
    StringBuilder query = new StringBuilder();
    query.append(" select DISTINCT ISSUER_GA_ID AS \"GA_ID\", ISSUER_GA_NAME as \"Issuer_Name\"  from RISK_ISSUER_INFO where UPDATED_DATE  = (select max(UPDATED_DATE) from RISK_ISSUER_INFO) ");
    query.append(" AND ISSUER_GA_ID IS NOT null ");
    try {
      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @SuppressWarnings("unchecked")
  @Step("Get issuer information from risk bond info")
  public static List<HashMap<String, Object>> getIssuerInfoFromBonInfo(String gaId) {
    StringBuilder query = new StringBuilder();
    query.append(" select * from RISK_BOND_INFO where UPDATED_DATE  = (select max(UPDATED_DATE) from RISK_BOND_INFO) ");
    query.append(" AND GA_ID = :gaId ");
    try {
      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("gaId", gaId)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get data from iRis")
  public static List<HashMap<String, Object>> getTree(String gaId) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT DISTINCT (GA_ID), LEVEL FROM RISK_BOND_INFO S ");
    queryBuilder.append(" START WITH S.GA_ID = :gaId  ");
    queryBuilder.append(" CONNECT BY S.GA_ID = PRIOR S.GROUP_ISSUER_ID  ");
    queryBuilder.append("  order by level desc ");
    try {
      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("gaId", gaId)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
