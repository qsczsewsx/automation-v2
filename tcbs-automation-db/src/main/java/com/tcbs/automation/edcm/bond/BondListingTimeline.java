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
@Table(name = "BOND_LISTING_TIMELINE")
public class BondListingTimeline {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @NotNull
  @Column(name = "BOND_ID")
  private String bondId;
  @Column(name = "ORDER_NUM")
  private String orderNum;
  @Column(name = "TIMELINE_TYPE_ID")
  private String timelineTypeId;
  @Column(name = "LISTING_EXCHANGE_ID")
  private String listingExchangeId;
  @Column(name = "TENTATIVE_DATE")
  private Date tentativeDate;
  @Column(name = "ACTUAL_DATE")
  private Date actualDate;
  @Column(name = "VALID_ACTUAL_DATE")
  private Date validActualDate;
  @Column(name = "VALID_TENTATIVE_DATE")
  private Date validTentativeDate;
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

  @Step
  public List<BondListingTimeline> getListDataByBondId(Integer bondId, String listingExchangeId) {
    EdcmConnection.connection.getSession().clear();
    Query<BondListingTimeline> query = EdcmConnection.connection.getSession().createNativeQuery(
      "SELECT * FROM BOND_LISTING_TIMELINE WHERE BOND_ID = :bondId AND LISTING_EXCHANGE_ID = :listingExchangeId ORDER BY ORDER_NUM  ", BondListingTimeline.class);
    query.setParameter("bondId", bondId);
    query.setParameter("listingExchangeId", listingExchangeId);
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


  public String getOrderNum() {
    return orderNum;
  }

  public void setOrderNum(String orderNum) {
    this.orderNum = orderNum;
  }


  public String getTimelineTypeId() {
    return timelineTypeId;
  }

  public void setTimelineTypeId(String timelineTypeId) {
    this.timelineTypeId = timelineTypeId;
  }


  public String getListingExchangeId() {
    return listingExchangeId;
  }

  public void setListingExchangeId(String listingExchangeId) {
    this.listingExchangeId = listingExchangeId;
  }


  public Date getTentativeDate() {
    return tentativeDate;
  }

  public void setTentativeDate(Date tentativeDate) {
    this.tentativeDate = tentativeDate;
  }


  public Date getActualDate() {
    return actualDate;
  }

  public void setActualDate(Date actualDate) {
    this.actualDate = actualDate;
  }


  public Date getValidActualDate() {
    return validActualDate;
  }

  public void setValidActualDate(Date validActualDate) {
    this.validActualDate = validActualDate;
  }


  public Date getValidTentativeDate() {
    return validTentativeDate;
  }

  public void setValidTentativeDate(Date validTentativeDate) {
    this.validTentativeDate = validTentativeDate;
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

}
