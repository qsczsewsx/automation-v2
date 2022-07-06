package com.tcbs.automation.iris.impactdaily;

import com.tcbs.automation.iris.AwsIRis;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetInputCalculateEntity {
  @Step("Get ticker impactype")
  public static List<HashMap<String, Object>> getTickerImpactType(String impactType, String fromDate, String toDate) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select * from RISK_RTT_DATA_CALCULATED t ");
    queryBuilder.append(" where IMPACT_TYPE = :impactType ");
    queryBuilder.append(" and UPDATED_DATE = (select max(UPDATED_DATE) from RISK_RTT_DATA_CALCULATED b where t.IMPACT_TYPE = b.IMPACT_TYPE ");
    queryBuilder.append(" and to_char(UPDATED_DATE,'yyyy-mm-dd')>= :fromDate and to_char(UPDATED_DATE,'yyyy-mm-dd') < :toDate ) ");
    try {

      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("impactType", impactType)
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }
}