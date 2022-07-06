package com.tcbs.automation.stockmarket;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@Builder

public class OverBidAskEntity {

  @Step("get bid ask from db")
  public static List<Map<String, Object>> getBidAsk(String ticker, long fromDate, long currentTimeCheck) {
//    if(ticker.startsWith("VN30F") || ticker.startsWith("GB10F") || ticker.startsWith("GB05F")){
//      return getBidAskDerivative(ticker, fromDate, currentTimeCheck);
//    }
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT 	");
    queryStringBuilder.append("	:ticker as ticker	");
    queryStringBuilder.append("	, seq_time,rn,exob,exos, sp	");
    queryStringBuilder.append("	, IF((exOverBought + exOverSold = 0), 0, ROUND(exOverBought/(exOverBought + exOverSold),4)) as obp	");
    queryStringBuilder.append("	, IF((exOverBought + exOverSold = 0), 0, ROUND(1 - exOverBought/(exOverBought + exOverSold),4)) as osp	");
    queryStringBuilder.append("FROM 	");
    queryStringBuilder.append("(	");
    queryStringBuilder.append("	select 	");
    queryStringBuilder.append("		seq_time,	");
    queryStringBuilder.append("		@row_number \\:= @row_number + 1 as rn,	");
    queryStringBuilder.append("		@overB \\:= IF(@row_number = 1, 0, @overB) as exob,	");
    queryStringBuilder.append("		@overS \\:= IF(@row_number = 1, 0, @overS) as exos,	");
    queryStringBuilder.append("		@overB \\:= IF(over_bought is null, @overB, over_bought) as exOverBought,	");
    queryStringBuilder.append("		@overS \\:= IF(over_sold is null, @overS, over_sold) as exOverSold,  (ap1 - bp1) as sp	");
    queryStringBuilder.append("	FROM	");
    queryStringBuilder.append("	(	");
//    queryStringBuilder.append("		SELECT tsr.time_series + :p_start_trading as seq_time, 	");
    queryStringBuilder.append("		SELECT seq_time, 	");
    queryStringBuilder.append("			over_bought, 	");
    queryStringBuilder.append("			over_sold, ap1, bp1	");
    queryStringBuilder.append("		FROM 	");
//    queryStringBuilder.append("		(	");
//    queryStringBuilder.append("			SELECT *	");
//    queryStringBuilder.append("			FROM  timeseries_1min_temp tsr 	");
//    queryStringBuilder.append("			WHERE (time_series + :p_start_trading) < :p_to_time	");
//    queryStringBuilder.append("		) tsr 	");
//    queryStringBuilder.append("		LEFT JOIN 	");
    queryStringBuilder.append("	 	(	");
    queryStringBuilder.append("	 		SELECT DISTINCT(seq_time), over_bought, over_sold, ask_price_1 as ap1, bid_price_1 as bp1	");
    queryStringBuilder.append("	 		FROM bid_ask_by_1min 	");
    queryStringBuilder.append("	 		WHERE ticker = :ticker AND seq_time >= :p_start_trading and seq_time < :p_to_time ");
    queryStringBuilder.append("	 		GROUP BY seq_time 	");
    queryStringBuilder.append("	 	) intraday	");
//    queryStringBuilder.append("	 	ON (tsr.time_series + :p_start_trading) = intraday.seq_time	");
    queryStringBuilder.append("	 		");
    queryStringBuilder.append("	) avgAccVolume	");
    queryStringBuilder.append("	cross join (select @row_number \\:= 0, @overB \\:=0, @overS \\:=0 ) params 	");
    queryStringBuilder.append("	ORDER BY seq_time ASC 	");
    queryStringBuilder.append(") t	;");
    try {
      return InvestorEntity.getHibernateEdition(ticker).getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("ticker", ticker)
        .setParameter("p_start_trading", fromDate)
        .setParameter("p_to_time", currentTimeCheck)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  private static List<Map<String, Object>> getBidAskDerivative(String ticker, long fromDate, long currentTimeCheck) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT 	");
    queryStringBuilder.append("	:ticker as ticker	");
    queryStringBuilder.append("	, seq_time,rn,exob,exos, sp	");
    queryStringBuilder.append("	, IF((exOverBought + exOverSold = 0), 0, ROUND(exOverBought/(exOverBought + exOverSold),4)) as obp	");
    queryStringBuilder.append("	, IF((exOverBought + exOverSold = 0), 0, ROUND(1 - exOverBought/(exOverBought + exOverSold),4)) as osp	");
    queryStringBuilder.append("FROM 	");
    queryStringBuilder.append("(	");
    queryStringBuilder.append("	select 	");
    queryStringBuilder.append("		seq_time,	");
    queryStringBuilder.append("		@row_number \\:= @row_number + 1 as rn,	");
    queryStringBuilder.append("		@overB \\:= IF(@row_number = 1, 0, @overB) as exob,	");
    queryStringBuilder.append("		@overS \\:= IF(@row_number = 1, 0, @overS) as exos,	");
    queryStringBuilder.append("		@overB \\:= IF(over_bought is null, @overB, over_bought) as exOverBought,	");
    queryStringBuilder.append("		@overS \\:= IF(over_sold is null, @overS, over_sold) as exOverSold,  (ap1 - bp1) as sp	");
    queryStringBuilder.append("	FROM	");
    queryStringBuilder.append("	(	");
    queryStringBuilder.append("		SELECT seq_time, 	");
    queryStringBuilder.append("			over_bought, 	");
    queryStringBuilder.append("			over_sold, ap1, bp1	");
    queryStringBuilder.append("		FROM 	");
    queryStringBuilder.append("	 	(	");
    queryStringBuilder.append("	 		SELECT DISTINCT(seq_time), over_bought, over_sold, ask_price_1 as ap1, bid_price_1 as bp1	");
    queryStringBuilder.append("	 		FROM bid_ask_by_1min 	");
    queryStringBuilder.append("	 		WHERE ticker = :ticker AND seq_time >= :p_start_trading and seq_time < (:p_to_time - 60)	");
    queryStringBuilder.append("	 		GROUP BY seq_time 	");
    queryStringBuilder.append("	 	) intraday	");
    queryStringBuilder.append("	) avgAccVolume	");
    queryStringBuilder.append("	cross join (select @row_number \\:= 0, @overB \\:=0, @overS \\:=0 ) params 	");
    queryStringBuilder.append("	ORDER BY seq_time ASC 	");
    queryStringBuilder.append(") t	;");
    try {
      return InvestorEntity.getHibernateEdition(ticker).getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("ticker", ticker)
        .setParameter("p_start_trading", fromDate)
        .setParameter("p_to_time", currentTimeCheck)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get avg 5 over bought from db")
  public static List<Map<String, Object>> getAvgOverBought(String ticker, String futureName, Long timeToday, List<Long> startTradingTime) {
    if (ticker.startsWith("VN30F") || ticker.startsWith("GB10F") || ticker.startsWith("GB05F")) {
      return getAvgOverBoughtDerivative(ticker, futureName, timeToday, startTradingTime);
    }
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" select :t_ticker as ticker, FROM_UNIXTIME(ts1 + :timeToday),  ");
    queryStringBuilder.append(" 	ts1 + :timeToday as seq_time, ");
    queryStringBuilder.append(" 	(t1ObRatio + t2ObRatio + t3ObRatio + t4ObRatio + t5ObRatio)/5 as aobp, ");
    queryStringBuilder.append(" 	(avsp1 + avsp2 + avsp3 + avsp4 + avsp5)/5 as avsp ");
    queryStringBuilder.append(" FROM ( ");
    queryStringBuilder.append("	SELECT tsr.time_series as ts1, ");
    queryStringBuilder.append("	@chis1 \\:= IF(t1.obRatio1 is null, @chis1, t1.obRatio1) as t1ObRatio, ");
    queryStringBuilder.append("	@chis2 \\:= IF(t2.obRatio2 is null, @chis2, t2.obRatio2) as t2ObRatio, ");
    queryStringBuilder.append("	@chis3 \\:= IF(t3.obRatio3 is null, @chis3, t3.obRatio3) as t3ObRatio, ");
    queryStringBuilder.append("	@chis4 \\:= IF(t4.obRatio4 is null, @chis4, t4.obRatio4) as t4ObRatio, ");
    queryStringBuilder.append("	@chis5 \\:= IF(t5.obRatio5 is null, @chis5, t5.obRatio5) as t5ObRatio, ");
    queryStringBuilder.append(" 	@avsp1 \\:= IF(t1.avsp1 is null, 0, t1.avsp1) as avsp1,  ");
    queryStringBuilder.append(" 	@avsp2 \\:= IF(t2.avsp2 is null, 0, t2.avsp2) as avsp2,  ");
    queryStringBuilder.append(" 	@avsp3 \\:= IF(t3.avsp3 is null, 0, t3.avsp3) as avsp3,  ");
    queryStringBuilder.append(" 	@avsp4 \\:= IF(t4.avsp4 is null, 0, t4.avsp4) as avsp4,  ");
    queryStringBuilder.append(" 	@avsp5 \\:= IF(t5.avsp5 is null, 0, t5.avsp5) as avsp5 ");
    queryStringBuilder.append(" 	FROM timeseries_1min_temp tsr LEFT JOIN (  ");
    queryStringBuilder.append(" 		SELECT DISTINCT(seq_time), over_bought/ (over_bought + over_sold) as obRatio1, (ask_price_1 - bid_price_1) as avsp1 	FROM bid_ask_by_1min ");
    queryStringBuilder.append(" 		where seq_time >= :timeStart1 AND seq_time < :timeToday AND ticker  = :t_ticker ");
    queryStringBuilder.append(" 		GROUP BY seq_time ) t1  ");
    queryStringBuilder.append(" 	ON (tsr.time_series + :timeStart1) = t1.seq_time  LEFT JOIN ( ");
    queryStringBuilder.append(" 		SELECT DISTINCT(seq_time), over_bought/ (over_bought + over_sold) as obRatio2, (ask_price_1 - bid_price_1) as avsp2 	FROM bid_ask_by_1min ");
    queryStringBuilder.append(" 		where seq_time >= :timeStart2 AND seq_time < :timeStart1 AND ticker  = :t_ticker ");
    queryStringBuilder.append(" 		GROUP BY seq_time ) t2  ");
    queryStringBuilder.append(" 	ON (tsr.time_series + :timeStart2) = t2.seq_time LEFT JOIN (  ");
    queryStringBuilder.append(" 		SELECT DISTINCT(seq_time), over_bought/ (over_bought + over_sold) as obRatio3, (ask_price_1 - bid_price_1) as avsp3		FROM bid_ask_by_1min ");
    queryStringBuilder.append(" 		where seq_time >= :timeStart3 AND seq_time < :timeStart2 AND ticker  = :t_ticker ");
    queryStringBuilder.append(" 		GROUP BY seq_time ) t3  ");
    queryStringBuilder.append(" 	ON (tsr.time_series + :timeStart3) = t3.seq_time LEFT JOIN ( ");
    queryStringBuilder.append(" 		SELECT DISTINCT(seq_time), over_bought/ (over_bought + over_sold) as obRatio4, (ask_price_1 - bid_price_1) as avsp4		FROM bid_ask_by_1min ");
    queryStringBuilder.append(" 		where seq_time >= :timeStart4 AND seq_time < :timeStart3 AND ticker  = :t_ticker ");
    queryStringBuilder.append(" 		GROUP BY seq_time	) t4  ");
    queryStringBuilder.append(" 	ON (tsr.time_series + :timeStart4) = t4.seq_time LEFT JOIN (  ");
    queryStringBuilder.append(" 		SELECT DISTINCT(seq_time), over_bought/ (over_bought + over_sold) as obRatio5, (ask_price_1 - bid_price_1) as avsp5		FROM bid_ask_by_1min ");
    queryStringBuilder.append(" 		where seq_time >= :timeStart5 AND seq_time < :timeStart4 AND ticker  = :t_ticker ");
    queryStringBuilder.append(" 		GROUP BY seq_time ) t5  ");
    queryStringBuilder.append(" 	ON (tsr.time_series + :timeStart5) = t5.seq_time ");
    queryStringBuilder.append("	 cross join (select @chis1 \\:=0, @chis2 \\:=0, @chis3 \\:=0, @chis4 \\:=0, @chis5 \\:=0) params ");
    queryStringBuilder.append(" ) avgOB ");
    queryStringBuilder.append(" ORDER BY seq_time ASC; ");

    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("t_ticker", ticker)
        .setParameter("timeToday", timeToday)
        .setParameter("timeStart1", startTradingTime.get(0))
        .setParameter("timeStart2", startTradingTime.get(1))
        .setParameter("timeStart3", startTradingTime.get(2))
        .setParameter("timeStart4", startTradingTime.get(3))
        .setParameter("timeStart5", startTradingTime.get(4))
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  private static List<Map<String, Object>> getAvgOverBoughtDerivative(String ticker, String futureName, Long timeToday, List<Long> startTradingTime) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" select :t_ticker as ticker, FROM_UNIXTIME(ts1 + :timeStart0) ,	ts1 + :timeStart0 as seq_time, ");
    queryStringBuilder.append("      	(t1ObRatio + t2ObRatio + t3ObRatio + t4ObRatio + t5ObRatio)/5 as aobp, ");
    queryStringBuilder.append("  	    (avsp1 + avsp2 + avsp3 + avsp4 + avsp5)/5 as avsp ");
    queryStringBuilder.append("  FROM (  ");
    queryStringBuilder.append(" 	 SELECT tsr.time_series as ts1,  ");
    queryStringBuilder.append("   IFNULL(t1.obRatio1, 0) as t1ObRatio, ");
    queryStringBuilder.append(" 	 IFNULL(t2.obRatio2, 0) as t2ObRatio,  ");
    queryStringBuilder.append(" 	 IFNULL(t3.obRatio3, 0) as t3ObRatio,  ");
    queryStringBuilder.append(" 	 IFNULL(t4.obRatio4, 0) as t4ObRatio,  ");
    queryStringBuilder.append(" 	 IFNULL(t5.obRatio5, 0) as t5ObRatio,  ");
    queryStringBuilder.append(" 	 IFNULL(t1.avsp1, 0) as avsp1,  ");
    queryStringBuilder.append(" 	 IFNULL(t2.avsp2, 0) as avsp2,  ");
    queryStringBuilder.append(" 	 IFNULL(t3.avsp3, 0) as avsp3,  ");
    queryStringBuilder.append(" 	 IFNULL(t4.avsp4, 0) as avsp4,  ");
    queryStringBuilder.append(" 	 IFNULL(t5.avsp5, 0) as avsp5 ");
    queryStringBuilder.append("  	FROM timeseries_1min_temp tsr LEFT JOIN (   ");
    queryStringBuilder.append("  		SELECT DISTINCT(seq_time), over_bought/ (over_bought + over_sold) as obRatio1 , (ask_price_1 - bid_price_1) as avsp1	FROM bid_ask_by_1min  ");
    queryStringBuilder.append("  		where seq_time >= :timeStart1 AND seq_time < :timeStart0  and ticker like 'VN30%' and func_idata_future_name_by_ticker_and_time(ticker, seq_time)  = :t_ticker ");
    queryStringBuilder.append("  		GROUP BY seq_time ) t1   ");
    queryStringBuilder.append("  	ON (tsr.time_series + :timeStart1) = t1.seq_time  LEFT JOIN (  ");
    queryStringBuilder.append("  		SELECT DISTINCT(seq_time), over_bought/ (over_bought + over_sold) as obRatio2, (ask_price_1 - bid_price_1) as avsp2 	FROM bid_ask_by_1min  ");
    queryStringBuilder.append("  		where seq_time >= :timeStart2 AND seq_time < :timeStart1 and ticker like 'VN30%' and func_idata_future_name_by_ticker_and_time(ticker, seq_time)  = :t_ticker ");
    queryStringBuilder.append("  		GROUP BY seq_time ) t2   ");
    queryStringBuilder.append("  	ON (tsr.time_series + :timeStart2) = t2.seq_time  LEFT JOIN (   ");
    queryStringBuilder.append("  		SELECT DISTINCT(seq_time), over_bought/ (over_bought + over_sold) as obRatio3, (ask_price_1 - bid_price_1) as avsp3		FROM bid_ask_by_1min  ");
    queryStringBuilder.append("  		where seq_time >= :timeStart3 AND seq_time < :timeStart2 and ticker like 'VN30%' and func_idata_future_name_by_ticker_and_time(ticker, seq_time)  = :t_ticker ");
    queryStringBuilder.append("  		GROUP BY seq_time ) t3   ");
    queryStringBuilder.append("  	ON (tsr.time_series + :timeStart3) = t3.seq_time  LEFT JOIN (  ");
    queryStringBuilder.append("  		SELECT DISTINCT(seq_time), over_bought/ (over_bought + over_sold) as obRatio4, (ask_price_1 - bid_price_1) as avsp4		FROM bid_ask_by_1min  ");
    queryStringBuilder.append("  		where seq_time >= :timeStart4 AND seq_time < :timeStart3 and ticker like 'VN30%' and func_idata_future_name_by_ticker_and_time(ticker, seq_time)  = :t_ticker ");
    queryStringBuilder.append("  		GROUP BY seq_time	) t4   ");
    queryStringBuilder.append("  	ON (tsr.time_series + :timeStart4) = t4.seq_time  LEFT JOIN (   ");
    queryStringBuilder.append("  		SELECT DISTINCT(seq_time), over_bought/ (over_bought + over_sold) as obRatio5, (ask_price_1 - bid_price_1) as avsp5		FROM bid_ask_by_1min  ");
    queryStringBuilder.append("  		where seq_time >= :timeStart5 AND seq_time < :timeStart4 and ticker like 'VN30%' and func_idata_future_name_by_ticker_and_time(ticker, seq_time)  = :t_ticker ");
    queryStringBuilder.append("  		GROUP BY seq_time ) t5   ");
    queryStringBuilder.append("  	ON (tsr.time_series + :timeStart5) = t5.seq_time  ");
    queryStringBuilder.append("  ) avgOB  ");
    queryStringBuilder.append("  ORDER BY seq_time ASC;  ");

    try {
      return FuturesMarket.futuresMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString()) //TODO doi db
        .setParameter("t_ticker", futureName)
        .setParameter("timeStart0", timeToday)
        .setParameter("timeStart1", startTradingTime.get(0))
        .setParameter("timeStart2", startTradingTime.get(1))
        .setParameter("timeStart3", startTradingTime.get(2))
        .setParameter("timeStart4", startTradingTime.get(3))
        .setParameter("timeStart5", startTradingTime.get(4))
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}
