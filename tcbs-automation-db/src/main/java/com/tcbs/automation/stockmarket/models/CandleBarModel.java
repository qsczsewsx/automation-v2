package com.tcbs.automation.stockmarket.models;

import com.tcbs.automation.stockmarket.FuturesMarket;
import com.tcbs.automation.stockmarket.Stockmarket;
import com.tcbs.automation.stoxplus.Stoxplus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.result.Output;
import org.hibernate.result.ResultSetOutput;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import wiremock.org.apache.commons.collections4.CollectionUtils;

import javax.persistence.ParameterMode;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandleBarModel {

  private static final String TICKER = "ticker";
  private static final String STOCK = "stock";
  private static final String FROM_TIME = "fromTime";
  private static final String TO_TIME = "toTime";

  private String ticker;


  private Long seqTime;


  private Float openPrice;


  private Float closePrice;


  private Float highPrice;


  private Float lowPrice;


  private Long volume;

  @Step("get one daily price from db")
  public static List<HashMap<String, Object>> getDailyPrice(String ticker) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" select trading_date as trading_date   ");
    queryStringBuilder.append(" 	, code as ticker ");
    queryStringBuilder.append(" 	, cast (DATEDIFF(s , '1970-01-01 00:00:00', trading_date) as bigint ) *1000 as seqTime ");
    queryStringBuilder.append(" 	, cast(OpenPrice_Adjusted as float(24)) as  openPrice ");
    queryStringBuilder.append(" 	, cast(ClosePrice_Adjusted as float(24)) as closePrice ");
    queryStringBuilder.append(" 	, cast(Lowest_Adjusted as float(24)) as  lowPrice ");
    queryStringBuilder.append(" 	, cast(Highest_Adjusted as float(24)) as highPrice ");
    queryStringBuilder.append(" 	, total_trading_qtty as volume ");
    queryStringBuilder.append(" from Smy_dwh_stox_MarketPrices ");
    queryStringBuilder.append(" where code = :ticker ");
    queryStringBuilder.append(" and trading_date = (SELECT max(trading_Date) from Smy_dwh_stox_MarketPrices); ");
    // queryStringBuilder.append(" order by trading_date  desc ; ");
    try {
      return Stoxplus.stoxDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter(TICKER, ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }


  @Step("get one intraday price from db")
  public static List<HashMap<String, Object>> getIntradayPrice(String ticker) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT t_ticker as ticker ");
    queryStringBuilder.append(" , i_seq_time as seqTime ");
    queryStringBuilder.append(" , f_open_price as openPrice ");
    queryStringBuilder.append(" , f_close_price as closePrice ");
    queryStringBuilder.append(" , f_low_price as lowPrice  ");
    queryStringBuilder.append(" , f_high_price as  highPrice ");
    queryStringBuilder.append(" , i_volume as volume  ");
    queryStringBuilder.append(" FROM intraday_by_1min WHERE t_ticker = :ticker ");
    queryStringBuilder.append(" AND case  ");
    queryStringBuilder.append(" when (SELECT  MAX(i_seq_time) from intraday_by_1min ) < unix_timestamp(now()) - 120 ");
    queryStringBuilder.append("   then i_seq_time  = (SELECT  unix_timestamp(now()))  ");
    queryStringBuilder.append(" when (SELECT  MAX(i_seq_time) from intraday_by_1min ) >= unix_timestamp(now()) - 120 ");
    queryStringBuilder.append("   then i_seq_time  = (SELECT  MAX(i_seq_time) from intraday_by_1min ) ");
    queryStringBuilder.append(" end");
    queryStringBuilder.append(" ; ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter(TICKER, ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get one intraday price from db")
  public static List<HashMap<String, Object>> getLongTermPrice(String ticker, String from, String to, String type, String resolution) {
    try {
      Session session = type.equals(STOCK) ? Stoxplus.stoxDbConnection.getSession() : Stockmarket.stockMarketConnection.getSession();
      ProcedureCall call = type.equals("index") ?
        session.createStoredProcedureCall("proc_idata_get_index_1D") :
        type.equals(STOCK) ?
          session.createStoredProcedureCall("proc_idata_get_long_term_price") :
          session.createStoredProcedureCall("proc_idata_get_ticker_1D");

      int index = 1;
      call.registerParameter(index++, String.class, ParameterMode.IN).bindValue(ticker);
      if (type.equals(STOCK)) {
        call.registerParameter(index++, String.class, ParameterMode.IN).bindValue(resolution);
      }
      call.registerParameter(index++, Integer.class, ParameterMode.IN).bindValue(Integer.valueOf(from));
      call.registerParameter(index, Integer.class, ParameterMode.IN).bindValue(Integer.valueOf(to));

      Output procOutput = call.getOutputs().getCurrent();

      List resultSets = ((ResultSetOutput) procOutput).getResultList();
      Stockmarket.stockMarketConnection.closeSession();

      List<HashMap<String, Object>> result = new ArrayList<>();
      for (Object rs : resultSets) {
        if (rs instanceof Object[]) {
          Object[] rsMap = (Object[]) rs;
          HashMap<String, Object> item = new HashMap<>();
          item.put(TICKER, rsMap[0]);
          item.put("seq", rsMap[1]);
          item.put("open", rsMap[2]);
          item.put("close", rsMap[3]);
          item.put("high", rsMap[4]);
          item.put("low", rsMap[5]);

          if (type.equals(STOCK)) {
            item.put("vol", ((BigDecimal) rsMap[6]).longValue());
          } else {
            item.put("vol", ((BigInteger) rsMap[6]).longValue());
          }
          result.add(item);
        }

      }
      return result;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get intraday in min")
  public static List<HashMap<String, Object>> getCandleBarShortTerm(String ticker, String wsize, String from, String to) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT t_ticker as ticker, f_open_price as open_price, f_high_price as high_price, f_low_price as low_price, ");
    queryStringBuilder.append(" f_close_price as close_price, i_volume as volume, i_seq_time as seq_time FROM ");
    if (wsize.equalsIgnoreCase("1")) {
      queryStringBuilder.append(" intraday_by_1min ");
    } else if (wsize.equalsIgnoreCase("5")) {
      queryStringBuilder.append(" intraday_by_5min ");
    } else if (wsize.equalsIgnoreCase("10")) {
      queryStringBuilder.append(" intraday_by_10min ");
    } else if (wsize.equalsIgnoreCase("15")) {
      queryStringBuilder.append(" intraday_by_15min ");
    } else if (wsize.equalsIgnoreCase("30")) {
      queryStringBuilder.append(" intraday_by_30min ");
    } else if (wsize.equalsIgnoreCase("60")) {
      queryStringBuilder.append(" intraday_by_60min ");
    }
    queryStringBuilder.append(" WHERE t_ticker = :ticker ");
    queryStringBuilder.append(" AND i_seq_time >= :fromTime AND i_seq_time < :toTime  ");
    queryStringBuilder.append(" group by i_seq_time, i_volume, f_open_price, f_high_price, f_close_price, f_low_price ; ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter(TICKER, ticker)
        .setParameter(FROM_TIME, from)
        .setParameter(TO_TIME, to)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get index 1 min")
  public static List<HashMap<String, Object>> getIndexShortTerm(String ticker, String wsize, String from, String to) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT t_market_code as ticker, f_open_index as open_price, f_high_index as high_price, f_low_index as low_price, ");
    queryStringBuilder.append(" f_close_index as close_price, i_volume as volume, i_seq_time as seq_time FROM ");
    if (wsize.equalsIgnoreCase("1")) {
      queryStringBuilder.append(" intradayindex_by_1min ");
    } else if (wsize.equalsIgnoreCase("5")) {
      queryStringBuilder.append(" intradayindex_by_5min ");
    } else if (wsize.equalsIgnoreCase("10")) {
      queryStringBuilder.append(" intradayindex_by_10min ");
    } else if (wsize.equalsIgnoreCase("15")) {
      queryStringBuilder.append(" intradayindex_by_15min ");
    } else if (wsize.equalsIgnoreCase("30")) {
      queryStringBuilder.append(" intradayindex_by_30min ");
    } else if (wsize.equalsIgnoreCase("60")) {
      queryStringBuilder.append(" intradayindex_by_60min ");
    }
    queryStringBuilder.append(" WHERE t_market_code = :ticker ");
    queryStringBuilder.append(" AND i_seq_time > :fromTime AND i_seq_time < :toTime  ");
    queryStringBuilder.append(" group by i_seq_time, f_open_index, f_high_index, f_low_index, f_close_index, i_volume ; ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter(TICKER, ticker)
        .setParameter(FROM_TIME, from)
        .setParameter(TO_TIME, to)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get intraday in min")
  public static List<HashMap<String, Object>> getIndexCoveredWarrDerivativeCandleBarHistory(String ticker, String from, String to, String type) {
    StringBuilder queryStringBuilder = new StringBuilder();
    if (type.equals("index")) {
      queryStringBuilder.append(" SELECT t_market_code as ticker, f_open_index as open, f_high_index as high, f_low_index as low, ");
      queryStringBuilder.append(" f_close_index as close, i_volume as vol, i_seq_time as seq  ");
      queryStringBuilder.append(" FROM intradayindex_by_1D ");
      queryStringBuilder.append(" WHERE t_market_code = :ticker ");
      queryStringBuilder.append(" AND i_seq_time >= :fromTime AND i_seq_time < :toTime  ORDER BY i_seq_time");
    } else {
      queryStringBuilder.append(" SELECT t_ticker as ticker, f_open_price as open, f_high_price as high, f_low_price as low, ");
      queryStringBuilder.append(" f_close_price as close, i_volume as vol, i_seq_time as seq  ");
      queryStringBuilder.append(" FROM intraday_by_1D WHERE t_ticker = :ticker ");
      queryStringBuilder.append(" AND i_seq_time > :fromTime AND i_seq_time < :toTime  ORDER BY i_seq_time");
    }

    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter(TICKER, ticker)
        .setParameter(FROM_TIME, from)
        .setParameter(TO_TIME, to)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get intraday in day")
  public static HashMap<String, Object> getIntradayTickerCandle(String ticker, String from, String type) {
    StringBuilder queryStringBuilder = new StringBuilder();

    queryStringBuilder.append("  SELECT t1.t_ticker as ticker, ").append(!StringUtils.equals(STOCK, type) ? ":fromTime" : "seq ");
    queryStringBuilder.append(" as seq, t2.f_open_price as open, ");
    queryStringBuilder.append("  	t3.f_close_price as close, t1.f_high_price as high, t1.f_low_price as low, t1.i_volume as vol");
    queryStringBuilder.append("  FROM ( ");
    queryStringBuilder.append("  	SELECT t_ticker, MAX(f_high_price) as f_high_price, MIN(f_low_price) as f_low_price, SUM(i_volume) as i_volume ");
    queryStringBuilder.append("  	FROM intraday_by_1min WHERE t_ticker = :ticker AND i_seq_time >= :fromTime ");
    queryStringBuilder.append("  	group by t_ticker ) t1 ");
    queryStringBuilder.append("  LEFT JOIN ( ");
    queryStringBuilder.append("  	SELECT t_ticker, f_open_price ");
    queryStringBuilder.append("  	FROM intraday_by_1min WHERE t_ticker = :ticker AND i_seq_time >= :fromTime ");
    queryStringBuilder.append("  	Order by i_seq_time asc limit 0,1 ");
    queryStringBuilder.append("  ) t2 ON 1 = 1 ");
    queryStringBuilder.append("  LEFT JOIN ( ");
    queryStringBuilder.append("  	SELECT t_ticker, f_close_price , i_seq_time as seq ");
    queryStringBuilder.append("  	FROM intraday_by_1min 	WHERE t_ticker = :ticker AND i_seq_time >= :fromTime ");
    queryStringBuilder.append("  	Order by i_seq_time desc limit 0,1 ) t3 ON 1 = 1; ");

    try {
      List<HashMap<String, Object>> lsEntity = Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
              .setParameter(TICKER, ticker)
              .setParameter(FROM_TIME, from)
              .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
              .getResultList();
      if (CollectionUtils.isNotEmpty(lsEntity)) {
        return lsEntity.get(0);
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return null;
  }

  @Step("get intraday in day")
  public static HashMap<String, Object> getIntradayIndexCandle(String ticker, String from) {
    StringBuilder queryStringBuilder = new StringBuilder();

    queryStringBuilder.append(" SELECT t1.t_market_code as ticker, seq, t2.f_open_index as open,  ");
    queryStringBuilder.append("  t3.f_close_index as close, t1.f_high_index as high, t1.f_low_index as low, t1.i_volume as vol");
    queryStringBuilder.append(" FROM ( ");
    queryStringBuilder.append("  SELECT t_market_code, MAX(f_high_index) as f_high_index, MIN(f_low_index) as f_low_index, SUM(i_volume) as i_volume ");
    queryStringBuilder.append("  FROM intradayindex_by_1min WHERE t_market_code = :ticker AND i_seq_time >= :fromTime ");
    queryStringBuilder.append("  group by t_market_code ) t1 ");
    queryStringBuilder.append(" LEFT JOIN ( ");
    queryStringBuilder.append("  SELECT t_market_code, f_open_index  ");
    queryStringBuilder.append("  FROM intradayindex_by_1min  WHERE t_market_code = :ticker AND i_seq_time >= :fromTime ");
    queryStringBuilder.append("  Order by i_seq_time asc ");
    queryStringBuilder.append("  limit 0,1 ");
    queryStringBuilder.append(" ) t2 ON 1 = 1 ");
    queryStringBuilder.append(" LEFT JOIN ( ");
    queryStringBuilder.append("  SELECT t_market_code, f_close_index, i_seq_time as seq  ");
    queryStringBuilder.append("  FROM intradayindex_by_1min  WHERE t_market_code = :ticker AND i_seq_time >= :fromTime ");
    queryStringBuilder.append("  Order by i_seq_time desc limit 0,1 ");
    queryStringBuilder.append(" ) t3 ON 1 = 1; ");

    try {
      List<HashMap<String, Object>> lsEntity = Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter(TICKER, ticker)
        .setParameter(FROM_TIME, from)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      if (CollectionUtils.isNotEmpty(lsEntity)) {
        return lsEntity.get(0);
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return null;
  }

  @Step("get derivation 1D")
  public static List<HashMap<String, Object>> getDerivativeCandleBarHistory(String ticker, String from, String to) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT future_name as ticker, seq_time as seq, open_price as open, ");
    queryStringBuilder.append(" 	close_price as close, high_price as high, low_price  as low, acc_vol  as vol ");
    queryStringBuilder.append(" from future_by_1D fbd  ");
    queryStringBuilder.append(" WHERE future_name = :ticker ");
    queryStringBuilder.append(" and seq_time >= :fromTime and seq_time < :toTime ");
    queryStringBuilder.append(" order by seq_time; ");

    try {
      return FuturesMarket.futuresMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter(TICKER, ticker)
        .setParameter(FROM_TIME, from)
        .setParameter(TO_TIME, to)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static HashMap<String, Object> getIntradayDerivativeCandle(String ticker, String from) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT t1.t_ticker as ticker, :fromTime as seq, t2.f_open_price as open,  ");
    queryStringBuilder.append(" t3.f_close_price as close, t1.high_price as high, t1.low_price as low, t1.acc_vol as vol ");
    queryStringBuilder.append(" FROM (  ");
    queryStringBuilder.append(" 	SELECT t_ticker, MAX(f_high_price) as high_price, MIN(f_low_price) as low_price, sum(i_volume) as acc_vol ");
    queryStringBuilder.append(" 	FROM intraday_by_1min fbm 	WHERE i_seq_time >= :fromTime AND t_ticker = :ticker ");
    queryStringBuilder.append(" 	group by t_ticker  ");
    queryStringBuilder.append(" ) t1  ");
    queryStringBuilder.append(" LEFT JOIN (  ");
    queryStringBuilder.append(" 	SELECT t_ticker, f_open_price  ");
    queryStringBuilder.append(" 	FROM intraday_by_1min  	WHERE i_seq_time >= :fromTime AND t_ticker = :ticker ");
    queryStringBuilder.append(" 	Order by i_seq_time asc limit 0,1  ");
    queryStringBuilder.append(" ) t2 ON 1 = 1  ");
    queryStringBuilder.append(" LEFT JOIN (  ");
    queryStringBuilder.append(" 	SELECT t_ticker, f_close_price  ");
    queryStringBuilder.append(" 	FROM intraday_by_1min  WHERE i_seq_time >= :fromTime AND t_ticker = :ticker ");
    queryStringBuilder.append(" 	Order by i_seq_time desc limit 0,1  ");
    queryStringBuilder.append(" ) t3 ON 1 = 1;  ");

    try {
      List<HashMap<String, Object>> lsEntity = FuturesMarket.futuresMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString()) //TODO doi db
        .setParameter(TICKER, ticker)
        .setParameter(FROM_TIME, from)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();

      if (CollectionUtils.isNotEmpty(lsEntity)) {
        return lsEntity.get(0);
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new HashMap<>();
  }

  public static List<HashMap<String, Object>> getDerivativeShortTerm(String ticker, String derivativeType, String wsize, String from, String to) {
    String table = StringUtils.EMPTY;
    if (wsize.equalsIgnoreCase("1")) {
      table = "intraday_by_1min";
    } else if (wsize.equalsIgnoreCase("5")) {
      table = "intraday_by_5min";
    } else if (wsize.equalsIgnoreCase("10")) {
      table = "intraday_by_10min";
    } else if (wsize.equalsIgnoreCase("15")) {
      table = "intraday_by_15min";
    } else if (wsize.equalsIgnoreCase("30")) {
      table = "intraday_by_30min";
    } else if (wsize.equalsIgnoreCase("60")) {
      table = "intraday_by_60min";
    }
    StringBuilder queryStringBuilder = new StringBuilder();
//    queryStringBuilder.append(" SELECT DISTINCT t_ticker as ticker, i_seq_time as seq_time, f_open_price,  f_close_price, f_high_price, f_low_price, i_volume as volume  ");
//    queryStringBuilder.append(" FROM ").append(table);
//    queryStringBuilder.append(" WHERE i_seq_time >= (FLOOR(:toTime/86400) * 86400) ");
//    queryStringBuilder.append(" and i_seq_time <= :toTime ");
//    queryStringBuilder.append(" and t_ticker = :ticker ");
//    queryStringBuilder.append(" UNION ALL ");
    queryStringBuilder.append(" SELECT DISTINCT t_ticker as ticker, i_seq_time as seq_time, f_open_price as open_price,  f_close_price as close_price, f_high_price as high_price, f_low_price as low_price, i_volume as volume  ");
    queryStringBuilder.append(" FROM ").append(table);
    queryStringBuilder.append(" WHERE i_seq_time >= :fromTime ");
//    queryStringBuilder.append(" and i_seq_time < (FLOOR(:toTime/86400) * 86400) ");
    queryStringBuilder.append(" and i_seq_time < :toTime and t_ticker like 'VN30F%' ");
    queryStringBuilder.append(" and func_idata_future_name_by_ticker_and_time(t_ticker, i_seq_time) = :future_name ");
    queryStringBuilder.append(" ORDER BY i_seq_time ASC ");
    try {
      return FuturesMarket.futuresMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString()) //TODO doi db
//        .setParameter(TICKER, ticker)
        .setParameter("future_name", derivativeType)
        .setParameter(FROM_TIME, from)
        .setParameter(TO_TIME, to)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
