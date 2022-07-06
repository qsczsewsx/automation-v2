package com.tcbs.automation.iris.impact;

import com.tcbs.automation.iris.AwsIRis;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetBonusShareDetailEntity {
  @Step("Get detail bonus share ")
  public static List<HashMap<String, Object>> getBonusShare(String etlUpdateDate, String custodycd) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append("  select a.*,CAASS_AFTER*min*LOAN_RATIO_AFTER/100 + CAAMT_AFTER as TA_INCREASE from (  ");
    queryBuilder.append("    select a.CUSTODYCD, a.AFACCTNO, SYMBOL symbol,nvl(CAASS_AFTER,0) CAASS_AFTER ,nvl(CAAMT_AFTER,0) CAAMT_AFTER,LOAN_PRICE_AFTER,LOAN_RATIO_AFTER,MARKET_PRICE,  ");
    queryBuilder.append("   case when LOAN_PRICE_AFTER >= MARKET_PRICE then MARKET_PRICE else LOAN_PRICE_AFTER end min  ");
    queryBuilder.append("  from RISK_RTT_CALCULATE_RESULT a, RISK_STOCK b  ");
    queryBuilder.append("  where a.CUSTODYCD = b.CUSTODYCD and  ");
    queryBuilder.append("  a.ETL_UPDATE_DATE = b.ETL_UPDATE_DATE  ");
    queryBuilder.append("  and a.ETL_UPDATE_DATE = :etlUpdateDate and a.CUSTODYCD = :custodycd  ");
    queryBuilder.append("  and ((CAASS_AFTER <> 0 or CAASS_AFTER is not null) or (CAAMT_AFTER <> 0 or CAAMT_AFTER is not null))  ");
    queryBuilder.append("  ) a  ");

    try {

      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("etlUpdateDate", etlUpdateDate)
        .setParameter("custodycd", custodycd)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

}
