package com.tcbs.automation.iris.margin;

import com.tcbs.automation.staging.AwsStagingDwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MonitorGetMarginDataEntity {

  @Step("Get list ticker")
  public static List<HashMap<String, Object>> getData(String fromDate, String toDate) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select * from iris_margin_check_valid where checkDate >= :fromDate and checkDate <= :toDate ");
    try {
      return AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}
