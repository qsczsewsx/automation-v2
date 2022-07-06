package com.tcbs.automation.stoxplus.stock;

import com.tcbs.automation.tca.TcAnalysis;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClosePriceStockEntity {

  @Step("get ticker price from db")
  public static List<HashMap<String, Object>> getClosePriceStock(List<String> tickers) {
    StringBuilder queryStringBuilder = new StringBuilder();

    queryStringBuilder.append("select ticker as ticker, ClosePrice as close_price, ClosePriceAdjusted as closeprice_adjusted, tradingDate as trading_date ");
    queryStringBuilder.append("from Smy_dwh_stox_MarketPrices ");
    queryStringBuilder.append("where ticker in :tickers ");
    queryStringBuilder.append("and tradingDate >= dbo.businessDaysAdd(-4, getdate()) ");
    queryStringBuilder.append("order by tradingDate asc;");

//    queryStringBuilder.append("select Ticker as ticker, ClosePrice as close_price, ClosePriceAdjusted as closeprice_adjusted, TradingDate as trading_date ");
//    queryStringBuilder.append("from Smy_dwh_stox_MarketPrices ");
//    queryStringBuilder.append("where Ticker in :tickers ");
//    queryStringBuilder.append("and TradingDate in :tradingDate ");
//    queryStringBuilder.append("order by TradingDate desc;");

    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("tickers", tickers)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}
