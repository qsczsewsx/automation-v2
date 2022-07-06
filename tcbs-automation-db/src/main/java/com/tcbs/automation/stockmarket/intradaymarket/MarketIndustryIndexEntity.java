package com.tcbs.automation.stockmarket.intradaymarket;

import com.tcbs.automation.stockmarket.Stockmarket;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MarketIndustryIndexEntity {

  @Step("get data")
  public static HashMap<String, Object> getLatestDate1m() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select max(seq_time) as latest from intraday_industry_index_by_1m ;");
    try {
      List<HashMap<String, Object>> rs = Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
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

  @Step("get data")
  public static List<HashMap<String, Object>> getData1MinWithClosePrice(List<String> tickers, String timeframe, long from, long date1d) {
    String table = "intraday_by_1min";
    if (StringUtils.equals("15", timeframe)) {
      table = "intraday_by_15min";
    }
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select distinct t2.ticker ticker, t3.i_seq_time, IFNULL(t3.f_close_price, t2.close_price_1d ) as close_price, t2.close_price_1d ref_price, ");
    queryBuilder.append("IFNULL(t3.i_volume, 0) vol,  IFNULL(t3.value,0) val, IFNULL(t2.share_issue, 0) share_issue ");
    queryBuilder.append("from (select * from market_watch_volatility_1d where seq_time = (SELECT MAX(seq_time) FROM market_watch_volatility_1d) ) t2 ");
    queryBuilder.append("left join ").append(table).append(" t3 on t2.ticker = t3.t_ticker ");
    queryBuilder.append("WHERE t2.ticker IN :tickers ");
    queryBuilder.append("and t3.i_seq_time >= (FLOOR(:from /86400) *86400 +2*3600) and t3.i_seq_time <= :from and t3.i_seq_time %86400 != 16200 ;"); //tu 9h00 -> 15h00
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .setParameter("tickers", tickers)
        .setParameter("from", from)
//        .setParameter("date1d", date1d)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static List<HashMap<String, Object>> getDataIndustryIndex(String industry, String exchange, String timeframe, long from) {
    String table = "intraday_industry_index_by_1m";
    if (StringUtils.equals("15", timeframe)) {
      table = "intraday_industry_index_by_15m";
    }
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select seq_time, icbCodeL2 , i_index , total_volume, exchange from ").append(table);
    queryBuilder.append(" where icbCodeL2 =:industry and exchange =:exchange and seq_time >= :from order by seq_time desc; ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .setParameter("industry", industry)
        .setParameter("exchange", exchange)
        .setParameter("from", from)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static List<HashMap<String, Object>> getIndexSectorValueMaxDate(String industry, String latest) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select DISTINCT date_report , industry_index  Ratio from market_price_marketcap a WHERE icbCodeL2 = :industry ");
    queryBuilder.append("and date_report = (select max(date_report) from market_price_marketcap where date_report < cast( :latest as date) ) order by date_report desc ; ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .setParameter("industry", industry)
        .setParameter("latest", latest)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static List<HashMap<String, Object>> getDataIntradayIndex(String exchange, String timeframe, long from) {
    String table = "intradayindex_by_1min";
    if (StringUtils.equals("15", timeframe)) {
      table = "intradayindex_by_15min";
    }
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select distinct FROM_UNIXTIME(i_seq_time), t_market_code, i_seq_time , f_close_index close_i , i_volume vol, i_value ");
    queryBuilder.append("from ").append(table).append(" ibm  where t_market_code =:code and i_seq_time >= FLOOR(:from /86400) *86400 and i_seq_time <= :from order by i_seq_time desc");

    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .setParameter("code", MarketLeaderEntity.getMarketCode(exchange))
        .setParameter("from", from)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static HashMap<String, Object> getMarketChange1D(String industry, String exchange) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select *, FROM_UNIXTIME(seq_time)  from market_index_change_1D micd ");
    queryBuilder.append("where exchange =:exchange and icbCodeL2 =:icbCodeL2 and seq_time = (select max(seq_time) from market_index_change_1D)");
    try {
      List<HashMap<String, Object>> list = Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .setParameter("icbCodeL2", industry)
        .setParameter("exchange", exchange)
        .getResultList();
      if (!list.isEmpty()) {
        return list.get(0);
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new HashMap<>();
  }

}
