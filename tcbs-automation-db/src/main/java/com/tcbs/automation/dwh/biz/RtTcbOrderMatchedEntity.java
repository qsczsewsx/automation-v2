package com.tcbs.automation.dwh.biz;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "RT_tcb_ORDER_MATCHED", schema = "dbo", catalog = "tcbs-dwh")
public class RtTcbOrderMatchedEntity {
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

  @Basic
  @Column(name = "MATCHED_ID", nullable = false)
  public int getMatchedId() {
    return matchedId;
  }

  public void setMatchedId(int matchedId) {
    this.matchedId = matchedId;
  }

  @Basic
  @Column(name = "PRODUCT_CODE", nullable = true, length = 50)
  public String getProductCode() {
    return productCode;
  }

  public void setProductCode(String productCode) {
    this.productCode = productCode;
  }

  @Basic
  @Column(name = "ASK_ID", nullable = true)
  public Integer getAskId() {
    return askId;
  }

  public void setAskId(Integer askId) {
    this.askId = askId;
  }

  @Basic
  @Column(name = "BID_ID", nullable = true)
  public Integer getBidId() {
    return bidId;
  }

  public void setBidId(Integer bidId) {
    this.bidId = bidId;
  }

  @Basic
  @Column(name = "VOLUME", nullable = true)
  public Integer getVolume() {
    return volume;
  }

  public void setVolume(Integer volume) {
    this.volume = volume;
  }

  @Basic
  @Column(name = "MATCHED_DATE", nullable = true)
  public Timestamp getMatchedDate() {
    return matchedDate;
  }

  public void setMatchedDate(Timestamp matchedDate) {
    this.matchedDate = matchedDate;
  }

  @Basic
  @Column(name = "STATUS", nullable = true, length = 30)
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @Basic
  @Column(name = "ACTIVE", nullable = true)
  public Byte getActive() {
    return active;
  }

  public void setActive(Byte active) {
    this.active = active;
  }

  @Basic
  @Column(name = "RATE", nullable = true, precision = 3)
  public BigDecimal getRate() {
    return rate;
  }

  public void setRate(BigDecimal rate) {
    this.rate = rate;
  }

  @Basic
  @Column(name = "TYPE", nullable = true)
  public Byte getType() {
    return type;
  }

  public void setType(Byte type) {
    this.type = type;
  }

  @Basic
  @Column(name = "ASK_PRICE", nullable = true, precision = 3)
  public BigDecimal getAskPrice() {
    return askPrice;
  }

  public void setAskPrice(BigDecimal askPrice) {
    this.askPrice = askPrice;
  }

  @Basic
  @Column(name = "ASK_UPRICE", nullable = true, precision = 3)
  public BigDecimal getAskUprice() {
    return askUprice;
  }

  public void setAskUprice(BigDecimal askUprice) {
    this.askUprice = askUprice;
  }

  @Basic
  @Column(name = "BID_PRICE", nullable = true, precision = 3)
  public BigDecimal getBidPrice() {
    return bidPrice;
  }

  public void setBidPrice(BigDecimal bidPrice) {
    this.bidPrice = bidPrice;
  }

  @Basic
  @Column(name = "BID_UPRICE", nullable = true, precision = 3)
  public BigDecimal getBidUprice() {
    return bidUprice;
  }

  public void setBidUprice(BigDecimal bidUprice) {
    this.bidUprice = bidUprice;
  }

  @Basic
  @Column(name = "CODE", nullable = true, length = 30)
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Basic
  @Column(name = "ASK_PRINCIPAL", nullable = true, precision = 3)
  public BigDecimal getAskPrincipal() {
    return askPrincipal;
  }

  public void setAskPrincipal(BigDecimal askPrincipal) {
    this.askPrincipal = askPrincipal;
  }

  @Basic
  @Column(name = "BID_PRINCIPAL", nullable = true, precision = 3)
  public BigDecimal getBidPrincipal() {
    return bidPrincipal;
  }

  public void setBidPrincipal(BigDecimal bidPrincipal) {
    this.bidPrincipal = bidPrincipal;
  }

  @Basic
  @Column(name = "ASK_BBPRICE", nullable = true, precision = 3)
  public BigDecimal getAskBbprice() {
    return askBbprice;
  }

  public void setAskBbprice(BigDecimal askBbprice) {
    this.askBbprice = askBbprice;
  }

  @Basic
  @Column(name = "BID_BBPRICE", nullable = true, precision = 3)
  public BigDecimal getBidBbprice() {
    return bidBbprice;
  }

  public void setBidBbprice(BigDecimal bidBbprice) {
    this.bidBbprice = bidBbprice;
  }

  @Basic
  @Column(name = "REINVESTMENT_INTEREST_RATE", nullable = true, precision = 3)
  public BigDecimal getReinvestmentInterestRate() {
    return reinvestmentInterestRate;
  }

  public void setReinvestmentInterestRate(BigDecimal reinvestmentInterestRate) {
    this.reinvestmentInterestRate = reinvestmentInterestRate;
  }

  @Basic
  @Column(name = "EtlCurDate", nullable = true)
  public Integer getEtlCurDate() {
    return etlCurDate;
  }

  public void setEtlCurDate(Integer etlCurDate) {
    this.etlCurDate = etlCurDate;
  }

  @Basic
  @Column(name = "EtlRunDateTime", nullable = true)
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
    return matchedId == that.matchedId &&
      Objects.equals(productCode, that.productCode) &&
      Objects.equals(askId, that.askId) &&
      Objects.equals(bidId, that.bidId) &&
      Objects.equals(volume, that.volume) &&
      Objects.equals(matchedDate, that.matchedDate) &&
      Objects.equals(status, that.status) &&
      Objects.equals(active, that.active) &&
      Objects.equals(rate, that.rate) &&
      Objects.equals(type, that.type) &&
      Objects.equals(askPrice, that.askPrice) &&
      Objects.equals(askUprice, that.askUprice) &&
      Objects.equals(bidPrice, that.bidPrice) &&
      Objects.equals(bidUprice, that.bidUprice) &&
      Objects.equals(code, that.code) &&
      Objects.equals(askPrincipal, that.askPrincipal) &&
      Objects.equals(bidPrincipal, that.bidPrincipal) &&
      Objects.equals(askBbprice, that.askBbprice) &&
      Objects.equals(bidBbprice, that.bidBbprice) &&
      Objects.equals(reinvestmentInterestRate, that.reinvestmentInterestRate) &&
      Objects.equals(etlCurDate, that.etlCurDate) &&
      Objects.equals(etlRunDateTime, that.etlRunDateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(matchedId, productCode, askId, bidId, volume, matchedDate, status, active, rate, type, askPrice, askUprice, bidPrice, bidUprice, code, askPrincipal, bidPrincipal, askBbprice,
      bidBbprice, reinvestmentInterestRate, etlCurDate, etlRunDateTime);
  }
}
