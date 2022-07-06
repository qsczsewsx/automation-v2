package com.tcbs.automation.stockmarket;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MatchVolEntity {

  @Step("select data match vol all")
  public static List<Map<String, Object>> getMatchVolAll(String ticker, String fromTime, String toTime) {
    int roundNum = 0;
    if (ticker.startsWith("VN30F") || ticker.startsWith("GB10F") || ticker.startsWith("GB05F")) {
      roundNum = 1;
    }
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT ROUND(close_price, :roundNum) as price, sum(close_vol) as vol, count(*) as trans ");
    queryStringBuilder.append("from buysellactive_trans bt  ");
    queryStringBuilder.append("WHERE ticker = :ticker  ");
    queryStringBuilder.append("AND seq_time >= :fromTime ");
    queryStringBuilder.append("AND seq_time <= :toTime  ");
    queryStringBuilder.append("GROUP BY ROUND(close_price, :roundNum) ");
    queryStringBuilder.append("ORDER BY ROUND(close_price, :roundNum) asc;");
    try {
      return InvestorEntity.getHibernateEdition(ticker).getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("ticker", ticker)
        .setParameter("fromTime", fromTime)
        .setParameter("toTime", toTime)
        .setParameter("roundNum", roundNum)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList();
  }

  @Step("select data match vol by type")
  public static List<Map<String, Object>> getMatchVolByType(String ticker, String type, String fromTime, String toTime) {
    int roundNum = -1;
    if (ticker.startsWith("VN30F") || ticker.startsWith("GB10F") || ticker.startsWith("GB05F")) {
      roundNum = 1;
    }
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT ROUND(avg_price, :round_num) as price, sum(close_vol) as vol, sum(no_trans) as trans ");
    queryStringBuilder.append("from investor_trade_his_by_1min ithbm ");
    queryStringBuilder.append("WHERE ticker = :ticker ");
    queryStringBuilder.append("AND i_type = :type ");
    queryStringBuilder.append("AND seq_time >= :fromTime ");
    queryStringBuilder.append("AND seq_time <= :toTime ");
    queryStringBuilder.append("GROUP BY ROUND(avg_price, :round_num) ");
    queryStringBuilder.append("ORDER BY price asc;");
    try {
      return InvestorEntity.getHibernateEdition(ticker).getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("ticker", ticker)
        .setParameter("type", type)
        .setParameter("fromTime", fromTime)
        .setParameter("toTime", toTime)
        .setParameter("round_num", roundNum)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList();
  }
}
