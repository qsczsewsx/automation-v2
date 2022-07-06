package com.tcbs.automation.tca.secondtcprice;

import com.tcbs.automation.tca.TcAnalysis;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdjustedClosePrice {

  @Step("get data")
  public static Map<String, Object> getAdjustedClosePriceByTicker(String ticker, String dateStr, int numberTDays, String resolution) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("DECLARE @date AS DATETIME; ");
    if (resolution.equals("D")) {
      queryBuilder.append("SET @date = dbo.businessDaysAdd(:number, CAST(:dateStr AS DATE)); ");
    } else if (resolution.equals("M")) {
      queryBuilder.append("SET @date = dbo.businessDaysAdd(-1, DATEADD(MONTH, :number, DATEADD(DAY, -1, CAST(:dateStr AS DATE)))); ");
    } else if (resolution.equals("Y")) {
      queryBuilder.append("SET @date = dbo.businessDaysAdd(-1, DATEADD(YEAR, :number, DATEADD(DAY, -1, CAST(:dateStr AS DATE)))); ");
    }

    queryBuilder.append(" SELECT Ticker, ClosePriceAdjusted ");
    queryBuilder.append(" FROM Smy_dwh_stox_MarketPrices ");
    queryBuilder.append(" WHERE TradingDate = @date ");
    queryBuilder.append(" AND Ticker = :ticker ");

    try {
      List<Map<String, Object>> resultList = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("ticker", ticker)
        .setParameter("number", numberTDays)
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

  @Step("get data")
  public static Map<String, Object> getVnIndex(String dateStr, int numberTDays, String resolution) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("DECLARE @date AS DATETIME; ");
    if (resolution.equals("D")) {
      queryBuilder.append("SET @date = dbo.businessDaysAdd(:number, CAST(:dateStr AS DATE)); ");
    } else if (resolution.equals("M")) {
      queryBuilder.append("SET @date = dbo.businessDaysAdd(-1, DATEADD(MONTH, :number, DATEADD(DAY, -1, CAST(:dateStr AS DATE)))); ");
    } else if (resolution.equals("Y")) {
      queryBuilder.append("SET @date = dbo.businessDaysAdd(-1, DATEADD(YEAR, :number, DATEADD(DAY, -1, CAST(:dateStr AS DATE)))); ");
    }

    queryBuilder.append("SELECT CloseIndex as VNIndex, TradingDate ");
    queryBuilder.append("FROM stx_mrk_HoseIndex  ");
    queryBuilder.append("WHERE ComGroupCode = 'VNINDEX' ");
    queryBuilder.append("AND TradingDate = @date ");

    try {
      List<Map<String, Object>> resultList = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("number", numberTDays)
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
