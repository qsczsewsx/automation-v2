package com.tcbs.automation.tca.secondtcprice;

import com.tcbs.automation.tca.TcAnalysis;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HighLowPrice {

  @Step("get data")
  public static Map<String, Object> getHighLowPriceByTicker(String ticker, String dateStr, int month) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("DECLARE @day1Ago AS DATETIME, @fromDate AS DATETIME; ");
    queryBuilder.append("SET @day1Ago = dbo.businessDaysAdd(-1, CAST(:dateStr AS DATE)); ");
    queryBuilder.append("SET @fromDate = dbo.businessDaysAdd(-1, DATEADD(MONTH, :month, DATEADD(DAY, -1, CAST(:dateStr AS DATE)))); ");
    queryBuilder.append(" SELECT Ticker AS ticker, MAX(ClosePriceAdjusted) AS highestPrice, MIN(ClosePriceAdjusted) AS lowestPrice ");
    queryBuilder.append(" FROM Smy_dwh_stox_MarketPrices ");
    queryBuilder.append(" WHERE TradingDate > @fromDate ");
    queryBuilder.append(" AND TradingDate <= @day1Ago ");
    queryBuilder.append(" AND Ticker = :ticker ");
    queryBuilder.append(" GROUP BY Ticker ");

    try {
      List<Map<String, Object>> resultList = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("ticker", ticker)
        .setParameter("month", month)
        .setParameter("dateStr", dateStr)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();

      if (!resultList.isEmpty()) {
        return resultList.get(0);
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new HashMap<>();
  }
}
