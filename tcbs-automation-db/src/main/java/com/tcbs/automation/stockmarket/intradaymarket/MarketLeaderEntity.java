package com.tcbs.automation.stockmarket.intradaymarket;

import com.tcbs.automation.stockmarket.Stockmarket;
import com.tcbs.automation.tca.TcAnalysis;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarketLeaderEntity {
  private static final String UPCOM = "UPCOM";

  @Step("select data")
  public static List<HashMap<String, Object>> getMarketLeader1M(String exchangeId, String industryId, long fromTime, long endTime) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("select DISTINCT exchange, seq_time, ticker, market_cap, close_price, ref_price, ");
    queryStringBuilder.append("acc_value, score, advanced, icbCodeL2 ");
    queryStringBuilder.append("from market_top_leader_1min ");
    queryStringBuilder.append("where exchange =:exchange_Id ");
    queryStringBuilder.append("and icbCodeL2 =:industryID ");
    queryStringBuilder.append("and seq_time >= :fromTime ");
    queryStringBuilder.append("and seq_time <= :endTime ");
    queryStringBuilder.append("and seq_time = (select max(seq_time) from market_top_leader_1min where exchange =:exchange_Id and icbCodeL2 =:industryID ) ");
    queryStringBuilder.append("order by score desc;");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("exchange_Id", exchangeId)
        .setParameter("fromTime", fromTime)
        .setParameter("endTime", endTime)
        .setParameter("industryID", industryId)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("select data")
  public static List<HashMap<String, Object>> getMarketLeader1D(String exchangeId, String industryId, String fromTime, String endTime) {
    String exchange = getExchange(exchangeId);
    if (StringUtils.isEmpty(exchange)) {
      return new ArrayList<>();
    }

    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("select DISTINCT date_report,market_cap,ticker,level2_id,index_number,exchange_id, ");
    queryStringBuilder.append("updated,total_value,ref_price,close_price,icbCodeL2, ");
    if (StringUtils.equals("ALL", exchangeId) && !StringUtils.equals("ALL", industryId)) {
      queryStringBuilder.append("industry_index as index_value ");
    } else {
      queryStringBuilder.append("index_value ");
    }
    queryStringBuilder.append("from market_price_marketcap ");
    queryStringBuilder.append("where 1= 1 ");
    if (!StringUtils.equals("ALL", exchangeId)) {
      queryStringBuilder.append("and exchange_id =:exchange_id ");
    }
    if (StringUtils.equals("VN30", exchangeId)) {
      queryStringBuilder.append("and index_number = 2 ");
    } else {
      queryStringBuilder.append("and index_number != 2 ");
    }
    if (!StringUtils.equals("ALL", industryId)) {
      queryStringBuilder.append("and icbCodeL2 =:industryId ");
    }
    queryStringBuilder.append("and date_report >= CONVERT(:fromTime, DATE) ");
    queryStringBuilder.append("and date_report <= CONVERT(:endTime, DATE) ;");
    try {
      NativeQuery sql = Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("fromTime", fromTime)
        .setParameter("endTime", endTime);

      if (!StringUtils.equals("ALL", exchangeId)) {
        sql.setParameter("exchange_id", exchange);
      }
      if (!StringUtils.equals("ALL", industryId)) {
        sql.setParameter("industryId", industryId);
      }
      return sql.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static String getExchange(String exchangeId) {
    String exchange;
    switch (StringUtils.upperCase(exchangeId)) {
      case "ALL":
        exchange = "ALL";
        break;
      case "HNX":
        exchange = "1";
        break;
      case UPCOM:
        exchange = "3";
        break;
      case "HOSE":
      case "VN30":
        exchange = "0";
        break;
      default:
        exchange = StringUtils.EMPTY;
    }
    return exchange;
  }

  @Step("select data")
  public static List<HashMap<String, Object>> getTotalMarketCapT(String exchangeId, String industryId, String fromTime) {
    String exchange = getExchange(exchangeId);
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("select distinct IFNULL(sum(market_cap), 0) as totalMc, exchange_id , index_number , level2_id  ");
    queryStringBuilder.append("from market_price_marketcap ");
    queryStringBuilder.append("where 1 = 1 ");
    if (!StringUtils.equals("ALL", exchangeId)) {
      queryStringBuilder.append("and exchange_id =:exchange_id ");
    }
    if (StringUtils.equals("VN30", exchangeId)) {
      queryStringBuilder.append("and index_number = 2 ");
    } else {
      queryStringBuilder.append("and index_number != 2 ");
    }
    if (!StringUtils.equals("ALL", industryId)) {
      queryStringBuilder.append("and icbCodeL2 =:industryId ");
    }
    queryStringBuilder.append("and date_report = CONVERT(:from_date, DATE) ;");
    try {
      NativeQuery sql = Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("from_date", fromTime);

      if (!StringUtils.equals("ALL", exchangeId)) {
        sql.setParameter("exchange_id", exchange);
      }
      if (!StringUtils.equals("ALL", industryId)) {
        sql.setParameter("industryId", industryId);
      }
      return sql.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("select data 1M")
  public static List<HashMap<String, Object>> getListTicker(String exchangeId, String industryId, boolean isBasicTicker) {
    String exchange = getExchange(exchangeId);
    if (StringUtils.isEmpty(exchange)) {
      return new ArrayList<>();
    }

    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select ticker, exchangeId as ExchangeID, exchangeName , indexNumber , icbCodeL2 as idLevel2 ");
    queryBuilder.append("from tca_vw_idata_index_industry_exchange_v2 ");
    queryBuilder.append("where 1= 1  ");
    if (!StringUtils.equals("ALL", exchangeId)) {
      queryBuilder.append("and exchangeId = :exchangeID ");
    }
    if (StringUtils.equals("VN30", exchangeId)) {
      queryBuilder.append("and indexNumber = :indexId ");
    }
    if (!StringUtils.equals("ALL", industryId)) {
      queryBuilder.append("and icbCodeL2 = :industry_id ");
    }
    if (isBasicTicker) {
      queryBuilder.append("and LENGTH(ticker) = 3 ");
    }
    try {
      NativeQuery query = Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString());
      if (!StringUtils.equals("ALL", exchangeId)) {
        query.setParameter("exchangeID", exchange);
      }
      if (StringUtils.equals("VN30", exchangeId)) {
        query.setParameter("indexId", "2");
      }
      if (!StringUtils.equals("ALL", industryId)) {
        query.setParameter("industry_id", industryId);
      }
      return query
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static String getMarketCode(String exchangeId) {
    String marketCode = StringUtils.EMPTY;
    if (StringUtils.equalsIgnoreCase("HOSE", exchangeId)) {
      marketCode = "VNINDEX";
    } else if (StringUtils.equalsIgnoreCase("HNX", exchangeId)) {
      marketCode = "HNXINDEX";
    } else if (StringUtils.equalsIgnoreCase("VN30", exchangeId)) {
      marketCode = "VN30";
    } else if (StringUtils.equalsIgnoreCase(UPCOM, exchangeId)) {
      marketCode = UPCOM;
    }

    return marketCode;
  }

  @Step("get data")
  public static List<Map<String, Object>> getIndex(String exchangeId, String industryId, long fromDate, long toDate) {
    if (StringUtils.equalsIgnoreCase("ALL", exchangeId) && !StringUtils.equalsIgnoreCase("ALL", industryId)) {
      return getIndexSector(industryId, fromDate, toDate);
    }
    String marketCode = getMarketCode(exchangeId);
    if (StringUtils.isEmpty(marketCode)) {
      return new ArrayList<>();
    }
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT t_market_code , IFNULL(f_close_index, 0) as indexScore, i_seq_time ");
    queryBuilder.append("from intradayindex_by_1D ibd  ");
    queryBuilder.append("where t_market_code =:exchangeID ");
    queryBuilder.append("and i_seq_time >= :fromDate and  i_seq_time <= :toDate ");
    queryBuilder.append("order by i_seq_time desc;");

    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("exchangeID", marketCode)
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static List<Map<String, Object>> getMarketPrice(Integer exchangeId, String industry) {
    StringBuilder queryBuilder = new StringBuilder();
    String index;
    String condition;
    if (StringUtils.equals("ALL", industry)) {
      index = "index_value";
      condition = "exchange_id = :exchange";
    } else {
      index = "industry_index";
      condition = "icbCodeL2 = :industry";
    }
    queryBuilder.append("select DISTINCT date_report, exchange_id , ").append(index).append(" as indexScore, ticker , market_cap as mkCap ");
    queryBuilder.append("from market_price_marketcap a WHERE ").append(condition).append(" and index_number != 2 ");
    queryBuilder.append("and date_report = (select max(date_report) from market_price_marketcap ) ");
    queryBuilder.append("order by date_report desc;");

    try {
      NativeQuery sql = Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString());
      if (StringUtils.equals("ALL", industry)) {
        sql.setParameter("exchange", exchangeId);
      } else {
        sql.setParameter("industry", industry);
      }
      return sql.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static List<Map<String, Object>> getIndexSector(String industryId, long fromDate, long toDate) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select top 1 IdLevel2 , SectorIndex as indexScore from tbl_idata_pe_sector_index t1  ");
    queryBuilder.append("WHERE IdLevel2 = :industry and :from <= DATEDIFF(SECOND, '19700101', ReportDate ) ");
    queryBuilder.append("and DATEDIFF(SECOND, '19700101', ReportDate ) < :to  order by ReportDate desc ");

    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("industry", industryId)
        .setParameter("from", fromDate)
        .setParameter("to", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static List<Map<String, Object>> getMarketCap(List<String> tickers) {
    StringBuilder queryBuilder = new StringBuilder();
//    queryBuilder.append("SELECT Ticker as ticker , F5_7 as mkCap, UpdateDate as i_seq_time ");
//    queryBuilder.append("from stox_tb_Ratio ");
//    queryBuilder.append("WHERE Ticker IN :lstTicker ");
//    queryBuilder.append("and UpdateDate = (SELECT max(UpdateDate) from stox_tb_Ratio where UpdateDate < CONVERT(date, GETDATE())) ;");

    queryBuilder.append("SELECT distinct Ticker as ticker , RTD11 as mkCap, TradingDate as i_seq_time ");
    queryBuilder.append("from stx_rto_RatioTTMDaily ");
    queryBuilder.append("WHERE Ticker IN :lstTicker ");
    queryBuilder.append("and TradingDate = (SELECT max(TradingDate) from stx_rto_RatioTTMDaily where TradingDate < CONVERT(date, GETDATE())) ;");

    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("lstTicker", tickers)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("select data price")
  public static List<HashMap<String, Object>> getPriceIntra(List<String> tickers, String fromDate, String toDate) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT i_seq_time, t_ticker as ticker, (f_open_price- f_open_price_change) as ref_price, f_close_price  as close_price, acc_value as val ");
    queryStringBuilder.append("from intraday_by_1min ");
    queryStringBuilder.append("WHERE t_ticker IN :lstTicker ");
//    queryStringBuilder.append("and i_seq_time >= :fromDate ");
//    queryStringBuilder.append("and i_seq_time <= :toDate ");
    queryStringBuilder.append("and i_seq_time = (select max(i_seq_time) from intraday_by_1min  ");
    queryStringBuilder.append(" where i_seq_time >= :fromDate and i_seq_time <= :toDate and i_seq_time %86400 != 16200 and i_seq_time < UNIX_TIMESTAMP(CURRENT_DATE()) + 8 *3600 ) ");
    queryStringBuilder.append("order by i_seq_time desc;");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("lstTicker", tickers)
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step
  public static List<HashMap<String, Object>> getTransHistory(List<String> tickerList, String fromDate, String toDate) {
    StringBuilder queryBuilder = new StringBuilder();
//    queryBuilder.append("SELECT code as ticker, ROUND(ClosePrice_Adjusted /close_price * basic_price, 0) basic_price , ClosePrice_Adjusted as close_price , Total_Value, trading_Date  ");
//    queryBuilder.append("from Smy_dwh_stox_MarketPrices ");
//    queryBuilder.append("WHERE code IN :tickers ");
//    queryBuilder.append("and trading_Date >= CONVERT(datetime, :from_time ) ");
//    queryBuilder.append("and trading_Date <= CONVERT(datetime, :to_time ) ");
//    queryBuilder.append("order by trading_Date desc; ");

//    queryBuilder.append("select Ticker as ticker, ROUND(ClosePriceAdjusted /ClosePrice * ReferencePrice , -2) basic_price ,  ");
    queryBuilder.append("select Ticker as ticker,  ReferencePrice  basic_price ,  ");
    queryBuilder.append("ClosePriceAdjusted as close_price , TotalMatchValue  as Total_Value, TradingDate trading_Date ");
    queryBuilder.append("from Smy_dwh_stox_MarketPrices ");
    queryBuilder.append("where Ticker IN :tickers  ");
    queryBuilder.append("and TradingDate >= CONVERT(datetime, :from_time ) ");
    queryBuilder.append("and TradingDate <= CONVERT(datetime, :to_time ) ");
    queryBuilder.append("order by TradingDate desc; ");
    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("tickers", tickerList)
        .setParameter("from_time", fromDate)
        .setParameter("to_time", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("select data")
  public static List<HashMap<String, Object>> getAllMarketLeader1M(String exchangeId, String industryId, long endTime) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("select t.* ");
    queryStringBuilder.append("from market_top_leader_1min t  ");
    queryStringBuilder.append("where exchange =:exchange and idlevel2 =:industryID ");
    queryStringBuilder.append("and seq_time <= :end_time and seq_time >= UNIX_TIMESTAMP(CURRENT_DATE())  ");
    queryStringBuilder.append("order by seq_time; ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("exchange", exchangeId)
        .setParameter("end_time", endTime)
        .setParameter("industryID", StringUtils.equals("0", industryId) ? "ALL" : industryId)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("select data price")
  public static List<HashMap<String, Object>> getPriceIntraBefore(List<String> tickers, String fromDate, String toDate) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("select *, FROM_UNIXTIME(i_seq_time) from ( ");
    queryStringBuilder.append("	SELECT i_seq_time, t_ticker as ticker, (f_open_price- f_open_price_change) as ref_price, f_close_price  as close_price, acc_value as val ");
    queryStringBuilder.append("	from intraday_by_1min  ");
    queryStringBuilder.append("	WHERE t_ticker IN :tickers  ");
    queryStringBuilder.append("	and i_seq_time > :from_date  ");
    queryStringBuilder.append("	and i_seq_time <= :to_date  ");
    queryStringBuilder.append("	order by i_seq_time desc ");
    queryStringBuilder.append(")tbl group by ticker ");
    queryStringBuilder.append("order by i_seq_time asc; ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("tickers", tickers)
        .setParameter("from_date", fromDate)
        .setParameter("to_date", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
