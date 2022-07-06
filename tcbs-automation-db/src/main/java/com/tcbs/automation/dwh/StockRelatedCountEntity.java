package com.tcbs.automation.dwh;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.steps.StepEventBus;
import wiremock.org.apache.commons.collections4.CollectionUtils;

import javax.persistence.Basic;
import javax.persistence.Column;
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
public class StockRelatedCountEntity {
  private String stock;
  @Id
  private String relatedStock;
  private Double count;
  private Double countRank;
  private Timestamp dateTime;
  private BigInteger countHolding;
  private Double percentage;

  public static List<StockRelatedCountEntity> getByStock(String stock) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT r.Stock, r.[Related Stock] AS relatedStock, r.Count, r.Count_Rank, h.Count_Holding, h.Percentage FROM StockRelatedCount r \r\n");
    queryStringBuilder.append("INNER JOIN StockHoldCount h ON r.[Related Stock] = h.Stock_Ticker  \r\n");
    queryStringBuilder.append("WHERE r.Stock = :stock AND r.Count_Rank <= 10 \r\n");
    queryStringBuilder.append("UNION ALL \r\n");
    queryStringBuilder.append("SELECT Stock_Ticker AS Stock, null, null, null, Count_Holding, Percentage \r\n");
    queryStringBuilder.append(" FROM StockHoldCount WHERE Stock_Ticker = :stock ; \r\n");

    List<StockRelatedCountEntity> listResult = new ArrayList<>();
    try {
      List<Object[]> result = Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("stock", stock)
        .getResultList();

      if (CollectionUtils.isNotEmpty(result)) {
        result.forEach(object -> {
            StockRelatedCountEntity stockInfo = StockRelatedCountEntity.builder()
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

  @Basic
  @Column(name = "Stock")
  public String getStock() {
    return stock;
  }

  public void setStock(String stock) {
    this.stock = stock;
  }

  @Id
  @Column(name = "Related Stock")
  public String getRelatedStock() {
    return relatedStock;
  }

  public void setRelatedStock(String relatedStock) {
    this.relatedStock = relatedStock;
  }

  @Basic
  @Column(name = "Count")
  public Double getCount() {
    return count;
  }

  public void setCount(Double count) {
    this.count = count;
  }

  @Basic
  @Column(name = "Count_Rank")
  public Double getCountRank() {
    return countRank;
  }

  public void setCountRank(Double countRank) {
    this.countRank = countRank;
  }

  @Basic
  @Column(name = "DateTime")
  public Timestamp getDateTime() {
    return dateTime;
  }

  public void setDateTime(Timestamp dateTime) {
    this.dateTime = dateTime;
  }
}
