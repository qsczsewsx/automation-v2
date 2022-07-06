package com.tcbs.automation.iris.loanreview;

import com.tcbs.automation.riskcloud.AwsRiskCloud;
import lombok.*;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name = "risk_margin_customer_detail")
public class MarginCustomerDetailEntity {
  @Id
  private String customerID;
  private Double asset;
  private Double netDebt;
  private Double loanRatio;
  private Double rateFSS;
  private Double vaR;
  private Double dayToLiquiDate;
  private String ticker1;
  private Double pctTicker1;
  private Double mcLimit;

  @Step("insert data")
  public static void insertData(MarginCustomerDetailEntity entity) {
    Session session = AwsRiskCloud.awsRiskCloudDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    StringBuilder queryStringBuilder = new StringBuilder();
    Query<?> query;

    queryStringBuilder.append(" INSERT INTO risk_margin_customer_detail (UpdateDate,CustomerID,Asset,NetDebt,LoanRatio,RateFSS,VaR,DaysToLiquidate,Ticker1,PctTicker1,MCLimit) ");
    queryStringBuilder.append(" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");
    query = session.createNativeQuery(queryStringBuilder.toString());
    query.setParameter(1, getMaxDate());
    query.setParameter(2, entity.getCustomerId());
    query.setParameter(3, entity.getAsset());
    query.setParameter(4, entity.getNetDebt());
    query.setParameter(5, entity.getLoanRatio());
    query.setParameter(6, entity.getRateFSS());
    query.setParameter(7, entity.getVaR());
    query.setParameter(8, entity.getDaysToLiquidate());
    query.setParameter(9, entity.getTicker1());
    query.setParameter(10, entity.getPctTicker1());
    query.setParameter(11, entity.getMCLimit());
    query.executeUpdate();
    trans.commit();
  }

  @Step("delete data by object")
  public static void deleteData(MarginCustomerDetailEntity entity) {
    Session session = AwsRiskCloud.awsRiskCloudDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    Query<?> query;

    query = session.createNativeQuery(" DELETE from risk_margin_customer_detail where CustomerID = :customerId");
    query.setParameter("customerId", entity.getCustomerId());
    query.executeUpdate();
    trans.commit();
  }

  @Step("get max date data in risk_margin_customer_detail table")
  public static LocalDateTime getMaxDate() {
    StringBuilder query = new StringBuilder();
    LocalDateTime maxDate = null;
    query.append(" select DISTINCT updatedate from risk_margin_customer_detail where  updatedate = (select max(updatedate) from risk_margin_customer_detail) ");
    try {
      HashMap<String, Object> date = (HashMap<String, Object>) AwsRiskCloud.awsRiskCloudDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList().get(0);
      String dateTime = date.get("updatedate").toString().replace(".0", "");
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      maxDate = LocalDateTime.parse(dateTime, formatter);
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return maxDate;
  }

  @Step("get max date data in risk_margin_customer_detail table")
  public static HashMap<String, Object> getMarginCustomerDetail(String customerId) {
    StringBuilder query = new StringBuilder();
    query.append(" select * from risk_margin_customer_detail where  updatedate = (select max(updatedate) from risk_margin_customer_detail) and customerid = :customerId ");
    try {
      HashMap<String, Object> marginCustomer = (HashMap<String, Object>) AwsRiskCloud.awsRiskCloudDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("customerId", customerId)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList().get(0);
      return marginCustomer;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new HashMap<>();
  }


  @Basic
  @Column(name = "CustomerID")
  public String getCustomerId() {
    return customerID;
  }

  public void setCustomerId(String customerID) {
    this.customerID = customerID;
  }

  @Basic
  @Column(name = "Asset")
  public Double getAsset() {
    return asset;
  }

  public void setAsset(Double asset) {
    this.asset = asset;
  }

  @Basic
  @Column(name = "NetDebt")
  public Double getNetDebt() {
    return netDebt;
  }

  public void setNetDebt(Double netDebt) {
    this.netDebt = netDebt;
  }

  @Basic
  @Column(name = "LoanRatio")
  public Double getLoanRatio() {
    return loanRatio;
  }

  public void setLoanRatio(Double loanRatio) {
    this.loanRatio = loanRatio;
  }

  @Basic
  @Column(name = "RateFSS")
  public Double getRateFSS() {
    return rateFSS;
  }

  public void setRateFSS(Double rateFSS) {
    this.rateFSS = rateFSS;
  }

  @Basic
  @Column(name = "VaR")
  public Double getVaR() {
    return vaR;
  }

  public void setVaR(Double vaR) {
    this.vaR = vaR;
  }

  @Basic
  @Column(name = "DaysToLiquidate")
  public Double getDaysToLiquidate() {
    return dayToLiquiDate;
  }

  public void setDaysToLiquidate(Double dayToLiquiDate) {
    this.dayToLiquiDate = dayToLiquiDate;
  }

  @Basic
  @Column(name = "Ticker1")
  public String getTicker1() {
    return ticker1;
  }

  public void setTicker1(String ticker1) {
    this.ticker1 = ticker1;
  }

  @Basic
  @Column(name = "MCLimit")
  public Double getMCLimit() {
    return mcLimit;
  }

  public void setMCLimit(Double mcLimit) {
    this.mcLimit = mcLimit;
  }

  @Basic
  @Column(name = "PctTicker1")
  public Double getPctTicker1() {
    return pctTicker1;
  }

  public void setPctTicker1(Double pctTicker1) {
    this.pctTicker1 = pctTicker1;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MarginCustomerDetailEntity that = (MarginCustomerDetailEntity) o;
    return Objects.equals(customerID, that.customerID) && Objects.equals(asset, that.asset) && Objects.equals(netDebt, that.netDebt) && Objects.equals(rateFSS,
      that.rateFSS) && Objects.equals(vaR, that.vaR) && Objects.equals(dayToLiquiDate, that.dayToLiquiDate) && Objects.equals(ticker1,
      that.ticker1) && Objects.equals(mcLimit, that.mcLimit) && Objects.equals(pctTicker1, that.pctTicker1);
  }

  @Override
  public int hashCode() {
    return Objects.hash(customerID, asset, netDebt, rateFSS, vaR, dayToLiquiDate, ticker1, pctTicker1, mcLimit);
  }
}

