package com.tcbs.automation.tca.secondtcprice;

import com.tcbs.automation.tca.TcAnalysis;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PriceSummary {

  @Step("get data")
  public static Map<String, Object> getPriceSummary52wPrice(String ticker) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT Ticker, ROUND(Price52WHighest, 0) AS highPrice1Y, ROUND(Price52WLowest, 0) AS lowPrice1Y ");
    queryBuilder.append("FROM tbl_idata_price_summary tps ");
    queryBuilder.append("WHERE CAST(ReportDate AS Date) = (SELECT CAST(max(ReportDate) AS date) FROM tbl_idata_price_summary where Ticker = :ticker) AND Ticker = :ticker ");

    try {
      List<Map<String, Object>> resultList = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("ticker", ticker)
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