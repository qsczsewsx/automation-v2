package com.tcbs.automation.tcbond;

import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "INV_CBF_PAYMENT_LIST")
//@IdClass(EventId.class)
public class InvCbfPaymentList {
  @EmbeddedId
  @AttributeOverrides(
    {
      @AttributeOverride(name = "bondOrderId", column = @Column(name = "BOND_ORDER_ID")),
      @AttributeOverride(name = "couponDate", column = @Column(name = "COUPON_DATE"))
    }
  )
  private EventId eventId;
  @Column(name = "BOND_CODE")
  private String bondCode;
  @Column(name = "COUPON_FIX_DATE")
  private String couponFixDate;
  @Column(name = "PLAN_INSTANCE_ID")
  private Integer planInstanceId;
  @Column(name = "CUSTOMER_TCBS_ID")
  private String customerTcbsId;
  @Column(name = "QUANTITY")
  private Float quantity;
  @Column(name = "COUPON_VALUE")
  private Float couponValue;
  @Column(name = "SENT_TO_BPM", columnDefinition = "BIT")
  private Boolean sentToBpm;
  @Column(name = "syncStatus", columnDefinition = "BIT")
  private Boolean syncStatus;


  public InvCbfPaymentList() {
  }

  public InvCbfPaymentList(EventId eventId, String bondCode, String couponFixDate, String couponDate, Integer bondOrderId,
                           Integer planInstanceId,
                           String customerTcbsId, Float quantity, Float couponValue, Boolean sentToBpm, Boolean syncStatus) {
    super();
    this.eventId = eventId;
    this.bondCode = bondCode;
    this.couponFixDate = couponFixDate;
    this.planInstanceId = planInstanceId;
    this.customerTcbsId = customerTcbsId;
    this.quantity = quantity;
    this.couponValue = couponValue;
    this.sentToBpm = sentToBpm;
    this.syncStatus = syncStatus;
  }

  public EventId getEventId() {
    return eventId;
  }

  public void setEventId(EventId eventId) {
    this.eventId = eventId;
  }

  public Boolean isSyncStatus() {
    return syncStatus;
  }

  public void setSyncStatus(Boolean syncStatus) {
    this.syncStatus = syncStatus;
  }

  public String getBondCode() {
    return bondCode;
  }

  public void setBondCode(String bondCode) {
    this.bondCode = bondCode;
  }

  public String getCouponFixDate() {
    return couponFixDate;
  }

  public void setCouponFixDate(String couponFixDate) {
    this.couponFixDate = couponFixDate;
  }

  public Integer getPlanInstanceId() {
    return planInstanceId;
  }

  public void setPlanInstanceId(Integer planInstanceId) {
    this.planInstanceId = planInstanceId;
  }

  public String getCustomerTcbsId() {
    return customerTcbsId;
  }

  public void setCustomerTcbsId(String customerTcbsId) {
    this.customerTcbsId = customerTcbsId;
  }

  public Float getQuantity() {
    return quantity;
  }

  public void setQuantity(Float quantity) {
    this.quantity = quantity;
  }

  public Float getCouponValue() {
    return couponValue;
  }

  public void setCouponValue(Float couponValue) {
    this.couponValue = couponValue;
  }

  public Boolean getSentToBpm() {
    return sentToBpm;
  }

  public void setSentToBpm(Boolean sentToBpm) {
    this.sentToBpm = sentToBpm;
  }

  @SuppressWarnings("unchecked")
  @Step
  public List<InvCbfPaymentList> getAllData() throws Exception {
    Query<InvCbfPaymentList> query = TcBond.tcBondDbConnection.getSession().createQuery("from InvCbfPaymentList", InvCbfPaymentList.class);
    List<InvCbfPaymentList> result = query.getResultList();
    System.out.println("number data: " + result.size());
    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }

  @Step
  public List<EventId> getListCbfPaymentId() {
    Query<EventId> query = TcBond.tcBondDbConnection.getSession().createQuery("select a.eventId from InvCbfPaymentList a "
      + " where a.sentToBpm = true "
      + "order by a.eventId.couponDate asc, a.eventId.bondOrderId asc", EventId.class);
    List<EventId> result = query.getResultList();
    System.out.println("number data: " + result.size());
    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }

  @Step
  public InvCbfPaymentList getDataFromEventId(EventId eventId) {
    Query<InvCbfPaymentList> query = TcBond.tcBondDbConnection.getSession()
      .createQuery("from InvCbfPaymentList a where a.eventId=:eventId", InvCbfPaymentList.class);
    query.setParameter("eventId", eventId);
    //query.setParameter("couponDate", eventId.getCouponDate());
    List<InvCbfPaymentList> result = query.getResultList();
    if (result.size() > 0) {
      return result.get(0);
    } else {
      return null;
    }
  }

}

