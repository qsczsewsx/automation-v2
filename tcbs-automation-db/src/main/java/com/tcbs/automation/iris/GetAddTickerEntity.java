package com.tcbs.automation.iris;

import com.tcbs.automation.staging.AwsStagingDwh;
import com.tcbs.automation.stoxplus.Stoxplus;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.Id;
import java.util.HashMap;

public class GetAddTickerEntity {

  @Id
  private String ticker;
  private String tradingDate;

  @Step("Get data by ticker")
  public static Object getClosePrice(String ticker, String tradingDate, String last20dDate, String last60dDate) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("  select a.Ticker as ticker, ClosePriceAdjusted, TotalVolume20,TotalVolume60, SLCPNY, ROUND((ClosePriceAdjusted * tb1.ShareCirculate) / 1000000000,0) AS MarketCap");
    queryBuilder.append("  from ( ");
    queryBuilder.append(" (select Ticker, ClosePriceAdjusted FROM Smy_dwh_stox_MarketPrices a ");
    queryBuilder.append(" where Ticker = :ticker and cast(TradingDate as Date) = cast(:tradingDate as Date)) as a ");
    queryBuilder.append(" inner join ");
    queryBuilder.append(" (SELECT Ticker,round(sum(TotalMatchVolume)/20,0) AS TotalVolume20 FROM Smy_dwh_stox_MarketPrices ");
    queryBuilder.append(" WHERE Ticker = :ticker and TradingDate > :last20dDate ");
    queryBuilder.append("  group by Ticker) as total20 ");
    queryBuilder.append(" on a.Ticker = total20.Ticker ");
    queryBuilder.append(" inner join ");
    queryBuilder.append(" (SELECT Ticker, round(sum(TotalMatchVolume)/60,0) AS TotalVolume60 FROM Smy_dwh_stox_MarketPrices ");
    queryBuilder.append(" WHERE Ticker = :ticker and TradingDate > :last60dDate ");
    queryBuilder.append(" group by Ticker) as total60 ");
    queryBuilder.append(" on a.Ticker = total60.Ticker ");
    queryBuilder.append("  inner join ");
    queryBuilder.append("  (SELECT Ticker AS Ticker, IssueShare AS SLCPNY, OutstandingShare as ShareCirculate ");
    queryBuilder.append("  FROM stx_cpf_Organization where ComGroupCode in ('VNINDEX','HNXIndex') and len(ticker) = 3 and ticker <> 'TCB') AS tb1 ");
    queryBuilder.append(" ON tb1.Ticker = a.Ticker ");
    queryBuilder.append(" ) ");
    try {
      Query query = AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString());
      query.setParameter("ticker", ticker)
        .setParameter("tradingDate", tradingDate)
        .setParameter("last20dDate", last20dDate)
        .setParameter("last60dDate", last60dDate);
      return (query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)).getSingleResult();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new HashMap<>();
  }
}
