package com.tcbs.automation.iris.impact;

import com.tcbs.automation.staging.AwsStagingDwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetImpactDataValidEntity {
  @Step("Get detail bonus share ")
  public static List<HashMap<String,Object>> getDate() {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append("   select *, case when (MR15 = MR01 and MR23 = MR12 and MR15 = MR12) then 'VALID' else 'INVALID' end valid  ");
    queryBuilder.append("   from (select max(TXDATE_FLEX) Mr15 from Stg_flx_MR0015) as MR15,  ");
    queryBuilder.append("  (select max(TXDATE_FLEX) Mr23 from Stg_flx_020023) as MR23,  ");
    queryBuilder.append("  (select max(TXDATE_FLEX) Mr12 from Stg_flx_020012) as MR12,  ");
    queryBuilder.append("   (select CONVERT(DATE,CONVERT(VARCHAR(20), max(EtlCurDate)),112) as  Mr01 from Stg_flx_VW_MR0001) as MR01  ");


    try {

      return  AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

}
