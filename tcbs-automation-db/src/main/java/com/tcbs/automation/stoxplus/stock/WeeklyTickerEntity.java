package com.tcbs.automation.stoxplus.stock;

import com.beust.jcommander.internal.Nullable;
import com.tcbs.automation.staging.AwsStagingDwh;
import com.tcbs.automation.stockmarket.Stockmarket;
import com.tcbs.automation.stoxplus.Stoxplus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
@Setter
public class WeeklyTickerEntity {
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
  public static List<Map<String, Object>> getWeeklyTickerList(String tradingDate, String last60dDate, String last20dDate) {
    StringBuilder query = new StringBuilder();
    query.append(" SELECT sdsmp.Ticker AS Ticker, TotalVolume20, TotalVolume60, SLCPNY ");
    query.append("  ,ROUND((ClosePriceAdjusted * ShareCirculate) / 1000000000,0) AS MarketCap ");
    query.append("   ,ClosePriceAdjusted AS ClosePriceAdjusted");
    query.append(" FROM Smy_dwh_stox_MarketPrices sdsmp ");
    query.append("INNER JOIN");
    query.append("   (SELECT Ticker AS Ticker, IssueShare AS SLCPNY, OutstandingShare as ShareCirculate ");
    query.append(" FROM stx_cpf_Organization where  ComGroupCode in ('VNINDEX','HNXIndex') and len(ticker) = 3) AS tb1 ON tb1.Ticker = sdsmp.Ticker ");
    query.append("INNER JOIN ");
    query.append("   (SELECT Ticker, ROUND(sum(Volume)/20,0) AS TotalVolume20 ");
    query.append("    FROM (SELECT Ticker, TotalMatchVolume AS Volume ");
    query.append("        FROM Smy_dwh_stox_MarketPrices ");
    query.append("          WHERE TradingDate > :last20dDate ) tv ");
    query.append("  GROUP BY Ticker) AS TotalVol20 ON sdsmp.Ticker = TotalVol20.Ticker");
    query.append(" INNER JOIN ");
    query.append("   (SELECT Ticker, ROUND(sum(Volume)/60,0) AS TotalVolume60 ");
    query.append("   FROM (SELECT Ticker, TotalMatchVolume AS Volume");
    query.append("         FROM Smy_dwh_stox_MarketPrices ");
    query.append("         WHERE TradingDate > :last60dDate ) tv ");
    query.append("   GROUP BY Ticker) AS TotalVol60 ON sdsmp.Ticker = TotalVol60.Ticker  ");
    query.append(" WHERE  Cast(TradingDate as Date) = Cast( :tradingDate as Date) ");
    try {
      List<Map<String, Object>> resultList = AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("tradingDate", tradingDate)
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

  @Step
  public static List<Map<String, Object>> getMa5(String tradingDate) {
    StringBuilder query = new StringBuilder();

    query.append(" select SUBSTRING(CONVERT_TZ(from_unixtime(seq_time) , '+00:00', '+07:00'),1,10) as time,ticker, sma5 ");
    query.append(" from indicators_by_1D ");
    query.append(" where  SUBSTRING(CONVERT_TZ(from_unixtime(seq_time) , '+00:00', '+07:00'),1,10) = :tradingDate ");

    try {
      List<Map<String, Object>> resultList = Stockmarket.stockMarketConnection.getSession().createNativeQuery(query.toString())
        .setParameter("tradingDate", tradingDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    Stockmarket.stockMarketConnection.closeSession();

    return new ArrayList<>();
  }
}
