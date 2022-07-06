package com.tcbs.automation.stockmarket.intradaymarket;

import com.tcbs.automation.stockmarket.Stockmarket;
import com.tcbs.automation.tca.TcAnalysis;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarketVolatilityEntity {

  private static final String EXCHANGE = "exchange";
  private static final String EXCHANGE_ID = "exchangeId";
  private static final String INDUSTRY = "industry";
  private static final String TICKERS = "tickers";
  private static final String DATE5D = "date5d";
  private static final String DATE1D = "date1d";
  private static final String DATE = "date";

  @Step("get data")
  public static List<HashMap<String, Object>> getDataVolatility1Min(String exchange, String industry) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select ticker,industry,exchange,seq_time,acc_vol,acc_val,f_acc_vol,f_acc_val,price_change_percent, ");
    queryBuilder.append("vol_change_1d,f_vol_change_1d ");
    queryBuilder.append("from market_watch_volatility_1min ");
    queryBuilder.append("WHERE ( exchange =:exchange or :exchange = 'ALL' ) and ( :industry = 'ALL' or industry =:industry ) and seq_time = (select max(seq_time) from market_watch_volatility_1min) ;");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .setParameter(EXCHANGE, exchange)
        .setParameter(INDUSTRY, industry)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static List<HashMap<String, Object>> getDataVolatility1MinWithClosePrice(List<String> tickers, long to, long from) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select distinct t1.*, t2.close_price_ori as close_price, ( t2.open_price_ori - t2.open_price_change_ori ) ref_price, ");
    queryBuilder.append("IFNULL(t3.market_cap,0) market_cap, t6.ceil_price, t6.floor_price,  ");
    queryBuilder.append("t4.bop, t5.total_bu/(t5.total_bu + t5.total_sd) as bsp ");
    queryBuilder.append("from market_watch_volatility_1min t1 left join intraday_by_1min t2 on t1.ticker = t2.t_ticker and t1.seq_time = t2.i_seq_time ");
    queryBuilder.append("left join (select ticker, seq_time , over_bought/ (over_bought+ over_sold) as bop from bid_ask_by_1min) t4 on t1.ticker = t4.ticker and t1.seq_time = t4.seq_time ");
    queryBuilder.append("left join (select ticker , seq_time , sheep_bu_acc + wolf_bu_acc + shark_bu_acc as total_bu, sheep_sd_acc + wolf_sd_acc + shark_sd_acc as total_sd from investor_classifier_by_1min) t5 ");
    queryBuilder.append("on t1.ticker = t5.ticker and t1.seq_time = t5.seq_time ");
    queryBuilder.append("left join market_watch_volatility_1d t3 on t1.ticker = t3.ticker and t3.seq_time = FLOOR(:from /86400 ) *86400 ");
    queryBuilder.append("left join orion_ticker_common t6 on t1.ticker = t6.ticker and UNIX_TIMESTAMP(CONVERT_TZ(FROM_UNIXTIME(t6.seq_time), '+00:00', '+7:00')) = FLOOR(:to /86400 ) *86400 ");
    queryBuilder.append("WHERE t1.ticker IN :tickers ");
    queryBuilder.append("and t1.seq_time >= :to and t1.seq_time = (select max(seq_time) from market_watch_volatility_1min) ;");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .setParameter(TICKERS, tickers)
        .setParameter("from", from)
        .setParameter("to", to)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static List<HashMap<String, Object>> getDataMarketPriceEndTrading(List<String> tickers, String date, long from) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select sum(IFNULL(t2.close_price, t2.ref_price) * t1.share_issue) / sum(t1.share_issue * t2.ref_price ) as divideMkCap  ");
    queryBuilder.append("from (select ticker, seq_time, share_issue from market_watch_volatility_1d where seq_time = FLOOR(:from /86400) *86400 ");
    queryBuilder.append("and close_price_1d != 0 ) t1 left join market_price_marketcap t2 on t1.ticker = t2.ticker  ");
    queryBuilder.append("where t1.ticker IN :tickers and t2.date_report =:date ");
    queryBuilder.append("group by t2.date_report ; ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .setParameter(TICKERS, tickers)
        .setParameter(DATE, date)
        .setParameter("from", from)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static List<HashMap<String, Object>> getDataVolatility1D(List<String> tickers, long date1d) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select t3.* ");
    queryBuilder.append("from (select * from market_watch_volatility_1d where seq_time = FLOOR(:to /86400)*86400 ) t3  ");
    queryBuilder.append("where t3.ticker IN :tickers and t3.close_price_1d != 0; ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .setParameter(TICKERS, tickers)
        .setParameter("to", date1d)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static List<HashMap<String, Object>> getDataVolatility1D(List<String> tickers) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select ticker,seq_time,exchange,close_price_1d,ref_price_1d,ref_price_1w,ref_price_1m,market_cap,share_issue, ");
    queryBuilder.append("acc_vol_1w,acc_vol_1m,acc_val_1w,acc_val_1m,f_acc_vol_1w,f_acc_vol_1m,f_acc_val_1w,f_acc_val_1m,market_cap_5d,market_cap_1m, close_price_1w, close_price_1m ");
    queryBuilder.append("from market_watch_volatility_1d WHERE ticker IN :tickers and close_price_1d != 0 and seq_time = (select max(seq_time) from market_watch_volatility_1d) ;");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .setParameter(TICKERS, tickers)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static List<Map<String, Object>> getLatestDate1Min() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select max(seq_time) as latest from market_watch_volatility_1min ;");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static List<Map<String, Object>> getLatestDate1Min(long to) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select max(seq_time) as latest from market_watch_volatility_1min where FLOOR(seq_time/86400)*86400 = FLOOR(:to /86400)*86400 ;");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .setParameter("to", to)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static List<Map<String, Object>> getLatestDate1D() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select max(seq_time) as latest from market_watch_volatility_1d ;");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static List<HashMap<String, Object>> calcVolatility1Min(String exchange, String industry, long date, String date1d) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select distinct t1.t_ticker ticker, t1.i_seq_time seq_time, t1.val acc_val, t1.vol acc_vol, t7.exchangeName exchange , t7.icbCodeL2 industry, ");
    queryBuilder.append("100* ((t1.close_price_ori / t1.ref_price) - 1) as price_change_percent, ");
    queryBuilder.append("IFNULL(vol ,0) - IFNULL(vol1d ,0 ) as vol_change_1d ");
    queryBuilder.append("from ( ");
    queryBuilder.append("	select t_ticker, i_seq_time , IFNULL(acc_value, 0)  as val, IFNULL(i_acc_volume,0)  as vol, close_price_ori, ( open_price_ori - open_price_change_ori ) ref_price ");
    queryBuilder.append("	from intraday_by_1min ibm where i_seq_time = :time ");
    queryBuilder.append(")t1 left join ( ");
    queryBuilder.append("	select t_ticker , IFNULL(i_acc_volume, 0) vol1d ");
    queryBuilder.append("	from intraday_by_1min a  ");
    queryBuilder.append("	where i_seq_time %86400 = :time % 86400 and FLOOR(i_seq_time /86400) *86400 = FLOOR(UNIX_TIMESTAMP(:date1d) /86400)*86400 ");
    queryBuilder.append(") t3 on t1.t_ticker = t3.t_ticker ");
    queryBuilder.append("left join tca_vw_idata_index_industry_exchange_v2 t7 on t1.t_ticker = t7.ticker  ");
    queryBuilder.append("where (t7.exchangeId =:exchange or :exchange = 'ALL' ) and (:industry ='ALL' or t7.icbCodeL2 =:industry ) and t7.lenTicker = 3 ");
    queryBuilder.append("group by t1.t_ticker ");

    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .setParameter(EXCHANGE, MarketLeaderEntity.getExchange(exchange))
        .setParameter(INDUSTRY, industry)
        .setParameter("time", date)
        .setParameter(DATE1D, date1d)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static List<HashMap<String, Object>> getForeignTrade1Min(String exchange, String industry, long date, String date1d) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select distinct t1.t_ticker ticker, t1.i_seq_time seq_time, t2.fvol f_acc_vol, t2.fval f_acc_val, ");
    queryBuilder.append("IFNULL(fvol, 0) - IFNULL(fvol1d, 0) as f_vol_change_1d ");
    queryBuilder.append("from ( select t_ticker, i_seq_time ");
    queryBuilder.append("	from intraday_by_1min ibm where i_seq_time = :time ");
    queryBuilder.append(")t1 left join ( ");
    queryBuilder.append("	select a.ticker, a.seq_time , IFNULL(acc_buy - acc_sell,0) as fvol, IFNULL(buy_acc_val - sell_acc_val ,0) as fval  ");
    queryBuilder.append("	from foreigntrade_by_1min a  ");
    queryBuilder.append("	inner join ( ");
    queryBuilder.append("		select DISTINCT ticker, max(seq_time) seq_time  ");
    queryBuilder.append("		from foreigntrade_by_1min where seq_time <= :time and FLOOR(seq_time/86400)*86400 = FLOOR(:time /86400)*86400  ");
    queryBuilder.append("		group by ticker  ");
    queryBuilder.append("	) t on a.ticker = t.ticker  and a.seq_time = t.seq_time  ");
    queryBuilder.append(")t2 on t1.t_ticker = t2.ticker left join ( ");
    queryBuilder.append("	select a.ticker, a.seq_time , IFNULL(acc_buy - acc_sell, 0) as fvol1d  ");
    queryBuilder.append("	from foreigntrade_by_1min a  inner join ( ");
    queryBuilder.append("		select DISTINCT ticker, max(seq_time) seq_time  ");
    queryBuilder.append("		from foreigntrade_by_1min where seq_time %86400 <= :time %86400  and FLOOR(seq_time/86400)*86400 = FLOOR(UNIX_TIMESTAMP(:date1d) /86400)*86400 ");
    queryBuilder.append("		group by ticker ) t on a.ticker = t.ticker  and a.seq_time = t.seq_time  ");
    queryBuilder.append(") t5 on t1.t_ticker = t5.ticker ");
    queryBuilder.append("  left join tca_vw_idata_index_industry_exchange_v2 t7 on t1.t_ticker = t7.ticker  ");
    queryBuilder.append("where (t7.exchangeId =:exchange or :exchange = 'ALL' ) and (:industry ='ALL' or t7.icbCodeL2 =:industry ) ");
    queryBuilder.append("and t7.lenTicker = 3 group by t1.t_ticker ");

    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .setParameter(EXCHANGE, MarketLeaderEntity.getExchange(exchange))
        .setParameter(INDUSTRY, industry)
        .setParameter("time", date)
        .setParameter(DATE1D, date1d)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static List<HashMap<String, Object>> getMarketPrice(List<String> tickers, String date, String date5d, String date1m) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select distinct t1.ticker, case when exchange_id = 0 then 'HOSE' when exchange_id = 1 then 'HNX' when exchange_id = 3 then 'UPCOM' end as exchange, ");
    queryBuilder.append(" icbCodeL2,  market_cap,  market_cap_5d, market_cap_1m ");
    queryBuilder.append(" from ( ");
    queryBuilder.append(" 	select ticker , exchange_id , icbCodeL2 ,  market_cap  ");
    queryBuilder.append(" 	from market_price_marketcap mpm 	WHERE date_report = :date  ");
    queryBuilder.append(" ) t1 ");
    queryBuilder.append(" left join (select ticker, round(avg(market_cap),0) as market_cap_5d from market_price_marketcap  ");
    queryBuilder.append(" 	WHERE date_report >= :date5d and date_report <= :date	group by ticker ");
    queryBuilder.append(" )t4 on t1.ticker = t4.ticker ");
    queryBuilder.append(" left join (select ticker, round(avg(market_cap),0) as market_cap_1m 	from market_price_marketcap  ");
    queryBuilder.append(" 	WHERE date_report >= :date1m and date_report <= :date	group by ticker ");
    queryBuilder.append(" )t5 on t1.ticker = t5.ticker ");
    queryBuilder.append(" where t1.ticker IN :tickers ");
    queryBuilder.append(" group by ticker; ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .setParameter(TICKERS, tickers)
        .setParameter(DATE, date)
        .setParameter(DATE5D, date5d)
        .setParameter("date1m", date1m)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static List<HashMap<String, Object>> getVolTrading(List<String> tickers, String date1d, String date5d, String date1m) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(";with view_vol_trading as ( SELECT Ticker , TradingDate  ");
    queryBuilder.append("		, ROUND(ClosePriceAdjusted, 0) as close_price   ");
    queryBuilder.append("		, ROUND(ReferencePriceAdjusted, 0) AS ref_price   ");
    queryBuilder.append("		, ForeignBuyVolumeMatched  - ForeignSellVolumeMatched as fvol ");
    queryBuilder.append("		, ForeignBuyValueMatched  - ForeignSellValueMatched as fval  ");
    queryBuilder.append("		, TotalMatchVolume, TotalMatchValue FROM stx_mrk_HoseStock  ");
    queryBuilder.append("	UNION SELECT Ticker , TradingDate  ");
    queryBuilder.append(", ROUND(ClosePriceAdjusted, 0) as close_price ");
    queryBuilder.append("		, ROUND(ReferencePriceAdjusted, 0)  AS ref_price ");
    queryBuilder.append("		, ForeignBuyVolumeMatched  - ForeignSellVolumeMatched  as fvol  ");
    queryBuilder.append("		, ForeignBuyValueMatched  - ForeignSellValueMatched as fval ");
    queryBuilder.append("		, TotalMatchVolume, TotalMatchValue FROM stx_mrk_HnxStock  ");
    queryBuilder.append("	UNION  SELECT Ticker , TradingDate  ");
    queryBuilder.append("		, ROUND(ClosePriceAdjusted, -2) as close_price ");
    queryBuilder.append("		, ROUND(ReferencePriceAdjusted, 0) as ref_price ");
    queryBuilder.append("		, ForeignBuyVolumeMatched  - ForeignSellVolumeMatched as fvol ");
    queryBuilder.append("		, ForeignBuyValueMatched  - ForeignSellValueMatched  as fval ");
    queryBuilder.append("		, TotalMatchVolume, TotalMatchValue 	FROM stx_mrk_UpcomStock ");
    queryBuilder.append(") ");
    queryBuilder.append("select distinct t.Ticker ticker, ISNULL(t.SharesOutstanding,0) share_issue , t1.totalVol acc_vol_1w, ");
    queryBuilder.append("t1.totalVal acc_val_1w, t1.totalFvol f_acc_vol_1w, t1.totalFval f_acc_val_1w, ");
    queryBuilder.append("t2.totalVol acc_vol_1m, t2.totalVal acc_val_1m, t2.totalFvol f_acc_vol_1m, t2.totalFval f_acc_val_1m, ");
    queryBuilder.append("ISNULL(vol1d,0) - ISNULL(avgVol5d,0) vol_change_1w, ISNULL(fvol1d,0) - ISNULL(avgFVol5d,0) fvol_change_1w, ");
    queryBuilder.append("ISNULL(vol1d,0) - ISNULL(avgVol1m,0) vol_change_1m, ISNULL(fvol1d,0) - ISNULL(avgFVol1m,0) fvol_change_1m, ");
    queryBuilder.append("ISNULL(t5.ref_price_1d,0) ref_price_1d, ISNULL(t5.close_price_1d, 0) close_price_1d, ISNULL(t3.ref_price_1w, 0) ref_price_1w, ISNULL(t4.ref_price_1m,0) ref_price_1m, ");
    queryBuilder.append("ISNULL(t3.close_price_1w, 0) close_price_1w, ISNULL(t4.close_price_1m,0) close_price_1m ");
    queryBuilder.append("from (select * from tbl_idata_pe_ticker_index where ReportDate = (select max(ReportDate) from tbl_idata_pe_ticker_index)) t  ");
    queryBuilder.append("left join ( ");
    queryBuilder.append("	select sum(TotalMatchVolume) as totalVol, sum (TotalMatchValue) as totalVal,  ");
    queryBuilder.append("	sum(fvol) as totalFvol, sum(fval) as totalFval, Ticker, ");
    queryBuilder.append("	ROUND(sum(TotalMatchVolume) / 5, 0) as avgVol5d, ROUND(sum(fvol) / 5, 0) as avgFVol5d ");
    queryBuilder.append("	from view_vol_trading where TradingDate >= :date5d and TradingDate <= :date1d group by Ticker ");
    queryBuilder.append(")t1 on t.Ticker = t1.Ticker left join ( ");
    queryBuilder.append("	select sum(TotalMatchVolume) as totalVol, sum (TotalMatchValue) as totalVal,  ");
    queryBuilder.append("	sum(fvol) as totalFvol, sum(fval) as totalFval, Ticker, ");
    queryBuilder.append("	ROUND(sum(TotalMatchVolume) / numDay, 0) as avgVol1m, ROUND(sum(fvol) / numDay, 0) as avgFVol1m ");
    queryBuilder.append("	from view_vol_trading t inner join (select count(distinct TradingDate) as numDay from view_vol_trading where TradingDate >= :date1m ) t1 on 1 = 1  ");
    queryBuilder.append("	where TradingDate >= :date1m and TradingDate <= :date1d group by Ticker, numDay ");
    queryBuilder.append(")t2 on t.Ticker = t2.Ticker left join ( ");
    queryBuilder.append("	select Ticker,ref_price ref_price_1d, close_price close_price_1d, TotalMatchVolume vol1d, fvol fvol1d ");
    queryBuilder.append("	from view_vol_trading where TradingDate = :date1d ");
    queryBuilder.append(")t5 on t.Ticker = t5.Ticker left join (");
    queryBuilder.append("	select Ticker,ref_price ref_price_1w, close_price close_price_1w ");
    queryBuilder.append("	from view_vol_trading where TradingDate = :date5d ");
    queryBuilder.append(")t3 on t.Ticker = t3.Ticker left join (");
    queryBuilder.append("	select Ticker,ref_price ref_price_1m, close_price close_price_1m ");
    queryBuilder.append("	from view_vol_trading where TradingDate = :date1m ");
    queryBuilder.append(")t4 on t.Ticker = t4.Ticker ");
    queryBuilder.append("where t.Ticker in :tickers ");

    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .setParameter(TICKERS, tickers)
        .setParameter(DATE1D, date1d)
        .setParameter(DATE5D, date5d)
        .setParameter("date1m", date1m)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }


  @Step("get data")
  public static List<HashMap<String, Object>> getMarketPrice(List<String> tickers, String date) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(";with view_vol_trading as ( SELECT Ticker , TradingDate  ");
    queryBuilder.append("		, ROUND(ClosePriceAdjusted, 0) as close_price ");
    queryBuilder.append("		, ROUND(ReferencePriceAdjusted, 0) AS ref_price ");
    queryBuilder.append("		FROM stx_mrk_HoseStock  ");
    queryBuilder.append("	UNION SELECT Ticker , TradingDate  ");
    queryBuilder.append("		, ROUND(ClosePriceAdjusted, 0) as close_price ");
    queryBuilder.append("		, ROUND(ReferencePriceAdjusted, 0)  AS ref_price ");
    queryBuilder.append("		FROM stx_mrk_HnxStock  ");
    queryBuilder.append("	UNION  SELECT Ticker , TradingDate  ");
    queryBuilder.append("		, ROUND(ClosePriceAdjusted, -2) as close_price ");
    queryBuilder.append("		, ROUND(ReferencePriceAdjusted, 0) as ref_price ");
    queryBuilder.append("		FROM stx_mrk_UpcomStock ) ");
    queryBuilder.append("select distinct Ticker ticker, ref_price, close_price ");
    queryBuilder.append("from view_vol_trading where TradingDate = :date ");
    queryBuilder.append("and Ticker in :tickers ");

    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .setParameter(TICKERS, tickers)
        .setParameter(DATE, date)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static List<HashMap<String, Object>> getIndexSectorValue(String industry, String date) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select DISTINCT date_report,  industry_index Ratio from market_price_marketcap WHERE icbCodeL2 = :industry ");
    queryBuilder.append("and index_number != 2 and date_report >=:dateStr ; ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .setParameter(INDUSTRY, industry)
        .setParameter("dateStr", date)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static List<HashMap<String, Object>> getIndustryIndexPresent(String industry, long now) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select seq_time, i_index industry_index from intraday_industry_index_by_1m WHERE icbCodeL2 = :industry ");
    queryBuilder.append(" and seq_time = (select max(seq_time) from intraday_industry_index_by_1m where FLOOR(seq_time /86400) *86400 = FLOOR( :latest /86400)*86400 ) ; ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .setParameter(INDUSTRY, industry)
        .setParameter("latest", now)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static List<HashMap<String, Object>> getIndexExchangeValue(String exchange, String date) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select * from ( ");
    queryBuilder.append("select t_market_code , i_seq_time , f_close_index indexPre ");
    queryBuilder.append("from intradayindex_by_1min where t_market_code =:exchange  ");
    queryBuilder.append("and i_seq_time = (select max(i_seq_time) from intradayindex_by_1min  where t_market_code =:exchange  ) ");
    queryBuilder.append(") t1 inner join  ");
    queryBuilder.append("(select DISTINCT date_report,  index_value indexPast from market_price_marketcap ");
    queryBuilder.append("where exchange_id =:exchangeId and index_number != 2 and date_report =:date ");
    queryBuilder.append(") t2 on 1= 1 ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .setParameter(EXCHANGE, MarketLeaderEntity.getMarketCode(exchange))
        .setParameter(EXCHANGE_ID, MarketLeaderEntity.getExchange(exchange))
        .setParameter(DATE, date)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static List<HashMap<String, Object>> getIndexExchangeValue(String exchange, String strDate, String strDate1d) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select t1.date_report, indexPre, indexPast from ");
    queryBuilder.append("(select DISTINCT date_report,  index_value indexPre from market_price_marketcap ");
    queryBuilder.append("where exchange_id =:exchangeId and index_number != 2 and date_report =:date ");
    queryBuilder.append(") t1 inner join  ");
    queryBuilder.append("(select DISTINCT date_report,  index_value indexPast from market_price_marketcap ");
    queryBuilder.append("where exchange_id =:exchangeId and index_number != 2 and date_report =:date1d ");
    queryBuilder.append(") t2 on 1= 1 ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .setParameter(EXCHANGE_ID, MarketLeaderEntity.getExchange(exchange))
        .setParameter(DATE, strDate)
        .setParameter(DATE1D, strDate1d)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static List<HashMap<String, Object>> getIndexExchangeClose(String exchange, String date) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select DISTINCT date_report,  index_value Ratio from market_price_marketcap ");
    queryBuilder.append("where exchange_id =:exchangeId and index_number != 2 and date_report >= :date ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .setParameter(EXCHANGE_ID, MarketLeaderEntity.getExchange(exchange))
        .setParameter(DATE, date)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }


  @Step("get buy on sell data")
  public static List<HashMap<String, Object>> getBuyOnSellTicker(List<String> ticker, long fromTime, long toTime) {
    StringBuilder query = new StringBuilder();
    query.append("select ticker, IFNULL(avg(bsp) , 0 ) bsp from ( ");
    query.append("select ticker, seq_day, bsp from ( ");
    query.append(" select distinct ticker, seq_time , FLOOR(seq_time /86400 ) *86400  seq_day , bs_acc_ratio as bsp ");
    query.append("	from buysellactive_by_1D babm  ");
    query.append("	WHERE ticker IN :tickers  ");
    query.append("	and seq_time >= FLOOR(:fromDate /86400) *86400 and  seq_time < FLOOR(:toDate /86400) *86400 ");
    query.append(" order by seq_time desc ) t  ");
    query.append("group by ticker, seq_day )tbl group by ticker ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(query.toString())
        .setParameter(TICKERS, ticker)
        .setParameter("fromDate", fromTime)
        .setParameter("toDate", toTime)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception e) {
      StepEventBus.getEventBus().testFailed(new AssertionError(e.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get bid on ask data")
  public static List<HashMap<String, Object>> getBidOnAskTicker(List<String> ticker, long fromTime, long toTime) {
    StringBuilder query = new StringBuilder();
    query.append("select ticker, IFNULL(avg(bop), 0) bop  from ( ");
    query.append("select ticker,seq_day , bop  from ( ");
    query.append("select distinct ticker,seq_time , FLOOR(seq_time /86400 ) *86400  seq_day,  over_bought /(over_bought+over_sold) as bop ");
    query.append("	from bid_ask_by_15min babm2  ");
    query.append("	WHERE ticker IN :tickers  ");
    query.append("	and seq_time >= :fromDate and  seq_time < FLOOR(:toDate /86400) *86400 ");
    query.append("	order by seq_time desc ) t group by ticker, seq_day ");
    query.append("	) tbl group by ticker ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(query.toString())
        .setParameter(TICKERS, ticker)
        .setParameter("fromDate", fromTime)
        .setParameter("toDate", toTime)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception e) {
      StepEventBus.getEventBus().testFailed(new AssertionError(e.getMessage()));
    }
    return new ArrayList<>();
  }
}
