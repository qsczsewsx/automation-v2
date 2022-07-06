package com.tcbs.automation.stoxplus.cw;

import com.tcbs.automation.stockmarket.Stockmarket;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class HighLowPriceCwEntity {
  private final static Logger logger = LoggerFactory.getLogger(HighLowPriceCwEntity.class);

  public static List<Map<String, Object>> getPrice(String ticker) {
    StringBuilder stringBuilder = new StringBuilder();
//    stringBuilder.append("SELECT Code as ticker, lowest_price, highest_price FROM ( ");
//    stringBuilder.append("  SELECT Code, ");
//    stringBuilder.append("  MIN(ClosePrice_Adjusted) as lowest_price, ");
//    stringBuilder.append("  MAX(ClosePrice_Adjusted) as highest_price ");
//    stringBuilder.append("  FROM Smy_dwh_stox_MarketPrices sdsmp ");
//    stringBuilder.append("  WHERE Code = :p_ticker AND trading_Date >= dbo.businessDaysAdd(-4, getdate()) GROUP BY Code )t ");

    stringBuilder.append("  SELECT t_ticker ticker, ");
    stringBuilder.append("  MIN(f_close_price) as lowest_price, ");
    stringBuilder.append("  MAX(f_close_price) as highest_price ");
    stringBuilder.append("  FROM intraday_by_1D sdsmp ");
    stringBuilder.append("  WHERE t_ticker = :p_ticker AND CAST(FROM_UNIXTIME(i_seq_time) AS date) >= businessDaysAdd(-4, CURDATE()) ");
    stringBuilder.append("  GROUP BY t_ticker ");

    logger.info("query {}", stringBuilder.toString());
    try {
      List<Map<String, Object>> lm = Stockmarket.stockMarketConnection.getSession().createNativeQuery(stringBuilder.toString())
        .setParameter("p_ticker", ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return lm;
    } catch (Exception e) {
      StepEventBus.getEventBus().testFailed(new AssertionError(e.getMessage()));
    }
    return new ArrayList<>();
  }
}
