package com.tcbs.automation.stockmarket.intradaymarket;

import com.tcbs.automation.stockmarket.Stockmarket;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MarketBidAskEntity {
  private static final String INDUSTRY = "industry";
  private static final String CURRENT_DATE = "currentDate";
  private static final String EXCHANGE = "exchange";
  @Step("get data")
  public static List<HashMap<String, Object>> getDataBidAskByExchange(String exchange, String currentDate){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT * FROM market_bidask_1min mbm ")
      .append(" WHERE DATE_FORMAT(FROM_UNIXTIME(seq_time), '%Y-%m-%d') = :currentDate ")
      .append(" AND exchange = :exchange ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter(EXCHANGE, exchange)
        .setParameter(CURRENT_DATE, currentDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static List<HashMap<String, Object>> getDataBidAskByIndustry(String industry, String currentDate){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT * FROM market_bidask_1min mbm ")
      .append(" WHERE DATE_FORMAT(FROM_UNIXTIME(seq_time), '%Y-%m-%d') = :currentDate ")
      .append(" AND icbCodeL2 = :industry  ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter(INDUSTRY, industry)
        .setParameter(CURRENT_DATE, currentDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static List<HashMap<String, Object>> getDataBidAskOneMinuteByExchange(String exchange, String currentDate){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT * FROM market_bidask_1min WHERE DATE_FORMAT(FROM_UNIXTIME(seq_time), '%Y-%m-%d') = :currentDate and exchange = :exchange   ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter(CURRENT_DATE, currentDate)
        .setParameter(EXCHANGE, exchange)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static List<HashMap<String, Object>> getDataBidAskOneMinuteByIndustry(String industry, String currentDate){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT * FROM market_bidask_1min WHERE DATE_FORMAT(FROM_UNIXTIME(seq_time), '%Y-%m-%d') = :currentDate and icbCodeL2 = :industry ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter(CURRENT_DATE, currentDate)
        .setParameter(INDUSTRY, industry)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static List<HashMap<String, Object>> getBidAskValueFromRawByExchange(Integer exchangeId, Long seqTime){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT SUM(bid_val_1) AS bid_val_1, SUM(bid_val_2) AS bid_val_2, SUM(bid_val_3) AS bid_val_3 ")
      .append(" , SUM(ask_val_1) AS ask_val_1, SUM(ask_val_2) AS ask_val_2, SUM(ask_val_3) AS ask_val_3 ")
      .append(" , seq_time FROM ( ")
      .append(" SELECT ticker, seq_time, bid_vol_1 * bid_price_1 AS bid_val_1, bid_vol_2 * bid_price_2 AS bid_val_2 ")
      .append(" , bid_vol_3 * bid_price_3 AS bid_val_3, ask_vol_1 * ask_price_1 AS ask_val_1, ask_vol_2 * ask_price_2 AS ask_val_2 ")
      .append(" , ask_vol_3 * ask_price_3 AS ask_val_3 ")
      .append(" FROM bid_ask_by_1min WHERE seq_time >= :seqTime ")
      .append(" AND ticker in (SELECT ticker FROM tca_vw_idata_index_industry_exchange_v2 WHERE exchangeId = :exchangeId and lenTicker = 3)) aaa ")
      .append(" GROUP BY seq_time   ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("exchangeId", exchangeId)
        .setParameter("seqTime", seqTime)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static List<HashMap<String, Object>> getBidAskValueFromRawByIndustry(String industry, Long seqTime){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT SUM(bid_val_1) AS bid_val_1, SUM(bid_val_2) AS bid_val_2, SUM(bid_val_3) AS bid_val_3 ")
      .append(" , SUM(ask_val_1) AS ask_val_1, SUM(ask_val_2) AS ask_val_2, SUM(ask_val_3) AS ask_val_3 ")
      .append(" , seq_time FROM ( ")
      .append(" SELECT ticker, seq_time, bid_vol_1 * bid_price_1 AS bid_val_1, bid_vol_2 * bid_price_2 AS bid_val_2 ")
      .append(" , bid_vol_3 * bid_price_3 AS bid_val_3, ask_vol_1 * ask_price_1 AS ask_val_1, ask_vol_2 * ask_price_2 AS ask_val_2 ")
      .append(" , ask_vol_3 * ask_price_3 AS ask_val_3 ")
      .append(" FROM bid_ask_by_1min WHERE seq_time >= :seqTime ")
      .append(" AND ticker in (SELECT ticker FROM tca_vw_idata_index_industry_exchange_v2 WHERE icbCodeL2 = :industry and lenTicker = 3)) aaa ")
      .append(" GROUP BY seq_time   ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter(INDUSTRY, industry)
        .setParameter("seqTime", seqTime)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static List<HashMap<String, Object>> getDataAvgBr5ByExchange(String exchange, Long fromDate, Long toDate){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT SUM(bid_val_1 + bid_val_2 + bid_val_3) / SUM(bid_val_1 + bid_val_2 + bid_val_3 + ask_val_1 + ask_val_2 + ask_val_3) AS bid_ratio_avg, seq_time % 86400 as time ")
      .append(" FROM market_bidask_1min WHERE seq_time > :fromDate AND seq_time < :toDate ")
      .append(" and exchange = :exchange GROUP BY seq_time % 86400 ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setParameter(EXCHANGE, exchange)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static List<HashMap<String, Object>> getDataAvgBr5ByIndustry(String industry, Long fromDate, Long toDate){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT SUM(bid_val_1 + bid_val_2 + bid_val_3) / SUM(bid_val_1 + bid_val_2 + bid_val_3 + ask_val_1 + ask_val_2 + ask_val_3) AS bid_ratio_avg, seq_time % 86400 as time ")
      .append(" FROM market_bidask_1min WHERE seq_time < :fromDate AND seq_time > :toDate ")
      .append(" and icbCodeL2 = :industry GROUP BY seq_time % 86400 ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setParameter(INDUSTRY, industry)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static List<HashMap<String, Object>> getLatestDate(){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT max(seq_time) latest FROM market_bidask_1min mbm ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}
