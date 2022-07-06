package com.tcbs.automation.stoxplus.stock;

import com.beust.jcommander.internal.Nullable;
import com.tcbs.automation.staging.AwsStagingDwh;
import com.tcbs.automation.stoxplus.Stoxplus;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

@Getter
@RequiredArgsConstructor
@Setter
public class AllowedTickerEntity {
  private String ticker;
  @Nullable
  private Double totalVolume20;
  @Nullable
  private Double totalVolume60;
  @Nullable
  private Double marketCap;
  @Nullable
  private Double shareIssue;
  @Nullable
  private Double closePriceAdjusted;

  @Step
  public static List<Map<String, Object>> getAllowedTickerList(String last60dDate, String last20dDate) {
    StringBuilder query = new StringBuilder();
    query.append(" SELECT sdsmp.Ticker AS Ticker, TotalVolume20, TotalVolume60, SLCPNY  ");
    query.append("  ,ROUND((ClosePriceAdjusted * ShareCirculate) / 1000000000,0) AS MarketCap ");
    query.append("   ,ClosePriceAdjusted AS ClosePriceAdjusted ");
    query.append(" FROM Smy_dwh_stox_MarketPrices sdsmp INNER JOIN  ");
    query.append("  (SELECT Ticker AS Ticker, IssueShare AS SLCPNY, OutstandingShare as ShareCirculate ");
    query.append(" FROM stx_cpf_Organization where ComGroupCode in ('VNINDEX','HNXIndex') and len(ticker) = 3 and ticker <> 'TCB') AS tb1 ON tb1.Ticker = sdsmp.Ticker INNER JOIN ");
    query.append("  (SELECT Ticker, ROUND(sum(Volume)/20,0) AS TotalVolume20 ");
    query.append("    FROM (SELECT Ticker, TotalMatchVolume AS Volume ");
    query.append("         FROM Smy_dwh_stox_MarketPrices ");
    query.append("          WHERE TradingDate > :last20dDate ) tv ");
    query.append("   GROUP BY Ticker) AS TotalVol20 ON sdsmp.Ticker = TotalVol20.Ticker AND TotalVol20.TotalVolume20 >= 10000 INNER JOIN ");
    query.append("  (SELECT Ticker, ROUND(sum(Volume)/60,0) AS TotalVolume60 ");
    query.append("   FROM (SELECT Ticker, TotalMatchVolume AS Volume ");
    query.append("         FROM Smy_dwh_stox_MarketPrices ");
    query.append("          WHERE TradingDate > :last60dDate ) tv ");
    query.append("   GROUP BY Ticker) AS TotalVol60 ON sdsmp.Ticker = TotalVol60.Ticker ");
    query.append(" WHERE TradingDate= (SELECT MAX(TradingDate)  FROM Smy_dwh_stox_MarketPrices) ");
    query.append(" AND (ShareCirculate* ClosePriceAdjusted) > 200000000000 ");
    try {
      List<Map<String, Object>> resultList = AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("last60dDate", last60dDate)
        .setParameter("last20dDate", last20dDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    Stoxplus.stoxDbConnection.closeSession();
    return new ArrayList<>();
  }
}
