package com.tcbs.automation.stockmarket.cw;

import com.tcbs.automation.stockmarket.Stockmarket;
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

public class GetCwPriceEntity {

  public static final String SELECT_LASTEST_CW_PRICE = "(SELECT MAX(i_seq_time) FROM intraday_by_1min WHERE t_ticker = :cw_symbol AND i_seq_time <=  :p_from_time) ";
  public static final String AND_SEQ_LT_TOTIME = "		AND i_seq_time <= :p_to_time ";
  public static final String SELECT_MIN_CW_TRADING = " (SELECT MIN(i_seq_time) FROM intraday_by_1D WHERE t_ticker = :p_cw_symbol) ";
  public static final String SELECT_MAX_CW_TRADING = " UNIX_TIMESTAMP(curdate()) ";
  public static final String AND_I_SEQ_TIME = "		AND i_seq_time >= ";
  public static final String AND_I_SEQ_TIME1 = " 		AND i_seq_time >= ";
  public static final String AND_I_SEQ_TIME2 = " 		AND i_seq_time <= ";

  @Step("get cw price intraday")
  public static List<HashMap<String, Object>> getPriceIntraday(String ticker, String cwSymbol, long fromDate, long toDate) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT :ticker as ticker, :cw_symbol as cw_symbol, i_seq_time, base_price, cw_price FROM ( ");
    queryStringBuilder.append("	SELECT t_ticker, t2.cw_symbol, CASE WHEN t1.i_seq_time IS NULL THEN t2.i_seq_time ELSE t1.i_seq_time END as i_seq_time, base_price, t2.cw_price FROM ( ");
    queryStringBuilder.append("		SELECT t_ticker, i_seq_time, f_close_price as base_price FROM intraday_by_1min ");
    queryStringBuilder.append("		WHERE t_ticker  =  :ticker ");
    queryStringBuilder.append(AND_I_SEQ_TIME + SELECT_LASTEST_CW_PRICE);
    queryStringBuilder.append(AND_SEQ_LT_TOTIME);
    queryStringBuilder.append("	)t1 ");
    queryStringBuilder.append("	LEFT JOIN ( ");
    queryStringBuilder.append("		SELECT t_ticker as cw_symbol, i_seq_time, f_close_price as cw_price  FROM intraday_by_1min ");
    queryStringBuilder.append("		WHERE t_ticker  = :cw_symbol ");
    queryStringBuilder.append(AND_I_SEQ_TIME + SELECT_LASTEST_CW_PRICE);
    queryStringBuilder.append(AND_SEQ_LT_TOTIME);
    queryStringBuilder.append("	)t2 ");
    queryStringBuilder.append("	ON t1.i_seq_time = t2.i_seq_time	");
    queryStringBuilder.append("	UNION DISTINCT ");
    queryStringBuilder.append("	SELECT t_ticker, t4.cw_symbol, CASE WHEN t3.i_seq_time IS NULL THEN t4.i_seq_time ELSE t3.i_seq_time END as i_seq_time, base_price, t4.cw_price FROM ( ");
    queryStringBuilder.append("		SELECT t_ticker, i_seq_time, f_close_price as base_price FROM intraday_by_1min ");
    queryStringBuilder.append("		WHERE t_ticker  =  :ticker ");
    queryStringBuilder.append(AND_I_SEQ_TIME + SELECT_LASTEST_CW_PRICE);
    queryStringBuilder.append(AND_SEQ_LT_TOTIME);
    queryStringBuilder.append("	)t3 ");
    queryStringBuilder.append("	RIGHT JOIN ( ");
    queryStringBuilder.append("		SELECT t_ticker as cw_symbol, i_seq_time, f_close_price as cw_price FROM intraday_by_1min ");
    queryStringBuilder.append("		WHERE t_ticker  = :cw_symbol ");
    queryStringBuilder.append(AND_I_SEQ_TIME + SELECT_LASTEST_CW_PRICE);
    queryStringBuilder.append(AND_SEQ_LT_TOTIME);
    queryStringBuilder.append("	)t4 ");
    queryStringBuilder.append("	ON t3.i_seq_time = t4.i_seq_time ");
    queryStringBuilder.append(" )t5 ; ");


    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("ticker", ticker)
        .setParameter("cw_symbol", cwSymbol)
        .setParameter("p_from_time", fromDate)
        .setParameter("p_to_time", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get cw price by date")
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

  @Step("get cw price date")
  public static List<HashMap<String, Object>> getPriceByDate(String ticker, String cwSymbol, long toDate) {
    StringBuilder queryStringBuilder = new StringBuilder();

    queryStringBuilder.append(" SELECT :p_ticker as ticker, :p_cw_symbol as cw_symbol, i_seq_time, FROM_UNIXTIME(dday), base_price, cw_price FROM ( ");
    queryStringBuilder.append(" 	SELECT t_ticker, t2.cw_symbol, t1.i_seq_time, base_price, t2.cw_price,t1.dday FROM ( ");
    queryStringBuilder.append(" SELECT * FROM (");
    queryStringBuilder.append(" 		SELECT t_ticker, i_seq_time, f_close_price as base_price,FLOOR(i_seq_time/86400)*86400 as dday FROM intraday_by_60min ");
    queryStringBuilder.append(" 		WHERE t_ticker  = :p_ticker ");
    queryStringBuilder.append(AND_I_SEQ_TIME1 + SELECT_MIN_CW_TRADING);
    queryStringBuilder.append(AND_I_SEQ_TIME2 + SELECT_MAX_CW_TRADING);
    queryStringBuilder.append(" ORDER BY i_seq_time desc  ");
    queryStringBuilder.append(" )tmp GROUP BY tmp.dday");
    queryStringBuilder.append(" 	)t1 ");
    queryStringBuilder.append(" 	LEFT JOIN ( ");
    queryStringBuilder.append(" SELECT * FROM (");
    queryStringBuilder.append(" 		SELECT t_ticker as cw_symbol, i_seq_time, f_close_price as cw_price,FLOOR(i_seq_time/86400)*86400 as dday  FROM intraday_by_1D ");
    queryStringBuilder.append(" 		WHERE t_ticker  = :p_cw_symbol ");
    queryStringBuilder.append(AND_I_SEQ_TIME1 + SELECT_MIN_CW_TRADING);
    queryStringBuilder.append(AND_I_SEQ_TIME2 + SELECT_MAX_CW_TRADING);
    queryStringBuilder.append(" ORDER BY i_seq_time desc  ");
    queryStringBuilder.append(" 	)tmp GROUP BY tmp.dday");
    queryStringBuilder.append(" 	)t2 ");
    queryStringBuilder.append(" 	ON t1.dday = t2.dday	 ");
    queryStringBuilder.append(")t5");
    queryStringBuilder.append(" 	UNION   ");
    queryStringBuilder.append(" SELECT * FROM ( ");
    queryStringBuilder.append(" SELECT t_ticker as ticker, NULL as cw_symbol, i_seq_time, FROM_UNIXTIME(i_seq_time), f_close_price as base_price, NULL as cw_price FROM intraday_by_1min");
    queryStringBuilder.append(" WHERE t_ticker = :p_ticker");
    queryStringBuilder.append(" ORDER BY i_seq_time DESC");
    queryStringBuilder.append(" LIMIT 1");
    queryStringBuilder.append(")t6;");



    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("p_ticker", ticker)
        .setParameter("p_cw_symbol", cwSymbol)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}
