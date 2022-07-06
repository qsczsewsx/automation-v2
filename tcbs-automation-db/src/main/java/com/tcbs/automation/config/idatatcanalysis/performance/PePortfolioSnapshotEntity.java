package com.tcbs.automation.config.idatatcanalysis.performance;

import com.tcbs.automation.config.idatatcanalysis.IData;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.*;
import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "PE_PORTFOLIO_SNAPSHOT")
public class PePortfolioSnapshotEntity {
  @Id
  private String accountId;
  private String ticker;
  private Time reportDate;
  private Double openVolume;
  private Long openValue;
  private Double buyingVolume;
  private Long buyingValue;
  private Double sellingVolume;
  private Long sellingValue;
  private Double closeVolume;
  private Long closeValue;

  public static List<HashMap<String, Object>> getLatestSnapshot(String reportDate) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append("SELECT ACCOUNT_ID, TICKER, REPORT_DATE, CLOSE_VOLUME, COGS_PER_UNIT, COGS_PER_UNIT_ADJ ");
    queryBuilder.append("FROM PE_PORTFOLIO_SNAPSHOT WHERE REPORT_DATE = ");
    queryBuilder.append("(SELECT MAX(REPORT_DATE) FROM PE_PORTFOLIO_SNAPSHOT ");
    queryBuilder.append("WHERE REPORT_DATE < To_date(:reportDate, 'YYYY-MM-DD'))");

    List<HashMap<String, Object>> latestSnapshot = IData.idataDbConnection.getSession().createNativeQuery(queryBuilder.toString())
      .setParameter("reportDate", reportDate)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    IData.idataDbConnection.closeSession();
    return latestSnapshot;
  }

  @Basic
  @Column(name = "ACCOUNT_ID")
  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  @Basic
  @Column(name = "TICKER")
  public String getTicker() {
    return ticker;
  }

  public void setTicker(String ticker) {
    this.ticker = ticker;
  }

  @Basic
  @Column(name = "REPORT_DATE")
  public Time getReportDate() {
    return reportDate;
  }

  public void setReportDate(Time reportDate) {
    this.reportDate = reportDate;
  }

  @Basic
  @Column(name = "OPEN_VOLUME")
  public Double getOpenVolume() {
    return openVolume;
  }

  public void setOpenVolume(Double openVolume) {
    this.openVolume = openVolume;
  }

  @Basic
  @Column(name = "OPEN_VALUE")
  public Long getOpenValue() {
    return openValue;
  }

  public void setOpenValue(Long openValue) {
    this.openValue = openValue;
  }

  @Basic
  @Column(name = "BUYING_VOLUME")
  public Double getBuyingVolume() {
    return buyingVolume;
  }

  public void setBuyingVolume(Double buyingVolume) {
    this.buyingVolume = buyingVolume;
  }

  @Basic
  @Column(name = "BUYING_VALUE")
  public Long getBuyingValue() {
    return buyingValue;
  }

  public void setBuyingValue(Long buyingValue) {
    this.buyingValue = buyingValue;
  }

  @Basic
  @Column(name = "SELLING_VOLUME")
  public Double getSellingVolume() {
    return sellingVolume;
  }

  public void setSellingVolume(Double sellingVolume) {
    this.sellingVolume = sellingVolume;
  }

  @Basic
  @Column(name = "SELLING_VALUE")
  public Long getSellingValue() {
    return sellingValue;
  }

  public void setSellingValue(Long sellingValue) {
    this.sellingValue = sellingValue;
  }

  @Basic
  @Column(name = "CLOSE_VOLUME")
  public Double getCloseVolume() {
    return closeVolume;
  }

  public void setCloseVolume(Double closeVolume) {
    this.closeVolume = closeVolume;
  }

  @Basic
  @Column(name = "CLOSE_VALUE")
  public Long getCloseValue() {
    return closeValue;
  }

  public void setCloseValue(Long closeValue) {
    this.closeValue = closeValue;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PePortfolioSnapshotEntity that = (PePortfolioSnapshotEntity) o;
    return Objects.equals(accountId, that.accountId) &&
      Objects.equals(ticker, that.ticker) &&
      Objects.equals(reportDate, that.reportDate) &&
      Objects.equals(openVolume, that.openVolume) &&
      Objects.equals(openValue, that.openValue) &&
      Objects.equals(buyingVolume, that.buyingVolume) &&
      Objects.equals(buyingValue, that.buyingValue) &&
      Objects.equals(sellingVolume, that.sellingVolume) &&
      Objects.equals(sellingValue, that.sellingValue) &&
      Objects.equals(closeVolume, that.closeVolume) &&
      Objects.equals(closeValue, that.closeValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountId, ticker, reportDate, openVolume, openValue, buyingVolume, buyingValue, sellingVolume, sellingValue, closeVolume, closeValue);
  }
}
