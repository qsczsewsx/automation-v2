package com.tcbs.automation.tca.secondtcprice;

import com.tcbs.automation.tca.TcAnalysis;
import java.util.List;
import java.util.Map;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StockMarketWatch {
  private static final Logger logger = LoggerFactory.getLogger(StockMarketWatch.class);
  private StockMarketWatch() {};

  @Step
  public static Map<String, Object> getStockMarketWatchByTicker(String ticker) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT tmw.[Mã CK] AS ticker, tmw.[5D%] AS marketWatch5D ");
    queryBuilder.append(" , tmw.[1M%] AS marketWatch1M, tmw.[3M%] AS marketWatch3M ");
    queryBuilder.append(" , tmw.[1YHigh] AS highPrice1Y, tmw.[1YLow] AS lowPrice1Y, tmw.[%1YHigh] AS highPrice1YPercent, tmw.[%1YLow] AS lowPrice1YPercent ");
    queryBuilder.append(" FROM TCBS_Market_Watch tmw ");
    queryBuilder.append(" WHERE tmw.[Mã CK] = :ticker ");

    try {
      List<Map<String, Object>> resultList = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
          .setParameter("ticker", ticker)
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
          .getResultList();

      if (resultList.size() > 0) {
        return resultList.get(0);
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return null;
  }
}
