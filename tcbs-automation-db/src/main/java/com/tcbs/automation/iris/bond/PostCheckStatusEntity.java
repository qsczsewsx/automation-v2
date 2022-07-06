package com.tcbs.automation.iris.bond;


import com.tcbs.automation.iris.AwsIRis;
import com.tcbs.automation.riskcloud.AwsRiskCloud;
import com.tcbs.automation.staging.AwsStagingDwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostCheckStatusEntity {
  private PostCheckStatusEntity() {
    throw new IllegalStateException("Static class");
  }

  @Step("Get data from iRis")
  public static List<HashMap<String, Object>> getValue(List<String> listBond) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select sum(Value) Value from Liquidity_iBondOustanding  ");
    queryBuilder.append(" where bondCode in :listBond  ");
    queryBuilder.append(" and UpdateDate = (select max(UpdateDate) from Liquidity_iBondOustanding  )  ");
    HashMap<String, Object> params = new HashMap<>();
    params.put("listBond", listBond);
    return executeQuery(AwsRiskCloud.awsRiskCloudDbConnection.getSession(), queryBuilder.toString(), params);
  }

  private static List<HashMap<String, Object>> executeQuery(Session ss, String query, HashMap<String, Object> params) {
    try {
      NativeQuery qr = ss.createNativeQuery(query);
      for (String key: params.keySet()) {
        qr = qr.setParameter(key, params.get(key));
      }
      return qr.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static List<HashMap<String, Object>> getGaId(String bondCode) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select * from RISK_BOND_INFO where BOND_CODE = :bondCode ");
    HashMap<String, Object> params = new HashMap<>();
    params.put("bondCode", bondCode);
    return executeQuery(AwsIRis.AwsIRisDbConnection.getSession(), queryBuilder.toString(), params);
  }

  @Step("Get data from iRis")
  public static List<HashMap<String, Object>> getTree(String gaId) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT GA_ID, LEVEL FROM RISK_BOND_INFO S ");
    queryBuilder.append(" START WITH S.GA_ID = :gaId  ");
    queryBuilder.append(" CONNECT BY S.GA_ID = PRIOR S.GROUP_ISSUER_ID  ");
    queryBuilder.append("  order by level desc ");
    HashMap<String, Object> params = new HashMap<>();
    params.put("gaId", gaId);
    return executeQuery(AwsIRis.AwsIRisDbConnection.getSession(), queryBuilder.toString(), params);
  }

  @Step("Get data from iRis")
  public static List<HashMap<String, Object>> getChildBondCode(String gaId1) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" with temp as (SELECT GA_ID, CONNECT_BY_ROOT GROUP_ISSUER_ID, LEVEL FROM RISK_BOND_INFO S ");
    queryBuilder.append(" START WITH S.GA_ID = :gaId1  ");
    queryBuilder.append(" CONNECT BY PRIOR S.GA_ID = S.GROUP_ISSUER_ID)  ");
    queryBuilder.append(" SELECT BOND_CODE from RISK_BOND_INFO where GA_ID in  ( select GA_ID from temp) ");
    HashMap<String, Object> params = new HashMap<>();
    params.put("gaId1", gaId1);
    return executeQuery(AwsIRis.AwsIRisDbConnection.getSession(), queryBuilder.toString(), params);
  }

  @Step("Get data from iRis")
  public static List<HashMap<String, Object>> getLimit(String issuerGaId, String issuerType) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select * from  RISK_CONFIG_LIMIT    ");
    queryBuilder.append(" where ISSUER_GA_ID = :issuerGaId and STATUS = 'ACTIVE' and ISSUER_TYPE = :issuerType ");
    HashMap<String, Object> params = new HashMap<>();
    params.put("issuerGaId", issuerGaId);
    params.put("issuerType", issuerType);
    return executeQuery(AwsIRis.AwsIRisDbConnection.getSession(), queryBuilder.toString(), params);
  }

  @Step("Get data from iRis")
  public static List<HashMap<String, Object>> getRoot(String gaId2) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" SELECT GA_ID, CONNECT_BY_ROOT GROUP_ISSUER_ID, LEVEL FROM RISK_BOND_INFO S ");
    queryBuilder.append(" START WITH S.GA_ID = :gaId2  ");
    queryBuilder.append(" CONNECT BY PRIOR S.GA_ID = S.GROUP_ISSUER_ID  ");

    HashMap<String, Object> params = new HashMap<>();
    params.put("gaId2", gaId2);
    return executeQuery(AwsIRis.AwsIRisDbConnection.getSession(), queryBuilder.toString(), params);
  }

  @Step("Get data from iRis")
  public static List<HashMap<String, Object>> getInfo(String gaId, String type) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" select distinct ID, ISSUER_ID, ISSUER_NAME, ISSUER_SHORT_NAME, ISSUER_TYPE, ISSUER_GA_ID, ISSUER_GA_GROUP_ID, ISSUER_GA_GROUP_NAME  ");
    queryBuilder.append(" from RISK_CONFIG_LIMIT where ISSUER_GA_ID = :gaId and ISSUER_TYPE = :type  ");

    HashMap<String, Object> params = new HashMap<>();
    params.put("gaId", gaId);
    params.put("type", type);
    return executeQuery(AwsIRis.AwsIRisDbConnection.getSession(), queryBuilder.toString(), params);
  }

  @Step("Get data from iRis")
  public static List<HashMap<String, Object>> getInfoIssuer(String bondCode) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" select * from RISK_CONFIG_LIMIT where ISSUER_GA_ID = (select GA_ID from RISK_BOND_INFO where BOND_CODE = :bondCode )  and ISSUER_TYPE = 'ISSUER'  ");

    HashMap<String, Object> params = new HashMap<>();
    params.put("bondCode", bondCode);
    return executeQuery(AwsIRis.AwsIRisDbConnection.getSession(), queryBuilder.toString(), params);
  }
}
