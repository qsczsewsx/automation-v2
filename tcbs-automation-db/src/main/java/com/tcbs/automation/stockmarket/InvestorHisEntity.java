package com.tcbs.automation.stockmarket;

import com.tcbs.automation.HibernateEdition;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.stockmarket.InvestorEntity.getHibernateEdition;

public class InvestorHisEntity {

  @Step("get investor his item from db")
  public static List<HashMap<String, Object>> getInvestorsHisItem(String ticker, long fromDate, long toDate) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("select * , DATE_FORMAT(CONVERT_TZ(from_unixtime(seq_time), '+00:00', '+07:00'), '%H:%i:%s') t_time ");
    queryStringBuilder.append("from investor_trade_his_by_1min ithbm ");
    queryStringBuilder.append("where ticker = :ticker ");
    queryStringBuilder.append("and seq_time BETWEEN :from_date and :to_date ;");
    try {
      return getHibernateEdition(ticker).getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("ticker", ticker)
        .setParameter("from_date", fromDate)
        .setParameter("to_date", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get TC price")
  public static List<HashMap<String, Object>> getTCPrice(String ticker, long fromDate, long toDate) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT * ");
    queryStringBuilder.append("from intraday_by_1min ibm  ");
    queryStringBuilder.append("WHERE t_ticker = :t_ticker ");
    queryStringBuilder.append("and i_seq_time BETWEEN :i_from_date and :i_to_date ");
    queryStringBuilder.append("order by i_seq_time desc;");
    try {
      return getHibernateEdition(ticker).getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("t_ticker", ticker)
        .setParameter("i_from_date", fromDate)
        .setParameter("i_to_date", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get investor his item from db")
  public static List<HashMap<String, Object>> getInvestorsTrans(String ticker, long fromDate, long toDate) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT *, DATE_FORMAT(CONVERT_TZ(from_unixtime(seq_time_ms), '+00:00', '+07:00'), '%H:%i:%s') t_time ");
    queryStringBuilder.append("from buysellactive_trans bt  ");
    queryStringBuilder.append("WHERE ticker = :ticker ");
    queryStringBuilder.append("and seq_time_ms BETWEEN :from_date AND :to_date ;");
    try {
      return getHibernateEdition(ticker).getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("ticker", ticker)
        .setParameter("from_date", fromDate)
        .setParameter("to_date", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
