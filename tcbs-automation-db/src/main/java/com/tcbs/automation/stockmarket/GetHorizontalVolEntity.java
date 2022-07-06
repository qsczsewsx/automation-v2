package com.tcbs.automation.stockmarket;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@NoArgsConstructor
@Builder

public class GetHorizontalVolEntity {

  @Step("get horizontal volume intraday")
  public static List<HashMap<String, Object>> getHorizontalVolIntraday(String ticker, long fromDate, long toDate) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT ticker, close_price, price_change, seq_time, SUM(volume) as total_volume from translog_save_1min ");
    queryStringBuilder.append("WHERE ticker = :ticker AND seq_time >= :p_from_time AND seq_time < :p_to_time ");
    queryStringBuilder.append("GROUP BY close_price; ");

    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("ticker", ticker)
        .setParameter("p_from_time", fromDate)
        .setParameter("p_to_time", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get horizontal volume by date")
  public static List<HashMap<String, Object>> getHorizontalVolByDate(String ticker, long fromDate, long toDate) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT ticker, close_price, price_change, seq_time, SUM(volume) AS total_volume FROM( ");
    queryStringBuilder.append("SELECT ticker, price_change, seq_time, volume, ");
    queryStringBuilder.append(" ( case when adjusted_price IS NULL then close_price else adjusted_price end ) as close_price ");
    queryStringBuilder.append("FROM translog_save_by_1D ");
    queryStringBuilder.append("WHERE ticker = :ticker AND seq_time >= :p_from_time AND seq_time < :p_to_time)t1 ");
    queryStringBuilder.append("GROUP BY t1.close_price; ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("ticker", ticker)
        .setParameter("p_from_time", fromDate)
        .setParameter("p_to_time", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}
