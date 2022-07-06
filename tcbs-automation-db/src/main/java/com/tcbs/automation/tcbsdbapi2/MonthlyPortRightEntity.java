package com.tcbs.automation.tcbsdbapi2;

import com.tcbs.automation.dwh.Dwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "MonthlyPort_Right")
public class MonthlyPortRightEntity {
  private Date dateReport;
  @Id
  private String custodyCd;
  private Date reportDate;
  private String symbol;
  private String tyle;
  private BigDecimal slcksh;
  private BigDecimal slckcv;
  private Date dueDate;
  private BigDecimal exPrice;
  private BigDecimal caQtty;
  private Integer etlCurDate;
  private Timestamp etlRunDateTime;

  @Step("Get data")
  public static List<HashMap<String, Object>> getRight(String custodyCd) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" select custodyCd, CONVERT(varchar, duedate, 23) as dueDate, symbol, slcksh, tyle, slckcv, exprice, caqtty  ");
    queryStringBuilder.append(" from MonthlyPort_Right   ");
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
  @Column(name = "ReportDate")
  public Date getReportDate() {
    return reportDate;
  }

  public void setReportDate(Date reportDate) {
    this.reportDate = reportDate;
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
  @Column(name = "Tyle")
  public String getTyle() {
    return tyle;
  }

  public void setTyle(String tyle) {
    this.tyle = tyle;
  }

  @Basic
  @Column(name = "SLCKSH")
  public BigDecimal getSlcksh() {
    return slcksh;
  }

  public void setSlcksh(BigDecimal slcksh) {
    this.slcksh = slcksh;
  }

  @Basic
  @Column(name = "SLCKCV")
  public BigDecimal getSlckcv() {
    return slckcv;
  }

  public void setSlckcv(BigDecimal slckcv) {
    this.slckcv = slckcv;
  }

  @Basic
  @Column(name = "DueDate")
  public Date getDueDate() {
    return dueDate;
  }

  public void setDueDate(Date dueDate) {
    this.dueDate = dueDate;
  }

  @Basic
  @Column(name = "ExPrice")
  public BigDecimal getExPrice() {
    return exPrice;
  }

  public void setExPrice(BigDecimal exPrice) {
    this.exPrice = exPrice;
  }

  @Basic
  @Column(name = "CaQtty")
  public BigDecimal getCaQtty() {
    return caQtty;
  }

  public void setCaQtty(BigDecimal caQtty) {
    this.caQtty = caQtty;
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
    MonthlyPortRightEntity that = (MonthlyPortRightEntity) o;
    return Objects.equals(dateReport, that.dateReport) &&
      Objects.equals(custodyCd, that.custodyCd) &&
      Objects.equals(reportDate, that.reportDate) &&
      Objects.equals(symbol, that.symbol) &&
      Objects.equals(tyle, that.tyle) &&
      Objects.equals(slcksh, that.slcksh) &&
      Objects.equals(slckcv, that.slckcv) &&
      Objects.equals(dueDate, that.dueDate) &&
      Objects.equals(exPrice, that.exPrice) &&
      Objects.equals(caQtty, that.caQtty) &&
      Objects.equals(etlCurDate, that.etlCurDate) &&
      Objects.equals(etlRunDateTime, that.etlRunDateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dateReport, custodyCd, reportDate, symbol, tyle, slcksh, slckcv, dueDate, exPrice, caQtty, etlCurDate, etlRunDateTime);
  }

}
