package com.tcbs.automation.iris.bond;

import com.tcbs.automation.staging.AwsStagingDwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostUpdateLimitEntity {
  private PostUpdateLimitEntity() {
    throw new IllegalStateException("Static class");
  }

  private static final String TARGET_ID = "targetId";
  private static final String TARGET_EXTERNAL_ID = "targetExternalId";

  @Step("Get data from investment limit corp bond")
  public static List<HashMap<String, Object>> getValue(Double resolutionId) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select *from Stg_risk_InvestmentLimit_CorpBond where updateDate = (select max(updateDate) from Stg_risk_InvestmentLimit_CorpBond)  ");
    queryBuilder.append(" and resolutions_Id = :resolutionId  ");
    HashMap<String, Object> params = new HashMap<>();
    params.put("resolutionId", resolutionId);
    return executeQuery(AwsStagingDwh.awsStagingDwhDbConnection.getSession(), queryBuilder.toString(), params);
  }

  private static List<HashMap<String, Object>> executeQuery(Session ss, String query, HashMap<String, Object> params) {
    try {
      NativeQuery qr = ss.createNativeQuery(query);
      for (String key : params.keySet()) {
        qr = qr.setParameter(key, params.get(key));
      }
      return qr.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get data from corp bond mapping")
  public static List<HashMap<String, Object>> getCorpBondMapping(List<Double> listLimitId) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select *from Stg_risk_InvestmentLimit_CorpBond_Mapping where updateDate  = (select max(updateDate) from Stg_risk_InvestmentLimit_CorpBond_Mapping) ");
    queryBuilder.append(" and limitId  IN :listLimitId ");
    HashMap<String, Object> params = new HashMap<>();
    params.put("listLimitId", listLimitId);
    return executeQuery(AwsStagingDwh.awsStagingDwhDbConnection.getSession(), queryBuilder.toString(), params);
  }

  @Step("Get data from corp bond attribute")
  public static List<HashMap<String, Object>> getCorpBondAttr(List<HashMap<String, Object>> listTargetIdAndExternalId) {
    List<HashMap<String, Object>> getCorpBondAttribute = new ArrayList<>();
    listTargetIdAndExternalId.forEach(v -> {
      StringBuilder queryBuilder = new StringBuilder();
      queryBuilder.append(" select *  from Stg_risk_InvestmentLimit_CorpBond_Attr where updateDate  = (select max(updateDate) from Stg_risk_InvestmentLimit_CorpBond_Mapping) ");
      queryBuilder.append(" and targetExternalId = :targetExternalId and targetId = :targetId ");
      try {
        List<HashMap<String, Object>> resultList = AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
          .setParameter(TARGET_ID, Double.parseDouble(v.get(TARGET_ID).toString()))
          .setParameter(TARGET_EXTERNAL_ID, Double.parseDouble(v.get(TARGET_EXTERNAL_ID).toString()))
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
        getCorpBondAttribute.addAll(resultList);
      } catch (Exception ex) {
        StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
      }
    });
    return getCorpBondAttribute;
  }
}
