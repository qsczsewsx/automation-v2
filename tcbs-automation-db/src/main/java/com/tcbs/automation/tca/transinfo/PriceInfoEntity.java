package com.tcbs.automation.tca.transinfo;

import com.tcbs.automation.tca.TcAnalysis;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PriceInfoEntity {
  public static List<Map<String, Object>> getPriceInfo(String ticker, int numDays) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(" SELECT TOP (:numDays) t.* ");
    stringBuilder.append(" FROM (");
    stringBuilder.append("    SELECT tm.ticker ,");
    stringBuilder.append("      DATEDIFF(SECOND,{d '1970-01-01'}, tm.tradingDate) as seq_time, ");
    stringBuilder.append("      CAST(tm.ClosePriceAdjusted AS FLOAT) as close_price,");
    stringBuilder.append("      CAST(tm.TotalMatchVolume AS FLOAT) as volume");
    stringBuilder.append("  FROM Smy_dwh_stox_MarketPrices as tm");
    stringBuilder.append("  WHERE tm.ticker  = :ticker ");
    stringBuilder.append(" ) t");
    stringBuilder.append(" ORDER BY t.seq_time DESC");
    try {
      List<Map<String, Object>> lm = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(stringBuilder.toString())
        .setParameter("ticker", ticker)
        .setParameter("numDays", numDays)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return lm;
    } catch (Exception e) {
      StepEventBus.getEventBus().testFailed(new AssertionError(e.getMessage()));
    }
    return new ArrayList<>();
  }
}
