package com.tcbs.automation.stockmarket.secondtcprice;

import com.tcbs.automation.stockmarket.Stockmarket;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class MinutelyIndicatorEntity {

  @Step("get data")
  public static HashMap<String, Object> getMinutelyIndicatorByTicker(String ticker, long endTime) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT seq_time, ticker, forecast_vol, ma_vol, net_shark_trading_vol, ma20, ma50, ma100 ");
    queryStringBuilder.append(" , net_shark_trading_percent, rsi, technical_signal, avg_signal ");
    queryStringBuilder.append(" , bid_vol, ask_vol, highest_matched_vol_price, market_score ");
    queryStringBuilder.append(" , sc_macd_signal, macd_value, close_price ");
    queryStringBuilder.append(" FROM second_tc_price_indicators_1min ");
    queryStringBuilder.append(" WHERE ticker = :ticker AND seq_time >= FLOOR(:endTime /86400)*86400 ");
    queryStringBuilder.append(" AND seq_time <= :endTime ");
    queryStringBuilder.append(" ORDER BY seq_time DESC ");
    queryStringBuilder.append(" LIMIT 1 ");

    try {
      List<HashMap<String, Object>> resultList = Stockmarket.stockMarketConnection.getSession()
        .createNativeQuery(queryStringBuilder.toString())
        .setParameter("ticker", ticker)
        .setParameter("endTime", endTime)
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

  @Step("get data")
  public static Map<String, Object> getMinutelyIndicatorFromTestByTicker(String ticker, Long timestamp, Long timeEndLO) {
    StringBuilder queryStringBuilder = new StringBuilder();
    Long trailing15M = timestamp - 15 * 60;
    if (trailing15M > timestampAtLunchStart() && trailing15M < timestampAtLunchEnd()) {
      trailing15M -= 5400;
    }
    queryStringBuilder.append(" SELECT tbl1.ticker, bidVol, askVol ");
    queryStringBuilder.append(" , COALESCE (tbl3.buyActivePercent, 0) AS buyActivePercent ");
    queryStringBuilder.append(" , COALESCE (tbl3.netSharkTradingVol, 0) AS netSharkTradingVol ");
    queryStringBuilder.append(" , COALESCE (tbl3.netSharkTradingPercent, 0) AS netSharkTradingPercent ");
    queryStringBuilder.append(" FROM (SELECT b.ticker ");
    queryStringBuilder.append(" , (b.ask_vol_1 + b.ask_vol_2 + b.ask_vol_3) AS askVol ");
    queryStringBuilder.append(" , (b.bid_vol_1 + b.bid_vol_2 + b.bid_vol_3) AS bidVol ");
    queryStringBuilder.append(" FROM bid_ask_by_1min b ");
    queryStringBuilder.append(" WHERE ticker = :ticker AND seq_time = (SELECT MAX(seq_time) FROM bid_ask_by_1min WHERE ticker = :ticker AND seq_time <= :seq_time AND seq_time >= FLOOR(:seq_time /86400) * 86400 ) ");
    queryStringBuilder.append(" ) tbl1 ");
    queryStringBuilder.append(" LEFT JOIN (SELECT ic.ticker, ");
    queryStringBuilder.append(" ROUND(100 * (SUM(ic.shark_bu + ic.wolf_bu + ic.sheep_bu) ");
    queryStringBuilder.append("  / SUM(ic.shark_bu + ic.wolf_bu + ic.sheep_bu + ic.shark_sd + ic.wolf_sd + ic.sheep_sd)), 1) as buyActivePercent, ");
    queryStringBuilder.append(" (SUM(ic.shark_bu) - SUM(ic.shark_sd)) AS netSharkTradingVol, ");
    queryStringBuilder.append(" ROUND(100 * (SUM(ic.shark_bu) - SUM(ic.shark_sd)) ");
    queryStringBuilder.append(" / SUM(ic.shark_bu + ic.wolf_bu + ic.sheep_bu + ic.shark_sd + ic.wolf_sd + ic.sheep_sd), 1) as netSharkTradingPercent ");
    queryStringBuilder.append(" FROM investor_classifier_by_1min ic ");
    queryStringBuilder.append(" WHERE ticker = :ticker AND seq_time > :trailing15M AND seq_time <= :timeEndLO AND seq_time >= FLOOR(:timeEndLO /86400) * 86400) tbl3 ON 1 = 1 ");

    try {
      List<Map<String, Object>> resultList = Stockmarket.stockMarketConnection.getSession()
        .createNativeQuery(queryStringBuilder.toString())
        .setParameter("ticker", ticker)
        .setParameter("seq_time", timestamp)
        .setParameter("trailing15M", trailing15M)
        .setParameter("timeEndLO", timeEndLO)
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

  public static long timestampAtLunchStart() {
    Calendar now = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
    now.set(Calendar.HOUR_OF_DAY, 11);
    now.set(Calendar.MINUTE, 30);
    now.set(Calendar.SECOND, 0);

    return now.getTimeInMillis() / 1000;
  }

  public static long timestampAtLunchEnd() {
    Calendar now = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
    now.set(Calendar.HOUR_OF_DAY, 13);
    now.set(Calendar.MINUTE, 00);
    now.set(Calendar.SECOND, 00);

    return now.getTimeInMillis() / 1000;
  }
}
