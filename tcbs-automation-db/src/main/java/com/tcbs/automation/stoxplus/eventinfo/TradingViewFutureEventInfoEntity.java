package com.tcbs.automation.stoxplus.eventinfo;

import com.tcbs.automation.tcbsdwh.Tcbsdwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TradingViewFutureEventInfoEntity {

  @Step("get data")
  public static List<HashMap<String, Object>> getFutureEventInfo(String ticker, String from, String to) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT DISTINCT ticker,maturity_date from staging.stg_stoxmarket_future_by_1d where ticker like '")
      .append(ticker).append("%' and maturity_date BETWEEN '")
      .append(from).append("' AND '").append(to).append("' ");
    try {
      return Tcbsdwh.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}
