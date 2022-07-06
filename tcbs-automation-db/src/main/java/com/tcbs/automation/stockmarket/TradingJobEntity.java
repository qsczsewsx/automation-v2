package com.tcbs.automation.stockmarket;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TradingJobEntity {

  @Step("select data")
  public static List<HashMap<String, Object>> getTradingState(String ticker, String fromTime, String toTime) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT *, DATE_FORMAT(CONVERT_TZ(from_unixtime(seq_time), '+00:00', '+07:00'), '%H:%i') t_time ");
    queryStringBuilder.append("from intraday_trading_state_15min itsm ");
    queryStringBuilder.append("WHERE ticker =:ticker ");
    queryStringBuilder.append("AND seq_time >= :fromTime ");
    queryStringBuilder.append("AND seq_time <= :toTime ;");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("ticker", ticker)
        .setParameter("fromTime", fromTime)
        .setParameter("toTime", toTime)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList();
  }
}
