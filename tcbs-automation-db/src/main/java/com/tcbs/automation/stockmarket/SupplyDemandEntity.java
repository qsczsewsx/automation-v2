package com.tcbs.automation.stockmarket;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SupplyDemandEntity {

  public static final String TICKER = "ticker";
  public static final String FROM_TIME = "fromTime";
  public static final String TO_TIME = "toTime";

  @Step("select data")
  public static List<Map<String, Object>> getBsaInfo(String ticker, long fromTime, long toTime) {
    StringBuilder queryStringBuilder = new StringBuilder();

    queryStringBuilder.append("  SELECT ticker, seq_time,  ");
    queryStringBuilder.append("    sheep_bu_acc, sheep_sd_acc, wolf_bu_acc, wolf_sd_acc, shark_bu_acc, shark_sd_acc, ");
    queryStringBuilder.append("    (sheep_bu_acc + wolf_bu_acc + shark_bu_acc) as total_bu_vol_acc,  ");
    queryStringBuilder.append("    (sheep_sd_acc + wolf_sd_acc + shark_sd_acc) as total_sd_vol_acc  ");
    queryStringBuilder.append("  FROM investor_classifier_by_1min  ");
    queryStringBuilder.append("  WHERE ticker = :ticker  ");
    queryStringBuilder.append("  ORDER BY seq_time ASC;  ");

    try {
      List<Map<String, Object>> list = InvestorEntity.getHibernateEdition(ticker).getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter(TICKER, ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      InvestorEntity.getHibernateEdition(ticker).closeSession();
      return list;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }


  @Step("select data")
  public static List<Map<String, Object>> getBsaExtInfo(String ticker, long fromTime, long toTime) {
    StringBuilder queryStringBuilder = new StringBuilder();

    queryStringBuilder.append(" SELECT * ");
    queryStringBuilder.append(" FROM ( ");
    queryStringBuilder.append("     SELECT seq_time, ticker, sheep_bu, sheep_sd, wolf_bu, wolf_sd, shark_bu, shark_sd, ");
    queryStringBuilder.append("     (sheep_bu + wolf_bu + shark_bu) as total_bu_vol, (sheep_sd + wolf_sd + shark_sd) as total_sd_vol  ");
    queryStringBuilder.append("     FROM investor_classifier_by_1min ");
    queryStringBuilder.append("     WHERE ticker = :ticker ");
    queryStringBuilder.append("     AND seq_time >= :fromTime ");
    queryStringBuilder.append("     AND (seq_time <= (:toTime - 60) OR ((:toTime - 60) >= (:fromTime + 20700) AND (sheep_bu + wolf_bu + shark_bu) > 0 AND (sheep_sd + wolf_sd + shark_sd) > 0)) ");
    queryStringBuilder.append(" ORDER BY seq_time DESC ");
    queryStringBuilder.append(" ) tbl ");
    queryStringBuilder.append(" ORDER BY seq_time ASC ");

    try {
      List<Map<String, Object>> list = InvestorEntity.getHibernateEdition(ticker).getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter(TICKER, ticker)
        .setParameter(FROM_TIME, fromTime)
        .setParameter(TO_TIME, toTime)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      InvestorEntity.getHibernateEdition(ticker).closeSession();
      return list;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("select data")
  public static List<Map<String, Object>> getBsaMonthInfo(String ticker, long fromTime, long toTime, String futureName) {
    if (ticker.startsWith("VN30F") || ticker.startsWith("GB10F") || ticker.startsWith("GB05F")) {
      return getBsaMonthDerivative(ticker, fromTime, toTime, futureName);
    }
    StringBuilder queryStringBuilder = new StringBuilder();

    queryStringBuilder.append(" SELECT seq_day as seq_time, ticker, SUM(sheep_bu) as sheep_bu, ");
    queryStringBuilder.append(" SUM(sheep_sd) as sheep_sd, ");
    queryStringBuilder.append(" SUM(wolf_bu) as wolf_bu, ");
    queryStringBuilder.append(" SUM(wolf_sd) as wolf_sd, ");
    queryStringBuilder.append(" SUM(shark_bu) as shark_bu, ");
    queryStringBuilder.append(" SUM(shark_sd) as shark_sd, ");
    queryStringBuilder.append(" SUM(total_bu_vol) as total_bu_vol, ");
    queryStringBuilder.append(" SUM(total_sd_vol) as total_sd_vol ");
    queryStringBuilder.append(" FROM ( ");
    queryStringBuilder.append("     SELECT DISTINCT(seq_time) as seq_time, FLOOR(seq_time/86400)*86400 as seq_day, ticker, sheep_bu, ");
    queryStringBuilder.append("     sheep_sd, wolf_bu, wolf_sd, shark_bu, shark_sd, ");
    queryStringBuilder.append("     (sheep_bu + wolf_bu + shark_bu) as total_bu_vol, (sheep_sd + wolf_sd + shark_sd) as total_sd_vol ");
    queryStringBuilder.append("     FROM investor_classifier_by_15min ");
    queryStringBuilder.append("     WHERE ticker = :ticker ");
    queryStringBuilder.append("     AND seq_time >= :fromTime ");
    queryStringBuilder.append("     AND seq_time <= :toTime ");
    queryStringBuilder.append("   ) tbl ");
    queryStringBuilder.append(" GROUP BY seq_day ");
    queryStringBuilder.append(" ORDER BY seq_day ASC ");

    try {
      List<Map<String, Object>> list = Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter(TICKER, ticker)
        .setParameter(FROM_TIME, fromTime)
        .setParameter(TO_TIME, toTime)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      Stockmarket.stockMarketConnection.closeSession();
      return list;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  private static List<Map<String, Object>> getBsaMonthDerivative(String ticker, long fromTime, long toTime, String futureName) {
    if (StringUtils.isEmpty(futureName)) {
      return new ArrayList<>();
    }
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT * FROM ( ");
    queryStringBuilder.append("	SELECT * FROM ( ");
    queryStringBuilder.append("	SELECT FROM_UNIXTIME(seq_day), seq_day as seq_time, ticker, ");
    queryStringBuilder.append("		 sum(sheep_bu) as sheep_bu, sum(sheep_sd) as sheep_sd, sum(wolf_bu) as wolf_bu, sum(wolf_sd) as wolf_sd, ");
    queryStringBuilder.append("		 sum(shark_bu) as shark_bu, sum(shark_sd) as shark_sd, sum(total_bu_vol) as total_bu_vol, sum(total_sd_vol) total_sd_vol ");
    queryStringBuilder.append("	FROM (  ");
    queryStringBuilder.append("		SELECT DISTINCT(seq_time) as seq_time, FLOOR(seq_time/86400)*86400 as seq_day, ticker, sheep_bu, sheep_sd, wolf_bu, wolf_sd, ");
    queryStringBuilder.append("		(sheep_bu + wolf_bu + shark_bu) as total_bu_vol, (sheep_sd + wolf_sd + shark_sd) as total_sd_vol, shark_bu, shark_sd ");
    queryStringBuilder.append("		FROM investor_classifier_by_15min ");
    queryStringBuilder.append("		WHERE ticker like 'VN30F%' ");
//    queryStringBuilder.append("		WHERE func_idata_future_name_by_ticker_and_time(ticker, seq_time) =:future_name "); //TODO change
    queryStringBuilder.append("		AND seq_time >= :fromTime ");
    queryStringBuilder.append("		AND seq_time <= :toTime ");
    queryStringBuilder.append("		ORDER BY seq_time DESC ");
    queryStringBuilder.append("	  ) tbl  ");
    queryStringBuilder.append("	  group by ticker, seq_day ");
    queryStringBuilder.append("	  ORDER BY seq_day DESC ");
    queryStringBuilder.append("	)t1 ");
    queryStringBuilder.append("	LEFT JOIN (  ");
    queryStringBuilder.append("		select seq_time as i_seq_time, ticker as t_ticker, future_name  ");
    queryStringBuilder.append("	 from future_by_1D fbd  ");
    queryStringBuilder.append("		WHERE seq_time >= :fromTime  "); //thieu 1 diem cua fromTime
    queryStringBuilder.append("		AND seq_time <= :toTime   ");
    queryStringBuilder.append("	)t2 ON t1.ticker = t2.t_ticker AND t1.seq_time = t2.i_seq_time  ");
    queryStringBuilder.append(")tb  ");
    queryStringBuilder.append("WHERE future_name = :future_name   ");
    queryStringBuilder.append("ORDER BY seq_time ASC ; ");
    try {
      List<Map<String, Object>> list = FuturesMarket.futuresMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter(FROM_TIME, fromTime)
        .setParameter(TO_TIME, toTime)
        .setParameter("future_name", futureName)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      FuturesMarket.futuresMarketConnection.closeSession();
      return list;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
