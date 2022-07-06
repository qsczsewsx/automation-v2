package com.tcbs.automation.stockmarket.secondtcprice;

import com.tcbs.automation.stockmarket.Stockmarket;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranslogEntity {

  @Step("get data")
  public static Map<String, Object> getTranslogAccVol(String ticker, Long timestamp) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT ticker, close_price, SUM(volume) AS matched_vol ");
    queryStringBuilder.append(" FROM translog_save_1min tsm ");
    queryStringBuilder.append(" WHERE ticker = :t_ticker AND seq_time >=  FLOOR(:toTime / 86400 ) *86400 AND seq_time <= :toTime  ");
    queryStringBuilder.append(" GROUP BY ticker, close_price ");
    queryStringBuilder.append(" ORDER BY SUM(volume) DESC ");
    queryStringBuilder.append(" LIMIT 1; ");

    try {
      List<Map<String, Object>> resultList = Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("t_ticker", ticker)
        .setParameter("toTime", timestamp)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();

      if (!resultList.isEmpty()) {
        return resultList.get(0);
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new HashMap<>();
  }

  @Step("get forecast rate")
  public static HashMap<String, Object> getForeCast15(String ticker, long lunchStart, long lunchEnd, long toTime) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT seq_time as seqTime, avg_vol_5d, intraday_acc_vol, forecast_vol, forecast_on_avg_5d    ");
    queryStringBuilder.append("FROM intraday_forecast_vol_15min  ");
    queryStringBuilder.append("WHERE ticker = :ticker ");
    queryStringBuilder.append("AND ((seq_time >= FLOOR(:toTime / 86400) *86400 AND seq_time <= :lunchStart) OR (seq_time >= :lunchEnd AND seq_time < :toTime))");
    queryStringBuilder.append(" AND updated_time = (SELECT MAX(updated_time) FROM intraday_forecast_vol_15min WHERE ticker = :ticker) ");
    queryStringBuilder.append("ORDER BY seq_time desc LIMIT 1 ");

    try {
      List<HashMap<String, Object>> map = Stockmarket.stockMarketConnection
        .getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("ticker", ticker)
        .setParameter("lunchStart", lunchStart)
        .setParameter("lunchEnd", lunchEnd)
        .setParameter("toTime", toTime)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();

      if (!map.isEmpty()) {
        return map.get(0);
      }

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new HashMap<>();
  }

  @Step("get ma vol")
  public static HashMap<String, Object> getMaVol(String ticker, long toTime) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT * FROM trailing_vol_ma_1min tbl ");
    queryStringBuilder.append("WHERE ticker = :ticker and seq_time >= FLOOR(:endTime / 86400 ) *86400 and seq_time <= :endTime ");
    queryStringBuilder.append("order by seq_time desc limit 1; ");

    try {
      List<HashMap<String, Object>> map = Stockmarket.stockMarketConnection
        .getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("ticker", ticker)
        .setParameter("endTime", toTime)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();

      if (!map.isEmpty()) {
        return map.get(0);
      }

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new HashMap<>();
  }
}
