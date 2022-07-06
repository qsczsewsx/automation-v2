package com.tcbs.automation.iris.bond;

import com.tcbs.automation.staging.AwsStagingDwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author minhnv8
 * @date 06/06/2022 16:39
 */
public class GetSyncCorpBondOnGoingEntity {
  @Step("Get data from iRis")
  public static List<HashMap<String, Object>> getResultCorpBondOnGoingFromDB() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select * from Stg_risk_InvestmentLimit_CorpBond_OnGoing  ");
    queryBuilder.append("where updateDate = (select max(updateDate) from Stg_risk_InvestmentLimit_CorpBond_OnGoing)");
    try {
      return AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString()).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
