package com.tcbs.automation.hfcdata;

import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CheckScoreRiskAppetiteEntity {

  public static List<HashMap<String, Object>> getScoreFromDwh() {
    StringBuilder query = new StringBuilder();
    query.append("select TCBSID, CALCULATED_SCORE, RATE from( " +
      "select TCBSID, CONVERT(decimal, CALCULATED_SCORE) as CALCULATED_SCORE, " +
      "case when calculated_score <= 40 then 'low' " +
      "when calculated_score > 40 and calculated_score <= 70 then 'mid' " +
      "when calculated_score > 70 then 'high' " +
      "end as RATE " +
      "from Stg_typeform_Responses where form_id = 'l5M94x') as risk_appetie group by TCBSID, CALCULATED_SCORE, RATE order by TCBSID");
    try {
      List<HashMap<String, Object>> resultList = HfcData.dwhDbConnection.getSession().createNativeQuery(query.toString()).setResultTransformer(
        AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static List<HashMap<String, Object>> getScoreFromHfc() {
    StringBuilder query = new StringBuilder();
    query.append("select TCBSID, CALCULATED_SCORE, RATE from cus_risk_appetite group by tcbsid, calculated_score, rate order by TCBSID");
    try {
      List<HashMap<String, Object>> resultList = HfcData.hfcDataDbConnection.getSession().createNativeQuery(query.toString()).setResultTransformer(
        AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
