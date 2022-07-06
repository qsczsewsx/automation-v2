package com.tcbs.automation.hfcdata;

import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetRiskAppetiteEntity {

  public static List<HashMap<String, Object>> getRiskAppetite(String tcbsid) {

    StringBuilder query = new StringBuilder();
    query.append("SELECT * FROM (SELECT * FROM CUS_RISK_APPETITE where TCBSID = :tcbsid ORDER BY SUBMITTED_AT DESC) WHERE ROWNUM = 1");

    try {
      List<HashMap<String, Object>> resultList = HfcData.hfcDataDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("tcbsid", tcbsid)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }
}
