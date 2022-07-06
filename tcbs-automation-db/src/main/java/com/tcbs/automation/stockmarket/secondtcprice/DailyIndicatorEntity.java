package com.tcbs.automation.stockmarket.secondtcprice;

import com.tcbs.automation.stockmarket.Stockmarket;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.HashMap;
import java.util.List;

public class DailyIndicatorEntity {

  @Step("get data")
  public static HashMap<String, Object> getDailyIndicatorByTicker(String ticker) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT * ");
    queryStringBuilder.append("FROM second_tc_price_indicators_1D ");
    queryStringBuilder.append("WHERE ticker = :ticker and seq_time <= UNIX_TIMESTAMP(CURRENT_DATE()) ");
    queryStringBuilder.append("ORDER BY seq_time DESC ");
    queryStringBuilder.append("LIMIT 1 ");

    try {
      List<HashMap<String, Object>> resultList = Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("ticker", ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();

      if (!resultList.isEmpty()) {
        return resultList.get(0);
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return null;
  }
}
