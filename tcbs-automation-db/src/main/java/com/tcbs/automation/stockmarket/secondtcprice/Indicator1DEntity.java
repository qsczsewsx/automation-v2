package com.tcbs.automation.stockmarket.secondtcprice;

import com.tcbs.automation.stockmarket.Stockmarket;
import java.util.List;
import java.util.Map;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

public class Indicator1DEntity {
  private Indicator1DEntity() {};
  public static Map<String, Object> getMAIndicatorByTicker(String ticker) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT Ticker, ROUND(ema20, 1) AS ma20 ");
    queryStringBuilder.append(" ,ROUND(ema50, 1) as ma50, ROUND(ema100, 1) as ma100 ");
    queryStringBuilder.append(" FROM indicators_by_1D ");
    queryStringBuilder.append(" WHERE ticker = :ticker ");
    queryStringBuilder.append(" ORDER BY seq_time DESC ");
    queryStringBuilder.append(" LIMIT 1 ");

    try {
      List<Map<String, Object>> resultList = Stockmarket.stockMarketConnection.getSession()
        .createNativeQuery(queryStringBuilder.toString())
        .setParameter("ticker", ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();

      if (resultList.size() > 0) {
        return resultList.get(0);
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return null;
  }
}
