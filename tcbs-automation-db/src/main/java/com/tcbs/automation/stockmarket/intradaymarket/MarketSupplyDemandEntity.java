package com.tcbs.automation.stockmarket.intradaymarket;

import com.tcbs.automation.stockmarket.Stockmarket;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MarketSupplyDemandEntity {
  @Step("select data")
  public static List<HashMap<String, Object>> getMarketSupplyDemand1M(String exchangeId, String industryId, long fromTime, long endTime) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("select * from market_supply_demand_1min ");
    queryStringBuilder.append("where exchange =:exchange_id and icbCodeL2 =:idLevel2 ");
    queryStringBuilder.append("and seq_time >= :fromDate and seq_time < :toDate ");
    queryStringBuilder.append("order by seq_time asc;");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("exchange_id", exchangeId)
        .setParameter("idLevel2", industryId)
        .setParameter("fromDate", fromTime)
        .setParameter("toDate", endTime)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("select data")
  public static List<HashMap<String, Object>> getMarketSupplyDemand(String exchangeId, String industryId, long from, long to, String type) {
    if (StringUtils.equals("1M", type)) {
      return getMarketSupplyDemand1D(exchangeId, industryId, from, to);
    }
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("select * ");
    queryStringBuilder.append("from market_supply_demand_1min ");
    queryStringBuilder.append("where exchange =:exchange_id and icbCodeL2 =:idLevel2 ");
    queryStringBuilder.append("and seq_time >= :from and seq_time <= :to ");
//    queryStringBuilder.append("and seq_time >= (SELECT FLOOR(MAX(seq_time)/86400) * 86400 FROM market_supply_demand_1min where exchange = :exchange_id ) ");
    queryStringBuilder.append("order by seq_time asc;");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("exchange_id", exchangeId)
        .setParameter("idLevel2", industryId)
        .setParameter("from", from)
        .setParameter("to", to)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("select data")
  public static List<HashMap<String, Object>> getMarketSupplyDemand1D(String exchangeId, String industryId, long from, long to) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("select * ");
    queryStringBuilder.append("from market_supply_demand_1day ");
    queryStringBuilder.append("where exchange =:exchange and industryCode =:industry ");
    queryStringBuilder.append("and seq_time >= :fromTime and seq_time <= :toTime order by seq_time asc;");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("exchange", exchangeId)
        .setParameter("industry", industryId)
        .setParameter("fromTime", from)
        .setParameter("toTime", to)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("select data 1M")
  public static List<HashMap<String, Object>> getCalcBuySellActive(List<String> tickers, long fromDate, long toDate) {
    StringBuilder queryBuilder = new StringBuilder();
//    queryBuilder.append("SELECT seq_time , IFNULL(sum(total_bu_vol), 0) as total_bu_vol, IFNULL(sum(total_sd_vol), 0) as total_sd_vol ");
//    queryBuilder.append("from ( ");
//    queryBuilder.append("	SELECT * from buysellactive_acc_by_1min babm  ");
//    queryBuilder.append("	WHERE ticker IN :tickers ");
//    queryBuilder.append("	and seq_time >= :fromDate and seq_time < :toDate ");
//    queryBuilder.append(") t1 ");
//    queryBuilder.append("GROUP by seq_time ");
//    queryBuilder.append("ORDER by seq_time; ");

    queryBuilder.append("select * from ( ");
    queryBuilder.append("	select FROM_UNIXTIME(tmt.time_series + FLOOR(UNIX_TIMESTAMP()/86400)*86400+7200),  ");
    queryBuilder.append("		cast(tmt.time_series + FLOOR(UNIX_TIMESTAMP()/86400)*86400+7200 as CHAR) as seq_time, ");
    queryBuilder.append("	 IFNULL(total_bu_vol, 0) as total_bu_vol, IFNULL(total_sd_vol, 0) as total_sd_vol ");
    queryBuilder.append("	from timeseries_1min_temp tmt  ");
    queryBuilder.append("	left join ( ");
    queryBuilder.append("		select seq_time, sum(total_bu_vol) as total_bu_vol, sum(total_sd_vol) as total_sd_vol  ");
    queryBuilder.append("		from buysellactive_acc_by_1min bs ");
    queryBuilder.append("		where ticker IN :tickers  ");
    queryBuilder.append("		and seq_time >= FLOOR(UNIX_TIMESTAMP()/86400)*86400+7200  ");
    queryBuilder.append("		group by seq_time ");
    queryBuilder.append("	) t1 on (tmt.time_series + FLOOR(UNIX_TIMESTAMP()/86400)*86400+7200) = t1.seq_time ");
    queryBuilder.append("	order by seq_time desc ");
    queryBuilder.append(") tbt where seq_time >= :fromDate and seq_time < :toDate ;  ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("tickers", tickers)
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("select data 1M")
  public static List<HashMap<String, Object>> getCalcBuySellActive1D(List<String> tickers, long date) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("	select FLOOR(:date/86400)*86400 as seq_time,  IFNULL(total_bu_vol, 0) as total_bu_vol, IFNULL(total_sd_vol, 0) as total_sd_vol ");
    queryBuilder.append("	from ( ");
    queryBuilder.append("		select seq_time, sum(total_bu_vol_acc) as total_bu_vol, sum(total_sd_vol_acc) as total_sd_vol ");
    queryBuilder.append("		from buysellactive_by_1D bs ");
    queryBuilder.append("		where ticker IN :tickers  ");
    queryBuilder.append("		and seq_time = FLOOR(:date/86400)*86400 ");
    queryBuilder.append("		group by seq_time ");
    queryBuilder.append("	) t1  order by seq_time desc ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("tickers", tickers)
        .setParameter("date", date)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get latest date from db job")
  public static Long getLatestDateFromDbJob(String exchange, String industry) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("select FLOOR(MAX(seq_time)/86400) * 86400 AS seq_time  ");
    queryStringBuilder.append(" from market_supply_demand_1day ");
    queryStringBuilder.append("where exchange = :exchangeId and industryCode =:industry ");
    queryStringBuilder.append("order by seq_time desc limit 1;");
    try {
      List<HashMap<String, Object>> resultList = Stockmarket.stockMarketConnection.getSession()
        .createNativeQuery(queryStringBuilder.toString())
        .setParameter("exchangeId", exchange)
        .setParameter("industry", industry)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();

      if (resultList.isEmpty() || resultList.get(0).get("seq_time") == null) {
        return 0L;
      }
      return Long.parseLong(resultList.get(0).get("seq_time").toString());
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return 0L;
  }
}
