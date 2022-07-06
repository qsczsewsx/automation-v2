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
@Table(name = "MonthlyPort_BondBal")
public class MonthlyPortBondBalEntity {
  private Date dateReport;
  @Id
  private String custodyCd;
  private String bondCode;
  private Double balanceQuantity;
  private Integer principal;
  private Date expiredDate;
  private String tradingCode;
  private Integer depositoryStatus;
  private Integer etlCurDate;
  private Timestamp etlRunDateTime;

  @Step("Get data")
  public static List<HashMap<String, Object>> getBondBal(String custodyCd, Integer type) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" select custodyCd, bondcode, balancequantity, principal, expireddate, tradingCode  ");
    queryStringBuilder.append(" from MonthlyPort_BondBal  ");
    queryStringBuilder.append(" where DepositoryStatus = :type and CustodyCD = :custodyCd  ");

    try {
      List<HashMap<String, Object>> result = DbApi2.dbApiDb2Connection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("custodyCd", custodyCd)
        .setParameter("type", type)
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
  @Column(name = "BondCode")
  public String getBondCode() {
    return bondCode;
  }

  public void setBondCode(String bondCode) {
    this.bondCode = bondCode;
  }

  @Basic
  @Column(name = "BalanceQuantity")
  public Double getBalanceQuantity() {
    return balanceQuantity;
  }

  public void setBalanceQuantity(Double balanceQuantity) {
    this.balanceQuantity = balanceQuantity;
  }

  @Basic
  @Column(name = "Principal")
  public Integer getPrincipal() {
    return principal;
  }

  public void setPrincipal(Integer principal) {
    this.principal = principal;
  }

  @Basic
  @Column(name = "ExpiredDate")
  public Date getExpiredDate() {
    return expiredDate;
  }

  public void setExpiredDate(Date expiredDate) {
    this.expiredDate = expiredDate;
  }

  @Basic
  @Column(name = "TradingCode")
  public String getTradingCode() {
    return tradingCode;
  }

  public void setTradingCode(String tradingCode) {
    this.tradingCode = tradingCode;
  }

  @Basic
  @Column(name = "DepositoryStatus")
  public Integer getDepositoryStatus() {
    return depositoryStatus;
  }

  public void setDepositoryStatus(Integer depositoryStatus) {
    this.depositoryStatus = depositoryStatus;
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
    MonthlyPortBondBalEntity that = (MonthlyPortBondBalEntity) o;
    return Objects.equals(dateReport, that.dateReport) &&
      Objects.equals(custodyCd, that.custodyCd) &&
      Objects.equals(bondCode, that.bondCode) &&
      Objects.equals(balanceQuantity, that.balanceQuantity) &&
      Objects.equals(principal, that.principal) &&
      Objects.equals(expiredDate, that.expiredDate) &&
      Objects.equals(tradingCode, that.tradingCode) &&
      Objects.equals(depositoryStatus, that.depositoryStatus) &&
      Objects.equals(etlCurDate, that.etlCurDate) &&
      Objects.equals(etlRunDateTime, that.etlRunDateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dateReport, custodyCd, bondCode, balanceQuantity, principal, expiredDate, tradingCode, depositoryStatus, etlCurDate, etlRunDateTime);
  }

}
