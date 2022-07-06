package com.tcbs.automation.dwh;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import wiremock.org.apache.commons.collections4.CollectionUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CusPortfolioPerformanceInPeriodEntity {
  private String custodyCd;
  @Id
  private String histDate;
  private BigDecimal denominator;
  private BigDecimal numerator;
  private BigDecimal portfolioIndex;
  private BigDecimal accumulatedProfit;
  private BigInteger rn;
  private Double dailyBuyAmt;
  private Double dailySellAmt;
  private Double netTradeAmt;
  private Double dailyTotalAmt;
  private String txnDetail;

  @Step("select To Date")
  public static Date getLatestDateByCus(String custodyCd) {
    StringBuilder queryToDate = new StringBuilder();
    queryToDate.append("  SELECT MAX(HistDate) ");
    queryToDate.append("  FROM prc_stock_indices ");
    queryToDate.append("  WHERE CustodyCd = :custodyCd ");
    try {
      Object resultDate = Dwh.dwhDbConnection.getSession().createNativeQuery(queryToDate.toString())
        .setParameter("custodyCd", custodyCd)
        .getSingleResult();
      return (Date) resultDate;

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return null;
  }

  @Step("select data")
  public static List<CusPortfolioPerformanceInPeriodEntity> getPortfolioInfo(String custodyCd, String fromDate, String toDate) {
    StringBuilder queryPorfolio = new StringBuilder();
    queryPorfolio.append("  SELECT si.CustodyCd, si.HistDate, si.Denominator, si.Numerator, si.PortfolioIndex, si.AccumulatedProfit, si.rn, ");
    queryPorfolio.append("    DailyBuyAmt, DailySellAmt, NetTradeAmt, DailyTotalAmt, TxnDetail ");
    queryPorfolio.append("  FROM ( ");
    queryPorfolio.append("    SELECT * , ROW_NUMBER() OVER( PARTITION BY CustodyCd, HistDate ORDER BY HistDate desc) as rn ");
    queryPorfolio.append("    FROM ( ");
    queryPorfolio.append("      SELECT CustodyCd, HistDate, Denominator, Numerator, PortfolioIndex, AccumulatedProfit ");
    queryPorfolio.append("      FROM prc_stock_indices ");
    queryPorfolio.append("      WHERE CustodyCd = :custodyCd ");
    queryPorfolio.append("  AND HistDate >= :fromDate ");
    queryPorfolio.append("  AND HistDate < dateadd(day,1,:toDate) ");
    queryPorfolio.append("  	) si0 ");
    queryPorfolio.append("  ) si ");
    queryPorfolio.append("  LEFT JOIN ( ");
    queryPorfolio.append("    SELECT * , ROW_NUMBER() OVER( PARTITION BY CustodyCd, BusDate ORDER BY BusDate desc) as rn ");
    queryPorfolio.append("  FROM ( ");
    queryPorfolio.append("    SELECT CustodyCd, BusDate, DailyBuyAmt, DailySellAmt, DailyBuyAmt - DailySellAmt as NetTradeAmt, DailyTotalAmt, TxnDetail ");
    queryPorfolio.append("    FROM prc_stock_txndetails ");
    queryPorfolio.append("    WHERE CustodyCd = :custodyCd ");
    queryPorfolio.append("  AND BusDate >= :fromDate ");
    queryPorfolio.append("  AND BusDate < dateadd(day,1,:toDate) ");
    queryPorfolio.append("  	) st0 ");
    queryPorfolio.append("  ) st ");
    queryPorfolio.append("  on si.CustodyCd = st.CustodyCd AND si.HistDate = st.BusDate AND st.rn = 1 ");
    queryPorfolio.append("  WHERE si.rn = 1 ");

    List<CusPortfolioPerformanceInPeriodEntity> listResult = new ArrayList<>();
    try {
      List<Object[]> resultPortfolio = Dwh.dwhDbConnection.getSession().createNativeQuery(queryPorfolio.toString())
        .setParameter("custodyCd", custodyCd)
        .setParameter("toDate", toDate)
        .setParameter("fromDate", fromDate)
        .getResultList();

      if (CollectionUtils.isNotEmpty(resultPortfolio)) {
        resultPortfolio.forEach(object -> {
            CusPortfolioPerformanceInPeriodEntity info = CusPortfolioPerformanceInPeriodEntity.builder()
              .custodyCd((String) object[0])
              .histDate(new SimpleDateFormat("yyyy-MM-dd").format(object[1]))
              .denominator((BigDecimal) object[2])
              .numerator((BigDecimal) object[3])
              .portfolioIndex((BigDecimal) object[4])
              .accumulatedProfit((BigDecimal) object[5])
              .rn((BigInteger) object[6])
              .dailyBuyAmt((Double) object[7])
              .dailySellAmt((Double) object[8])
              .netTradeAmt((Double) object[9])
              .dailyTotalAmt((Double) object[10])
              .txnDetail((String) object[11])
              .build();
            listResult.add(info);
          }
        );
        return listResult;
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
