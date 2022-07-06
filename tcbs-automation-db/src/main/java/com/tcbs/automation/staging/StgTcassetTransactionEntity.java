package com.tcbs.automation.staging;

import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Stg_tcasset_TRANSACTION")
public class StgTcassetTransactionEntity {
  private double id;
  private String transactionCode;
  private double portfolioId;
  private double action;
  private String ticker;
  private Double productId;
  private BigDecimal unit;
  private BigDecimal price;
  private BigDecimal fee;
  private Double currencyId;
  private BigDecimal yield;
  private Double broker;
  private Double counterParty;
  private String domainCode;
  private String params;
  private Date transactionDate;
  private Date settlementDate;
  private Date paymentDate;
  private Double cashStatus;
  private Double assetBankAccountId;
  private Date createdTimestamp;
  private Date updatedTimestamp;
  private BigDecimal taxRate;
  private Short typeTransaction;
  private Date repoDate;
  private BigDecimal repoPrice;
  private Double refId;
  private Integer etlCurDate;
  private Timestamp etlRunDateTime;

  public static List<HashMap<String, Object>> sumUpTransaction(String reportDate) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append("EXEC proc_idata_pe_getTrans_FunStation @TransDate = :reportDate");

    List<HashMap<String, Object>> listResult = Staging.stagingDbConnection.getSession().createNativeQuery(queryBuilder.toString())
      .setParameter("reportDate", reportDate)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    return listResult;
  }

  @Id
  @Column(name = "ID")
  public double getId() {
    return id;
  }

  public void setId(double id) {
    this.id = id;
  }

  @Basic
  @Column(name = "TRANSACTION_CODE")
  public String getTransactionCode() {
    return transactionCode;
  }

  public void setTransactionCode(String transactionCode) {
    this.transactionCode = transactionCode;
  }

  @Basic
  @Column(name = "PORTFOLIO_ID")
  public double getPortfolioId() {
    return portfolioId;
  }

  public void setPortfolioId(double portfolioId) {
    this.portfolioId = portfolioId;
  }

  @Basic
  @Column(name = "ACTION")
  public double getAction() {
    return action;
  }

  public void setAction(double action) {
    this.action = action;
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
  @Column(name = "PRODUCT_ID")
  public Double getProductId() {
    return productId;
  }

  public void setProductId(Double productId) {
    this.productId = productId;
  }

  @Basic
  @Column(name = "UNIT")
  public BigDecimal getUnit() {
    return unit;
  }

  public void setUnit(BigDecimal unit) {
    this.unit = unit;
  }

  @Basic
  @Column(name = "PRICE")
  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  @Basic
  @Column(name = "FEE")
  public BigDecimal getFee() {
    return fee;
  }

  public void setFee(BigDecimal fee) {
    this.fee = fee;
  }

  @Basic
  @Column(name = "CURRENCY_ID")
  public Double getCurrencyId() {
    return currencyId;
  }

  public void setCurrencyId(Double currencyId) {
    this.currencyId = currencyId;
  }

  @Basic
  @Column(name = "YIELD")
  public BigDecimal getYield() {
    return yield;
  }

  public void setYield(BigDecimal yield) {
    this.yield = yield;
  }

  @Basic
  @Column(name = "BROKER")
  public Double getBroker() {
    return broker;
  }

  public void setBroker(Double broker) {
    this.broker = broker;
  }

  @Basic
  @Column(name = "COUNTER_PARTY")
  public Double getCounterParty() {
    return counterParty;
  }

  public void setCounterParty(Double counterParty) {
    this.counterParty = counterParty;
  }

  @Basic
  @Column(name = "DOMAIN_CODE")
  public String getDomainCode() {
    return domainCode;
  }

  public void setDomainCode(String domainCode) {
    this.domainCode = domainCode;
  }

  @Basic
  @Column(name = "PARAMS")
  public String getParams() {
    return params;
  }

  public void setParams(String params) {
    this.params = params;
  }

  @Basic
  @Column(name = "TRANSACTION_DATE")
  public Date getTransactionDate() {
    return transactionDate;
  }

  public void setTransactionDate(Date transactionDate) {
    this.transactionDate = transactionDate;
  }

  @Basic
  @Column(name = "SETTLEMENT_DATE")
  public Date getSettlementDate() {
    return settlementDate;
  }

  public void setSettlementDate(Date settlementDate) {
    this.settlementDate = settlementDate;
  }

  @Basic
  @Column(name = "PAYMENT_DATE")
  public Date getPaymentDate() {
    return paymentDate;
  }

  public void setPaymentDate(Date paymentDate) {
    this.paymentDate = paymentDate;
  }

  @Basic
  @Column(name = "CASH_STATUS")
  public Double getCashStatus() {
    return cashStatus;
  }

  public void setCashStatus(Double cashStatus) {
    this.cashStatus = cashStatus;
  }

  @Basic
  @Column(name = "ASSET_BANK_ACCOUNT_ID")
  public Double getAssetBankAccountId() {
    return assetBankAccountId;
  }

  public void setAssetBankAccountId(Double assetBankAccountId) {
    this.assetBankAccountId = assetBankAccountId;
  }

  @Basic
  @Column(name = "CREATED_TIMESTAMP")
  public Date getCreatedTimestamp() {
    return createdTimestamp;
  }

  public void setCreatedTimestamp(Date createdTimestamp) {
    this.createdTimestamp = createdTimestamp;
  }

  @Basic
  @Column(name = "UPDATED_TIMESTAMP")
  public Date getUpdatedTimestamp() {
    return updatedTimestamp;
  }

  public void setUpdatedTimestamp(Date updatedTimestamp) {
    this.updatedTimestamp = updatedTimestamp;
  }

  @Basic
  @Column(name = "TAX_RATE")
  public BigDecimal getTaxRate() {
    return taxRate;
  }

  public void setTaxRate(BigDecimal taxRate) {
    this.taxRate = taxRate;
  }

  @Basic
  @Column(name = "TYPE_TRANSACTION")
  public Short getTypeTransaction() {
    return typeTransaction;
  }

  public void setTypeTransaction(Short typeTransaction) {
    this.typeTransaction = typeTransaction;
  }

  @Basic
  @Column(name = "REPO_DATE")
  public Date getRepoDate() {
    return repoDate;
  }

  public void setRepoDate(Date repoDate) {
    this.repoDate = repoDate;
  }

  @Basic
  @Column(name = "REPO_PRICE")
  public BigDecimal getRepoPrice() {
    return repoPrice;
  }

  public void setRepoPrice(BigDecimal repoPrice) {
    this.repoPrice = repoPrice;
  }

  @Basic
  @Column(name = "REF_ID")
  public Double getRefId() {
    return refId;
  }

  public void setRefId(Double refId) {
    this.refId = refId;
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
    StgTcassetTransactionEntity that = (StgTcassetTransactionEntity) o;
    return Double.compare(that.id, id) == 0 &&
      Double.compare(that.portfolioId, portfolioId) == 0 &&
      Double.compare(that.action, action) == 0 &&
      Objects.equals(transactionCode, that.transactionCode) &&
      Objects.equals(ticker, that.ticker) &&
      Objects.equals(productId, that.productId) &&
      Objects.equals(unit, that.unit) &&
      Objects.equals(price, that.price) &&
      Objects.equals(fee, that.fee) &&
      Objects.equals(currencyId, that.currencyId) &&
      Objects.equals(yield, that.yield) &&
      Objects.equals(broker, that.broker) &&
      Objects.equals(counterParty, that.counterParty) &&
      Objects.equals(domainCode, that.domainCode) &&
      Objects.equals(params, that.params) &&
      Objects.equals(transactionDate, that.transactionDate) &&
      Objects.equals(settlementDate, that.settlementDate) &&
      Objects.equals(paymentDate, that.paymentDate) &&
      Objects.equals(cashStatus, that.cashStatus) &&
      Objects.equals(assetBankAccountId, that.assetBankAccountId) &&
      Objects.equals(createdTimestamp, that.createdTimestamp) &&
      Objects.equals(updatedTimestamp, that.updatedTimestamp) &&
      Objects.equals(taxRate, that.taxRate) &&
      Objects.equals(typeTransaction, that.typeTransaction) &&
      Objects.equals(repoDate, that.repoDate) &&
      Objects.equals(repoPrice, that.repoPrice) &&
      Objects.equals(refId, that.refId) &&
      Objects.equals(etlCurDate, that.etlCurDate) &&
      Objects.equals(etlRunDateTime, that.etlRunDateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, transactionCode, portfolioId, action, ticker, productId, unit, price, fee, currencyId, yield, broker, counterParty, domainCode, params, transactionDate, settlementDate,
      paymentDate, cashStatus, assetBankAccountId, createdTimestamp, updatedTimestamp, taxRate, typeTransaction, repoDate, repoPrice, refId, etlCurDate, etlRunDateTime);
  }

  @Step("insert data")
  public boolean saveTransaction(StgTcassetTransactionEntity trans) {
    Session session = Staging.stagingDbConnection.getSession();
    session.beginTransaction();
    session.save(trans);
    session.getTransaction().commit();
    return true;
  }

  @Step("delete data by object")
  public void deleteTransaction(StgTcassetTransactionEntity trans) {
    Session session = Staging.stagingDbConnection.getSession();
    session.beginTransaction();
    Query query = session.createQuery("DELETE StgTcassetTransactionEntity WHERE transactionDate = :transactionDate ");
    query.setParameter("transactionDate", trans.getTransactionDate());
    query.executeUpdate();
    session.getTransaction().commit();
    Staging.stagingDbConnection.closeSession();
  }
}
