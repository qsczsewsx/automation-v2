package com.tcbs.automation.dwh.businessfinance;

import com.tcbs.automation.dwh.Dwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetDailyFeeExchangeEntity {

  @Step("get data")
  public static List<HashMap<String, Object>> getDailyFeeExchange(String from, String to) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" EXECUTE dbo.getdailyfeebyexchange'").append(from).append("','").append(to).append("',").append(0).append(",").append(10000).append("; ");
    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}
