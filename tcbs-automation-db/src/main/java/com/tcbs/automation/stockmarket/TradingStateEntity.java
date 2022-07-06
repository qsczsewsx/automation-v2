package com.tcbs.automation.stockmarket;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TradingStateEntity {
  private static final Logger logger = LoggerFactory.getLogger(TradingStateEntity.class);
  private static final String TICKER = "ticker";
  private static final String FROM_TIME = "fromTime";
  private static final String TO_TIME = "toTime";

  @Step("select data")
  public static List<Map<String, Object>> getIntraPriceInfo(String ticker, String fromTime, String toTime) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT i_seq_time as seq_time, f_close_price, f_open_price_change , i_acc_volume AS volume,  ");
    queryStringBuilder.append("   f_open_price_change / (f_open_price - f_open_price_change) AS price_change_percent  ");
    queryStringBuilder.append("  ,i_volume, f_close_price - (f_open_price - f_open_price_change) as absolute_price_change ");
    queryStringBuilder.append("  , f_close_price / (f_open_price - f_open_price_change) - 1  AS relative_price_change");
    queryStringBuilder.append(" FROM intraday_by_15min intra  ");
    queryStringBuilder.append(" WHERE t_ticker = :ticker  ");
    queryStringBuilder.append(" AND i_seq_time >= :fromTime  ");
    queryStringBuilder.append(" AND i_seq_time < :toTime ;");
    logger.info("query {}", queryStringBuilder.toString());
    logger.info("{} {} {}", ticker, fromTime, toTime);
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter(TICKER, ticker)
        .setParameter(FROM_TIME, fromTime)
        .setParameter(TO_TIME, toTime)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList();
  }

  @Step("select data")
  public static List<Map<String, Object>> getBidAskInfo(String ticker, String fromTime, String toTime) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT seq_time,  ");
    queryStringBuilder.append(" (over_bought) / (over_bought + over_sold) as remain_bid ,  ");
    queryStringBuilder.append(" (over_sold) / (over_bought + over_sold) as remain_ask  ");
    queryStringBuilder.append(" ,IF( over_bought != 0 && over_sold = 0, 1, (over_bought / over_sold)) as bid_on_ask ");
    queryStringBuilder.append(" FROM bid_ask_by_15min  ");
    queryStringBuilder.append(" WHERE ticker = :ticker  ");
    queryStringBuilder.append(" AND seq_time >= :fromTime  ");
    queryStringBuilder.append(" AND seq_time <= :toTime  ");

    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter(TICKER, ticker)
        .setParameter(FROM_TIME, fromTime)
        .setParameter(TO_TIME, toTime)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList();
  }

  @Step("select data")
  public static List<Map<String, Object>> getActiveBuySellInfo(String ticker, String fromTime, String toTime) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT seq_time,  ");
    queryStringBuilder.append(" ticker, sheep_bu, sheep_sd, wolf_bu, wolf_sd, shark_bu, shark_sd  ");
    queryStringBuilder.append(" , (sheep_bu + wolf_bu + shark_bu) as total_bu, ");
    queryStringBuilder.append(" (sheep_sd + wolf_sd + shark_sd) as total_sd ");
    queryStringBuilder.append(" , (sheep_bu_acc + sheep_sd_acc) as sheep_acc_vol ");
    queryStringBuilder.append(" , (wolf_bu_acc + wolf_sd_acc) as wolf_acc_vol ");
    queryStringBuilder.append(" , (shark_bu_acc + shark_sd_acc) as shark_acc_vol ");
    queryStringBuilder.append(" FROM investor_classifier_by_1min  ");
    queryStringBuilder.append(" WHERE ticker = :ticker  ");
    queryStringBuilder.append(" AND seq_time >= :fromTime  ");
    queryStringBuilder.append(" AND seq_time < :toTime  ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter(TICKER, ticker)
        .setParameter(FROM_TIME, fromTime)
        .setParameter(TO_TIME, toTime)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList();
  }

  @Step("select data")
  public static List<Map<String, Object>> getTradingState(String ticker, String fromTime, String toTime) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT * ");
    queryStringBuilder.append(" FROM intraday_trading_state_15min itsm ");
    queryStringBuilder.append(" WHERE ticker = :t_ticker ");
    queryStringBuilder.append(" AND seq_time >= :from_time ");
    queryStringBuilder.append(" AND seq_time <= :to_time ;");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("t_ticker", ticker)
        .setParameter("from_time", fromTime)
        .setParameter("to_time", toTime)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList();
  }
}