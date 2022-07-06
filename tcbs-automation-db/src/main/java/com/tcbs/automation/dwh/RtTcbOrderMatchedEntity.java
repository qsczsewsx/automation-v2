package com.tcbs.automation.dwh;

import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "RT_tcb_ORDER_MATCHED", schema = "dbo", catalog = "tcbs-dwh")
public class RtTcbOrderMatchedEntity {
  private Long id;
  private int matchedId;
  private String productCode;
  private Integer askId;
  private Integer bidId;
  private Integer volume;
  private Timestamp matchedDate;
  private String status;
  private Byte active;
  private BigDecimal rate;
  private Byte type;
  private BigDecimal askPrice;
  private BigDecimal askUprice;
  private BigDecimal bidPrice;
  private BigDecimal bidUprice;
  private String code;
  private BigDecimal askPrincipal;
  private BigDecimal bidPrincipal;
  private BigDecimal askBbprice;
  private BigDecimal bidBbprice;
  private BigDecimal reinvestmentInterestRate;
  private Integer etlCurDate;
  private Timestamp etlRunDateTime;

  @Id
  @GeneratedValue
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Basic
  @Column(name = "MATCHED_ID")
  public int getMatchedId() {
    return matchedId;
  }

  public void setMatchedId(int matchedId) {
    this.matchedId = matchedId;
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
  @Column(name = "ASK_ID")
  public Integer getAskId() {
    return askId;
  }

  public void setAskId(Integer askId) {
    this.askId = askId;
  }

  @Basic
  @Column(name = "BID_ID")
  public Integer getBidId() {
    return bidId;
  }

  public void setBidId(Integer bidId) {
    this.bidId = bidId;
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
  @Column(name = "MATCHED_DATE")
  public Timestamp getMatchedDate() {
    return matchedDate;
  }

  public void setMatchedDate(Timestamp matchedDate) {
    this.matchedDate = matchedDate;
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
  @Column(name = "ACTIVE")
  public Byte getActive() {
    return active;
  }

  public void setActive(Byte active) {
    this.active = active;
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
  @Column(name = "TYPE")
  public Byte getType() {
    return type;
  }

  public void setType(Byte type) {
    this.type = type;
  }

  @Basic
  @Column(name = "ASK_PRICE")
  public BigDecimal getAskPrice() {
    return askPrice;
  }

  public void setAskPrice(BigDecimal askPrice) {
    this.askPrice = askPrice;
  }

  @Basic
  @Column(name = "ASK_UPRICE")
  public BigDecimal getAskUprice() {
    return askUprice;
  }

  public void setAskUprice(BigDecimal askUprice) {
    this.askUprice = askUprice;
  }

  @Basic
  @Column(name = "BID_PRICE")
  public BigDecimal getBidPrice() {
    return bidPrice;
  }

  public void setBidPrice(BigDecimal bidPrice) {
    this.bidPrice = bidPrice;
  }

  @Basic
  @Column(name = "BID_UPRICE")
  public BigDecimal getBidUprice() {
    return bidUprice;
  }

  public void setBidUprice(BigDecimal bidUprice) {
    this.bidUprice = bidUprice;
  }

  @Basic
  @Column(name = "CODE")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Basic
  @Column(name = "ASK_PRINCIPAL")
  public BigDecimal getAskPrincipal() {
    return askPrincipal;
  }

  public void setAskPrincipal(BigDecimal askPrincipal) {
    this.askPrincipal = askPrincipal;
  }

  @Basic
  @Column(name = "BID_PRINCIPAL")
  public BigDecimal getBidPrincipal() {
    return bidPrincipal;
  }

  public void setBidPrincipal(BigDecimal bidPrincipal) {
    this.bidPrincipal = bidPrincipal;
  }

  @Basic
  @Column(name = "ASK_BBPRICE")
  public BigDecimal getAskBbprice() {
    return askBbprice;
  }

  public void setAskBbprice(BigDecimal askBbprice) {
    this.askBbprice = askBbprice;
  }

  @Basic
  @Column(name = "BID_BBPRICE")
  public BigDecimal getBidBbprice() {
    return bidBbprice;
  }

  public void setBidBbprice(BigDecimal bidBbprice) {
    this.bidBbprice = bidBbprice;
  }

  @Basic
  @Column(name = "REINVESTMENT_INTEREST_RATE")
  public BigDecimal getReinvestmentInterestRate() {
    return reinvestmentInterestRate;
  }

  public void setReinvestmentInterestRate(BigDecimal reinvestmentInterestRate) {
    this.reinvestmentInterestRate = reinvestmentInterestRate;
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
    RtTcbOrderMatchedEntity that = (RtTcbOrderMatchedEntity) o;
    return matchedId == that.matchedId && Objects.equals(productCode, that.productCode) && Objects.equals(askId, that.askId) && Objects.equals(bidId, that.bidId) && Objects.equals(volume,
      that.volume) && Objects.equals(matchedDate, that.matchedDate) && Objects.equals(status, that.status) && Objects.equals(active, that.active) && Objects.equals(rate, that.rate) && Objects.equals(
      type, that.type) && Objects.equals(askPrice, that.askPrice) && Objects.equals(askUprice, that.askUprice) && Objects.equals(bidPrice, that.bidPrice) && Objects.equals(bidUprice,
      that.bidUprice) && Objects.equals(code, that.code) && Objects.equals(askPrincipal, that.askPrincipal) && Objects.equals(bidPrincipal, that.bidPrincipal) && Objects.equals(askBbprice,
      that.askBbprice) && Objects.equals(bidBbprice, that.bidBbprice) && Objects.equals(reinvestmentInterestRate, that.reinvestmentInterestRate) && Objects.equals(etlCurDate,
      that.etlCurDate) && Objects.equals(etlRunDateTime, that.etlRunDateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(matchedId, productCode, askId, bidId, volume, matchedDate, status, active, rate, type, askPrice, askUprice, bidPrice, bidUprice, code, askPrincipal, bidPrincipal, askBbprice,
      bidBbprice, reinvestmentInterestRate, etlCurDate, etlRunDateTime);
  }

  @Step("insert data")
  public boolean saveRtTcbOrderMatched(RtTcbOrderMatchedEntity rtTcbOrderMatchedEntity) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.beginTransaction();
    session.save(rtTcbOrderMatchedEntity);
    session.getTransaction().commit();
    return true;
  }

  @Step("delete data by object")
  public void deleteRtTcbOrderMatched(RtTcbOrderMatchedEntity rtTcbOrderMatchedEntity) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.beginTransaction();
    Query query = session.createQuery("DELETE RtTcbOrderMatchedEntity WHERE matchedId = :matchedId ");
    query.setParameter("matchedId", rtTcbOrderMatchedEntity.getMatchedId());
    query.executeUpdate();
    session.getTransaction().commit();
    Dwh.dwhDbConnection.closeSession();
  }
}
