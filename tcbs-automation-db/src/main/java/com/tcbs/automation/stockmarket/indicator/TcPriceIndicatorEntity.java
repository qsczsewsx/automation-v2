package com.tcbs.automation.stockmarket.indicator;

import com.tcbs.automation.stockmarket.Stockmarket;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TcPriceIndicatorEntity {

  @Step("get data")
  public static List<HashMap<String, Object>> getIndicatorDaily (String ticker){
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT * FROM indicators_by_1min WHERE ticker = :p_ticker order by seq_time desc, updated desc limit 1 ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("p_ticker", ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}
