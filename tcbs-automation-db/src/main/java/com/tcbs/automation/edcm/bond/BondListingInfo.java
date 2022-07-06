package com.tcbs.automation.edcm.bond;

import com.tcbs.automation.edcm.EdcmConnection;
import com.tcbs.automation.functions.PublicConstant;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

@Entity
@Table(name = "BOND_LISTING_INFO")
public class BondListingInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @NotNull
  @Column(name = "BOND_ID")
  private String bondId;
  @Column(name = "LISTING_STATUS")
  private String listingStatus;
  @Column(name = "TRADING_STATUS")
  private String tradingStatus;
  @Column(name = "LISTING_EXCHANGE")
  private String listingExchange;
  @Column(name = "VSD_STATUS")
  private String vsdStatus;
  @Column(name = "STOCK_EXCHANGE_STATUS")
  private String stockExchangeStatus;
  @Column(name = "LISTING_CODE")
  private String listingCode;
  @Column(name = "ISIN_CODE")
  private String isinCode;
  @Column(name = "LISTING_QUANTITY")
  private String listingQuantity;
  @NotNull
  @Column(name = "CREATED_AT")
  private Timestamp createdAt;
  @NotNull
  @Column(name = "CREATED_BY")
  private String createdBy;
  @Column(name = "UPDATED_AT")
  private Timestamp updatedAt;
  @Column(name = "UPDATED_BY")
  private String updatedBy;
  @Column(name = "GENERATE_TIMELINE_DATE")
  private Date generateTimelineDate;
  @Column(name = "LISTING_MAXIMUM_AMOUNT")
  private String listingMaximumAmount;

  @Step
  public List<BondListingInfo> getListDataByBondId(Integer bondId) {
    EdcmConnection.connection.getSession().clear();
    Query<BondListingInfo> query = EdcmConnection.connection.getSession().createNativeQuery(
      "SELECT * FROM BOND_LISTING_INFO p WHERE p.BOND_ID = :bondId ", BondListingInfo.class);
    query.setParameter("bondId", bondId);
    return query.getResultList();
  }


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  public String getBondId() {
    return bondId;
  }

  public void setBondId(String bondId) {
    this.bondId = bondId;
  }


  public String getListingStatus() {
    return listingStatus;
  }

  public void setListingStatus(String listingStatus) {
    this.listingStatus = listingStatus;
  }


  public String getTradingStatus() {
    return tradingStatus;
  }

  public void setTradingStatus(String tradingStatus) {
    this.tradingStatus = tradingStatus;
  }


  public String getListingExchange() {
    return listingExchange;
  }

  public void setListingExchange(String listingExchange) {
    this.listingExchange = listingExchange;
  }


  public String getVsdStatus() {
    return vsdStatus;
  }

  public void setVsdStatus(String vsdStatus) {
    this.vsdStatus = vsdStatus;
  }


  public String getStockExchangeStatus() {
    return stockExchangeStatus;
  }

  public void setStockExchangeStatus(String stockExchangeStatus) {
    this.stockExchangeStatus = stockExchangeStatus;
  }


  public String getListingCode() {
    return listingCode;
  }

  public void setListingCode(String listingCode) {
    this.listingCode = listingCode;
  }


  public String getIsinCode() {
    return isinCode;
  }

  public void setIsinCode(String isinCode) {
    this.isinCode = isinCode;
  }


  public String getListingQuantity() {
    return listingQuantity;
  }

  public void setListingQuantity(String listingQuantity) {
    this.listingQuantity = listingQuantity;
  }


  public String getCreatedAt() {
    return createdAt == null ? null : PublicConstant.dateTimeFormat.format(createdAt);
  }

  public void setCreatedAt(String createdAt) throws ParseException {
    this.createdAt = new Timestamp(PublicConstant.dateTimeFormat.parse(createdAt).getTime());
  }


  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }


  public String getUpdatedAt() {
    return updatedAt == null ? null : PublicConstant.dateTimeFormat.format(updatedAt);
  }

  public void setUpdatedAt(String updatedAt) throws ParseException {
    this.updatedAt = new Timestamp(PublicConstant.dateTimeFormat.parse(updatedAt).getTime());
  }


  public String getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }


  public Date getGenerateTimelineDate() {
    return generateTimelineDate;
  }

  public void setGenerateTimelineDate(Date generateTimelineDate) {
    this.generateTimelineDate = generateTimelineDate;
  }


  public String getListingMaximumAmount() {
    return listingMaximumAmount;
  }

  public void setListingMaximumAmount(String listingMaximumAmount) {
    this.listingMaximumAmount = listingMaximumAmount;
  }

}
