package com.tcbs.automation.tcbsdwh.platform;

import com.tcbs.automation.Database;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Wso2DataTypeEntity {

  @Step(" Get data ")
  public static List<HashMap<String, Object>> getDataWso2() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select * from idata_test_wso2 itw ");
    try {
      return Database.TCBS_DWH.getConnection().getSession()
        .createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
