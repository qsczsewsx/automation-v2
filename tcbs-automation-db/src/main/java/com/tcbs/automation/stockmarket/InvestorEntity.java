package com.tcbs.automation.stockmarket;

import com.tcbs.automation.HibernateEdition;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@NoArgsConstructor
@Builder

public class InvestorEntity {

  @Step("get investor intraday from db")
  public static List<HashMap<String, Object>> getInvestorsIntraday(String ticker, String fromDate, String currentTimeCheck) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT ticker, sheep_bu, sheep_sd, wolf_bu, wolf_sd, shark_bu, shark_sd, seq_time, ");
    queryStringBuilder.append("sheep_bu_acc, sheep_sd_acc, wolf_bu_acc, wolf_sd_acc, shark_bu_acc, shark_sd_acc FROM investor_classifier_by_1min ");
    queryStringBuilder.append("WHERE ticker = :ticker_f AND seq_time >= :start_trading AND seq_time < :to_time and ( seq_time % 86400) != 16200 ");
    queryStringBuilder.append("ORDER BY seq_time ASC; ");
    try {
      return getHibernateEdition(ticker).getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("ticker_f", ticker)
        .setParameter("start_trading", fromDate)
        .setParameter("to_time", currentTimeCheck)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  private static boolean checkDerivative(String ticker) {
    return ticker.startsWith("VN30F") || ticker.startsWith("GB10F") || ticker.startsWith("GB05F");
  }

  @Step("get investor 15min from db")
  public static List<HashMap<String, Object>> getInvestors15Min(String ticker, String fromDate, String currentTimeCheck, String futureName) {
    if (checkDerivative(ticker)) {
      return getInvestorsIntradayDerivative(ticker, fromDate, currentTimeCheck, futureName);
    }
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT distinct ticker, sheep_bu, sheep_sd, wolf_bu, wolf_sd, shark_bu, shark_sd, seq_time, ");
    queryStringBuilder.append("sheep_bu_acc, sheep_sd_acc, wolf_bu_acc, wolf_sd_acc, shark_bu_acc, shark_sd_acc FROM investor_classifier_by_15min ");
    queryStringBuilder.append("WHERE ticker = :ticker AND seq_time >= :p_start_trading AND seq_time < :p_to_time ");
    queryStringBuilder.append("ORDER BY seq_time ASC; ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
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

  private static List<HashMap<String, Object>> getInvestorsIntradayDerivative(String ticker, String fromDate, String currentTimeCheck, String futureName) {
    if (StringUtils.isEmpty(futureName)) {
      return new ArrayList<>();
    }
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT * ");
    queryStringBuilder.append(" FROM ( ");
    queryStringBuilder.append(" 	SELECT ticker, sheep_bu, sheep_sd, wolf_bu, wolf_sd, shark_bu,shark_sd, seq_time ");
    queryStringBuilder.append(" 	, sheep_bu_acc, sheep_sd_acc, wolf_bu_acc, wolf_sd_acc, shark_bu_acc, shark_sd_acc FROM ( ");
    queryStringBuilder.append(" 		SELECT ticker, sheep_bu, sheep_sd, wolf_bu, wolf_sd, shark_bu, shark_sd, seq_time, FLOOR(seq_time/86400)*86400 as seq_day ");
    queryStringBuilder.append(" 		, sheep_bu_acc, sheep_sd_acc, wolf_bu_acc, wolf_sd_acc, shark_bu_acc, shark_sd_acc ");
    queryStringBuilder.append(" 		FROM investor_classifier_by_15min  ");
    queryStringBuilder.append(" 		WHERE func_idata_future_name_by_ticker_and_time(ticker, seq_time)  = :future_name ");
    queryStringBuilder.append(" 		AND seq_time >= :fromDate AND seq_time <  FLOOR(:toDate/86400)*86400  ");
    queryStringBuilder.append(" 		ORDER BY seq_time ASC )t1 ");
//    queryStringBuilder.append(" 	 inner join ( ");
//    queryStringBuilder.append(" 		SELECT seq_time as i_seq_time, ticker as t_ticker, future_name  ");
//    queryStringBuilder.append(" 		FROM future_by_1D fbd  ");
//    queryStringBuilder.append(" 		WHERE seq_time < FLOOR(:toDate/86400)*86400  ");
//    queryStringBuilder.append(" 		AND future_name  = :future_name )t2 ");
//    queryStringBuilder.append(" 	ON t1.ticker = t2.t_ticker AND t1.seq_day = t2.i_seq_time ");
    queryStringBuilder.append(" 	UNION ALL  ");
    queryStringBuilder.append(" 	SELECT ticker, sheep_bu, sheep_sd, wolf_bu, wolf_sd, shark_bu, 	shark_sd, seq_time ");
    queryStringBuilder.append(" 	,sheep_bu_acc, sheep_sd_acc, wolf_bu_acc, wolf_sd_acc, shark_bu_acc, shark_sd_acc ");
    queryStringBuilder.append(" 	FROM investor_classifier_by_15min ");
    queryStringBuilder.append(" 	WHERE ticker = :t_ticker ");
    queryStringBuilder.append(" 	AND seq_time >= FLOOR(:toDate/86400)*86400  ");
    queryStringBuilder.append(" 	AND seq_time < :toDate ");
    queryStringBuilder.append(" 	ORDER BY seq_time ASC ");
    queryStringBuilder.append(" )tb ");
    queryStringBuilder.append(" ORDER BY seq_time ASC; ");
    try {
      return FuturesMarket.futuresMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString()) //TODO doi db
        .setParameter("t_ticker", ticker)
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", currentTimeCheck)
        .setParameter("future_name", futureName)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get investor intraday acc from db")
  public static List<HashMap<String, Object>> getInvestorsAcc(String ticker, String fromDate, String currentTimeCheck, String futureName) {
    if (checkDerivative(ticker)) {
      return getInvestorsAccDerivative(ticker, fromDate, currentTimeCheck, futureName);
    }
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT FROM_UNIXTIME(seq_day), seq_time, seq_day, ticker, sum(sheep_bu) as sheep_bu, sum(sheep_sd) as sheep_sd, sum(wolf_bu) as wolf_bu, ");
    queryStringBuilder.append("sum(wolf_sd) as wolf_sd, sum(shark_bu) as shark_bu, sum(shark_sd) as shark_sd ");
    queryStringBuilder.append("FROM ( ");
    queryStringBuilder.append("  SELECT DISTINCT(seq_time) as  seq_time, FLOOR(seq_time/86400)*86400 as seq_day, ");
    queryStringBuilder.append("  ticker, sheep_bu as  sheep_bu, sheep_sd as  sheep_sd, wolf_bu as  wolf_bu, ");
    queryStringBuilder.append("  wolf_sd as  wolf_sd, shark_bu as  shark_bu, shark_sd as  shark_sd ");
    queryStringBuilder.append("  FROM investor_classifier_by_15min ");
    queryStringBuilder.append("  WHERE ticker = :ticker AND seq_time >= :p_start_trading AND seq_time <= :p_to_time ");
    queryStringBuilder.append("  ORDER BY seq_time desc ");
    queryStringBuilder.append(") tbl group by seq_day; ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
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

  private static List<HashMap<String, Object>> getInvestorsAccDerivative(String ticker, String fromDate, String currentTimeCheck, String futureName) {
    if (StringUtils.isEmpty(futureName)) {
      return new ArrayList<>();
    }
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT * FROM (  ");
    queryStringBuilder.append(" 	SELECT FROM_UNIXTIME(seq_time), seq_time, seq_day, ticker, sum(sheep_bu) as sheep_bu, sum(sheep_sd) as sheep_sd, ");
    queryStringBuilder.append("     	sum(wolf_bu) as wolf_bu, sum(wolf_sd) as wolf_sd, sum(shark_bu) as shark_bu, sum(shark_sd) as shark_sd FROM ( ");
    queryStringBuilder.append("     	SELECT seq_time, seq_day, ticker, sheep_bu, sheep_sd, wolf_bu, wolf_sd, shark_bu, shark_sd 	FROM (  ");
    queryStringBuilder.append("      		SELECT DISTINCT(seq_time) as seq_time, FLOOR(seq_time/86400)*86400 as seq_day,   ");
    queryStringBuilder.append("         			ticker, sheep_bu,  sheep_sd, wolf_bu, wolf_sd, shark_bu, shark_sd ");
    queryStringBuilder.append("      		FROM investor_classifier_by_15min   ");
    queryStringBuilder.append("      		WHERE func_idata_future_name_by_ticker_and_time(ticker, seq_time)  = :future_name AND seq_time >= :fromDate AND seq_time <  FLOOR(:toDate/86400)*86400  ");
    queryStringBuilder.append("      	)t1  ");
//    queryStringBuilder.append("      	inner join (  ");
//    queryStringBuilder.append("      		SELECT seq_time as i_seq_time, ticker as t_ticker, future_name   ");
//    queryStringBuilder.append("      		FROM future_by_1D fbd   ");
//    queryStringBuilder.append("      		WHERE seq_time >= FLOOR(:fromDate/86400)*86400  ");
//    queryStringBuilder.append("      		AND seq_time < FLOOR(:toDate/86400)*86400   ");
//    queryStringBuilder.append("      		AND future_name  = :future_name	)t2  ");
//    queryStringBuilder.append("      	ON t1.ticker = t2.t_ticker AND t1.seq_day = t2.i_seq_time  ");
    queryStringBuilder.append("      	ORDER BY seq_time DESC  ");
    queryStringBuilder.append("  	) t4 ");
    queryStringBuilder.append("  	GROUP BY seq_day ");
    queryStringBuilder.append("  	UNION ALL   ");
    queryStringBuilder.append("  	select FROM_UNIXTIME(seq_time), seq_time, seq_day, ticker, sheep_bu, sheep_sd, wolf_bu, wolf_sd, shark_bu, shark_sd  from ( ");
    queryStringBuilder.append("      	SELECT DISTINCT(seq_time) as seq_time, FLOOR(seq_time/86400)*86400 as seq_day,   ");
    queryStringBuilder.append("         			ticker, sheep_bu_acc as sheep_bu, sheep_sd_acc as sheep_sd, wolf_bu_acc as wolf_bu,  ");
    queryStringBuilder.append("         			wolf_sd_acc as wolf_sd, shark_bu_acc as shark_bu, shark_sd_acc as shark_sd    ");
    queryStringBuilder.append("      	FROM investor_classifier_by_15min  "); //sua ve investor_classifier_by_1min
    queryStringBuilder.append("      	WHERE ticker = :t_ticker  ");
    queryStringBuilder.append("      	AND seq_time >= FLOOR(:toDate/86400)*86400   ");
    queryStringBuilder.append("      	AND seq_time <= :toDate  ");
    queryStringBuilder.append("      	ORDER BY seq_time desc  ");
    queryStringBuilder.append("  	) t3 group by seq_day ");
    queryStringBuilder.append("  )tbl ORDER BY seq_time ASC;  ");
    try {
      return FuturesMarket.futuresMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("t_ticker", ticker)
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", currentTimeCheck)
        .setParameter("future_name", futureName)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get investor from db")
  public static List<HashMap<String, Object>> getInvestor(String ticker, long fromTime, long endTime) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT :ticker_s as ticker, (tmt.time_series + :fromTime) as seq_time, ");
    queryStringBuilder.append(" IFNULL(sheep_bu, 0) as sheep_bu, IFNULL(sheep_sd, 0) as sheep_sd, IFNULL(wolf_bu, 0) as wolf_bu, ");
    queryStringBuilder.append(" IFNULL(wolf_sd, 0) as wolf_sd, IFNULL(shark_bu, 0) as shark_bu, IFNULL(shark_sd , 0) as shark_sd , ");
    queryStringBuilder.append(" IFNULL(num_sheep_bu, 0) as num_sheep_bu, IFNULL(num_sheep_sd, 0) as num_sheep_sd, IFNULL(num_wolf_bu, 0) as num_wolf_bu, ");
    queryStringBuilder.append(" IFNULL(num_wolf_sd, 0) as num_wolf_sd, IFNULL(num_shark_bu, 0) as num_shark_bu, IFNULL(num_shark_sd, 0) as num_shark_sd, ");
    queryStringBuilder.append(" IFNULL(sheep_bu_val, 0) as sheep_bu_val, IFNULL(sheep_sd_val, 0) as sheep_sd_val, IFNULL(wolf_bu_val, 0) as wolf_bu_val, ");
    queryStringBuilder.append(" IFNULL(wolf_sd_val, 0) as wolf_sd_val, IFNULL(shark_bu_val, 0) as shark_bu_val, IFNULL(shark_sd_val , 0) as shark_sd_val  ");
    queryStringBuilder.append(" from timeseries_1min_temp tmt  ");
    queryStringBuilder.append(" left join ( ");
    queryStringBuilder.append(" 	SELECT ticker , seq_time ,  ");
    queryStringBuilder.append(" 	sheep_bu , sheep_sd , wolf_bu , wolf_sd , shark_bu , shark_sd ,   ");
    queryStringBuilder.append(" 	num_sheep_bu, num_sheep_sd, num_wolf_bu, num_wolf_sd, num_shark_bu, num_shark_sd,  ");
    queryStringBuilder.append(" 	sheep_bu_val, sheep_sd_val, wolf_bu_val, wolf_sd_val, shark_bu_val, shark_sd_val  ");
    queryStringBuilder.append(" 	from investor_classifier_by_1min a  ");
    queryStringBuilder.append(" 	WHERE ticker = :ticker_s and seq_time >= :fromTime and seq_time <= :endTime  ");
    queryStringBuilder.append(" )t2 ");
    queryStringBuilder.append(" on (tmt.time_series + :fromTime) = t2.seq_time  ");
    queryStringBuilder.append(" group by (tmt.time_series + :fromTime); ");
    try {
      return getHibernateEdition(ticker).getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("ticker_s", ticker)
        .setParameter("fromTime", fromTime)
        .setParameter("endTime", endTime)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get buy trans from db")
  public static List<HashMap<String, Object>> getBuyTrans(String ticker, long fromTime, long endTime, long sheepConf, long sharkConf) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT FROM_UNIXTIME((tmt.time_series + :fromTime)), (tmt.time_series + :fromTime) as seq_time , :ticker_s as ticker,   ");
    queryStringBuilder.append("      IFNULL(sum(sheepBu), 0) as sheepBu , IFNULL(sum(numSheepBu), 0) as numSheepBu, IFNULL(sum(sheepBuVal), 0) as sheepBuVal,    ");
    queryStringBuilder.append("      IFNULL(sum(sheepSd), 0) as sheepSd , IFNULL(sum(numSheepSd), 0) as numSheepSd, IFNULL(sum(sheepSdVal), 0) as sheepSdVal,    ");
    queryStringBuilder.append("      IFNULL(sum(wolfBu), 0) as wolfBu , IFNULL(sum(numWolfBu), 0) as numWolfBu, IFNULL(sum(wolfBuVal), 0) as wolfBuVal,    ");
    queryStringBuilder.append("      IFNULL(sum(wolfSd), 0) as wolfSd , IFNULL(sum(numWolfSd), 0) as numWolfSd, IFNULL(sum(wolfSdVal), 0) as wolfSdVal,    ");
    queryStringBuilder.append("      IFNULL(sum(sharkBu), 0) as sharkBu , IFNULL(sum(numSharkBu), 0) as numSharkBu, IFNULL(sum(sharkBuVal), 0) as sharkBuVal,    ");
    queryStringBuilder.append("      IFNULL(sum(sharkSd), 0) as sharkSd , IFNULL(sum(numSharkSd), 0) as numSharkSd, IFNULL(sum(sharkSdVal), 0) as sharkSdVal    ");
    queryStringBuilder.append("  FROM timeseries_1min_temp tmt    ");
    queryStringBuilder.append("  LEFT JOIN   ");
    queryStringBuilder.append("  (   ");
    queryStringBuilder.append("  	SELECT ticker, t2.time_s as seq_time , ");
    queryStringBuilder.append("  	IF(`action` = 'BU' and val * @priceUnit < :sheepConf, close_vol, 0) as sheepBu,   ");
    queryStringBuilder.append("  	IF(`action` = 'BU' and val * @priceUnit < :sheepConf, 1, 0) as numSheepBu,   ");
    queryStringBuilder.append("  	IF(`action` = 'BU' and val * @priceUnit < :sheepConf, val * @priceUnit, 0) as sheepBuVal,   ");
    queryStringBuilder.append("  	IF(`action` = 'SD' and val * @priceUnit < :sheepConf, close_vol, 0) as sheepSd,   ");
    queryStringBuilder.append("  	IF(`action` = 'SD' and val * @priceUnit < :sheepConf, 1, 0) as numSheepSd,   ");
    queryStringBuilder.append("  	IF(`action` = 'SD' and val * @priceUnit < :sheepConf, val * @priceUnit, 0) as sheepSdVal,   ");
    queryStringBuilder.append("  	IF(`action` = 'BU' and val * @priceUnit >= :sheepConf and val * @priceUnit <= :sharkConf, close_vol, 0) as wolfBu,   ");
    queryStringBuilder.append("  	IF(`action` = 'BU' and val * @priceUnit >= :sheepConf and val * @priceUnit <= :sharkConf, 1, 0) as numWolfBu,   ");
    queryStringBuilder.append("  	IF(`action` = 'BU' and val * @priceUnit >= :sheepConf and val * @priceUnit <= :sharkConf, val * @priceUnit, 0) as wolfBuVal,   ");
    queryStringBuilder.append("  	IF(`action` = 'SD' and val * @priceUnit >= :sheepConf and val * @priceUnit <= :sharkConf, close_vol, 0) as wolfSd,   ");
    queryStringBuilder.append("  	IF(`action` = 'SD' and val * @priceUnit >= :sheepConf and val * @priceUnit <= :sharkConf, 1, 0) as numWolfSd,   ");
    queryStringBuilder.append("  	IF(`action` = 'SD' and val * @priceUnit >= :sheepConf and val * @priceUnit <= :sharkConf, val * @priceUnit, 0) as wolfSdVal,   ");
    queryStringBuilder.append("  	IF(`action` = 'BU' and val * @priceUnit > :sharkConf, close_vol, 0) as sharkBu,   ");
    queryStringBuilder.append("  	IF(`action` = 'BU' and val * @priceUnit > :sharkConf, 1, 0) as numSharkBu,   ");
    queryStringBuilder.append("  	IF(`action` = 'BU' and val * @priceUnit > :sharkConf, val * @priceUnit, 0) as sharkBuVal,   ");
    queryStringBuilder.append("  	IF(`action` = 'SD' and val * @priceUnit > :sharkConf, close_vol, 0) as sharkSd,   ");
    queryStringBuilder.append("  	IF(`action` = 'SD' and val * @priceUnit > :sharkConf, 1, 0) as numSharkSd,   ");
    queryStringBuilder.append("  	IF(`action` = 'SD' and val * @priceUnit > :sharkConf, val * @priceUnit, 0) as sharkSdVal   ");
    queryStringBuilder.append("  	FROM (   ");
    queryStringBuilder.append("  		SELECT ticker, sum(close_vol) as close_vol, sum(val) as val, seq_time, seq_time_ms , `action`, FLOOR(seq_time_ms/60)*60 as time_s ");
    queryStringBuilder.append(" 		FROM ( ");
    queryStringBuilder.append(" 			SELECT ticker, close_price, close_vol,  close_price * close_vol as val, ");
    queryStringBuilder.append(" 				@isTimeSame \\:= IF((FLOOR(@seqTimeMs/60)*60) = (FLOOR(seq_time_ms/60)*60) , 1, 0), ");
    queryStringBuilder.append(" 				@seqTime \\:= IF( (@seqTimeMsTemp + :time_merge) > seq_time_ms AND @actionType = `action` AND @isTimeSame = 1 , @seqTime, seq_time) as seq_time, ");
    queryStringBuilder.append(
      " 				@seqTimeMs \\:= IF( (@seqTimeMsTemp + :time_merge) > seq_time_ms AND @actionType = `action` AND @isTimeSame = 1 , @seqTimeMs, seq_time_ms) as seq_time_ms, ");
    queryStringBuilder.append(
      " 				@actionType \\:= IF((@seqTimeMsTemp + :time_merge) > seq_time_ms AND @actionType = `action` AND @isTimeSame = 1 , @actionType, `action`) as `action`, ");
    queryStringBuilder.append(" 				@seqTimeMsTemp \\:= seq_time_ms as seq_time_ms_temp ");
    queryStringBuilder.append(" 			FROM (  ");
    queryStringBuilder.append(" 				SELECT ticker, seq_time, close_price, close_vol, `action`, seq_time_ms, FROM_UNIXTIME(seq_time) , ");
    queryStringBuilder.append(" 					@seqTime \\:= IFNULL(@seqTime, seq_time) as seq_ms_first, ");
    queryStringBuilder.append(" 					@seqTimeMs \\:= IFNULL(@seqTimeMs, seq_time_ms) as seq_first, ");
    queryStringBuilder.append(" 					@actionType \\:= IFNULL(@actionType, `action`) as action_first, ");
    queryStringBuilder.append(" 					@seqTimeMsTemp \\:= IFNULL(@seqTimeMsTemp, seq_time_ms) as seq_temp_first ");
    queryStringBuilder.append(" 				FROM buysellactive_trans bt  ");
    queryStringBuilder.append(" 				WHERE ticker = :ticker_s  and seq_time >= :fromTime and seq_time <= :endTime AND `action` != ''	 ");
    queryStringBuilder.append(" 				order by seq_time asc ");
    queryStringBuilder.append(" 			)t1 ");
    queryStringBuilder.append(" 		)tbl ");
    queryStringBuilder.append(" 		group by seq_time_ms, `action` ");
    queryStringBuilder.append(" 	)t2 ");
    queryStringBuilder.append("  	cross join (   ");
    queryStringBuilder.append("  		select @priceUnit \\:= IF(:ticker_s like 'VN30F%' or :ticker_s like 'GB%F%', 100000, 1), ");
    queryStringBuilder.append("  				@seqTime \\:= NULL, @actionType \\:= NULL, @seqTimeMs \\:= NULL, @seqTimeMsTemp \\:= NULL, @isTimeSame \\:= 0 ");
    queryStringBuilder.append("  	) params   ");
    queryStringBuilder.append("  ) t3  ");
    queryStringBuilder.append("  on (tmt.time_series + :fromTime) = t3.seq_time   ");
    queryStringBuilder.append("  group by (tmt.time_series + :fromTime) ; ");
    try {
      return getHibernateEdition(ticker).getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("ticker_s", ticker)
        .setParameter("fromTime", fromTime)
        .setParameter("endTime", endTime)
        .setParameter("sheepConf", sheepConf)
        .setParameter("sharkConf", sharkConf)
        .setParameter("time_merge", 1)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static HibernateEdition getHibernateEdition(String ticker) {
    HibernateEdition hibernateEdition = Stockmarket.stockMarketConnection;
    if (checkDerivative(ticker)) {
      hibernateEdition = FuturesMarket.futuresMarketConnection;
    }
    return hibernateEdition;
  }
}
