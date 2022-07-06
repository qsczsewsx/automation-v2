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
public class GetStockVolumeEntity {
  private static final Logger logger = LoggerFactory.getLogger(GetStockVolumeEntity.class);
  private static final String TICKER_STR = "ticker";

  @Step("get volume foreign")
  public static List<HashMap<String, Object>> getForeignTrade(String ticker, Long fromDate, Long currentTimeCheck) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT seq_time as t,ticker,net_trade as fv,acc_trade as fav ");
    queryStringBuilder.append("FROM foreigntrade_by_1min    ");
    queryStringBuilder.append("WHERE ticker = :ticker  ");
    queryStringBuilder.append("AND seq_time >= :p_start_trading AND seq_time <= :p_to_time  ");
    queryStringBuilder.append("ORDER BY seq_time ASC ;  ");
    logger.info("query {}", queryStringBuilder.toString());
    try {
      return InvestorEntity.getHibernateEdition(ticker).getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("p_start_trading", fromDate)
        .setParameter("p_to_time", currentTimeCheck)
        .setParameter(TICKER_STR, ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get volume rate")
  public static List<HashMap<String, Object>> getVolumeRate(String ticker, Long fromDate, Long currentTimeCheckDate) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT FROM_UNIXTIME(t1.i_seq_time), t1.i_seq_time as t, v , vma FROM (  ");
    queryStringBuilder.append("SELECT seq_time as i_seq_time , IFNULL(vol, 0) as v , IFNULL(ratio, 0) as vma  ");
//    queryStringBuilder.append("FROM ( SELECT * FROM (select time_series, time_desc from timeseries_1min_temp  UNION select 21600, '15:00') tsr ");
//    queryStringBuilder.append("WHERE (time_series + :p_start_trading)  < :p_to_time) tsr  ");
    queryStringBuilder.append("FROM ( select * from ( ");
    queryStringBuilder.append("	SELECT DISTINCT(seq_time),vol, ratio FROM trailing_vol_ma_1min   ");
    queryStringBuilder.append("	WHERE ticker = :ticker and seq_time >= :p_start_trading and seq_time < :p_to_time ");
    queryStringBuilder.append(" order by ratio asc)t2 group by seq_time ");
    queryStringBuilder.append(") t1    ");
//    queryStringBuilder.append("ON (tsr.time_series + :p_start_trading = t1.seq_time)   ");
    queryStringBuilder.append("ORDER BY i_seq_time DESC  ");
    queryStringBuilder.append(") t1 order by t asc;    ");

    try {
      return InvestorEntity.getHibernateEdition(ticker).getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter(TICKER_STR, ticker)
        .setParameter("p_start_trading", fromDate)
        .setParameter("p_to_time", currentTimeCheckDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();


    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get acc and forecast rate")
  public static List<HashMap<String, Object>> getAccVolume(String ticker, Long fromTime, Long lunchStart, Long lunchEnd, Long endTime) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT seq_time as seqTime, avg_vol_5d, intraday_acc_vol, forecast_vol, forecast_on_avg_5d    ");
    queryStringBuilder.append("FROM intraday_forecast_vol_15min  ");
    queryStringBuilder.append("WHERE ticker = :ticker ");
    queryStringBuilder.append("AND ((seq_time >= :fromTime AND seq_time <= :lunchStart) OR (seq_time >= :lunchEnd AND seq_time < :endTime))");
    queryStringBuilder.append(" AND updated_time = (SELECT MAX(updated_time) FROM intraday_forecast_vol_15min WHERE ticker = :ticker) ");
    queryStringBuilder.append("ORDER BY seq_time  ");

    try {
      return InvestorEntity.getHibernateEdition(ticker).getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter(TICKER_STR, ticker)
        .setParameter("fromTime", fromTime)
        .setParameter("lunchStart", lunchStart)
        .setParameter("lunchEnd", lunchEnd)
        .setParameter("endTime", endTime)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();


    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
