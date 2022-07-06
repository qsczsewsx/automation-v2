package com.tcbs.automation.staging;

import com.tcbs.automation.iris.IRis;
import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class RiskCompAllowedEntity {

  @Step("get all riskComAllow")
  public static List<HashMap<String, Object>> getAllRiskComAllow() {
    StringBuilder query = new StringBuilder();
    query.append("select * from Stg_flx_020201 where LEN(SYMBOL) = 3 and SYMBOL <> 'TCB' ");
    try {
      return AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList();
  }

}
