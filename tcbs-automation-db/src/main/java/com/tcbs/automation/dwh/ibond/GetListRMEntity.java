package com.tcbs.automation.dwh.ibond;

import com.tcbs.automation.dwh.Dwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetListRMEntity {

  @Step("Get list rbo from db")
  public static List<HashMap<String, Object>> getListRM() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select * from smy_dwhtcb_rm_qualified sdrq order by branch");
    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}
