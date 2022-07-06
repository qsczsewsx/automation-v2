package com.tcbs.automation.stockmarket.flowmap;

import com.tcbs.automation.stockmarket.Stockmarket;
import com.tcbs.automation.stockmarket.intradaymarket.MarketLeaderEntity;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.HashMap;
import java.util.List;

public class FlowMapEntity {

  @Step("get data db")
  public static HashMap<String, Object> getMarketBreadthIntraday(String exchange, String industry, long from) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT exchange , icbCodeL2 , seq_time , sideways , advanced , declined  FROM market_watch_breadth_investor_1min ");
    queryStringBuilder.append("WHERE exchange = :exchange AND icbCodeL2 = :industry ");
    queryStringBuilder.append("AND seq_time = (select max(seq_time) from market_watch_breadth_investor_1min where seq_time >= FLOOR(:from / 86400) *86400 and  exchange = :exchange AND icbCodeL2 = :industry) ");

    try {
      List<HashMap<String, Object>> rs = Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("exchange", exchange)
        .setParameter("industry", industry)
        .setParameter("from", from)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      if (!rs.isEmpty()) {
        return rs.get(0);
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new HashMap<>();
  }

  @Step("get data db")
  public static HashMap<String, Object> getMarketBreadth1M(String exchange, String industry, String date1M, String date1d) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("select sum(declined) declined, sum(sideways) sideways, sum(advanced) advanced from ( ");
    queryStringBuilder.append("	select t1.ticker, closeT1 , closeT2 ,  ");
    queryStringBuilder.append("	case when closeT1 < closeT2 then 1 else 0 end as declined, ");
    queryStringBuilder.append("	case when closeT1 = closeT2 then 1 else 0 end as sideways, ");
    queryStringBuilder.append("	case when closeT1 > closeT2 then 1 else 0 end as advanced ");
    queryStringBuilder.append("	from ( ");
    queryStringBuilder.append("		select DISTINCT date_report , ticker , close_price as closeT1 from market_price_marketcap mpm  ");
    queryStringBuilder.append("		where date_report = (select max(date_report) from market_price_marketcap where date_report <= :date1d)  ");
    queryStringBuilder.append("		and (:exchange = 'ALL' or (exchange_id =:exchange_id and index_number != 2)) and (icbCodeL2 = 'ALL' or icbCodeL2 =:indsutry ) ");
    queryStringBuilder.append("	)t1 left join ( ");
    queryStringBuilder.append("		select DISTINCT date_report , ticker , close_price as closeT2 from market_price_marketcap mpm  ");
    queryStringBuilder.append("		where date_report = (select min(date_report) from market_price_marketcap where date_report >= :date1m) ");
    queryStringBuilder.append("		and (:exchange = 'ALL' or (exchange_id =:exchange_id and index_number != 2)) and (icbCodeL2 = 'ALL' or icbCodeL2 =:indsutry ) ");
    queryStringBuilder.append("	) t2 on t1.ticker = t2.ticker  ");
    queryStringBuilder.append(") tbl ");

    try {
      List<HashMap<String, Object>> rs = Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("exchange", exchange)
        .setParameter("exchange_id", MarketLeaderEntity.getExchange(exchange))
        .setParameter("industry", industry)
        .setParameter("date1d", date1d)
        .setParameter("date1m", date1M)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      if (!rs.isEmpty()) {
        return rs.get(0);
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new HashMap<>();
  }
}
