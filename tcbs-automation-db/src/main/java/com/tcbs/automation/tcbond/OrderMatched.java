package com.tcbs.automation.tcbond;

import com.tcbs.automation.enumerable.bond.OrderMarketStatus;
import lombok.Getter;
import lombok.ToString;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Entity
@Getter
@ToString
@Table(name = "ORDER_MATCHED")
public class OrderMatched {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "MATCHED_ID")
  public Integer matchedId;
  @Column(name = "PRODUCT_CODE")
  public String productCode;
  @Column(name = "ASK_ID")
  public Integer askId;
  @Column(name = "BID_ID")
  public Integer bidId;
  @Column(name = "VOLUME")
  public Integer volume;
  @Column(name = "MATCHED_DATE")
  public Date matchedDate;
  @Column(name = "STATUS")
  public String status;
  @Column(name = "ACTIVE")
  public Integer active;
  @Column(name = "RATE")
  public Double rate;
  @Column(name = "TYPE")
  public Integer type;
  @Column(name = "ASK_PRICE")
  public BigDecimal askPrice;
  @Column(name = "ASK_UPRICE")
  public Integer askUprice;
  @Column(name = "BID_PRICE")
  public BigDecimal bidPrice;
  @Column(name = "BID_UPRICE")
  public Integer bidUprice;
  @Column(name = "CODE")
  public String code;
  @Column(name = "ASK_PRINCIPAL")
  public BigDecimal askPrincipal;
  @Column(name = "BID_PRINCIPAL")
  public BigDecimal bidPrincipal;
  @Column(name = "ASK_BBPRICE")
  public BigDecimal askBbprice;
  @Column(name = "BID_BBPRICE")
  public BigDecimal bidBbprice;
  @Column(name = "REINVESTMENT_INTEREST_RATE_ORIGINAL")
  public Double reinvestmentInterestRateOriginal;
  @Column(name = "REINVESTMENT_INTEREST_RATE")
  public Double reinvestmentInterestRate;
  @Column(name = "ACCRUED_INTEREST")
  public BigDecimal accruedInterest;
  @Column(name = "ASK_FEE")
  public BigDecimal askFee;
  @Column(name = "ASK_TAX")
  public BigDecimal askTax;
  @Column(name = "BID_FEE")
  public BigDecimal bidFee;
  @Column(name = "BID_TAX")
  public BigDecimal bidTax;
  @Column(name = "RATE_CORP")
  public Double rateCorp;
  @Column(name = "REINVESTMENT_INTEREST_RATE_CORP")
  public Double reinvestmentInterestRateCorp;
  @Column(name = "UNIT_PRICE_CLEAN")
  public Integer unitPriceClean;

  public OrderMatched setMatchedId(Integer matchedId) {
    this.matchedId = matchedId;
    return this;
  }


  public OrderMatched setProductCode(String productCode) {
    this.productCode = productCode;
    return this;
  }


  public OrderMatched setAskId(Integer askId) {
    this.askId = askId;
    return this;
  }


  public OrderMatched setBidId(Integer bidId) {
    this.bidId = bidId;
    return this;
  }


  public OrderMatched setVolume(Integer volume) {
    this.volume = volume;
    return this;
  }


  public OrderMatched setMatchedDate(String matchedDate) {
    this.matchedDate = Date.valueOf(matchedDate);
    return this;
  }


  public OrderMatched setStatus(String status) {
    this.status = status;
    return this;
  }

  public OrderMatched setStatus(OrderMarketStatus status) {
    this.status = status.getValue();
    return this;
  }


  public OrderMatched setActive(Integer active) {
    this.active = active;
    return this;
  }


  public OrderMatched setRate(Double rate) {
    this.rate = rate;
    return this;
  }


  public OrderMatched setType(Integer type) {
    this.type = type;
    return this;
  }


  public OrderMatched setAskPrice(BigDecimal askPrice) {
    this.askPrice = askPrice;
    return this;
  }


  public OrderMatched setAskUprice(Integer askUprice) {
    this.askUprice = askUprice;
    return this;
  }


  public OrderMatched setBidPrice(BigDecimal bidPrice) {
    this.bidPrice = bidPrice;
    return this;
  }


  public OrderMatched setBidUprice(Integer bidUprice) {
    this.bidUprice = bidUprice;
    return this;
  }


  public OrderMatched setCode(String code) {
    this.code = code;
    return this;
  }


  public OrderMatched setAskPrincipal(BigDecimal askPrincipal) {
    this.askPrincipal = askPrincipal;
    return this;
  }


  public OrderMatched setBidPrincipal(BigDecimal bidPrincipal) {
    this.bidPrincipal = bidPrincipal;
    return this;
  }


  public OrderMatched setAskBbprice(BigDecimal askBbprice) {
    this.askBbprice = askBbprice;
    return this;
  }


  public OrderMatched setBidBbprice(BigDecimal bidBbprice) {
    this.bidBbprice = bidBbprice;
    return this;
  }


  public OrderMatched setReinvestmentInterestRateOriginal(Double reinvestmentInterestRateOriginal) {
    this.reinvestmentInterestRateOriginal = reinvestmentInterestRateOriginal;
    return this;
  }


  public OrderMatched setReinvestmentInterestRate(Double reinvestmentInterestRate) {
    this.reinvestmentInterestRate = reinvestmentInterestRate;
    return this;
  }


  public OrderMatched setAccruedInterest(BigDecimal accruedInterest) {
    this.accruedInterest = accruedInterest;
    return this;
  }


  public OrderMatched setAskFee(BigDecimal askFee) {
    this.askFee = askFee;
    return this;
  }


  public OrderMatched setAskTax(BigDecimal askTax) {
    this.askTax = askTax;
    return this;
  }


  public OrderMatched setBidFee(BigDecimal bidFee) {
    this.bidFee = bidFee;
    return this;
  }


  public OrderMatched setBidTax(BigDecimal bidTax) {
    this.bidTax = bidTax;
    return this;
  }


  public OrderMatched setRateCorp(Double rateCorp) {
    this.rateCorp = rateCorp;
    return this;
  }


  public OrderMatched setReinvestmentInterestRateCorp(Double reinvestmentInterestRateCorp) {
    this.reinvestmentInterestRateCorp = reinvestmentInterestRateCorp;
    return this;
  }


  public OrderMatched setUnitPriceClean(Integer unitPriceClean) {
    this.unitPriceClean = unitPriceClean;
    return this;
  }

  @Step("Get order by suy orderId from OrderMarket")
  public OrderMatched getOrderMatchedByBuyOrderId(Integer buyOrderId) {
    OrderMatched result = null;
    Query<OrderMatched> query = TcBond.tcBondDbConnection.getSession().createQuery(
      "from OrderMatched where askId =: askId");
    query.setParameter("askId", buyOrderId);
    List<OrderMatched> listResult = query.getResultList();
    if (listResult.size() == 1) {
      result = listResult.get(0);
    }
    return result;
  }

  @Step("Get order by sell orderId from OrderMarket")
  public OrderMatched getOrderMatchedBySellOrderId(Integer sellOrderId) {
    OrderMatched result = null;
    Query<OrderMatched> query = TcBond.tcBondDbConnection.getSession().createQuery(
      "from OrderMatched where bidId =: bidId");
    query.setParameter("bidId", sellOrderId);
    List<OrderMatched> listResult = query.getResultList();
    if (listResult.size() == 1) {
      result = listResult.get(0);
    }
    return result;
  }

  @Step("Delete OrderMatched by matchedId")
  public void deleteByMatchedId() {
    Session session = TcBond.tcBondDbConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<OrderMarket> query = session.createQuery(
      "DELETE FROM OrderMatched WHERE matchedId=:matchedId "
    );
    query.setParameter("matchedId", this.getMatchedId());
    query.executeUpdate();
    session.getTransaction().commit();
  }

  @Step("Delete OrderMatched by matchedId")
  public void deleteBySellOrderId(Integer matchedId) {
    Session session = TcBond.tcBondDbConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<OrderMarket> query = session.createQuery(
      "DELETE FROM OrderMatched WHERE matchedId=:matchedId "
    );
    query.setParameter("matchedId", this.getMatchedId());
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public void insert() {
    Session session = TcBond.tcBondDbConnection.getSession();
    session.clear();
    session.beginTransaction();
    session.save(this);
    session.getTransaction().commit();
  }
}
