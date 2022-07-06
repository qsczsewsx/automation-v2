package com.tcbs.automation.stockmarket;

import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BuySellOnPriceBoardEntity {
  private static final Logger LOG = LoggerFactory.getLogger(BuySellOnPriceBoardEntity.class);

  public static List<HashMap<String, Object>> getBuySellOnPriceBoard(String ticker, long fromTime, long toTime) {
    String sqlQuery = "SELECT bidask.ticker, buysell.total_bu_vol,buysell.total_sd_vol, bidask.over_bought, bidask.over_sold, bidask.seq_time \n" +
      "FROM (SELECT ticker, seq_time, over_bought, over_sold FROM bid_ask_by_1min \n" +
      "  WHERE  seq_time >= :from_time  \n" +
      "  AND seq_time <= :to_time \n" +
      "  AND ticker = :ticker ) bidask \n" +
      "LEFT JOIN( \n" +
      "  SELECT ticker, total_bu_vol, total_sd_vol, seq_time FROM buysellactive_acc_by_1min p \n" +
      "  WHERE  p.seq_time >= :from_time \n" +
      "  AND p.seq_time <= :to_time  \n" +
      "  AND ticker = :ticker ) buysell \n" +
      "ON buysell.ticker = bidask.ticker  \n" +
      "AND buysell.seq_time = bidask.seq_time ";

    try {
      List<HashMap<String, Object>> lm = Stockmarket.stockMarketConnection.getSession().createNativeQuery(sqlQuery)
        .setParameter("ticker", ticker)
        .setParameter("from_time", fromTime)
        .setParameter("to_time", toTime)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return lm;
    } catch (Exception e) {
      StepEventBus.getEventBus().testFailed(new AssertionError(e.getMessage()));
    } finally {
      Stockmarket.stockMarketConnection.closeSession();
    }
    return new ArrayList<>();
  }
}