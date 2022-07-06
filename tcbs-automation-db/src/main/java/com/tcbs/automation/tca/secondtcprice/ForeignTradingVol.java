package com.tcbs.automation.tca.secondtcprice;

import com.tcbs.automation.tca.TcAnalysis;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForeignTradingVol {

  @Step("get data")
  public static Map<String, Object> getForeignTradingVol10DByTicker(String ticker, String dateStr, int fromNumberDays, int toNumberDays) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("DECLARE @day1Ago AS DATETIME, @day10Ago AS DATETIME; ");
    queryBuilder.append("SET @day1Ago = dbo.businessDaysAdd(:toNumberDays, CAST(:dateStr AS DATE)); ");
    queryBuilder.append("SET @day10Ago = dbo.businessDaysAdd(:fromNumberDays, CAST(:dateStr AS DATE)); ");
    queryBuilder.append(" SELECT Ticker , ROUND(SUM(ForeignBuyVolumeTotal - ForeignSellVolumeTotal), 0) AS netForeignTradingVol ");
    queryBuilder.append(" , ROUND(AVG(ForeignBuyVolumeTotal - ForeignSellVolumeTotal), 0) AS avgForeignVolume ");
    queryBuilder.append(" FROM stx_mrk_HoseStock ");
    queryBuilder.append(" WHERE TradingDate BETWEEN @day10Ago AND @day1Ago ");
    queryBuilder.append(" AND Ticker = :ticker ");
    queryBuilder.append(" GROUP BY Ticker ");
    queryBuilder.append(" UNION ");
    queryBuilder.append(" SELECT Ticker , SUM(ForeignBuyVolumeTotal - ForeignSellVolumeTotal) AS netForeignTradingVol ");
    queryBuilder.append(" , ROUND(AVG(ForeignBuyVolumeTotal - ForeignSellVolumeTotal), 0) AS avgForeignVolume ");
    queryBuilder.append(" FROM stx_mrk_HnxStock ");
    queryBuilder.append(" WHERE TradingDate BETWEEN @day10Ago AND @day1Ago ");
    queryBuilder.append(" AND Ticker = :ticker ");
    queryBuilder.append(" GROUP BY Ticker ");
    queryBuilder.append(" UNION ");
    queryBuilder.append(" SELECT Ticker , SUM(ForeignBuyVolumeTotal - ForeignSellVolumeTotal) AS netForeignTradingVol ");
    queryBuilder.append(" , ROUND(AVG(ForeignBuyVolumeTotal - ForeignSellVolumeTotal), 0) AS avgForeignVolume ");
    queryBuilder.append(" FROM stx_mrk_UpcomStock ");
    queryBuilder.append(" WHERE TradingDate BETWEEN @day10Ago AND @day1Ago ");
    queryBuilder.append(" AND Ticker = :ticker ");
    queryBuilder.append(" GROUP BY Ticker ");

    try {
      List<Map<String, Object>> resultList = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("ticker", ticker)
        .setParameter("toNumberDays", toNumberDays)
        .setParameter("dateStr", dateStr)
        .setParameter("fromNumberDays", fromNumberDays)
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
