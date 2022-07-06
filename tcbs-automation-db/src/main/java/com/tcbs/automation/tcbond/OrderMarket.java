package com.tcbs.automation.tcbond;

import com.tcbs.automation.enumerable.bond.OrderMarketStatus;
import lombok.*;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ORDER_MARKET")
public class OrderMarket {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ORDER_ID")
  public Integer orderId;
  @Column(name = "ACCOUNT_ID")
  public String accountId;
  @Column(name = "ACTION_ID")
  public Integer actionId;
  @Column(name = "ACTIVE")
  public Integer active;
  @Column(name = "CONFIRM_STT")
  public Integer confirmStt;
  @Column(name = "COUNTER_PARTY")
  public String counterParty;
  @Column(name = "CREATED_DATE")
  public Date createdDate;
  @Column(name = "EXPIRED_DATE")
  @Temporal(TemporalType.DATE)
  public Date expiredDate;
  @Column(name = "PRICE")
  public BigDecimal price;
  @Column(name = "PRODUCT_CODE")
  public String productCode;
  @Column(name = "PRODUCT_ID")
  public Integer productId;
  @Column(name = "PURCHASER_ID")
  public String purchaserId;
  @Column(name = "REFER_ID")
  public Integer referId;
  @Column(name = "START_DATE")
  @Temporal(TemporalType.DATE)
  public Date startDate;
  @Column(name = "STATUS")
  public String status;
  @Column(name = "UNIT_PRICE")
  public Integer unitPrice;
  @Column(name = "UPDATED_DATE")
  @Temporal(TemporalType.DATE)
  public Date updatedDate;
  @Column(name = "VOLUME")
  public Integer volume;
  @Column(name = "REMAIN_VOLUME")
  public Integer remainVolume;
  @Column(name = "RATE")
  public Double rate;
  @Column(name = "CASH_FLOW")
  public BigDecimal cashFlow;
  @Column(name = "AGENCY_FEE")
  public BigDecimal agencyFee;
  @Column(name = "RM_INCENTIVE")
  public BigDecimal rmIncentive;
  @Column(name = "ORDER_CODE")
  public String orderCode;
  @Column(name = "PRINCIPAL")
  public BigDecimal principal;
  @Column(name = "BBPRICE")
  public BigDecimal bbprice;
  @Column(name = "ORDER_TYPE")
  public Integer orderType;
  @Column(name = "TYPE_LOGIC")
  public String typeLogic;
  @Column(name = "IS_DIRECT")
  public Integer isDirect;
  @Column(name = "COUNTER_ORDER")
  public Integer counterOrder;
  @Column(name = "REFERENCE_ID")
  public String referenceId;
  @Column(name = "FLEX_STATUS")
  public Integer flexStatus;
  @Column(name = "CPTY_PRICE")
  public BigDecimal cptyPrice;
  @Column(name = "CUSTOM_CONTACT")
  public String customContact;
  @Column(name = "REF_NO")
  public String refNo;
  @Column(name = "ACCOUNT_TYPE")
  public String accountType;
  @Column(name = "FEE")
  public BigDecimal fee;
  @Column(name = "TAX")
  public BigDecimal tax;
  @Column(name = "INVESTMENT_TIME_BY_MONTH")
  public Double investmentTimeByMonth;
  @Column(name = "SELL_TRADING_CODE")
  public String sellTradingCode;
  @Column(name = "PAPERLESS_MARKET_STATUS")
  public Integer paperlessMarketStatus;
  @Column(name = "LISTED_BOND")
  public Integer listedBond;
  @Column(name = "RECOMMENDED_STATUS")
  public Integer recommendedStatus;
  @Column(name = "REFEREE")
  public String referee;
  @Column(name = "REINVEST_RATE")
  public Double reinvestRate;
  @Column(name = "OPTLOCK")
  public Integer optlock;
  @Column(name = "BOND_CODE")
  public String bondCode;
  @Column(name = "TRANSACTION_ID")
  public String transactionId;
  @Column(name = "REINVEST_RATE_2")
  public Double reinvestRate2;
  @Column(name = "TOTAL_TRADING")
  public Integer totalTrading;
  @Column(name = "UNHOLD_OOD_MONEY")
  public Integer unholdOodMoney;
  @Column(name = "CP_CUST_TYPE")
  public String cpCustType;
  @Column(name = "INVEST_RATE")
  public Double investRate;
  @Column(name = "MARKET_STATUS")
  public Integer marketStatus;
  @Column(name = "ACCOUNT_NO")
  public String accountNo;

  public OrderMarket setAccountNo(String accountNo) {
    this.accountNo = accountNo;
    return this;
  }

  public OrderMarket setOrderId(Integer orderId) {
    this.orderId = orderId;
    return this;
  }

  public OrderMarket setAccountId(String accountId) {
    this.accountId = accountId;
    return this;
  }

  public OrderMarket setActionId(Integer actionId) {
    this.actionId = actionId;
    return this;
  }

  public OrderMarket setActionId(String action) {
    if (action.toUpperCase().equals("BUY")) {
      this.actionId = 1;
    } else if (action.toUpperCase().equals("SELL")) {
      this.actionId = 2;
    }
    return this;
  }

  public OrderMarket setActive(Integer active) {
    this.active = active;
    return this;
  }

  public OrderMarket setConfirmStt(Integer confirmStt) {
    this.confirmStt = confirmStt;
    return this;
  }

  public OrderMarket setCounterParty(String counterParty) {
    this.counterParty = counterParty;
    return this;
  }

  public OrderMarket setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
    return this;
  }

  public OrderMarket setExpiredDate(Date expiredDate) {
    this.expiredDate = expiredDate;
    return this;
  }

  public OrderMarket setPrice(BigDecimal price) {
    this.price = price;
    return this;
  }

  public OrderMarket setProductCode(String productCode) {
    this.productCode = productCode;
    return this;
  }

  public OrderMarket setProductId(Integer productId) {
    this.productId = productId;
    return this;
  }

  public OrderMarket setPurchaserId(String purchaserId) {
    this.purchaserId = purchaserId;
    return this;
  }

  public OrderMarket setReferId(Integer referId) {
    this.referId = referId;
    return this;
  }

  public OrderMarket setStartDate(Date startDate) {
    this.startDate = startDate;
    return this;
  }

  public OrderMarket setStatus(OrderMarketStatus status) {
    this.status = status.toString();
    return this;
  }

  public OrderMarket setStatus(String status) {
    this.status = status;
    return this;
  }

  public OrderMarket setUnitPrice(Integer unitPrice) {
    this.unitPrice = unitPrice;
    return this;
  }

  public OrderMarket setUpdatedDate(Date updatedDate) {
    this.updatedDate = updatedDate;
    return this;
  }

  public OrderMarket setVolume(Integer volume) {
    this.volume = volume;
    return this;
  }

  public OrderMarket setRemainVolume(Integer remainVolume) {
    this.remainVolume = remainVolume;
    return this;
  }

  public OrderMarket setRate(Double rate) {
    this.rate = rate;
    return this;
  }

  public OrderMarket setCashFlow(BigDecimal cashFlow) {
    this.cashFlow = cashFlow;
    return this;
  }

  public OrderMarket setAgencyFee(BigDecimal agencyFee) {
    this.agencyFee = agencyFee;
    return this;
  }

  public OrderMarket setRmIncentive(BigDecimal rmIncentive) {
    this.rmIncentive = rmIncentive;
    return this;
  }

  public OrderMarket setOrderCode(String orderCode) {
    this.orderCode = orderCode;
    return this;
  }

  public OrderMarket setPrincipal(BigDecimal principal) {
    this.principal = principal;
    return this;
  }

  public OrderMarket setBbprice(BigDecimal bbprice) {
    this.bbprice = bbprice;
    return this;
  }

  public OrderMarket setOrderType(Integer orderType) {
    this.orderType = orderType;
    return this;
  }

  public OrderMarket setTypeLogic(String typeLogic) {
    this.typeLogic = typeLogic;
    return this;
  }

  public OrderMarket setIsDirect(Integer isDirect) {
    this.isDirect = isDirect;
    return this;
  }

  public OrderMarket setCounterOrder(Integer counterOrder) {
    this.counterOrder = counterOrder;
    return this;
  }

  public OrderMarket setReferenceId(String referenceId) {
    this.referenceId = referenceId;
    return this;
  }

  public OrderMarket setFlexStatus(Integer flexStatus) {
    this.flexStatus = flexStatus;
    return this;
  }

  public OrderMarket setCptyPrice(BigDecimal cptyPrice) {
    this.cptyPrice = cptyPrice;
    return this;
  }

  public OrderMarket setCustomContact(String customContact) {
    this.customContact = customContact;
    return this;
  }

  public OrderMarket setRefNo(String refNo) {
    this.refNo = refNo;
    return this;
  }

  public OrderMarket setAccountType(String accountType) {
    this.accountType = accountType;
    return this;
  }

  public OrderMarket setFee(BigDecimal fee) {
    this.fee = fee;
    return this;
  }

  public OrderMarket setTax(BigDecimal tax) {
    this.tax = tax;
    return this;
  }

  public OrderMarket setInvestmentTimeByMonth(Double investmentTimeByMonth) {
    this.investmentTimeByMonth = investmentTimeByMonth;
    return this;
  }

  public OrderMarket setSellTradingCode(String sellTradingCode) {
    this.sellTradingCode = sellTradingCode;
    return this;
  }

  public OrderMarket setPaperlessMarketStatus(Integer paperlessMarketStatus) {
    this.paperlessMarketStatus = paperlessMarketStatus;
    return this;
  }

  public OrderMarket setListedBond(Integer listedBond) {
    this.listedBond = listedBond;
    return this;
  }

  public OrderMarket setRecommendedStatus(Integer recommendedStatus) {
    this.recommendedStatus = recommendedStatus;
    return this;
  }

  public OrderMarket setReferee(String referee) {
    this.referee = referee;
    return this;
  }

  public OrderMarket setReinvestRate(Double reinvestRate) {
    this.reinvestRate = reinvestRate;
    return this;
  }

  public OrderMarket setOptlock(Integer optlock) {
    this.optlock = optlock;
    return this;
  }

  public OrderMarket setBondCode(String bondCode) {
    this.bondCode = bondCode;
    return this;
  }

  public OrderMarket setTransactionId(String transactionId) {
    this.transactionId = transactionId;
    return this;
  }

  public OrderMarket setReinvestRate2(Double reinvestRate2) {
    this.reinvestRate2 = reinvestRate2;
    return this;
  }

  public OrderMarket setTotalTrading(Integer totalTrading) {
    this.totalTrading = totalTrading;
    return this;
  }

  public OrderMarket setUnholdOodMoney(Integer unholdOodMoney) {
    this.unholdOodMoney = unholdOodMoney;
    return this;
  }

  public OrderMarket setCpCustType(String cpCustType) {
    this.cpCustType = cpCustType;
    return this;
  }

  public OrderMarket setInvestRate(Double investRate) {
    this.investRate = investRate;
    return this;
  }

  public OrderMarket setMarketStatus(Integer marketStatus) {
    this.marketStatus = marketStatus;
    return this;
  }

  @Step("Get order by orderId from OrderMarket")
  public OrderMarket getOrderByOrderId(Integer orderId) {
    OrderMarket result = null;
    Query<OrderMarket> query = TcBond.tcBondDbConnection.getSession().createQuery(
      "from OrderMarket where orderId =: orderId");
    query.setParameter("orderId", orderId);
    List<OrderMarket> listResult = query.getResultList();
    if (listResult.size() == 1) {
      result = listResult.get(0);
    }
    return result;
  }

  public void insert() {
    Session session = TcBond.tcBondDbConnection.getSession();
    session.clear();
    session.beginTransaction();
    session.save(this);
    session.getTransaction().commit();
  }

  public void deleteByOrderId() {
    Session session = TcBond.tcBondDbConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<OrderMarket> query = session.createQuery(
      "DELETE FROM OrderMarket WHERE orderId=:orderId "
    );
    query.setParameter("orderId", this.getOrderId());
    query.executeUpdate();
    session.getTransaction().commit();
  }

  @Override
  protected Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
