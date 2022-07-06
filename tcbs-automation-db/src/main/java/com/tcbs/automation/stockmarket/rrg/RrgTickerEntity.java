package com.tcbs.automation.stockmarket.rrg;

import com.tcbs.automation.stockmarket.Stockmarket;
import com.tcbs.automation.tca.TcAnalysis;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RrgTickerEntity {

  @Step("get rrg data")
  public static List<HashMap<String, Object>> getRrgTicker(List<String> ticker, String fromTime, String toTime, String lang, String type) {
    StringBuilder query = new StringBuilder();
    query.append("select tbl.*, t5.RS_Norm, t5.RS_Momen  from ( ");
    query.append("	select distinct CONCAT(CASE WHEN DATEPART(ISO_WEEK, t1.TradingDate) > 50 AND MONTH(t1.TradingDate) = 1 THEN YEAR(t1.TradingDate) - 1 ");
    query.append("				  WHEN DATEPART(ISO_WEEK, t1.TradingDate) = 1 AND MONTH(t1.TradingDate) = 12 THEN YEAR(t1.TradingDate) + 1 ");
    query.append("				  ELSE YEAR(t1.TradingDate) END ");
    query.append("			   , FORMAT(DATEPART(ISO_WEEK, t1.TradingDate), '00') ");
    query.append("		) as weekYear ");
    query.append("		, t1.TradingDate as tradingDate, t1.Bourse  as exchange, t1.Ticker ");
    query.append("		, IIF(:lang = 'EN', t4.en_OrganName, t4.OrganName) as organName , t3.IdLevel2  ");
    query.append("		, IIF(:lang = 'EN', t3.NameEnl2, t3.NameL2) as idName, (t1.ClosePriceAdjusted / t1.ClosePrice) * t1.ReferencePrice as refPrice ");
    query.append("		, t1.ClosePriceAdjusted  as price, ((t1.ClosePrice/ t1.ReferencePrice) - 1) *100 as priceChange "); // == ClosePriceAdjusted/ReferencePriceAdjusted
    query.append("		, t1.TotalMatchVolume as vol, RTD11/1000000000 as mkCap, t6.mkCap as market_cap ");
    query.append("	from Smy_dwh_stox_MarketPrices t1 ");
    query.append("	left join stx_rto_RatioTTMDaily t2 on t1.Ticker = t2.Ticker and t1.TradingDate = t2.TradingDate  ");
    query.append("	left join ( ");
    query.append("		select Ticker as T, RTD11 as mkCap , ROW_NUMBER() over(PARTITION BY Ticker order by TradingDate desc) as rn from stx_rto_RatioTTMDaily tv ");
    query.append("		where tv.Ticker in :Ticker and tv.TradingDate >= :from_date and  tv.TradingDate < :to_date ");
    query.append("	) t6 on t1.Ticker = t6.T and t6.rn = 1 ");
    query.append("	left join ( select Ticker, IcbCode, en_OrganName, OrganName from [stx_cpf_Organization]) t4 on t1.Ticker = t4.Ticker ");
    query.append("	left join view_idata_industry t3 on t4.IcbCode = cast(t3.IdLevel4 as varchar ) ");
    query.append("	where t1.Ticker in :Ticker ");
    query.append("	and t1.TradingDate >= DATEADD(month, -1, :from_date ) and t1.TradingDate < :to_date ");
    query.append("	and (t2.Status is null or t2.Status = 1) ");
    query.append(") tbl  ");
    if (StringUtils.equalsIgnoreCase(type, "1Y")) {
      query.append("left join (select DISTINCT * from smy_stox_industry_rrg_weekly_ticker ");
      query.append("  where ETLRunDateTime = (select max(ETLRunDateTime) from smy_stox_industry_rrg_weekly_ticker) ");
      query.append(") t5 on tbl.Ticker = t5.Ticker and tbl.IdLevel2 = t5.IdLevel2 and tbl.weekYear = t5.WeekYear ");
    } else {
      query.append("left join ( select DISTINCT * from smy_stox_industry_rrg_ticker ");
      query.append(" where ETLRunDateTime = (select max(ETLRunDateTime) from smy_stox_industry_rrg_ticker) ");
      query.append(") t5 on tbl.Ticker = t5.Ticker and tbl.IdLevel2 = t5.IdLevel2 and tbl.TradingDate = t5.DateReport  ");
      query.append("where RS_Norm is not null and RS_Momen is not null ");
    }
    query.append("order by tbl.TradingDate; ");

    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("Ticker", ticker)
        .setParameter("from_date", fromTime)
        .setParameter("to_date", toTime)
        .setParameter("lang", lang)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception e) {
      StepEventBus.getEventBus().testFailed(new AssertionError(e.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get buy on sell data")
  public static List<HashMap<String, Object>> getBuyOnSellTicker(List<String> ticker, String fromTime, String toTime) {
    StringBuilder query = new StringBuilder();
    query.append("select distinct ticker as Ticker , FLOOR(seq_time/86400)*86400 as seq_day, ");
    query.append("	AVG(total_bu_vol/(total_bu_vol+total_sd_vol)) as bsp, YEARWEEK(FROM_UNIXTIME(seq_time) ,1) as wy");
    query.append("	from buysellactive_acc_by_15min babm  ");
    query.append("	WHERE ticker IN :ticker  ");
    query.append("	and seq_time >= UNIX_TIMESTAMP(DATE_ADD(:fromDate, INTERVAL -1 MONTH)) and  seq_time <= UNIX_TIMESTAMP(:toDate)  ");
    query.append("	group by ticker, FLOOR(seq_time/86400)*86400 ");

    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(query.toString())
        .setParameter("ticker", ticker)
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
  public static List<HashMap<String, Object>> getBidOnAskTicker(List<String> ticker, String fromTime, String toTime) {
    StringBuilder query = new StringBuilder();
    query.append("select distinct FLOOR(seq_time/86400)*86400 as seq_day, ticker as Ticker, ");
    query.append("	avg(over_bought /(over_bought+over_sold)) as bop, YEARWEEK(FROM_UNIXTIME(seq_time) ,1) as wy ");
    query.append("	from bid_ask_by_15min babm2  ");
    query.append("	WHERE ticker IN :ticker  ");
    query.append("	and seq_time >= UNIX_TIMESTAMP(DATE_ADD(:fromDate, INTERVAL -1 MONTH)) and  seq_time <= UNIX_TIMESTAMP(:toDate)  ");
    query.append("	group by ticker, FLOOR(seq_time/86400)*86400; ");

    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(query.toString())
        .setParameter("ticker", ticker)
        .setParameter("fromDate", fromTime)
        .setParameter("toDate", toTime)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception e) {
      StepEventBus.getEventBus().testFailed(new AssertionError(e.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get tickers")
  public static List<HashMap<String, Object>> getTickers(String ticker, String fromDate, String toDate) {
    StringBuilder query = new StringBuilder();
    query.append("select DISTINCT t1.Ticker from Smy_dwh_stox_MarketPrices t1 ");
    query.append("left join stx_rto_RatioTTMDaily t2 on t1.Ticker = t2.Ticker and t1.TradingDate = t2.TradingDate ");
    query.append("left join ( select Ticker, IcbCode, en_OrganName, OrganName from [stx_cpf_Organization]) t4 on t1.Ticker = t4.Ticker ");
    query.append("left join view_idata_industry t3 on t4.IcbCode = cast(t3.IdLevel4 as varchar ) ");
    query.append("where t1.TradingDate between :from_Date and :to_Date ");
    query.append("and t1.Ticker IN (select value from STRING_SPLIT(:t_ticker,',')) ");

    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("t_ticker", ticker)
        .setParameter("from_Date", fromDate)
        .setParameter("to_Date", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception e) {
      StepEventBus.getEventBus().testFailed(new AssertionError(e.getMessage()));
    }
    return new ArrayList<>();
  }
}
