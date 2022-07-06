package com.tcbs.automation.iris.impact;

import com.tcbs.automation.iris.AwsIRis;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetRttEntity {
  @Step("Get data ")
  public static List<HashMap<String, Object>> getData(String eltUpdateDate) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(
      "  select a.*, (case when DELTA_1 <0 then TICKER_1 else null end  ||case  when (TICKER_2 is not null and DELTA_2 <0) then (','|| TICKER_2) end || case when (TICKER_3 is not null and DELTA_3<0) then (','|| TICKER_3) end)  as TOP_TICKER ");
    queryBuilder.append("  from RISK_RTT_CALCULATE_RESULT a ");
    queryBuilder.append("  where ETL_UPDATE_DATE = :etlUpdateDate ");
    try {

      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("etlUpdateDate", eltUpdateDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }
}
