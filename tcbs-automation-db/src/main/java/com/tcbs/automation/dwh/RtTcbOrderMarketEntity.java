package com.tcbs.automation.dwh;

import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "RT_tcb_ORDER_MARKET", schema = "dbo", catalog = "tcbs-dwh")
public class RtTcbOrderMarketEntity {
  private Long id;
  private int orderId;
  private String accountId;
  private Integer actionId;
  private Byte active;
  private Byte confirmStt;
  private String counterParty;
  private Timestamp createdDate;
  private Date expiredDate;
  private BigDecimal price;
  private String productCode;
  private Integer productId;
  private String purchaserId;
  private Integer referId;
  private Date startDate;
  private String status;
  private BigDecimal unitPrice;
  private Timestamp updatedDate;
  private Integer volume;
  private Integer remainVolume;
  private BigDecimal rate;
  private BigDecimal cashFlow;
  private BigDecimal agencyFee;
  private BigDecimal rmIncentive;
  private String orderCode;
  private BigDecimal principal;
  private BigDecimal bbprice;
  private Byte orderType;
  private String typeLogic;
  private Integer isDirect;
  private Integer counterOrder;
  private String referenceId;
  private Byte flexStatus;
  private BigDecimal cptyPrice;
  private String customContact;
  private String refNo;
  private String accountType;
  private BigDecimal fee;
  private BigDecimal tax;
  private BigDecimal investmentTimeByMonth;
  private String sellTradingCode;
  private Integer paperlessMarketStatus;
  private Byte listedBond;
  private Integer etlCurDate;
  private Timestamp etlRunDateTime;
  private Integer recommendedStatus;
  private String referee;
  private BigDecimal reinvestRate;
  private Integer optlock;
  private String bondCode;
  private String transactionId;
  private BigDecimal reinvestRate2;

  @Id
  @GeneratedValue
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Basic
  @Column(name = "ORDER_ID")
  public int getOrderId() {
    return orderId;
  }

  public void setOrderId(int orderId) {
    this.orderId = orderId;
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
  @Column(name = "ACTION_ID")
  public Integer getActionId() {
    return actionId;
  }

  public void setActionId(Integer actionId) {
    this.actionId = actionId;
  }

  @Basic
  @Column(name = "ACTIVE")
  public Byte getActive() {
    return active;
  }

  public void setActive(Byte active) {
    this.active = active;
  }

  @Basic
  @Column(name = "CONFIRM_STT")
  public Byte getConfirmStt() {
    return confirmStt;
  }

  public void setConfirmStt(Byte confirmStt) {
    this.confirmStt = confirmStt;
  }

  @Basic
  @Column(name = "COUNTER_PARTY")
  public String getCounterParty() {
    return counterParty;
  }

  public void setCounterParty(String counterParty) {
    this.counterParty = counterParty;
  }

  @Basic
  @Column(name = "CREATED_DATE")
  public Timestamp getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Timestamp createdDate) {
    this.createdDate = createdDate;
  }

  @Basic
  @Column(name = "EXPIRED_DATE")
  public Date getExpiredDate() {
    return expiredDate;
  }

  public void setExpiredDate(Date expiredDate) {
    this.expiredDate = expiredDate;
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
  @Column(name = "PRODUCT_CODE")
  public String getProductCode() {
    return productCode;
  }

  public void setProductCode(String productCode) {
    this.productCode = productCode;
  }

  @Basic
  @Column(name = "PRODUCT_ID")
  public Integer getProductId() {
    return productId;
  }

  public void setProductId(Integer productId) {
    this.productId = productId;
  }

  @Basic
  @Column(name = "PURCHASER_ID")
  public String getPurchaserId() {
    return purchaserId;
  }

  public void setPurchaserId(String purchaserId) {
    this.purchaserId = purchaserId;
  }

  @Basic
  @Column(name = "REFER_ID")
  public Integer getReferId() {
    return referId;
  }

  public void setReferId(Integer referId) {
    this.referId = referId;
  }

  @Basic
  @Column(name = "START_DATE")
  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  @Basic
  @Column(name = "STATUS")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @Basic
  @Column(name = "UNIT_PRICE")
  public BigDecimal getUnitPrice() {
    return unitPrice;
  }

  public void setUnitPrice(BigDecimal unitPrice) {
    this.unitPrice = unitPrice;
  }

  @Basic
  @Column(name = "UPDATED_DATE")
  public Timestamp getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(Timestamp updatedDate) {
    this.updatedDate = updatedDate;
  }

  @Basic
  @Column(name = "VOLUME")
  public Integer getVolume() {
    return volume;
  }

  public void setVolume(Integer volume) {
    this.volume = volume;
  }

  @Basic
  @Column(name = "REMAIN_VOLUME")
  public Integer getRemainVolume() {
    return remainVolume;
  }

  public void setRemainVolume(Integer remainVolume) {
    this.remainVolume = remainVolume;
  }

  @Basic
  @Column(name = "RATE")
  public BigDecimal getRate() {
    return rate;
  }

  public void setRate(BigDecimal rate) {
    this.rate = rate;
  }

  @Basic
  @Column(name = "CASH_FLOW")
  public BigDecimal getCashFlow() {
    return cashFlow;
  }

  public void setCashFlow(BigDecimal cashFlow) {
    this.cashFlow = cashFlow;
  }

  @Basic
  @Column(name = "AGENCY_FEE")
  public BigDecimal getAgencyFee() {
    return agencyFee;
  }

  public void setAgencyFee(BigDecimal agencyFee) {
    this.agencyFee = agencyFee;
  }

  @Basic
  @Column(name = "RM_INCENTIVE")
  public BigDecimal getRmIncentive() {
    return rmIncentive;
  }

  public void setRmIncentive(BigDecimal rmIncentive) {
    this.rmIncentive = rmIncentive;
  }

  @Basic
  @Column(name = "ORDER_CODE")
  public String getOrderCode() {
    return orderCode;
  }

  public void setOrderCode(String orderCode) {
    this.orderCode = orderCode;
  }

  @Basic
  @Column(name = "PRINCIPAL")
  public BigDecimal getPrincipal() {
    return principal;
  }

  public void setPrincipal(BigDecimal principal) {
    this.principal = principal;
  }

  @Basic
  @Column(name = "BBPRICE")
  public BigDecimal getBbprice() {
    return bbprice;
  }

  public void setBbprice(BigDecimal bbprice) {
    this.bbprice = bbprice;
  }

  @Basic
  @Column(name = "ORDER_TYPE")
  public Byte getOrderType() {
    return orderType;
  }

  public void setOrderType(Byte orderType) {
    this.orderType = orderType;
  }

  @Basic
  @Column(name = "TYPE_LOGIC")
  public String getTypeLogic() {
    return typeLogic;
  }

  public void setTypeLogic(String typeLogic) {
    this.typeLogic = typeLogic;
  }

  @Basic
  @Column(name = "IS_DIRECT")
  public Integer getIsDirect() {
    return isDirect;
  }

  public void setIsDirect(Integer isDirect) {
    this.isDirect = isDirect;
  }

  @Basic
  @Column(name = "COUNTER_ORDER")
  public Integer getCounterOrder() {
    return counterOrder;
  }

  public void setCounterOrder(Integer counterOrder) {
    this.counterOrder = counterOrder;
  }

  @Basic
  @Column(name = "REFERENCE_ID")
  public String getReferenceId() {
    return referenceId;
  }

  public void setReferenceId(String referenceId) {
    this.referenceId = referenceId;
  }

  @Basic
  @Column(name = "FLEX_STATUS")
  public Byte getFlexStatus() {
    return flexStatus;
  }

  public void setFlexStatus(Byte flexStatus) {
    this.flexStatus = flexStatus;
  }

  @Basic
  @Column(name = "CPTY_PRICE")
  public BigDecimal getCptyPrice() {
    return cptyPrice;
  }

  public void setCptyPrice(BigDecimal cptyPrice) {
    this.cptyPrice = cptyPrice;
  }

  @Basic
  @Column(name = "CUSTOM_CONTACT")
  public String getCustomContact() {
    return customContact;
  }

  public void setCustomContact(String customContact) {
    this.customContact = customContact;
  }

  @Basic
  @Column(name = "REF_NO")
  public String getRefNo() {
    return refNo;
  }

  public void setRefNo(String refNo) {
    this.refNo = refNo;
  }

  @Basic
  @Column(name = "ACCOUNT_TYPE")
  public String getAccountType() {
    return accountType;
  }

  public void setAccountType(String accountType) {
    this.accountType = accountType;
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
  @Column(name = "TAX")
  public BigDecimal getTax() {
    return tax;
  }

  public void setTax(BigDecimal tax) {
    this.tax = tax;
  }

  @Basic
  @Column(name = "INVESTMENT_TIME_BY_MONTH")
  public BigDecimal getInvestmentTimeByMonth() {
    return investmentTimeByMonth;
  }

  public void setInvestmentTimeByMonth(BigDecimal investmentTimeByMonth) {
    this.investmentTimeByMonth = investmentTimeByMonth;
  }

  @Basic
  @Column(name = "SELL_TRADING_CODE")
  public String getSellTradingCode() {
    return sellTradingCode;
  }

  public void setSellTradingCode(String sellTradingCode) {
    this.sellTradingCode = sellTradingCode;
  }

  @Basic
  @Column(name = "PAPERLESS_MARKET_STATUS")
  public Integer getPaperlessMarketStatus() {
    return paperlessMarketStatus;
  }

  public void setPaperlessMarketStatus(Integer paperlessMarketStatus) {
    this.paperlessMarketStatus = paperlessMarketStatus;
  }

  @Basic
  @Column(name = "LISTED_BOND")
  public Byte getListedBond() {
    return listedBond;
  }

  public void setListedBond(Byte listedBond) {
    this.listedBond = listedBond;
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

  @Basic
  @Column(name = "RECOMMENDED_STATUS")
  public Integer getRecommendedStatus() {
    return recommendedStatus;
  }

  public void setRecommendedStatus(Integer recommendedStatus) {
    this.recommendedStatus = recommendedStatus;
  }

  @Basic
  @Column(name = "REFEREE")
  public String getReferee() {
    return referee;
  }

  public void setReferee(String referee) {
    this.referee = referee;
  }

  @Basic
  @Column(name = "REINVEST_RATE")
  public BigDecimal getReinvestRate() {
    return reinvestRate;
  }

  public void setReinvestRate(BigDecimal reinvestRate) {
    this.reinvestRate = reinvestRate;
  }

  @Basic
  @Column(name = "OPTLOCK")
  public Integer getOptlock() {
    return optlock;
  }

  public void setOptlock(Integer optlock) {
    this.optlock = optlock;
  }

  @Basic
  @Column(name = "BOND_CODE")
  public String getBondCode() {
    return bondCode;
  }

  public void setBondCode(String bondCode) {
    this.bondCode = bondCode;
  }

  @Basic
  @Column(name = "TRANSACTION_ID")
  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  @Basic
  @Column(name = "REINVEST_RATE_2")
  public BigDecimal getReinvestRate2() {
    return reinvestRate2;
  }

  public void setReinvestRate2(BigDecimal reinvestRate2) {
    this.reinvestRate2 = reinvestRate2;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RtTcbOrderMarketEntity that = (RtTcbOrderMarketEntity) o;
    return orderId == that.orderId && Objects.equals(accountId, that.accountId) && Objects.equals(actionId, that.actionId) && Objects.equals(active, that.active) && Objects.equals(confirmStt,
      that.confirmStt) && Objects.equals(counterParty, that.counterParty) && Objects.equals(createdDate, that.createdDate) && Objects.equals(expiredDate, that.expiredDate) && Objects.equals(price,
      that.price) && Objects.equals(productCode, that.productCode) && Objects.equals(productId, that.productId) && Objects.equals(purchaserId, that.purchaserId) && Objects.equals(referId,
      that.referId) && Objects.equals(startDate, that.startDate) && Objects.equals(status, that.status) && Objects.equals(unitPrice, that.unitPrice) && Objects.equals(updatedDate,
      that.updatedDate) && Objects.equals(volume, that.volume) && Objects.equals(remainVolume, that.remainVolume) && Objects.equals(rate, that.rate) && Objects.equals(cashFlow,
      that.cashFlow) && Objects.equals(agencyFee, that.agencyFee) && Objects.equals(rmIncentive, that.rmIncentive) && Objects.equals(orderCode, that.orderCode) && Objects.equals(principal,
      that.principal) && Objects.equals(bbprice, that.bbprice) && Objects.equals(orderType, that.orderType) && Objects.equals(typeLogic, that.typeLogic) && Objects.equals(isDirect,
      that.isDirect) && Objects.equals(counterOrder, that.counterOrder) && Objects.equals(referenceId, that.referenceId) && Objects.equals(flexStatus, that.flexStatus) && Objects.equals(cptyPrice,
      that.cptyPrice) && Objects.equals(customContact, that.customContact) && Objects.equals(refNo, that.refNo) && Objects.equals(accountType, that.accountType) && Objects.equals(fee,
      that.fee) && Objects.equals(tax, that.tax) && Objects.equals(investmentTimeByMonth, that.investmentTimeByMonth) && Objects.equals(sellTradingCode, that.sellTradingCode) && Objects.equals(
      paperlessMarketStatus, that.paperlessMarketStatus) && Objects.equals(listedBond, that.listedBond) && Objects.equals(etlCurDate, that.etlCurDate) && Objects.equals(etlRunDateTime,
      that.etlRunDateTime) && Objects.equals(recommendedStatus, that.recommendedStatus) && Objects.equals(referee, that.referee) && Objects.equals(reinvestRate, that.reinvestRate) && Objects.equals(
      optlock, that.optlock) && Objects.equals(bondCode, that.bondCode) && Objects.equals(transactionId, that.transactionId) && Objects.equals(reinvestRate2, that.reinvestRate2);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId, accountId, actionId, active, confirmStt, counterParty, createdDate, expiredDate, price, productCode, productId, purchaserId, referId, startDate, status, unitPrice,
      updatedDate, volume, remainVolume, rate, cashFlow, agencyFee, rmIncentive, orderCode, principal, bbprice, orderType, typeLogic, isDirect, counterOrder, referenceId, flexStatus, cptyPrice,
      customContact, refNo, accountType, fee, tax, investmentTimeByMonth, sellTradingCode, paperlessMarketStatus, listedBond, etlCurDate, etlRunDateTime, recommendedStatus, referee, reinvestRate,
      optlock, bondCode, transactionId, reinvestRate2);
  }

  @Step("insert data")
  public boolean saveRtTcbOrderMarketEntity(RtTcbOrderMarketEntity rtTcbOrderMarketEntity) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.beginTransaction();
    session.save(rtTcbOrderMarketEntity);
    session.getTransaction().commit();
    return true;
  }

  @Step("delete data by object")
  public void deleteRtTcbOrderMarketEntity(RtTcbOrderMarketEntity rtTcbOrderMarketEntity) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.beginTransaction();
    Query query = session.createQuery("DELETE RtTcbOrderMarketEntity WHERE orderId = :orderId ");
    query.setParameter("orderId", rtTcbOrderMarketEntity.getOrderId());
    query.executeUpdate();
    session.getTransaction().commit();
    Dwh.dwhDbConnection.closeSession();
  }

}