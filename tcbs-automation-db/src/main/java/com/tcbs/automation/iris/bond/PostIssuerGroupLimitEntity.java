package com.tcbs.automation.iris.bond;

import com.tcbs.automation.iris.AwsIRis;
import com.tcbs.automation.staging.AwsStagingDwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PostIssuerGroupLimitEntity {
  private static final String ISSUER_GA_ID = "Issuer_Ga_Id";
  private static final String ISSUER_NAME = "Issuer_Name";
  private static final String ISSUER_GA_GROUP_ID = "Issuer_Ga_Group_Id";
  private static final String ISSUER_GA_GROUP_NAME = "Issuer_Ga_Group_Name";
  private static final String LIMIT_ID = "Limit_Id";
  private static final String LIMIT_VALUE = "Limit_Value";
  private static final String LIMIT_TYPE = "Limit_Type";
  private static final String APPLY_FOR = "apply_For";

  @Step("Get data from iRis")
  public static List<HashMap<String, Object>> getIssuerGroupLimit(String request) {
    try {
      if (request == null || request.equals("")) {
        return getAllIssuerGroupLimit();
      } else {
        return getListIssuerGroupLimit(request);
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  private static List<HashMap<String, Object>> getAllIssuerGroupLimit() {
    StringBuilder query = new StringBuilder();
    List<HashMap<String, Object>> result = new ArrayList<>();
    List<HashMap<String, Object>> response;
    query.append(" select * from Stg_risk_Bond_Limit_Expose where Updated_Date = (select max(Updated_Date) from Stg_risk_Bond_Limit_Expose) ");
    response = AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(query.toString())
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    try {
      List<HashMap<String, Object>> listAllIssuerGroupLimit = addingGroupInfo(response);
      List<Double> listIssuerGaGroupId = getListIssuerGaGroupId(listAllIssuerGroupLimit);
      result = combineIssuerGaIdHasSameGroupId(listAllIssuerGroupLimit, listIssuerGaGroupId);
      return result;

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();

  }

  private static List<HashMap<String, Object>> combineIssuerGaIdHasSameGroupId(List<HashMap<String, Object>> listAllIssuerGroupLimit, List<Double> listIssuerGaGroupId) {
    List<HashMap<String, Object>> result = new ArrayList<>();
    listIssuerGaGroupId.forEach(v -> {
      HashMap<String, Object> hashMapGroup = getLimitByType(v, "GROUP", listAllIssuerGroupLimit);
      if (!hashMapGroup.isEmpty()) {
        result.add(hashMapGroup);
      }
      HashMap<String, Object> hashMapIssuer = getLimitByType(v, "ISSUER", listAllIssuerGroupLimit);
      if (!hashMapIssuer.isEmpty()) {
        result.add(hashMapIssuer);
      }
    });
    return result;
  }

  private static HashMap<String, Object> getLimitByType(Double groupId, String type, List<HashMap<String, Object>> listAllIssuerGroupLimit) {
    List<HashMap<String, Object>> listApplyFor = new ArrayList<>();
    HashMap<String, Object> hashMapGroup = new HashMap<>();
    for (HashMap<String, Object> group : listAllIssuerGroupLimit) {
      if (Double.parseDouble(group.get(ISSUER_GA_GROUP_ID).toString()) == groupId && group.get(LIMIT_TYPE).equals(type)) {
        hashMapGroup.put(ISSUER_GA_GROUP_ID, Double.parseDouble(group.get(ISSUER_GA_GROUP_ID).toString()));
        hashMapGroup.put(ISSUER_GA_GROUP_NAME, group.get(ISSUER_GA_GROUP_NAME));
        hashMapGroup.put(LIMIT_VALUE, Double.parseDouble(group.get(LIMIT_VALUE).toString()));
        hashMapGroup.put(LIMIT_TYPE, group.get(LIMIT_TYPE));
        HashMap<String, Object> applyFor = new HashMap<>();
        applyFor.put(ISSUER_GA_ID, Double.parseDouble(group.get(ISSUER_GA_ID).toString()));
        applyFor.put(ISSUER_NAME, group.get(ISSUER_NAME));
        listApplyFor.add(applyFor);
        hashMapGroup.put(APPLY_FOR, listApplyFor);
      } else {
        continue;
      }
    }
    return hashMapGroup;
  }

  private static List<Double> getListIssuerGaGroupId(List<HashMap<String, Object>> data) {
    try {
      List<Double> listGaGroupId = new ArrayList<>();
      data.forEach(v -> {
        if (!listGaGroupId.contains(Double.parseDouble(v.get(ISSUER_GA_GROUP_ID).toString()))) {
          listGaGroupId.add(Double.parseDouble(v.get(ISSUER_GA_GROUP_ID).toString()));
        }
      });
      return listGaGroupId;

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  private static List<HashMap<String, Object>> getListIssuerGroupLimit(String request) {
    StringBuilder query = new StringBuilder();
    List<HashMap<String, Object>> result;
    request = request.replace("[", "").replace("]", "");
    List<String> listGaId = Arrays.asList(request.split(","));
    query.append("select * from Stg_risk_Bond_Limit_Expose where Updated_Date = (select max(Updated_Date) from Stg_risk_Bond_Limit_Expose) ");
    query.append(" and Issuer_Ga_Id IN :listGaId");
    List<HashMap<String, Object>> response = AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(query.toString())
      .setParameter("listGaId", listGaId)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    try {
      List<HashMap<String, Object>> listIssuerGroupLimit = addingGroupInfo(response);
      List<Double> listIssuerGaGroupId = getListIssuerGaGroupId(listIssuerGroupLimit);
      List<HashMap<String, Object>> getListIssuerGroupLimitFinal = getListIssuerGroupLimitIncludeMissingIssuerGroup(listIssuerGaGroupId);
      result = combineIssuerGaIdHasSameGroupId(getListIssuerGroupLimitFinal, listIssuerGaGroupId);
      return result;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  private static List<HashMap<String, Object>> getListIssuerGroupLimitIncludeMissingIssuerGroup(List<Double> listIssuerGroupLimit) {
    StringBuilder query = new StringBuilder();
    query.append("select * from Stg_risk_Bond_Limit_Expose where Updated_Date = (select max(Updated_Date) from Stg_risk_Bond_Limit_Expose) ");
    query.append(" and (Issuer_Ga_Group_Id  IN  :listIssuerGroupLimit ");
    query.append(" or (Issuer_Ga_Group_Id is null and Issuer_Ga_Id IN :listIssuerGroupLimit)) ");
    try {
      if(listIssuerGroupLimit.size() == 0) {
        return new ArrayList<>();
      }
      List<HashMap<String, Object>> result;
      List<HashMap<String, Object>> resultList = AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("listIssuerGroupLimit", listIssuerGroupLimit)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      result = addingGroupInfo(resultList);
      return result;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }


  private static List<HashMap<String, Object>> addingGroupInfo(List<HashMap<String, Object>> data) {
    List<HashMap<String, Object>> result = new ArrayList<>();
    data.forEach(v -> {
      HashMap<String, Object> hashMap = new HashMap<>();
      hashMap.put(ISSUER_GA_GROUP_ID, v.get(ISSUER_GA_GROUP_ID) == null ? Double.parseDouble(v.get(ISSUER_GA_ID).toString()) : Double.parseDouble(v.get(ISSUER_GA_GROUP_ID).toString()));
      hashMap.put(ISSUER_GA_GROUP_NAME, v.get(ISSUER_GA_GROUP_NAME) == null ? v.get(ISSUER_NAME) : v.get(ISSUER_GA_GROUP_NAME));
      hashMap.put(LIMIT_VALUE, Double.parseDouble(v.get(LIMIT_VALUE).toString()));
      hashMap.put(LIMIT_TYPE, v.get(LIMIT_TYPE));
      hashMap.put(ISSUER_GA_ID, Double.parseDouble(v.get(ISSUER_GA_ID).toString()));
      hashMap.put(ISSUER_NAME, v.get(ISSUER_NAME));
      hashMap.put(LIMIT_ID, v.get(LIMIT_ID));
      result.add(hashMap);
    });
    return result;
  }


  @Step("Get data from iRis")
  public static List<HashMap<String, Object>> getAllStatus() {
    StringBuilder query = new StringBuilder();
    query.append(" select * from RISK_CONFIG_LIMIT  ");
    try {
      List<HashMap<String, Object>> resultList = AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
