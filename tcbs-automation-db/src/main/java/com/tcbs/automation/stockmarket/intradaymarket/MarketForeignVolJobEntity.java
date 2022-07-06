package com.tcbs.automation.stockmarket.intradaymarket;

import com.tcbs.automation.stockmarket.Stockmarket;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.math.BigInteger;
import java.util.*;

public class MarketForeignVolJobEntity {
  private static final String CURRENT_DATE = "currentDate";
  private static final String INDUSTRY = "industry";

  public static List<HashMap<String, Object>> getTotalNetVolByIndustry(String currentDate, String industry) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT seq_time, cast(sum(net_trade) as DECIMAL ) as net_vol  ")
      .append(" FROM foreigntrade_by_1min WHERE ticker in (SELECT ticker FROM tca_vw_idata_index_industry_exchange_v2 WHERE icbCodeL2 = :industry and lenTicker = 3) ")
      .append(" and DATE_FORMAT(FROM_UNIXTIME(seq_time), '%Y-%m-%d') = :currentDate ")
      .append(" group by seq_time ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .setParameter(CURRENT_DATE, currentDate)
        .setParameter(INDUSTRY, industry)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }


  public static List<HashMap<String, Object>> getTotalNetVolByIndustry15(String currentDate, String industry) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT * ")
      .append(" FROM foreigntrade_by_1min WHERE ticker in (SELECT ticker FROM tca_vw_idata_index_industry_exchange_v2 WHERE icbCodeL2 = :industry and lenTicker = 3) ")
      .append(" and DATE_FORMAT(FROM_UNIXTIME(seq_time), '%Y-%m-%d') = :currentDate ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .setParameter(CURRENT_DATE, currentDate)
        .setParameter(INDUSTRY, industry)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static long timestampStartTrading() {
    Calendar now = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
    now.set(Calendar.HOUR_OF_DAY, 9);
    now.set(Calendar.MINUTE, 0);
    now.set(Calendar.SECOND, 0);

    return now.getTimeInMillis() / 1000;
  }

  public static List<HashMap<String, Object>> getAccNet(String type, long maxSeqTime) {
    long fromSeqTime = timestampStartTrading();

    StringBuilder queryBuilder = new StringBuilder();
    if (type.equalsIgnoreCase("1d") || type.equalsIgnoreCase("1W")) {
      queryBuilder.append("SELECT " +
        " tt1.ticker" +
        " ,tt1.acc_trade" +
        " FROM foreigntrade_by_1min fbm2 " +
        " join (" +
        " SELECT " +
        " fbm.ticker" +
        " , acc_trade" +
        " ,fbm.seq_time FROM foreigntrade_by_1min fbm " +
        " INNER JOIN (" +
        " SELECT ticker" +
        " , MAX(seq_time) AS seq_time FROM foreigntrade_by_1min fbm " +
        " WHERE seq_time <= :maxSeqTime and seq_time >= :fromSeqTime" +
        " GROUP BY ticker" +
        " ) tt" +
        " ON fbm.ticker = tt.ticker AND fbm.seq_time = tt.seq_time" +
        " ) tt1 on tt1.seq_time = fbm2.seq_time and tt1.ticker = fbm2.ticker" +
        " group by ticker;");
    } else {
      queryBuilder.append("SELECT * FROM foreigntrade_by_1d fbd where trading_date = DATE_FORMAT(FROM_UNIXTIME(:seqTime), '%Y-%m-%d')");
    }
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .setParameter("maxSeqTime", maxSeqTime)
        .setParameter("fromSeqTime", fromSeqTime)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }


  public static List<HashMap<String, Object>> getIndustryForeignTradeBy1min(String exchange, String industry, String currentDate) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT DISTINCT * FROM industry_foreign_trading_1min where exchange = :exchange and icbCodeL2 = :industry and DATE_FORMAT(FROM_UNIXTIME(seq_time), '%Y-%m-%d') = :currentDate ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .setParameter("exchange", exchange)
        .setParameter(INDUSTRY, industry)
        .setParameter(CURRENT_DATE, currentDate)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static List<HashMap<String, Object>> getIndustryForeignTradeBy15min(String exchange, String industry, String currentDate) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT * FROM industry_foreign_trading_15min " +
      "where exchange = :exchange " +
      "and icbCodeL2 = :industry " +
      "and DATE_FORMAT(FROM_UNIXTIME(seq_time), '%Y-%m-%d') = :currentDate");

    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .setParameter("exchange", exchange)
        .setParameter(INDUSTRY, industry)
        .setParameter(CURRENT_DATE, currentDate)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static List<HashMap<String, Object>> getMaxSeqTime() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT max(seq_time) as seq_time FROM industry_foreign_trading_15min");

    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static long getMaxTimeSeq() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select max(seq_time) as max_seq_time from industry_foreign_trading_1min");
    try {
      List<HashMap<String, Object>> listData = Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();

      return ((BigInteger) listData.get(0).get("max_seq_time")).longValue();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return 0L;
  }


  public static BigInteger selectAccNetVol15(long endTime, String indicator) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select sum(t.acc_trade) as acc_net_vol from " +
      "( " +
      " SELECT " +
      " fbm.ticker" +
      " , acc_trade" +
      " ,fbm.seq_time   FROM foreigntrade_by_1min fbm " +
      " INNER JOIN (" +
      "  SELECT ticker" +
      "  , MAX(seq_time) AS seq_time  FROM foreigntrade_by_1min fbm " +
      "  WHERE seq_time <= :endTime and seq_time >= (UNIX_TIMESTAMP(CURDATE()) + 2 * 60 * 60)" +
      "  GROUP BY ticker" +
      " ) tt" +
      " ON fbm.ticker = tt.ticker AND fbm.seq_time = tt.seq_time" +
      "  where fbm.ticker in (SELECT ticker FROM tca_vw_idata_index_industry_exchange_v2 WHERE icbCodeL2 = :icbCodeL2 and lenTicker = 3)" +
      "" +
      ") t;");
    try {
      List<HashMap<String, Object>> listData = Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .setParameter("endTime", endTime)
        .setParameter("icbCodeL2", indicator)
        .getResultList();

      return listData.get(0).get("acc_net_vol") != null ? BigInteger.valueOf(((Double) listData.get(0).get("acc_net_vol")).longValue()) : null;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return null;
  }

  public static List<HashMap<String, Object>> getListIndustryExchange(String exchange, String indicator) {
    int caseIndustry = 0;
    if ("ALL".equalsIgnoreCase(exchange)) {
      caseIndustry = 1;
    } else {
      if ("ALL".equalsIgnoreCase(indicator)) {
        caseIndustry = 3;
      } else {
        caseIndustry = 2;
      }
    }
    StringBuilder stringBuilder = new StringBuilder();
    if (1 == caseIndustry) {
      stringBuilder.append(" SELECT ticker,exchangeId,exchangeName,icbCodeL2 FROM tca_vw_idata_index_industry_exchange_v2  " +
        "where exchangeId IN (0, 1, 3) and icbCodeL2 = _indicator_ and lenTicker=3".replace("_indicator_", indicator));
    } else if (2 == caseIndustry) {
      String exchangeId = "";
      if (exchange.equalsIgnoreCase("HOSE")) {
        exchangeId = "0";
      } else if (exchange.equalsIgnoreCase("HNX")) {
        exchangeId = "1";
      } else {
        exchangeId = "3";
      }
      stringBuilder.append(" SELECT ticker,exchangeId,exchangeName,icbCodeL2 FROM tca_vw_idata_index_industry_exchange_v2 " +
        "where exchangeId = _exchangeId_ and icbCodeL2 is not null and lenTicker=3".replace("_exchangeId_", exchangeId));
    } else {
      stringBuilder.append("SELECT ticker,exchangeId,exchangeName,icbCodeL2 FROM tca_vw_idata_index_industry_exchange_v2 " +
        "where indexNumber = 2 and lenTicker=3");
    }

    try {
      List<HashMap<String, Object>> listData = Stockmarket.stockMarketConnection.getSession().createNativeQuery(stringBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();

      return listData;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
