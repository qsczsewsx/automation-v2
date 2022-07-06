package com.tcbs.automation.config.idatatcanalysis.performance;

import com.tcbs.automation.config.idatatcanalysis.IData;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "PE_PERFORMANCE_SNAPSHOT")
public class PePerformanceSnapshotEntity {
  @Id
  private String accountId;
  private Time reportDate;
  private Long dailyProfit;
  private Long accProfit;
  private Long numerator;
  private Long denominator;
  private Double portfIndex;

  public static Date getMaxReportDate(String reportDate) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" SELECT MAX(REPORT_DATE) AS REPORT_DATE ");
    queryBuilder.append(" FROM PE_PERFORMANCE_SNAPSHOT pups ");
    queryBuilder.append(" WHERE REPORT_DATE <= to_date(:reportDate, 'yyyy-MM-dd') ");

    List<HashMap<String, Object>> listResult = IData.idataDbConnection.getSession().createNativeQuery(queryBuilder.toString())
      .setParameter("reportDate", reportDate)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    IData.idataDbConnection.closeSession();
    if (CollectionUtils.isNotEmpty(listResult)) {
      listResult.get(0).get("REPORT_DATE");
    }
    return null;
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
  @Column(name = "REPORT_DATE")
  public Time getReportDate() {
    return reportDate;
  }

  public void setReportDate(Time reportDate) {
    this.reportDate = reportDate;
  }

  @Basic
  @Column(name = "DAILY_PROFIT")
  public Long getDailyProfit() {
    return dailyProfit;
  }

  public void setDailyProfit(Long dailyProfit) {
    this.dailyProfit = dailyProfit;
  }

  @Basic
  @Column(name = "ACC_PROFIT")
  public Long getAccProfit() {
    return accProfit;
  }

  public void setAccProfit(Long accProfit) {
    this.accProfit = accProfit;
  }

  @Basic
  @Column(name = "NUMERATOR")
  public Long getNumerator() {
    return numerator;
  }

  public void setNumerator(Long numerator) {
    this.numerator = numerator;
  }

  @Basic
  @Column(name = "DENOMINATOR")
  public Long getDenominator() {
    return denominator;
  }

  public void setDenominator(Long denominator) {
    this.denominator = denominator;
  }

  @Basic
  @Column(name = "PORTF_INDEX")
  public Double getPortfIndex() {
    return portfIndex;
  }

  public void setPortfIndex(Double portfIndex) {
    this.portfIndex = portfIndex;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PePerformanceSnapshotEntity that = (PePerformanceSnapshotEntity) o;
    return Objects.equals(accountId, that.accountId) &&
      Objects.equals(reportDate, that.reportDate) &&
      Objects.equals(dailyProfit, that.dailyProfit) &&
      Objects.equals(accProfit, that.accProfit) &&
      Objects.equals(numerator, that.numerator) &&
      Objects.equals(denominator, that.denominator) &&
      Objects.equals(portfIndex, that.portfIndex);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountId, reportDate, dailyProfit, accProfit, numerator, denominator, portfIndex);
  }
}
