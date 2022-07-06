package com.tcbs.automation.stockmarket.future;

import com.tcbs.automation.stockmarket.FuturesMarket;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FuturesDeviationVn30Entity {

  @Step("get from db")
  public static List<HashMap<String, Object>> getDeviationFutureVN30(String ticker, String futureName, Long timeToday) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("select :t_ticker as ticker, FROM_UNIXTIME(ts1 + :timeStart0),    ");
    queryStringBuilder.append("  	ts1 + :timeStart0 as seq_time, diff ");
    queryStringBuilder.append("  FROM (   ");
    queryStringBuilder.append(" 	SELECT tsr.time_series as ts1,  ");
    queryStringBuilder.append(" 	@close0 \\:= IF(t.f_close_price is null, @close0, t.f_close_price) as close0,  ");
    queryStringBuilder.append("	 @index0 \\:= IF(t.f_close_index is null, @index0, t.f_close_index) as index0,  ");
    queryStringBuilder.append(" 	if(@close0 is null or @index0 is null, null, @close0- @index0) as diff ");
    queryStringBuilder.append("  	FROM timeseries_1min_temp tsr LEFT JOIN (    ");
    queryStringBuilder.append("  		select t1.i_seq_time as seq_time, t1.f_close_price , t2.f_close_index  ");
    queryStringBuilder.append("  		from intraday_by_1min t1 left join intradayindex_by_1min t2  ");
    queryStringBuilder.append("  		on t1.i_seq_time  = t2.i_seq_time  ");
    queryStringBuilder.append("  		where t1.i_seq_time  >= :timeStart0 and t2.t_market_code ='VN30' ");
    queryStringBuilder.append("  		and t1.t_ticker like 'VN30%' and func_idata_future_name_by_ticker_and_time(t1.t_ticker, t1.i_seq_time)  = :t_ticker  ");
    queryStringBuilder.append("  		GROUP BY t1.i_seq_time ) t ");
    queryStringBuilder.append("  	ON (tsr.time_series + :timeStart0) = t.seq_time ");
    queryStringBuilder.append(" 	 cross join (select @close0 \\:=null, @index0 \\:= null) params ");
    queryStringBuilder.append("  ) avgOB ");
    queryStringBuilder.append("  ORDER BY seq_time ASC;   ");

    try {
      return FuturesMarket.futuresMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("t_ticker", futureName)
        .setParameter("timeStart0", timeToday)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get from db")
  public static List<HashMap<String, Object>> getDeviationFutureVN30Month(String futureName) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("select t1.i_seq_time as seq_time, t1.t_ticker, (t1.f_close_price - t2.f_close_index)  as diff  ");
    queryStringBuilder.append("from ( ");
    queryStringBuilder.append("	SELECT t_ticker, i_seq_time , f_close_price from intraday_by_1D  ");
    queryStringBuilder.append("	where t_ticker like 'VN30%' and func_idata_future_name_by_ticker_and_time(t_ticker , i_seq_time) = :future_name ");
    queryStringBuilder.append("	and i_seq_time <  UNIX_TIMESTAMP(CURRENT_DATE())  ");
    queryStringBuilder.append(") t1 ");
    queryStringBuilder.append("left join ( ");
    queryStringBuilder.append("	SELECT t_market_code, floor(i_seq_time/86400)*86400 as seq_time, f_close_index from intradayindex_by_1D  ");
    queryStringBuilder.append("	where t_market_code ='VN30' and i_seq_time < UNIX_TIMESTAMP(CURRENT_DATE())  ");
    queryStringBuilder.append(") t2 ");
    queryStringBuilder.append("on t1.i_seq_time = t2.seq_time ");
    queryStringBuilder.append("order by t1.i_seq_time desc limit 59; "); //60 diem
    try {
      return FuturesMarket.futuresMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("future_name", futureName)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get from db")
  public static List<HashMap<String, Object>> calcAvg5d(String futureName, Long timeToday, List<Long> startTradingTime) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("select i_seq_time, sum(diff)/5 as avg from ( ");
    queryStringBuilder.append("	select t1.i_seq_time, f_close_price - f_close_index as diff  ");
    queryStringBuilder.append("	from (select i_seq_time, t_ticker, f_close_price  ");
    queryStringBuilder.append("		from intraday_by_1D a  ");
    queryStringBuilder.append("		WHERE t_ticker like 'VN30%' and func_idata_future_name_by_ticker_and_time(t_ticker, i_seq_time) =:future_name ");
    queryStringBuilder.append("	) t1 left join ( ");
    queryStringBuilder.append("		select i_seq_time, f_close_index  ");
    queryStringBuilder.append("		from intradayindex_by_1D a  ");
    queryStringBuilder.append("		WHERE t_market_code ='VN30' ");
    queryStringBuilder.append("	) t2 on t1.i_seq_time = t2.i_seq_time ");
    queryStringBuilder.append("	where t1.i_seq_time < :timeStart0 and t1.t1.i_seq_time >=  FLOOR( :timeStart5/86400)*86400 ");
    queryStringBuilder.append("	order by t1.i_seq_time desc ");
    queryStringBuilder.append(") tbl; ");

    try {
      return FuturesMarket.futuresMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("future_name", futureName)
        .setParameter("timeStart0", timeToday)
        .setParameter("timeStart5", startTradingTime.get(4))
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
