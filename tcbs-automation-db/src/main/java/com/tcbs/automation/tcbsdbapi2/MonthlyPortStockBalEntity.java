package com.tcbs.automation.tcbsdbapi2;

import com.tcbs.automation.dwh.Dwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "MonthlyPort_StockBal")
public class MonthlyPortStockBalEntity {
  private Timestamp dateReport;
  @Id
  private String custodyCd;
  private String symbol;
  private String qttType;
  private Integer basicPrice;
  private Double costPrice;
  private Double quantity;
  private Double mktAmt;
  private Integer etlCurDate;
  private Timestamp etlRunDateTime;

  @Step("Get data")
  public static List<HashMap<String, Object>> getStockBal(String custodyCd) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" select custodyCd, symbol, quantity, qttType, costPrice, basicPrice, mktamt  ");
    queryStringBuilder.append(" from MonthlyPort_StockBal   ");
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
  public Timestamp getDateReport() {
    return dateReport;
  }

  public void setDateReport(Timestamp dateReport) {
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
  @Column(name = "Symbol")
  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  @Basic
  @Column(name = "QttType")
  public String getQttType() {
    return qttType;
  }

  public void setQttType(String qttType) {
    this.qttType = qttType;
  }

  @Basic
  @Column(name = "BasicPrice")
  public Integer getBasicPrice() {
    return basicPrice;
  }

  public void setBasicPrice(Integer basicPrice) {
    this.basicPrice = basicPrice;
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
  @Column(name = "Quantity")
  public Double getQuantity() {
    return quantity;
  }

  public void setQuantity(Double quantity) {
    this.quantity = quantity;
  }

  @Basic
  @Column(name = "MktAmt")
  public Double getMktAmt() {
    return mktAmt;
  }

  public void setMktAmt(Double mktAmt) {
    this.mktAmt = mktAmt;
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
    MonthlyPortStockBalEntity that = (MonthlyPortStockBalEntity) o;
    return Objects.equals(dateReport, that.dateReport) &&
      Objects.equals(custodyCd, that.custodyCd) &&
      Objects.equals(symbol, that.symbol) &&
      Objects.equals(qttType, that.qttType) &&
      Objects.equals(basicPrice, that.basicPrice) &&
      Objects.equals(costPrice, that.costPrice) &&
      Objects.equals(quantity, that.quantity) &&
      Objects.equals(mktAmt, that.mktAmt) &&
      Objects.equals(etlCurDate, that.etlCurDate) &&
      Objects.equals(etlRunDateTime, that.etlRunDateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dateReport, custodyCd, symbol, qttType, basicPrice, costPrice, quantity, mktAmt, etlCurDate, etlRunDateTime);
  }

}
