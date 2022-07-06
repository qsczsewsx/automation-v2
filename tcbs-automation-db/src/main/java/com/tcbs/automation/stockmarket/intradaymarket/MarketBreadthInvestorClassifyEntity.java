package com.tcbs.automation.stockmarket.intradaymarket;

import com.tcbs.automation.stockmarket.Stockmarket;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.stockmarket.intradaymarket.MarketLeaderEntity.getMarketCode;

@Data
@NoArgsConstructor
@Builder

public class MarketBreadthInvestorClassifyEntity {

  @Step("get market breadth from db")
  public static List<HashMap<String, Object>> getMarketBreadth(String exchange, String industry, String type, long fromDate, long toDate) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT * FROM ( ");
    queryStringBuilder.append("       SELECT exchange, icbCodeL2 as industry, seq_time, advanced, declined, sideways ");
    queryStringBuilder.append("       , sheep_bu, sheep_sd, wolf_bu, wolf_sd, shark_bu, shark_sd, total_bu, total_sd, 0 as isToday ");
    if ("1d".equals(type)) {
      queryStringBuilder.append("       FROM market_watch_breadth_investor_1min ");
      queryStringBuilder.append("       WHERE seq_time >= :from_date AND seq_time >= :to_date AND exchange = :exchange_id AND icbCodeL2 = :industry");
    } else {
      queryStringBuilder.append("       FROM market_watch_breadth_investor_1D ");
      queryStringBuilder.append("       WHERE seq_time >= :from_date AND seq_time < FLOOR(:to_date /86400) *86400 AND exchange = :exchange_id AND icbCodeL2 = :industry");
      queryStringBuilder.append("       union all ");
      queryStringBuilder.append("       SELECT exchange, icbCodeL2 as industry, seq_time, advanced, declined, sideways ");
      queryStringBuilder.append("       , sheep_bu, sheep_sd, wolf_bu, wolf_sd, shark_bu, shark_sd, total_bu, total_sd, 1 as isToday ");
      queryStringBuilder.append("       FROM market_watch_breadth_investor_1min ");
      queryStringBuilder.append("       WHERE seq_time = (select max(seq_time) from market_watch_breadth_investor_1min where exchange = :exchange_id AND icbCodeL2 = :industry AND seq_time >= :to_date) ");
      queryStringBuilder.append("       AND exchange = :exchange_id AND icbCodeL2 = :industry");
    }
    queryStringBuilder.append(") t1 order by seq_time asc; ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("exchange_id", exchange)
        .setParameter("industry", industry)
        .setParameter("from_date", fromDate)
        .setParameter("to_date", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get investor classification from db")
  public static List<HashMap<String, Object>> getInvestorClassify(String exchange, String industry, String type, long fromDate, long toDate) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT * FROM ( ");
    queryStringBuilder.append("       SELECT exchange, icbCodeL2 as industry, seq_time ");
    queryStringBuilder.append("       , sheep_bu, sheep_sd, wolf_bu, wolf_sd, shark_bu, shark_sd, total_bu, total_sd, 0 as isToday  ");
    if ("1d".equals(type)) {
      queryStringBuilder.append("       FROM market_watch_breadth_investor_1min mwb1 ");
      queryStringBuilder.append("       WHERE seq_time BETWEEN :from_Date AND :to_Date AND exchange = :exchange AND icbCodeL2 = :industry");
    } else if ("1w".equals(type)) {
      queryStringBuilder.append("       FROM market_watch_breadth_investor_15min ");
      queryStringBuilder.append("       WHERE seq_time >= :from_Date AND seq_time <= :to_Date AND exchange = :exchange AND icbCodeL2 = :industry");
    } else {
      queryStringBuilder.append("       FROM market_watch_breadth_investor_1D ");
      queryStringBuilder.append("       WHERE seq_time >= :from_Date AND seq_time < (FLOOR(:to_Date/86400) * 86400) AND exchange = :exchange AND icbCodeL2 = :industry");
      queryStringBuilder.append("       union all ");
      queryStringBuilder.append("       SELECT exchange, icbCodeL2 as industry, max(seq_time) as seq_time, ");
      queryStringBuilder.append("       sum(sheep_bu), sum(sheep_sd), sum(wolf_bu), sum(wolf_sd), sum(shark_bu), sum(shark_sd), sum(total_bu), sum(total_sd), 1 as isToday ");
      queryStringBuilder.append("       FROM market_watch_breadth_investor_1min mwb1 ");
      queryStringBuilder.append("       WHERE seq_time > (FLOOR(:to_Date/86400) * 86400) AND seq_time <= :to_Date ");
      queryStringBuilder.append("       AND exchange = :exchange AND icbCodeL2 = :industry ");
    }
    queryStringBuilder.append(") tbl; ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("exchange", exchange)
        .setParameter("industry", industry)
        .setParameter("from_Date", fromDate)
        .setParameter("to_Date", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get investor classification from db")
  public static List<HashMap<String, Object>> getInvestorAcc(String exchange, String industry, String type, long fromDate, long toDate) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT IFNULL(sum(sheep_bu),0) as sheep_bu, IFNULL(sum(sheep_sd),0) as sheep_sd, ");
    queryStringBuilder.append("		IFNULL(sum(wolf_bu),0) as wolf_bu, IFNULL(sum(wolf_sd),0) as wolf_sd, ");
    queryStringBuilder.append("		IFNULL(sum(shark_bu),0) as shark_bu, IFNULL(sum(shark_sd),0) as shark_sd, ");
    queryStringBuilder.append("		min(fromDate) as fromDate, max(toDate) as toDate FROM (");
    if (!StringUtils.equals("1d", type)) {
      queryStringBuilder.append("SELECT IFNULL(sum(sheep_bu),0) as sheep_bu, IFNULL(sum(sheep_sd),0) as sheep_sd, ");
      queryStringBuilder.append("		IFNULL(sum(wolf_bu),0) as wolf_bu, IFNULL(sum(wolf_sd),0) as wolf_sd, ");
      queryStringBuilder.append("		IFNULL(sum(shark_bu),0) as shark_bu, IFNULL(sum(shark_sd),0) as shark_sd, ");
      queryStringBuilder.append("			min(seq_time) as fromDate, max(seq_time) as toDate ");
      queryStringBuilder.append("	from market_watch_breadth_investor_1D mwbid ");
      queryStringBuilder.append("	where exchange = :exchange and icbCodeL2 = :industryId ");
      queryStringBuilder.append("	and seq_time >= :from_date and seq_time < (FLOOR(:to_date/86400) * 86400) ");
      queryStringBuilder.append("	UNION ALL ");
    }
    queryStringBuilder.append("	SELECT 	IFNULL(sum(sheep_bu_acc),0) as sheep_bu, IFNULL(sum(sheep_sd_acc),0) as sheep_sd,");
    queryStringBuilder.append("			IFNULL(sum(wolf_bu_acc),0) as wolf_bu, IFNULL(sum(wolf_sd_acc),0) as wolf_sd, ");
    queryStringBuilder.append("			IFNULL(sum(shark_bu_acc),0) as shark_bu, IFNULL(sum(shark_sd_acc) ,0)as shark_sd, ");
    queryStringBuilder.append("		 min(fromDate) as fromDate, max(toDate) as toDate FROM (");
    queryStringBuilder.append("		SELECT IFNULL(sum(sheep_bu),0) as sheep_bu_acc , IFNULL(sum(sheep_sd),0) as sheep_sd_acc ,");
    queryStringBuilder.append("		IFNULL(sum(wolf_bu),0) as wolf_bu_acc , IFNULL(sum(wolf_sd),0) as wolf_sd_acc ,");
    queryStringBuilder.append("		IFNULL(sum(shark_bu),0) as shark_bu_acc , IFNULL(sum(shark_sd),0) as shark_sd_acc, ");
    if (StringUtils.equals("1d", type)) {
      queryStringBuilder.append("		min(seq_time) as fromDate, max(seq_time) as toDate ");
    } else {
      queryStringBuilder.append("		max(seq_time) as fromDate, max(seq_time) as toDate ");
    }
    queryStringBuilder.append("		FROM market_watch_breadth_investor_1min ");
    queryStringBuilder.append("		WHERE exchange = :exchange and icbCodeL2 = :industryId ");
    queryStringBuilder.append("		AND seq_time >= ((FLOOR(:to_date/86400) * 86400) + :startLO)  AND seq_time <= :to_date ");
    queryStringBuilder.append("	) tmp ");
    queryStringBuilder.append(") tlb;");
    try {
      NativeQuery sql = Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("exchange", exchange)
        .setParameter("industryId", industry)
        .setParameter("to_date", toDate)
        .setParameter("startLO", StringUtils.equals("HOSE", exchange) || StringUtils.equals("VN30", exchange) ? 8100 : 0); //HOSE 9:15

      if (!StringUtils.equals("1d", type)) {
        sql.setParameter("from_date", fromDate);
      }
      return sql
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get from db job")
  public static List<HashMap<String, Object>> getDbJob(String exchange, String industry, String type, long fromDate, long toDate) {
    String table = "";
    if (StringUtils.equals("1d", type)) {
      table = "market_watch_breadth_investor_1min";
    } else if (StringUtils.equals("1w", type)) {
      table = "market_watch_breadth_investor_15min";
    } else {
      table = "market_watch_breadth_investor_1D";
    }

    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("select distinct exchange, idlevel2 , icbCodeL2, advanced, declined, sideways, ");
    queryStringBuilder.append("sheep_bu, sheep_sd, wolf_bu , wolf_sd, shark_bu, shark_sd, total_bu, total_sd");
    if (StringUtils.equals("1M", type)) {
      queryStringBuilder.append(", FLOOR(seq_time/86400)*86400 as seq_time ");
    } else {
      queryStringBuilder.append(", seq_time ");
    }
    queryStringBuilder.append(" from ").append(table).append(" ");
    queryStringBuilder.append("where exchange = :exchangeId and icbCodeL2 = :industryId ");
    queryStringBuilder.append("and seq_time >= :from_Date and seq_time < :to_Date ");
    queryStringBuilder.append("order by seq_time desc;");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("exchangeId", exchange)
        .setParameter("industryId", industry)
        .setParameter("from_Date", fromDate)
        .setParameter("to_Date", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get from db job")
  public static List<HashMap<String, Object>> getInvestor(List<String> tickers, String type, long fromDate, long toDate) {
    if (StringUtils.equals("1M", type)) {
      return getInvestor1D(tickers, fromDate, toDate);
    }
    String table = StringUtils.equals("1d", type) ? "investor_classifier_by_1min" : "investor_classifier_by_15min";
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT distinct  seq_time ");
    queryStringBuilder.append(", IFNULL(sum(sheep_bu_val),0) as sheep_bu_val, IFNULL(sum(sheep_sd_val),0) as sheep_sd_val ");
    queryStringBuilder.append(", IFNULL(sum(wolf_bu_val),0) as wolf_bu_val, IFNULL(sum(wolf_sd_val),0) as wolf_sd_val ");
    queryStringBuilder.append(", IFNULL(sum(shark_bu_val),0) as shark_bu_val, IFNULL(sum(shark_sd_val),0) as shark_sd_val, ");
    queryStringBuilder.append("IFNULL(sum(sheep_bu_val) +sum(wolf_bu_val)+ sum(shark_bu_val) ,0) as total_bu_val,  ");
    queryStringBuilder.append("IFNULL(sum(sheep_sd_val) + sum(wolf_sd_val) + sum(shark_sd_val),0) as total_sd_val ");
    queryStringBuilder.append("from ( ");
    queryStringBuilder.append("	SELECT * ");
    queryStringBuilder.append("	from ").append(table).append(" icbm  ");
    queryStringBuilder.append("	where ticker IN :lstTickers and seq_time >= :fromTime and seq_time < :toTime ");
    queryStringBuilder.append(") tbl ");
    queryStringBuilder.append("group by seq_time ; ");

    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("lstTickers", tickers)
        .setParameter("fromTime", fromDate)
        .setParameter("toTime", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get from db job")
  public static List<HashMap<String, Object>> getInvestor1D(List<String> tickers, long fromDate, long toDate) {
    StringBuilder queryStringBuilder = new StringBuilder();
//    queryStringBuilder.append("select FLOOR(seq_time/86400)*86400 as seq_time,  ");
//    queryStringBuilder.append("IFNULL(sum(sheep_bu_val),0) as sheep_bu_val, IFNULL(sum(sheep_sd_val),0) as sheep_sd_val, ");
//    queryStringBuilder.append("IFNULL(sum(wolf_bu_val),0) as wolf_bu_val, IFNULL(sum(wolf_sd_val),0) as wolf_sd_val, ");
//    queryStringBuilder.append("IFNULL(sum(shark_bu_val),0) as shark_bu_val, IFNULL(sum(shark_sd_val),0) as shark_sd_val, ");
//    queryStringBuilder.append("IFNULL(sum(total_bu_val) ,0) as total_bu_val, IFNULL(sum(total_sd_val),0) as total_sd_val ");
//    queryStringBuilder.append("from (");
    queryStringBuilder.append(" SELECT seq_day as seq_time,  ");
    queryStringBuilder.append("IFNULL(sum(sheep_bu_val),0) as sheep_bu_val, IFNULL(sum(sheep_sd_val),0) as sheep_sd_val, ");
    queryStringBuilder.append("IFNULL(sum(wolf_bu_val),0) as wolf_bu_val, IFNULL(sum(wolf_sd_val),0) as wolf_sd_val, ");
    queryStringBuilder.append("IFNULL(sum(shark_bu_val),0) as shark_bu_val, IFNULL(sum(shark_sd_val),0) as shark_sd_val, ");
    queryStringBuilder.append("IFNULL(sum(sheep_bu_val) +sum(wolf_bu_val)+ sum(shark_bu_val) ,0) as total_bu_val,  ");
    queryStringBuilder.append("IFNULL(sum(sheep_sd_val) + sum(wolf_sd_val) + sum(shark_sd_val),0) as total_sd_val ");
    queryStringBuilder.append(" from( ");
    queryStringBuilder.append("	 SELECT FLOOR(seq_time/86400)*86400 seq_day, icbm.* ");
    queryStringBuilder.append("	 from investor_classifier_by_15min icbm  ");
    queryStringBuilder.append("	 where ticker IN :tickers and seq_time >= :fromTime and seq_time <= :toTime ");
    queryStringBuilder.append(" )tbl ");
    queryStringBuilder.append(" group by seq_day  ");
//    queryStringBuilder.append(")t2 ; ");

    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("tickers", tickers)
        .setParameter("fromTime", fromDate)
        .setParameter("toTime", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static List<HashMap<String, Object>> getIndexFilter(List<String> tickers, String type, long fromDate, long toDate) {
    StringBuilder queryStringBuilder = new StringBuilder();
    if (StringUtils.equals("1d", type)) {
      queryStringBuilder.append("SELECT sum(tang) as advances, sum(giam) as declines, sum(dung) as sideways, ");
      queryStringBuilder.append(" seq_time as seq_time FROM (	 ");
      queryStringBuilder.append("	SELECT (tmt.time_series + FLOOR(UNIX_TIMESTAMP()/86400) *86400 +7200) as seq_time, ");
      queryStringBuilder.append(strCommon());
      queryStringBuilder.append("	FROM timeseries_1min_temp tmt left join (	");
      queryStringBuilder.append("		select ticker from tca_vw_idata_index_industry_exchange_v2 iiie ");
      queryStringBuilder.append("		WHERE ticker IN :tickers and exchangeId IN (0, 1, 3) ");
      queryStringBuilder.append("		and etlrundatetime = (select max(etlrundatetime) from tca_vw_idata_index_industry_exchange_v2 i ) ");
      queryStringBuilder.append("	) t2  on 1 = 1 ");
      queryStringBuilder.append("	left join ( select t_ticker , i_seq_time, open_price_change_ori ");
      queryStringBuilder.append("		from intraday_by_1min ");
      queryStringBuilder.append("	)t1 on t2.ticker = t1.t_ticker and (tmt.time_series + FLOOR(UNIX_TIMESTAMP()/86400) *86400 +7200) = t1.i_seq_time ");
      queryStringBuilder.append(") tbl WHERE seq_time >= :fromDate and seq_time < :toDate  group by seq_time; ");
    } else {
      queryStringBuilder = getIndexFilterYear(type);
    }
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
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

  private static String strCommon() {
    return "case when open_price_change_ori > 0 then 1 else 0 end as tang, " +
      "case when open_price_change_ori < 0 then 1 else 0 end as giam, " +
      "case when open_price_change_ori is null or open_price_change_ori = 0 then 1 else 0 end as dung ";
  }

  private static StringBuilder getIndexFilterYear(String type) {
    StringBuilder queryStringBuilder = new StringBuilder();
    if (StringUtils.equals("1M", type)) {
      queryStringBuilder.append("SELECT sum(tang) as advances, sum(giam) as declines, sum(dung) as sideways, FLOOR(seq_time/86400)*86400 as seq_time	 ");
      queryStringBuilder.append("FROM (	SELECT i_seq_time as seq_time, ").append(strCommon());
      queryStringBuilder.append("	FROM (select ticker from tca_vw_idata_index_industry_exchange_v2 iiie ");
      queryStringBuilder.append("		WHERE ticker IN :tickers ");
      queryStringBuilder.append("	and exchangeId IN (0, 1, 3) 	and etlrundatetime = (select max(etlrundatetime) from tca_vw_idata_index_industry_exchange_v2 ) ");
      queryStringBuilder.append("	) t2 left join (");
      queryStringBuilder.append("	SELECT * FROM ( ");
      queryStringBuilder.append("		 select t_ticker , i_seq_time, open_price_change_ori ");
      queryStringBuilder.append("		 from intraday_by_60min ");
      queryStringBuilder.append("		 WHERE t_ticker IN :tickers ");
      queryStringBuilder.append("		 and i_seq_time >= :fromDate and i_seq_time <= :toDate ");
      queryStringBuilder.append("		 order by i_seq_time desc ");
      queryStringBuilder.append("		) t2	group by t_ticker ");
      queryStringBuilder.append("	)t1 on t1.t_ticker = t2.ticker ");
      queryStringBuilder.append(") tbl; ");
    } else {
      queryStringBuilder.append("SELECT sum(tang) as advances, sum(giam) as declines, sum(dung) as sideways, seq_time as seq_time FROM (	 ");
      queryStringBuilder.append("	SELECT (tmt.time_series + FLOOR(UNIX_TIMESTAMP()/86400) *86400 +7200) as seq_time, ");
      queryStringBuilder.append(strCommon());
      queryStringBuilder.append("	FROM timeseries_15min_temp_full tmt left join (	");
      queryStringBuilder.append("		select ticker from tca_vw_idata_index_industry_exchange_v2 iiie ");
      queryStringBuilder.append("		WHERE ticker IN :tickers and exchangeId IN (0, 1, 3) ");
      queryStringBuilder.append("		and etlrundatetime = (select max(etlrundatetime)) ");
      queryStringBuilder.append("	) t2  on 1 = 1 ");
      queryStringBuilder.append("	left join ( ");
      queryStringBuilder.append("		select t_ticker , i_seq_time, open_price_change_ori ");
      queryStringBuilder.append("		from intraday_by_15min where t_ticker IN :tickers ");
      queryStringBuilder.append("	)t1 on (tmt.time_series + FLOOR(UNIX_TIMESTAMP()/86400) *86400 +7200) = t1.i_seq_time ");
      queryStringBuilder.append(") tbl WHERE seq_time >= :fromDate and seq_time < :toDate  group by seq_time; ");
    }
    return queryStringBuilder;
  }

  public static List<HashMap<String, Object>> getIndexAll(String exchange, String type, long fromDate, long toDate) {
    String marketCode = getMarketCode(exchange);
    if (StringUtils.isEmpty(marketCode)) {
      return new ArrayList<>();
    }

    String table;
    if (StringUtils.equals("1d", type)) {
      table = "intradayindex_by_1min";
    } else if (StringUtils.equals("1w", type)) {
      table = "intradayindex_by_15min";
    } else {
      table = "intradayindex_by_60min";
    }
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("select distinct t_market_code, advances, declines, sideways,  ");
    if (StringUtils.equals("1M", type)) {
      queryStringBuilder.append("FLOOR(i_seq_time/86400)*86400 as seq_time ");
    } else {
      queryStringBuilder.append("i_seq_time as seq_time ");
    }
    queryStringBuilder.append("from ").append(table).append(" ");
    queryStringBuilder.append("WHERE t_market_code = :code ");
    queryStringBuilder.append("and i_seq_time >= :fromDate and i_seq_time <= :toDate ");
    if (StringUtils.equals("1M", type)) {
      queryStringBuilder.append("and i_seq_time = (select max(i_seq_time) from intradayindex_by_60min where  t_market_code = :code and i_seq_time >= :fromDate and i_seq_time <= :toDate) ");
    }
    queryStringBuilder.append(" ; ");

    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setParameter("code", marketCode)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get latest date from db job")
  public static Long getLatestDateFromDbJob(String exchange, String type) {
    String table = "";
    if (StringUtils.equals("1d", type)) {
      table = "market_watch_breadth_investor_1min";
    } else if (StringUtils.equals("1w", type)) {
      table = "market_watch_breadth_investor_15min";
    } else {
      table = "market_watch_breadth_investor_1D";
    }

    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("select exchange ");
    if (StringUtils.equals("1M", type)) {
      queryStringBuilder.append(", FLOOR(seq_time/86400) * 86400 as seq_time ");
    } else {
      queryStringBuilder.append(", seq_time ");
    }
    queryStringBuilder.append(" from ").append(table).append(" ");
    queryStringBuilder.append("where exchange = :exchangeId ");
    queryStringBuilder.append("order by seq_time desc limit 1;");
    try {
      List<HashMap<String, Object>> resultList = Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("exchangeId", exchange)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();

      if (resultList.isEmpty()) {
        return 0L;
      }

      return Long.parseLong(resultList.get(0).get("seq_time").toString());
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return 0L;
  }
}
