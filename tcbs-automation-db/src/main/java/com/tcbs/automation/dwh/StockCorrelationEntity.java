package com.tcbs.automation.dwh;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.steps.StepEventBus;
import wiremock.org.apache.commons.collections4.CollectionUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class StockCorrelationEntity {
  private String stock;
  @Id
  private String relatedStock;
  private Double count;
  private Double countRank;
  private Timestamp dateTime;
  private BigInteger countHolding;
  private Double percentage;


  public static List<StockCorrelationEntity> getByStockAndType(String ticker, String type) {
    StringBuilder queryStringBuilder = new StringBuilder();
    if (type.equals("top")) {
      queryStringBuilder.append("select top(10) ticker as Stock, [Other Ticker] as RelatedStock, Correlation as Count, Corr_Rank,  \r\n");
      queryStringBuilder.append("Count_Holding, Percentage \r\n");
      queryStringBuilder.append("from StockCorrelation sc \r\n");
      queryStringBuilder.append("left join StockHoldCount sh on sc.[Other Ticker] = sh.Stock_Ticker \r\n");
      queryStringBuilder.append("where len(sc.[Other Ticker]) = 3 and Ticker = :ticker \r\n");
      queryStringBuilder.append("order by Corr_Rank ; \r\n");
    } else {
      queryStringBuilder.append("select sc.Stock, sc.RelatedStock, sc.Count, sc.CountRank, Count_Holding, Percentage  \r\n");
      queryStringBuilder.append("from ( \r\n");
      queryStringBuilder.append("select top(10) ticker as Stock, [Other Ticker] as RelatedStock, Correlation as Count, Corr_Rank as CountRank \r\n");
      queryStringBuilder.append("from StockCorrelation  \r\n");
      queryStringBuilder.append("where len([Other Ticker]) = 3 and Ticker = :ticker \r\n");
      queryStringBuilder.append("order by Corr_Rank desc) sc \r\n");
      queryStringBuilder.append("left join StockHoldCount sh on sc.relatedStock = sh.Stock_Ticker \r\n");
      queryStringBuilder.append("order by countRank asc; \r\n");
    }

    List<StockCorrelationEntity> listResult = new ArrayList<>();
    try {
      List<Object[]> result = Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("ticker", ticker)
        .getResultList();

      if (CollectionUtils.isNotEmpty(result)) {
        result.forEach(object -> {
            StockCorrelationEntity stockInfo = StockCorrelationEntity.builder()
              .stock((String) object[0])
              .relatedStock((String) object[1])
              .count((Double) object[2])
              .countRank((Double) object[3])
              .countHolding((BigInteger) object[4])
              .percentage((Double) object[5])
              .build();
            listResult.add(stockInfo);
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
