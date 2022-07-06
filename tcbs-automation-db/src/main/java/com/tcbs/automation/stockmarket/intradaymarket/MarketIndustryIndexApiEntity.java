package com.tcbs.automation.stockmarket.intradaymarket;

import com.tcbs.automation.stockmarket.Stockmarket;
import com.tcbs.automation.tca.TcAnalysis;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MarketIndustryIndexApiEntity {

  private static final String EXCHANGE = "exchange";
  private static final String INDUSTRY = "industry";

  @Step("get data")
  public static List<HashMap<String, Object>> getIndustryIndexOneMinute(String industry, String currentDate, String exchange) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT icbCodeL2, i_index as i_index, total_volume, seq_time, exchange FROM intraday_industry_index_by_1m iiibm WHERE icbCodeL2 = :industry ")
      .append(" and DATE_FORMAT(FROM_UNIXTIME(seq_time), '%Y-%m-%d') = :currentDate and exchange = :exchange ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .setParameter(INDUSTRY, industry)
        .setParameter("currentDate", currentDate)
        .setParameter(EXCHANGE, exchange)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static List<HashMap<String, Object>> getIndustryIndexFifteenMinutes(String industry, String currentDate, String exchange) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT i_index as i, total_volume as v, seq_time FROM intraday_industry_index_by_15m iiibm WHERE icbCodeL2 = :industry ")
      .append(" and DATE_FORMAT(FROM_UNIXTIME(seq_time), '%Y-%m-%d') <= :currentDate and exchange = :exchange ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .setParameter(INDUSTRY, industry)
        .setParameter("currentDate", currentDate)
        .setParameter(EXCHANGE, exchange)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static List<HashMap<String, Object>> getIndustryIndexByMonth(String fromDate, String toDate, String exchange, Integer pExchange) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT FROM_UNIXTIME(i_seq_time) AS date_report, f_close_index AS i , i_volume AS v  ")
      .append("FROM intradayindex_by_1D ibd  ")
      .append("WHERE i_seq_time >= UNIX_TIMESTAMP(:fromDate)  and i_seq_time < UNIX_TIMESTAMP(:toDate)  ")
      .append("AND t_market_code = :p_exchange	 ")
      .append("UNION ALL ")
      .append("select CONVERT_TZ(FROM_UNIXTIME(seq_time), '+00:00', '+7:00')  , i, v from ( ")
      .append("	select exchange , i_index i, acc_total_volume v , seq_time from intraday_industry_index_by_1m a where exchange = :exchange ")
      .append("	and seq_time = (select max(seq_time ) from intraday_industry_index_by_1m  where exchange = :exchange and seq_time >= UNIX_TIMESTAMP(:toDate))  ")
      .append(") t1 ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setParameter("p_exchange", MarketLeaderEntity.getMarketCode(exchange))
        .setParameter(EXCHANGE, exchange)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static List<HashMap<String, Object>> getIndustryIndexByMonthTypeAll(String fromDate, String toDate, String industry) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT ReportDate as da, SectorIndex as idx, v vol FROM  tbl_idata_pe_sector_index tipsi left join ( ")
      .append(" SELECT TradingDate, sum(TotalMatchVolume) as v FROM view_HOSE_HNX_UPCOM_trading WHERE TradingDate >= :fromDate AND TradingDate < :toDate ")
      .append(" and Ticker in ( SELECT Ticker FROM stx_cpf_Organization sco INNER JOIN view_idata_industry vii on sco.IcbCode = vii.IdLevel4 WHERE IdLevel2 = :industry ) ")
      .append(" GROUP BY TradingDate ) aa ON aa.TradingDate = tipsi.ReportDate ")
      .append(" WHERE ReportDate >= :fromDate AND ReportDate < :toDate AND IdLevel2 = :industry ");

    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setParameter(INDUSTRY, industry)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static HashMap<String, Object> getIndustryIndexByMonthTypeAllToday(String toDate, String industry) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder
      .append("select CONVERT_TZ(FROM_UNIXTIME(seq_time) , '+00:00', '+7:00') as da, i idx , v vol from ( ")
      .append("	select icbCodeL2 , i_index i, seq_time  from intraday_industry_index_by_1m a where icbCodeL2 = :industry  ")
      .append("	and seq_time = (select max(seq_time ) from intraday_industry_index_by_1m  where icbCodeL2 = :industry and seq_time >= UNIX_TIMESTAMP(:date))  ")
      .append(") t1 left join ( ")
      .append("	select icbCodeL2 , sum( total_volume) v  from intraday_industry_index_by_1m a where icbCodeL2 = :industry  ")
      .append("	and seq_time >= UNIX_TIMESTAMP(:date) group by icbCodeL2 ")
      .append(") t2 on t1.icbCodeL2 = t2.icbCodeL2  ");

    try {
      List<HashMap<String, Object>> rs = Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .setParameter("date", toDate)
        .setParameter(INDUSTRY, industry)
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
