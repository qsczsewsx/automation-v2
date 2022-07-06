package com.tcbs.automation.iris.bond;

import com.tcbs.automation.staging.AwsStagingDwh;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetCurveEntity {
  public static List<HashMap<String, Object>> getCurve() {
    StringBuilder query = new StringBuilder();

    query.append(
      " select round(sob*100, 2) sob, round(ftp*100, 2) ftp, round(vgb*100, 2) vgb,date from iris_zero_curve where  updated_date = (select max(updated_date) from iris_zero_curve) and  is_generate = 0 ");

    try {
      return AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
