package com.tcbs.automation.tcbsdbapi2;

import com.tcbs.automation.dwh.Dwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "MonthlyPort_FundBal")
public class MonthlyPortFundBalEntity {
  private Date dateReport;
  @Id
  private String custodyCd;
  private String product;
  private Double balance;
  private Double costPrice;
  private Double costAmount;
  private Double currPrice;
  private Double currAmt;
  private Integer etlCurDate;
  private Timestamp etlRunDateTime;

  @Step("Get data")
  public static List<HashMap<String, Object>> getFundBal(String custodyCd) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" select custodyCd, product, balance, costprice, currprice, CurrAmt  ");
    queryStringBuilder.append(" from MonthlyPort_FundBal   ");
    queryStringBuilder.append(" where CustodyCD = :custodyCd  ");

    try {
      List<HashMap<String, Object>> result = DbApi2.dbApiDb2Connection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("custodyCd", custodyCd)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

      Dwh.dwhDbConnection.closeSession();
      return result;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Basic
  @Column(name = "DateReport")
  public Date getDateReport() {
    return dateReport;
  }

  public void setDateReport(Date dateReport) {
    this.dateReport = dateReport;
  }

  @Basic
  @Column(name = "CustodyCD")
  public String getCustodyCd() {
    return custodyCd;
  }

  public void setCustodyCd(String custodyCd) {
    this.custodyCd = custodyCd;
  }

  @Basic
  @Column(name = "Product")
  public String getProduct() {
    return product;
  }

  public void setProduct(String product) {
    this.product = product;
  }

  @Basic
  @Column(name = "Balance")
  public Double getBalance() {
    return balance;
  }

  public void setBalance(Double balance) {
    this.balance = balance;
  }

  @Basic
  @Column(name = "CostPrice")
  public Double getCostPrice() {
    return costPrice;
  }

  public void setCostPrice(Double costPrice) {
    this.costPrice = costPrice;
  }

  @Basic
  @Column(name = "CostAmount")
  public Double getCostAmount() {
    return costAmount;
  }

  public void setCostAmount(Double costAmount) {
    this.costAmount = costAmount;
  }

  @Basic
  @Column(name = "CurrPrice")
  public Double getCurrPrice() {
    return currPrice;
  }

  public void setCurrPrice(Double currPrice) {
    this.currPrice = currPrice;
  }

  @Basic
  @Column(name = "CurrAmt")
  public Double getCurrAmt() {
    return currAmt;
  }

  public void setCurrAmt(Double currAmt) {
    this.currAmt = currAmt;
  }

  @Basic
  @Column(name = "EtlCurDate")
  public Integer getEtlCurDate() {
    return etlCurDate;
  }

  public void setEtlCurDate(Integer etlCurDate) {
    this.etlCurDate = etlCurDate;
  }

  @Basic
  @Column(name = "EtlRunDateTime")
  public Timestamp getEtlRunDateTime() {
    return etlRunDateTime;
  }

  public void setEtlRunDateTime(Timestamp etlRunDateTime) {
    this.etlRunDateTime = etlRunDateTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MonthlyPortFundBalEntity that = (MonthlyPortFundBalEntity) o;
    return Objects.equals(dateReport, that.dateReport) &&
      Objects.equals(custodyCd, that.custodyCd) &&
      Objects.equals(product, that.product) &&
      Objects.equals(balance, that.balance) &&
      Objects.equals(costPrice, that.costPrice) &&
      Objects.equals(costAmount, that.costAmount) &&
      Objects.equals(currPrice, that.currPrice) &&
      Objects.equals(currAmt, that.currAmt) &&
      Objects.equals(etlCurDate, that.etlCurDate) &&
      Objects.equals(etlRunDateTime, that.etlRunDateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dateReport, custodyCd, product, balance, costPrice, costAmount, currPrice, currAmt, etlCurDate, etlRunDateTime);
  }

}
