package com.tcbs.automation.stockmarket;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MatchedVolumeByPriceEntity {
  static final String TICKER = "ticker";

  @Step("get data from db")
  public static List<HashMap<String, Object>> getMatchedVolumeByPrice(List<String> ticker) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT * FROM ");
    queryStringBuilder.append("(SELECT distinct *,FROM_UNIXTIME(seq_time, '%Y-%m-%d') as date FROM trailing_vol_ma_1min WHERE ticker in (:ticker)) as tbl ");
    queryStringBuilder.append(
      "WHERE date = (SELECT max(date) from (SELECT*,FROM_UNIXTIME(seq_time, '%Y-%m-%d') as date FROM trailing_vol_ma_1min WHERE ticker in (:ticker)) as tblm) order by seq_time; ");
    try {
      return InvestorEntity.getHibernateEdition(ticker.get(0)).getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter(TICKER, ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data from db")
  public static List<HashMap<String, Object>> getMatchedVolumeToday(List<String> ticker, long fromDate) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT * FROM ");
    queryStringBuilder.append("(SELECT *,FROM_UNIXTIME(seq_time, '%Y-%m-%d') as date FROM trailing_vol_ma_1min WHERE ticker in (:ticker)) as tbl ");
    queryStringBuilder.append(
      "WHERE date = (SELECT max(date) from (SELECT*,FROM_UNIXTIME(seq_time, '%Y-%m-%d') as date FROM trailing_vol_ma_1min WHERE ticker in (:ticker) and seq_time >= :from ) as tblm) order by seq_time; ");
    try {
      return InvestorEntity.getHibernateEdition(ticker.get(0)).getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter(TICKER, ticker)
        .setParameter("from", fromDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data from db")
  public static List<HashMap<String, Object>> getIVolume(String ticker) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT * FROM (SELECT *,FROM_UNIXTIME(i_seq_time, '%Y-%m-%d') as date FROM intraday_by_1min WHERE t_ticker = :ticker) as tbl ");
    queryStringBuilder.append("WHERE i_seq_time IN (select DISTINCT seq_time from trailing_vol_ma_1min tvmm  ");
    queryStringBuilder.append("where FROM_UNIXTIME(seq_time, '%Y-%m-%d') = (SELECT max(date) from (SELECT seq_time,FROM_UNIXTIME(seq_time, '%Y-%m-%d') as date  ");
    queryStringBuilder.append("FROM trailing_vol_ma_1min WHERE ticker = :ticker) as tblm ) and ticker =:ticker )");
    try {
      return InvestorEntity.getHibernateEdition(ticker).getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter(TICKER, ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get ma underlying from db")
  public static List<HashMap<String, Object>> getMaUnderlying(String ticker, Long toTime) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT t_ticker, sum(i_volume) as total_vol FROM (");
    queryStringBuilder.append("SELECT *,FROM_UNIXTIME(i_seq_time,'%Y-%m-%d') ");
    queryStringBuilder.append("FROM intraday_by_1D ibd ");
    queryStringBuilder.append("WHERE t_ticker = :ticker and i_seq_time < FLOOR(:toTime/86400)*86400 ");
    queryStringBuilder.append("order by FROM_UNIXTIME(i_seq_time,'%Y-%m-%d') DESC LIMIT 10 ");
    queryStringBuilder.append(") tmp GROUP BY t_TICKER");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter(TICKER, ticker)
        .setParameter("toTime", toTime)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get ma underlying from db")
  public static List<HashMap<String, Object>> getMaDerivative(String contractName) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT future_name, sum(i_volume) as total_vol FROM( ");
    queryStringBuilder.append("SELECT *,FROM_UNIXTIME(i_seq_time,'%Y-%m-%d') , func_idata_future_name_by_ticker_and_time(t_ticker, i_seq_time) as future_name ");
    queryStringBuilder.append("FROM intraday_by_1D ibd ");
    queryStringBuilder.append("WHERE func_idata_future_name_by_ticker_and_time(t_ticker, i_seq_time) = :contractName order by FROM_UNIXTIME(i_seq_time,'%Y-%m-%d') DESC ");
    queryStringBuilder.append("LIMIT 10) tmp group by future_name;");
    try {
      return FuturesMarket.futuresMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString()) //TODO sua
        .setParameter("contractName", contractName)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
