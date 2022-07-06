package com.tcbs.automation.stockmarket.flowmap;

import com.tcbs.automation.stockmarket.Stockmarket;
import com.tcbs.automation.stockmarket.intradaymarket.MarketLeaderEntity;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MarketValuePercentTradingEntity {
  private static final String INDUSTRY = "industry";
  private static final String EXCHANGE = "exchange";
  private static final String CURRENT_DATE = "currentDate";
  private static final String EXCHANGE_ID = "exchangeId";

  @Step("get data")
  public static List<HashMap<String, Object>> getMarketValuePercentTrading(String exchange, String industry, String currentDate) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT * FROM market_flow_value_percent_trading_15min ")
      .append(" WHERE DATE_FORMAT(FROM_UNIXTIME(seq_time), '%Y-%m-%d') = :currentDate ")
      .append(" AND exchange = :exchange AND industryCode = :industry ");
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
  public static List<HashMap<String, Object>> getMarketValuePercentTrading1M(String exchange, String industry, String currentDate, String fromDate) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT * FROM market_flow_value_percent_trading_1D ")
      .append(" WHERE DATE_FORMAT(FROM_UNIXTIME(seq_time), '%Y-%m-%d') <= :currentDate AND DATE_FORMAT(FROM_UNIXTIME(seq_time), '%Y-%m-%d') >=  :fromDate ")
      .append(" AND exchange = :exchange AND industryCode = :industry ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .setParameter(INDUSTRY, industry)
        .setParameter(CURRENT_DATE, currentDate)
        .setParameter(EXCHANGE, exchange)
        .setParameter("fromDate", fromDate)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get market cap")
  public static List<HashMap<String, Object>> getMarketCap(String industry, Integer exchangeId) {
    StringBuilder queryBuilder = new StringBuilder();
    String listTicker;
    if (industry.equals("ALL")) {
      listTicker = " LENGTH (ticker) = 3 AND exchange_id = :exchangeId ";
    } else {
      listTicker = " icbCodeL2 = :industry AND LENGTH (ticker) = 3 AND exchange_id in (0,1,3) ";
    }
    queryBuilder.append(" SELECT sum(market_cap) as market_cap FROM market_price_marketcap WHERE ")
      .append(listTicker).append(" AND date_report = ( Select max(date_report) from market_price_marketcap ) ");
    try {
      if (industry.equals("ALL")) {
        return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
          .setParameter(EXCHANGE_ID, exchangeId)
          .getResultList();
      } else {
        return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
          .setParameter(INDUSTRY, industry)
          .getResultList();
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get total market cap")
  public static List<HashMap<String, Object>> getTotalMarketCap() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT sum(market_cap) as market_cap FROM market_price_marketcap WHERE ")
      .append(" ticker in (SELECT ticker FROM tca_vw_idata_index_industry_exchange_v2 WHERE lenTicker = 3 AND exchangeId in (0,1,3)) ")
      .append(" AND date_report = ( Select max(date_report) from market_price_marketcap ) ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get percent trading by industry")
  public static List<HashMap<String, Object>> getDataByIndustry(String industry, String currentDate) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select accValue, totalAccValue, a.seq_time from ( ")
      .append(" 	select i_seq_time seq_time , sum(value) accValue from intraday_by_1min t1  ")
      .append(" 	left join tca_vw_idata_index_industry_exchange_v2 t2 on t1.t_ticker = t2.ticker ")
      .append("	where DATE_FORMAT(FROM_UNIXTIME(i_seq_time), '%Y-%m-%d') = :currentDate")
      .append(" 	and t2.icbCodeL2  = :industry and exchangeId in (0,1,3) 	group by i_seq_time ")
      .append(" )a inner join ( ")
      .append(" 	select i_seq_time  seq_time, sum(value) totalAccValue from intraday_by_1min t2 ")
      .append(" 	left join tca_vw_idata_index_industry_exchange_v2 t3 on t2.t_ticker = t3.ticker  ")
      .append(" where exchangeId in (0,1,3) and DATE_FORMAT(FROM_UNIXTIME(i_seq_time), '%Y-%m-%d') = :currentDate ")
      .append(" 	group by i_seq_time) bbb on a.seq_time = bbb.seq_time order by a.seq_time asc ;");
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

  @Step("get percent trading by industry")
  public static List<HashMap<String, Object>> getDataByExchange(String exchange, String currentDate) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select accValue, totalAccValue, a.seq_time from ( ")
      .append(" 	select i_seq_time seq_time , sum(value) accValue")
      .append(" 	from intraday_by_1min t1 ")
      .append(" 	left join tca_vw_idata_index_industry_exchange_v2 t2 on t1.t_ticker = t2.ticker ")
      .append(" 	where DATE_FORMAT(FROM_UNIXTIME(i_seq_time), '%Y-%m-%d') = :currentDate")
      .append(" 	and t2.exchangeId  = :exchangeId ")
      .append(" 	group by i_seq_time ")
      .append(" )a inner join ( ")
      .append(" 	select i_seq_time  seq_time, sum(value) totalAccValue")
      .append(" 	from intraday_by_1min t2 left join tca_vw_idata_index_industry_exchange_v2 t3 on t2.t_ticker = t3.ticker ")
      .append(" 	where DATE_FORMAT(FROM_UNIXTIME(i_seq_time), '%Y-%m-%d') = :currentDate and exchangeId in(0,1,3)")
      .append(" 	group by i_seq_time ")
      .append(" ) bbb on a.seq_time = bbb.seq_time order by a.seq_time asc ;");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter(EXCHANGE_ID, MarketLeaderEntity.getExchange(exchange))
        .setParameter(CURRENT_DATE, currentDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static List<HashMap<String, Object>> getData1MByIndustry(String industry, String currentDate) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select accValue as acc_value, accValue / totalAccValue as acc_value_percent, aaa.seq_time, FROM_UNIXTIME(aaa.seq_time)  from ( ")
      .append(" 	select i_seq_time seq_time , sum(value) accValue ")
      .append(" 	from intraday_by_1min t3  ")
      .append(" 	left join tca_vw_idata_index_industry_exchange_v2 t2 on t3.t_ticker = t2.ticker  ")
      .append(" 	where DATE_FORMAT(FROM_UNIXTIME(i_seq_time), '%Y-%m-%d') = :currentDate  ")
      .append(" 	and t2.icbCodeL2 =:industry and exchangeId in(0,1,3) ")
      .append(" )aaa inner join ( ")
      .append(" 	select i_seq_time  seq_time, sum(value) totalAccValue ")
      .append(" 	from intraday_by_1min t1 left join tca_vw_idata_index_industry_exchange_v2 t2 on t1.t_ticker = t2.ticker  ")
      .append(" 	where exchangeId in(0,1,3) and DATE_FORMAT(FROM_UNIXTIME(i_seq_time), '%Y-%m-%d') = :currentDate ")
      .append(" ) bbb on aaa.seq_time = bbb.seq_time order by aaa.seq_time asc ; ");
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
  public static List<HashMap<String, Object>> getData1MByExchange(String exchange, String currentDate) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select accValue as acc_value, accValue / totalAccValue as acc_value_percent, aaa.seq_time, FROM_UNIXTIME(aaa.seq_time)  from ( ")
      .append(" 	select i_seq_time seq_time , sum(value) accValue ")
      .append(" 	from intraday_by_1min t1  ")
      .append(" 	left join tca_vw_idata_index_industry_exchange_v2 t2 on t1.t_ticker = t2.ticker  ")
      .append(" 	where DATE_FORMAT(FROM_UNIXTIME(i_seq_time), '%Y-%m-%d') = :currentDate  ")
      .append(" 	and t2.exchangeId =:exchangeId ")
      .append(" )aaa inner join ( ")
      .append(" 	select i_seq_time  seq_time, sum(value) totalAccValue ")
      .append(" 	from intraday_by_1min t1 left join tca_vw_idata_index_industry_exchange_v2 t2 on t1.t_ticker = t2.ticker ")
      .append(" 	where DATE_FORMAT(FROM_UNIXTIME(i_seq_time), '%Y-%m-%d') = :currentDate and exchangeId in(0,1,3) ")
      .append(" ) bbb on aaa.seq_time = bbb.seq_time order by aaa.seq_time asc ; ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter(EXCHANGE_ID, MarketLeaderEntity.getExchange(exchange))
        .setParameter(CURRENT_DATE, currentDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
