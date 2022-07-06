package com.tcbs.automation.iris.coco;

import com.tcbs.automation.iris.AwsIRis;
import com.tcbs.automation.tcbsdwh.Tcbsdwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UpdateDwhEntity {
  @Step("Get data")
  public static List<HashMap<String, Object>> getDataDwh() {

    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("  select * from  Stg_RISK_ANALYST_MARGIN_REVIEWED_FULL where EtlRunDateTime = (select max(EtlRunDateTime) from Stg_RISK_ANALYST_MARGIN_REVIEWED_FULL)  ");
    try {
      List<HashMap<String, Object>> result = new ArrayList<>();
      List<HashMap<String, Object>> dataFromDB = (List<HashMap<String, Object>>) Tcbsdwh.tcbsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      dataFromDB.forEach(v -> {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("TICKER", v.get("ticker"));
        hashMap.put("LOAN_TYPE", v.get("loan_type"));
        hashMap.put("PRICE_TYPE", v.get("price_type"));
        hashMap.put("LOAN_PRICE", v.get("loan_price"));
        hashMap.put("REASON", v.get("reason"));
        hashMap.put("NOTE", v.get("note"));
        hashMap.put("ANALYST", v.get("analyst"));
        hashMap.put("LOAN_RATIO", v.get("loan_ratio"));
        hashMap.put("ROOM_FINAL", v.get("room_final"));
        hashMap.put("CLOSE_PRICE_ADJUSTED", v.get("close_price_adjusted"));
        hashMap.put("ESTIMATED_LOAN", v.get("estimated_loan"));
        result.add(hashMap);
      });
      return result;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

  @Step("Get data")
  public static List<HashMap<String, Object>> getDataRisk() {

    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("  select * from  RISK_ANALYST_MARGIN_REVIEWED_FULL where LOAN_TYPE = 'LOAN'  ");
    try {
      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }
}
