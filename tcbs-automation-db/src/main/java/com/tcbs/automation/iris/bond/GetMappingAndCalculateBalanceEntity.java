package com.tcbs.automation.iris.bond;

import com.tcbs.automation.iris.AwsIRis;
import com.tcbs.automation.staging.AwsStagingDwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class GetMappingAndCalculateBalanceEntity {
  private static final String TARGET_EXTERNAL_ID = "targetExternalId";
  private static final String TARGET_ID = "targetId";
  private static final String LIMIT_ID = "limitId";
  private static final String LIMIT_VALUE = "limitValue";

  @SuppressWarnings("unchecked")
  @Step("Get corp bond limit balance remain")
  public static List<HashMap<String, Object>> getCorpBondLimitBalanceRemain() {
    StringBuilder query = new StringBuilder();
    query.append(" select * from Stg_Risk_Bond_Limit_Balance_Remain where Updated_Date = (select max(Updated_Date) from Stg_Risk_Bond_Limit_Balance_Remain) ");
    try {
      return AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @SuppressWarnings("unchecked")
  @Step("Get corp bond balance")
  public static List<HashMap<String, Object>> getCorpBondBalance() {
    StringBuilder query = new StringBuilder();
    query.append(" select * from Stg_Risk_Bond_Balance where Updated_Date = (select max(Updated_Date) from Stg_Risk_Bond_Balance) ");
    try {
      return AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @SuppressWarnings("unchecked")
  @Step("Get corp bond info")
  public static List<HashMap<String, Object>> getCorpBondInformation() {
    StringBuilder query = new StringBuilder();
    query.append(" select * from RISK_BOND_INFO where UPDATED_DATE  = (select max(UPDATED_DATE) from RISK_BOND_INFO) ");
    try {
      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @SuppressWarnings("unchecked")
  @Step("Get remain final info")
  public static List<HashMap<String, Object>> getRemainFinalInfo() {
    StringBuilder query = new StringBuilder();
    query.append(" select * from Stg_risk_InvestmentLimit_CorpBond_Final where Updated_Date = (select max(Updated_Date) from Stg_risk_InvestmentLimit_CorpBond_Final) ");
    try {
      return AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @SuppressWarnings("unchecked")
  public static List<HashMap<String, Object>> mappingInvestmentLimit(Double limitId, Double targetType, Double targetId, Double targetExternalId) {
    StringBuilder query = new StringBuilder();
    query.append(" select * from Stg_risk_InvestmentLimit_CorpBond where  limitId = :limitId and targetId = :targetId ");
    query.append(" and targetType = :targetType and targetExternalId = :targetExternalId ");
    query.append(" and updateDate = (select max(updateDate) from Stg_risk_InvestmentLimit_CorpBond) ");
    try {
      List<HashMap<String, Object>> limitInfo = AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter(LIMIT_ID, limitId)
        .setParameter(TARGET_ID, targetId)
        .setParameter("targetType", targetType)
        .setParameter(TARGET_EXTERNAL_ID, targetExternalId)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      limitInfo.forEach(v -> {
        v.put("limit_" + targetType, Double.parseDouble(v.get("limitHtm").toString()) + Double.parseDouble(v.get("limitTrading").toString()));
      });
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @SuppressWarnings("unchecked")
  public static Double calculateBalance(String type, String value) {
    List<Double> listBondBalance = new ArrayList<>();
    Double sumBalance = null;
    StringBuilder query = new StringBuilder();
    query.append("select Bond_Balance from Stg_Risk_Bond_Balance where Updated_Date = (select max(Updated_Date) from Stg_Risk_Bond_Balance) and " + type + " = :value ");
    try {
      List<HashMap<String, Object>> balanceInfo = AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("value", value)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      if (balanceInfo.isEmpty()) {
        return sumBalance;
      }
      balanceInfo.forEach(v -> {
        if (v.get("Bond_Balance") != null) {
          listBondBalance.add(Double.parseDouble(v.get("Bond_Balance").toString()));
        }
      });
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    if (!listBondBalance.isEmpty()) {
      sumBalance = listBondBalance.stream().mapToDouble(i -> i).sum();
    }
    return sumBalance;
  }

  @SuppressWarnings("unchecked")
  public static List<HashMap<String, Object>> getListTargetExternalIdOfBond(String bondCode) {
    List<HashMap<String, Object>> result = new ArrayList<>();
    StringBuilder query = new StringBuilder();
    query.append(
      "select targetExternalId, targetId from Stg_risk_InvestmentLimit_CorpBond_Attr where updateDate  = (select max(updateDate) from Stg_risk_InvestmentLimit_CorpBond_Mapping) and bondCode = :bondCode ");
    try {
      List<HashMap<String, Object>> listTargetExternalId = AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("bondCode", bondCode)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      if (listTargetExternalId.isEmpty()) {
        return result;
      }
      listTargetExternalId.forEach(v -> {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(TARGET_EXTERNAL_ID, v.get(TARGET_EXTERNAL_ID).toString());
        hashMap.put(TARGET_ID, v.get(TARGET_ID).toString());
        result.add(hashMap);
      });
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  public static HashMap<String, Object> getLimitId(Double targetId, Double targetExternalId) {
    HashMap<String, Object> resultLimit = new HashMap<>();
    StringBuilder query = new StringBuilder();
    query.append("select * from Stg_risk_InvestmentLimit_CorpBond_Mapping where updateDate  = (select max(updateDate) from Stg_risk_InvestmentLimit_CorpBond_Mapping)   ");
    query.append("and targetId = :targetId and targetExternalId = :targetExternalId and targetStatus = 1");
    try {
      HashMap<String, Object> hashMapLimitInfo = (HashMap<String, Object>) AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter(TARGET_ID, targetId)
        .setParameter(TARGET_EXTERNAL_ID, targetExternalId)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList().get(0);
      if (hashMapLimitInfo.isEmpty()) {
        return new HashMap<>();
      }
      HashMap<String, Object> limitCorpBondInfo = getLimitInformationFromLimitCorpBond(Double.parseDouble(hashMapLimitInfo.get(LIMIT_ID).toString()), hashMapLimitInfo.get("targetType").toString());
      if(limitCorpBondInfo != null && !limitCorpBondInfo.isEmpty()) {
        resultLimit.put("type", limitCorpBondInfo.get("type"));
        resultLimit.put(LIMIT_VALUE, limitCorpBondInfo.get(LIMIT_VALUE));
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return resultLimit;
  }

  @SuppressWarnings("unchecked")
  public static HashMap<String, Object> getLimitInformationFromLimitCorpBond(Double limitId, String type) {
    HashMap<String, Object> result = new HashMap<>();
    StringBuilder query = new StringBuilder();
    query.append(" select *from Stg_risk_InvestmentLimit_CorpBond where updateDate = (select max(updateDate) from Stg_risk_InvestmentLimit_CorpBond) and limitId = :limitId");
    try {
      HashMap<String, Object> listLimitInfo = (HashMap<String, Object>) AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter(LIMIT_ID, limitId)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList().get(0);
      if (listLimitInfo.isEmpty()) {
        return null;
      }
      result.put("type", type);
      result.put(LIMIT_VALUE, Double.parseDouble(listLimitInfo.get("limitHtm").toString()) + Double.parseDouble(listLimitInfo.get("limitTrading").toString()));
      return result;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return null;
  }

  public static List<HashMap<String, Object>> findMinValue(List<String> listType, List<HashMap<String, Object>> result) {
    AtomicReference<Double> minValue = new AtomicReference<>(0.0);
    List<HashMap<String, Object>> resultFinal = new ArrayList<>();
    for (String type : listType) {
      HashMap<String, Object> hashMap = new HashMap<>();
      final int[] i = {0};
      result.forEach(v -> {
        if (v.get("type").equals(type)) {
          if (i[0] == 0) {
            minValue.set(Double.parseDouble(v.get(LIMIT_VALUE).toString()));
            i[0] = i[0] + 1;
          } else {
            if (Double.parseDouble(v.get(LIMIT_VALUE).toString()) < minValue.get()) {
              minValue.set(Double.parseDouble(v.get(LIMIT_VALUE).toString()));
            }
          }
        }
      });
      hashMap.put("type", type);
      hashMap.put(LIMIT_VALUE, Double.parseDouble(minValue.toString()));
      resultFinal.add(hashMap);
    }
    return resultFinal;
  }

  public static String mappingLimitType(String type) {
    String limitType = null;
    switch (type) {
      case "bond_temp":
        limitType = "Bond_Temp_Limit";
        break;
      case "bond_case":
        limitType = "Bond_Case_Limit";
        break;
      case "issuer":
        limitType = "Issuer_Limit";
        break;
      case "group":
        limitType = "Group_Limit";
        break;
      default:
        break;
    }
    return limitType;
  }
}
