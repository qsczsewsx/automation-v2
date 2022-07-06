package com.tcbs.automation.stockmarket;

import com.tcbs.automation.stoxplus.StoxplusV2;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LatestPriceEntity {
  @Step("get latest price from db")
  public static List<HashMap<String, Object>> getLatestPrice(List<String> tickers) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT T1.Ticker as ticker, T2.ClosePriceAdjusted as close_price FROM ( ");
    queryStringBuilder.append("  SELECT Ticker, MAX(TradingDate) as MaxTradingDate FROM Smy_dwh_stox_MarketPrices_AllData  ");
    queryStringBuilder.append("  WHERE Ticker  IN (:tickers)  ");
    queryStringBuilder.append("  GROUP BY Ticker ");
    queryStringBuilder.append(")T1 ");
    queryStringBuilder.append("INNER JOIN Smy_dwh_stox_MarketPrices_AllData T2 ");
    queryStringBuilder.append("ON T1.Ticker = T2.Ticker AND T1.MaxTradingDate = T2.TradingDate  ");

    try {
      return StoxplusV2.stoxV2DbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .setParameter("tickers", tickers)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get intraday price from db")
  public static List<HashMap<String, Object>> getIntradayPrice(List<String> tickers) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT t1.t_ticker , t2.f_close_price ");
    queryStringBuilder.append(" FROM ( ");
    queryStringBuilder.append(" 	SELECT t_ticker, max(i_seq_time) AS max_time FROM intraday_by_1min ");
    queryStringBuilder.append(" 	WHERE t_ticker in (:tickers) ");
    queryStringBuilder.append(" 	GROUP BY t_ticker  ");
    queryStringBuilder.append(" )t1 ");
    queryStringBuilder.append(" INNER JOIN intraday_by_1min t2 ");
    queryStringBuilder.append(" WHERE t1.t_ticker = t2.t_ticker AND t1.max_time = t2.i_seq_time ");

    try {
      return Stockmarket.stockMarketConnection.getSession()
        .createNativeQuery(queryStringBuilder.toString())
        .setParameter("tickers", tickers)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
