package com.tcbs.automation.dwh;

import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.List;

import static com.tcbs.automation.dwh.Dwh.dwhDbConnection;


@Entity
@Table(name = "StockHoldCount", schema = "dbo", catalog = "[tcbs-dwh]")
public class StockHoldCountEntity {
  @Id
  @Column(name = "Stock_Ticker")
  private String stockTicker;
  @Column(name = "Count_Holding")
  private Long countHolding;
  @Column(name = "Percentage")
  private Double percentage;
  @Column(name = "DateTime")
  private Timestamp dateTime;

  @Step
  public static List<StockHoldCountEntity> getByStock(List<String> stocks) {
    Query<StockHoldCountEntity> query = dwhDbConnection.getSession().createQuery(
      "from StockHoldCountEntity a where a.stockTicker in :stocks ", StockHoldCountEntity.class);
    query.setParameter("stocks", stocks);
    List<StockHoldCountEntity> result = query.getResultList();
    return result;
  }

  public String getStockTicker() {
    return stockTicker;
  }

  public void setStockTicker(String stockTicker) {
    this.stockTicker = stockTicker;
  }

  public Long getCountHolding() {
    return countHolding;
  }

  public void setCountHolding(Long countHolding) {
    this.countHolding = countHolding;
  }

  public Double getPercentage() {
    return percentage;
  }

  public void setPercentage(Double percentage) {
    this.percentage = percentage;
  }

  public Timestamp getDateTime() {
    return dateTime;
  }

  public void setDateTime(Timestamp dateTime) {
    this.dateTime = dateTime;
  }

}
