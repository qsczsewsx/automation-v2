package com.tcbs.automation.stockmarket.intradaymarket;

import com.tcbs.automation.stockmarket.Stockmarket;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MarketForeignVolEntity {
  private static final String INDUSTRY = "industry";
  private static final String CURRENT_DATE = "currentDate";
  private static final String EXCHANGE = "exchange";

  @Step("get data")
  public static List<HashMap<String, Object>> getMarketForeignVolOneMinute(String industry, String currentDate, String exchange) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT net_vol as v, acc_net_vol as av, seq_time FROM industry_foreign_trading_1min iftbm WHERE icbCodeL2 = :industry ")
      .append(" and DATE_FORMAT(FROM_UNIXTIME(seq_time), '%Y-%m-%d') = :currentDate and exchange = :exchange ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .setParameter(INDUSTRY, industry)
        .setParameter(CURRENT_DATE, currentDate)
        .setParameter(EXCHANGE, exchange)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static List<HashMap<String, Object>> getMarketForeignVolFifteenMinute(String industry, String currentDate, String exchange) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT net_vol as v,acc_net_vol as av,seq_time FROM industry_foreign_trading_15min WHERE icbCodeL2 = :industry ")
      .append(" and DATE_FORMAT(FROM_UNIXTIME(seq_time), '%Y-%m-%d') <= :currentDate and exchange = :exchange ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .setParameter(INDUSTRY, industry)
        .setParameter(CURRENT_DATE, currentDate)
        .setParameter(EXCHANGE, exchange)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static List<HashMap<String, Object>> getMarketForeignVolAllType(String industry, String fromDate, String toDate) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT sum(acc_trade) as v, CAST(trading_date AS CHAR) as trading_date FROM foreigntrade_by_1d fbd WHERE ticker in (SELECT ticker FROM tca_vw_idata_index_industry_exchange_v2")
      .append(" WHERE icbCodeL2 = :industry and exchangeId in (0,1,3) and lenTicker = 3) ")
      .append(" and trading_date >= :fromDate and trading_date < :toDate group by trading_date ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .setParameter(INDUSTRY, industry)
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static List<HashMap<String, Object>> getMarketForeignVolByExchange(Integer pExchange, String fromDate, String toDate) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT sum(acc_trade) as v, CAST(trading_date AS CHAR) as trading_date FROM foreigntrade_by_1d fbd WHERE ticker in (SELECT ticker FROM tca_vw_idata_index_industry_exchange_v2")
      .append(" WHERE exchangeId = :pExchange and lenTicker = 3) ")
      .append(" and trading_date >= :fromDate and trading_date < :toDate group by trading_date ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .setParameter("pExchange", pExchange)
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static HashMap<String, Object> getMarketForeignVolByExchangeToday(String exchange, String currentDate) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT max(seq_time) seq_time, sum(net_vol) acc_net_vol  FROM industry_foreign_trading_1min iftbm WHERE icbCodeL2 = 'ALL' ")
      .append(" and DATE_FORMAT(FROM_UNIXTIME(seq_time), '%Y-%m-%d') = :currentDate and exchange = :exchange ")
      .append(" group by exchange, icbCodeL2 ");
    try {
      List<HashMap<String, Object>> rs = Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .setParameter(EXCHANGE, exchange)
        .setParameter(CURRENT_DATE, currentDate)
        .getResultList();
      if (!rs.isEmpty()) {
        return rs.get(0);
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new HashMap<>();
  }


  @Step("get data")
  public static HashMap<String, Object> getMarketForeignVolAllTypeToday(String industry, String currentDate) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT max(seq_time) seq_time, sum(net_vol) acc_net_vol  FROM industry_foreign_trading_1min iftbm WHERE icbCodeL2 = :industry ")
      .append(" and DATE_FORMAT(FROM_UNIXTIME(seq_time), '%Y-%m-%d') = :currentDate and exchange = 'ALL'  ")
      .append(" group by exchange, icbCodeL2 ");
    try {
      List<HashMap<String, Object>> rs = Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .setParameter(INDUSTRY, industry)
        .setParameter(CURRENT_DATE, currentDate)
        .getResultList();
      if (!rs.isEmpty()) {
        return rs.get(0);
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new HashMap<>();
  }

}
