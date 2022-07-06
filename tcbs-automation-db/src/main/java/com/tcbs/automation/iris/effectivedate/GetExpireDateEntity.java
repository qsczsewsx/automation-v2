package com.tcbs.automation.iris.effectivedate;

import com.tcbs.automation.iris.AwsIRis;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetExpireDateEntity {
  @Step("Get data")
  public static List<HashMap<String, Object>> getData(String parentId) {

    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("   select * from risk_save_expire_date where PARENT_ID = :parentId  ");
    queryBuilder.append("  and UPDATE_DATE = (select max(UPDATE_DATE) from risk_save_expire_date where PARENT_ID = :parentId )  ");

    try {
      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("parentId", parentId)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }
}

