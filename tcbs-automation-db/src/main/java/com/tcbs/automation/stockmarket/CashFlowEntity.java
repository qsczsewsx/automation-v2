package com.tcbs.automation.stockmarket;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
public class CashFlowEntity {
  private static final Logger logger = LoggerFactory.getLogger(CashFlowEntity.class);

  @Step("get getBidAskIntraday from db")
  public static List<HashMap<String, Object>> getBidAskIntraday(List<String> tickerList, String fromDate, String toDate, int resolution) {
    StringBuilder queryStringBuilder = new StringBuilder();

    String tableName = "";

    if (resolution == 1) {
      tableName += "bid_ask_by_1min ";
    } else if (resolution == 15) {
      tableName += "bid_ask_by_15min ";
    }
    queryStringBuilder.append("SELECT ticker, seq_time, over_bought/(over_bought + over_sold) as ba_ratio FROM " + tableName);
    queryStringBuilder.append("WHERE ticker in (:tickers) ");
    queryStringBuilder.append("AND seq_time >= :from_time ");
    queryStringBuilder.append("AND seq_time < :to_time ");
    queryStringBuilder.append("ORDER BY seq_time ASC ");

    try {
      String tickers = String.join(",", tickerList);
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("tickers", tickers)
        .setParameter("from_time", fromDate)
        .setParameter("to_time", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get getBidAskByDay from db")
  public static List<HashMap<String, Object>> getBidAskByDay(List<String> tickerList, String fromDate, String toDate) {
    StringBuilder queryStringBuilder = new StringBuilder();

    queryStringBuilder.append("SELECT ticker, FLOOR(seq_time/86400) * 86400 as seq_time ,AVG(over_bought /(over_bought + over_sold)) as ba_ratio ");
    queryStringBuilder.append("FROM bid_ask_by_15min ");
    queryStringBuilder.append("WHERE ticker in (:tickers) ");
    queryStringBuilder.append("AND seq_time >= :from_time ");
    queryStringBuilder.append("AND seq_time < :to_time ");
    queryStringBuilder.append("GROUP BY FLOOR(seq_time/86400) * 86400, ticker;");

    try {
      String tickers = String.join(",", tickerList);
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("tickers", tickers)
        .setParameter("from_time", fromDate)
        .setParameter("to_time", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get getBidAskByMonth from db")
  public static List<HashMap<String, Object>> getBidAskByMonth(List<String> tickerList, String fromDate, String toDate) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT ticker, seq_time, AVG(date_ratio) AS month_ratio, month_year FROM ( ");
    queryStringBuilder.append("SELECT ticker, seq_time, AVG(ba_ratio) AS date_ratio, dday, DATE_FORMAT(FROM_UNIXTIME(seq_time), '%m-%Y') as month_year FROM ( ");
    queryStringBuilder.append("SELECT ticker, seq_time, over_bought, over_sold, over_bought/(over_bought+over_sold) AS ba_ratio, FLOOR(seq_time/86400)*86400 AS dday ");
    queryStringBuilder.append("FROM bid_ask_by_15min ");
    queryStringBuilder.append("WHERE ticker IN (:tickers) ");
    queryStringBuilder.append("AND seq_time >= :from_time ");
    queryStringBuilder.append("AND seq_time < :to_time ");
    queryStringBuilder.append("ORDER BY seq_time  DESC )tb1 ");
    queryStringBuilder.append("GROUP BY tb1.ticker, tb1.dday)tb2 ");
    queryStringBuilder.append("GROUP BY tb2.ticker, tb2.month_year;");

    try {
      String tickers = String.join(",", tickerList);
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("tickers", tickers)
        .setParameter("from_time", fromDate)
        .setParameter("to_time", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }


  @Step("get getBuySellIntraday from db")
  public static List<HashMap<String, Object>> getBuySellIntraday(List<String> tickerList, String fromDate, String toDate, int resolution) {
    StringBuilder queryStringBuilder = new StringBuilder();

    String tableName = "";

    if (resolution == 1) {
      tableName += "buysellactive_acc_by_1min ";
    } else if (resolution == 15) {
      tableName += "buysellactive_acc_by_15min ";
    }
    queryStringBuilder.append("SELECT ticker, total_bu_vol, total_sd_vol, seq_time FROM " + tableName);
    queryStringBuilder.append("WHERE ticker in (:tickers) ");
    queryStringBuilder.append("AND seq_time >= :from_time ");
    queryStringBuilder.append("AND seq_time < :to_time ");
    queryStringBuilder.append("ORDER BY seq_time ASC ");

    try {
      String tickers = String.join(",", tickerList);
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("tickers", tickers)
        .setParameter("from_time", fromDate)
        .setParameter("to_time", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get getBuySellByDay from db")
  public static List<HashMap<String, Object>> getBuySellByDay(List<String> tickerList, String fromDate, String toDate) {
    StringBuilder queryStringBuilder = new StringBuilder();

    queryStringBuilder.append("SELECT ticker, FLOOR(seq_time/86400) * 86400 as seq_time, AVG(total_bu_vol/(total_bu_vol+total_sd_vol)) as bs_ratio ");
    queryStringBuilder.append("FROM buysellactive_acc_by_15min ");
    queryStringBuilder.append("WHERE ticker in (:tickers) ");
    queryStringBuilder.append("AND seq_time >= :from_time ");
    queryStringBuilder.append("AND seq_time < :to_time ");
    queryStringBuilder.append("GROUP BY FLOOR(seq_time/86400) * 86400, ticker;");

    try {
      String tickers = String.join(",", tickerList);
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("tickers", tickers)
        .setParameter("from_time", fromDate)
        .setParameter("to_time", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get getBuySellByMonth from db")
  public static List<HashMap<String, Object>> getBuySellByMonth(List<String> tickerList, String fromDate, String toDate) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT ticker, seq_time, SUM(sum_bu) as total_bu, SUM(sum_sd) as total_sd, AVG(sum_bu/(sum_bu + sum_sd)) as bs_ratio, month_year FROM ( ");
    queryStringBuilder.append("SELECT *, SUM(total_bu_vol) as sum_bu, SUM(total_sd_vol) as sum_sd, DATE_FORMAT(FROM_UNIXTIME(seq_time), '%m-%Y') as month_year FROM ( ");
    queryStringBuilder.append("SELECT *, FLOOR(seq_time/86400) * 86400 as dday from buysellactive_acc_by_15min ");
    queryStringBuilder.append("WHERE ticker IN (:tickers) ");
    queryStringBuilder.append("AND seq_time >= :from_time ");
    queryStringBuilder.append("AND seq_time < :to_time ");
    queryStringBuilder.append("ORDER BY seq_time DESC)tb1 ");
    queryStringBuilder.append("GROUP BY tb1.dday, ticker)tb2 ");
    queryStringBuilder.append("GROUP BY tb2.month_year, ticker;");

    try {
      String tickers = String.join(",", tickerList);
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("tickers", tickers)
        .setParameter("from_time", fromDate)
        .setParameter("to_time", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get getPriceIntraday from db")
  public static List<HashMap<String, Object>> getPriceIntraday(List<String> tickerList, String fromDate, String toDate, int resolution) {
    logger.info("getPriceIntraday tickers {}, from {}, to {}", tickerList, fromDate, toDate);
    String tableName = "";
    if (resolution == 1) {
      tableName = "intraday_by_1min ";
    } else if (resolution == 5) {
      tableName = "intraday_by_5min ";
    } else if (resolution == 10) {
      tableName = "intraday_by_10min ";
    } else if (resolution == 15) {
      tableName = "intraday_by_15min ";
    } else if (resolution == 30) {
      tableName = "intraday_by_30min ";
    } else if (resolution == 60) {
      tableName = "intraday_by_60min ";
    }


    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT t_ticker, i_seq_time as seq_time, (f_open_price - f_open_price_change) as open_price, f_close_price, i_volume, i_acc_volume FROM " + tableName);
    queryStringBuilder.append("WHERE t_ticker IN (:tickers) ");
    queryStringBuilder.append("AND i_seq_time >= :from_time ");
    queryStringBuilder.append("AND i_seq_time < :to_time ");
    queryStringBuilder.append("ORDER BY i_seq_time ASC;");

    try {
      String tickers = String.join(",", tickerList);
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("tickers", tickers)
        .setParameter("from_time", fromDate)
        .setParameter("to_time", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }


}