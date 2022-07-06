package com.tcbs.automation.iris;

import com.tcbs.automation.staging.AwsStagingDwh;
import com.tcbs.automation.stoxplus.Stoxplus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor


public class SyncClosePriceAdjustedEntity {
  @Id
  private String ticker;
  private Integer closePriceAdjusted;
  private String reviewedDate;
  private String updatedDate;
  private String updateBy;
  private String tradingDate;
  private Double ma5;


  @Step("Get close price adjusted data")
  public static List<HashMap<String, Object>> getClosePriceAdjusted() {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" SELECT  TICKER,MA_5, CLOSE_PRICE_ADJUSTED, REVIEWED_DATE, UPDATED_DATE, UPDATED_BY ");
    queryBuilder.append(" FROM RISK_ANALYST_MARGIN_REVIEWED_FULL  ");
    try {

      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

  @Step("Get close price from smy")
  public static HashMap<String, Object> getClosePriceAdjustedFromSmy(String ticker, String tradingDate) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("  SELECT  CLOSEPRICEADJUSTED ");
    queryBuilder.append(" FROM SMY_DWH_STOX_MARKETPRICES ");
    queryBuilder.append(" WHERE Ticker = :ticker ");
    queryBuilder.append(" AND CONVERT(VARCHAR(10), TradingDate, 120) = CONVERT(VARCHAR(10), :tradingDate, 120) ");
    try {
      return (HashMap<String, Object>) (AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("ticker", ticker)
        .setParameter("tradingDate", tradingDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)).getSingleResult();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new HashMap<>();
  }

  @Step("Get close price adjusted data")
  public static List<HashMap<String, Object>> getTicker() {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" SELECT  TICKER, REVIEWED_DATE,MA_5  ");
    queryBuilder.append(" FROM RISK_ANALYST_MARGIN_REVIEWED_FULL  ");
    try {

      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

}
