package com.tcbs.automation.dwh;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "iCalendar_AllData")
public class ICalendarAllDataEntity implements Serializable {

  private static final long serialVersionUID = 741378985231847013L;

  private String productType;
  private String eventType;
  private String defId;
  private String defType;
  private String objId;
  private String data;

  private String eventId;
  private Date eventDueDate;
  private String eventStatus;
  private Integer eventIndex;
  private String custodyCd;
  private String tcbsid;
  private String stockCaCode;
  private Long stockBalance;
  private Long stockQuantity;
  private Long stockAmount;
  private String stockStatus;
  private BigDecimal couponRate;
  private Double couponPaymentAmtAfterTax;
  private Integer etlCurDate;
  private Timestamp etlRunDatetime;
  private Double rmCouponPaymentAmtAfterTax;
  private String milestoneMeta;
  private String rmCustodyCd;
  private String rmtcbsid;

  @Basic
  @Column(name = "ProductType")
  public String getProductType() {
    return productType;
  }

  public void setProductType(String productType) {
    this.productType = productType;
  }

  @Basic
  @Column(name = "EventType")
  public String getEventType() {
    return eventType;
  }

  public void setEventType(String eventType) {
    this.eventType = eventType;
  }

  @Basic
  @Column(name = "DefId")
  public String getDefId() {
    return defId;
  }

  public void setDefId(String defId) {
    this.defId = defId;
  }

  @Basic
  @Column(name = "DefType")
  public String getDefType() {
    return defType;
  }

  public void setDefType(String defType) {
    this.defType = defType;
  }

  @Basic
  @Column(name = "ObjId")
  public String getObjId() {
    return objId;
  }

  public void setObjId(String objId) {
    this.objId = objId;
  }

  @Basic
  @Column(name = "Data")
  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  @Id
  @Column(name = "EventId")
  public String getEventId() {
    return eventId;
  }

  public void setEventId(String eventId) {
    this.eventId = eventId;
  }

  @Basic
  @Column(name = "EventDueDate")
  public Date getEventDueDate() {
    return eventDueDate;
  }

  public void setEventDueDate(Date eventDueDate) {
    this.eventDueDate = eventDueDate;
  }

  @Basic
  @Column(name = "EventStatus")
  public String getEventStatus() {
    return eventStatus;
  }

  public void setEventStatus(String eventStatus) {
    this.eventStatus = eventStatus;
  }

  @Basic
  @Column(name = "EventIndex")
  public Integer getEventIndex() {
    return eventIndex;
  }

  public void setEventIndex(Integer eventIndex) {
    this.eventIndex = eventIndex;
  }

  @Basic
  @Column(name = "CustodyCd")
  public String getCustodyCd() {
    return custodyCd;
  }

  public void setCustodyCd(String custodyCd) {
    this.custodyCd = custodyCd;
  }

  @Basic
  @Column(name = "tcbsid")
  public String getTcbsid() {
    return tcbsid;
  }

  public void setTcbsid(String tcbsid) {
    this.tcbsid = tcbsid;
  }

  @Basic
  @Column(name = "StockCACode")
  public String getStockCaCode() {
    return stockCaCode;
  }

  public void setStockCaCode(String stockCaCode) {
    this.stockCaCode = stockCaCode;
  }

  @Basic
  @Column(name = "StockBalance")
  public Long getStockBalance() {
    return stockBalance;
  }

  public void setStockBalance(Long stockBalance) {
    this.stockBalance = stockBalance;
  }

  @Basic
  @Column(name = "StockQuantity")
  public Long getStockQuantity() {
    return stockQuantity;
  }

  public void setStockQuantity(Long stockQuantity) {
    this.stockQuantity = stockQuantity;
  }

  @Basic
  @Column(name = "StockAmount")
  public Long getStockAmount() {
    return stockAmount;
  }

  public void setStockAmount(Long stockAmount) {
    this.stockAmount = stockAmount;
  }

  @Basic
  @Column(name = "StockStatus")
  public String getStockStatus() {
    return stockStatus;
  }

  public void setStockStatus(String stockStatus) {
    this.stockStatus = stockStatus;
  }

  @Basic
  @Column(name = "CouponRate")
  public BigDecimal getCouponRate() {
    return couponRate;
  }

  public void setCouponRate(BigDecimal couponRate) {
    this.couponRate = couponRate;
  }

  @Basic
  @Column(name = "CouponPaymentAmtAfterTax")
  public Double getCouponPaymentAmtAfterTax() {
    return couponPaymentAmtAfterTax;
  }

  public void setCouponPaymentAmtAfterTax(Double couponPaymentAmtAfterTax) {
    this.couponPaymentAmtAfterTax = couponPaymentAmtAfterTax;
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
  @Column(name = "EtlRunDatetime")
  public Timestamp getEtlRunDatetime() {
    return etlRunDatetime;
  }

  public void setEtlRunDatetime(Timestamp etlRunDatetime) {
    this.etlRunDatetime = etlRunDatetime;
  }

  @Basic
  @Column(name = "RM_CouponPaymentAmtAfterTax")
  public Double getRmCouponPaymentAmtAfterTax() {
    return rmCouponPaymentAmtAfterTax;
  }

  public void setRmCouponPaymentAmtAfterTax(Double rmCouponPaymentAmtAfterTax) {
    this.rmCouponPaymentAmtAfterTax = rmCouponPaymentAmtAfterTax;
  }

  @Basic
  @Column(name = "Milestone_Meta")
  public String getMilestoneMeta() {
    return milestoneMeta;
  }

  public void setMilestoneMeta(String milestoneMeta) {
    this.milestoneMeta = milestoneMeta;
  }

  @Basic
  @Column(name = "RM_CustodyCd")
  public String getRmCustodyCd() {
    return rmCustodyCd;
  }

  public void setRmCustodyCd(String rmCustodyCd) {
    this.rmCustodyCd = rmCustodyCd;
  }

  @Basic
  @Column(name = "RM_tcbsid")
  public String getRmtcbsid() {
    return rmtcbsid;
  }

  public void setRmtcbsid(String rmtcbsid) {
    this.rmtcbsid = rmtcbsid;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ICalendarAllDataEntity that = (ICalendarAllDataEntity) o;
    return etlCurDate == that.etlCurDate &&
      Objects.equals(productType, that.productType) &&
      Objects.equals(eventType, that.eventType) &&
      Objects.equals(defId, that.defId) &&
      Objects.equals(defType, that.defType) &&
      Objects.equals(objId, that.objId) &&
      Objects.equals(data, that.data) &&
      Objects.equals(eventId, that.eventId) &&
      Objects.equals(eventDueDate, that.eventDueDate) &&
      Objects.equals(eventStatus, that.eventStatus) &&
      Objects.equals(eventIndex, that.eventIndex) &&
      Objects.equals(custodyCd, that.custodyCd) &&
      Objects.equals(tcbsid, that.tcbsid) &&
      Objects.equals(stockCaCode, that.stockCaCode) &&
      Objects.equals(stockBalance, that.stockBalance) &&
      Objects.equals(stockQuantity, that.stockQuantity) &&
      Objects.equals(stockAmount, that.stockAmount) &&
      Objects.equals(stockStatus, that.stockStatus) &&
      Objects.equals(couponRate, that.couponRate) &&
      Objects.equals(couponPaymentAmtAfterTax, that.couponPaymentAmtAfterTax) &&
      Objects.equals(etlRunDatetime, that.etlRunDatetime) &&
      Objects.equals(rmCouponPaymentAmtAfterTax, that.rmCouponPaymentAmtAfterTax) &&
      Objects.equals(milestoneMeta, that.milestoneMeta) &&
      Objects.equals(rmCustodyCd, that.rmCustodyCd) &&
      Objects.equals(rmtcbsid, that.rmtcbsid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(productType, eventType, defId, defType, objId, data, eventId, eventDueDate, eventStatus, eventIndex, custodyCd, tcbsid, stockCaCode, stockBalance, stockQuantity, stockAmount,
      stockStatus, couponRate, couponPaymentAmtAfterTax, etlCurDate, etlRunDatetime, rmCouponPaymentAmtAfterTax, milestoneMeta, rmCustodyCd, rmtcbsid);
  }

  @Step("Get customer anniversary event data")
  public List<ICalendarAllDataEntity> byCondition(String parentCus, List<String> childCus, List<String> productType, List<String> eventType, String startDate, String endDate, List<String> eventStatus) {
    try {
      // validation
      if (productType.get(0).equals("")) {
        productType = Arrays.asList("Customer", "Stock", "Bond");
      }
      if (eventType.get(0).equals("")) {
        eventType = Arrays.asList("Birthday", "Coupon", "Dividend", "OpenAccount");
      }
      if (eventStatus.get(0).equals("")) {
        eventStatus = Arrays.asList("HAPPENED", "ACTIVE");
      }
      // process
      SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
      java.util.Date fromDate = df.parse(startDate);
      java.util.Date toDate = df.parse(endDate);
      Query<ICalendarAllDataEntity> query;
      if (!parentCus.equals("") && childCus.get(0).equals("")) {
        query = Dwh.dwhDbConnection.getSession().createQuery(
          "from ICalendarAllDataEntity a where a.tcbsid = :tcbsid and a.eventStatus in :eventStatus and a.eventDueDate between :startDate and :endDate and a.productType in :productType and a.eventType in :eventType",
          ICalendarAllDataEntity.class);

      } else if (!parentCus.equals("") && childCus.get(0).equals("all")) {
        query = Dwh.dwhDbConnection.getSession().createQuery(
          "from ICalendarAllDataEntity a " +
            "where (a.rmtcbsid=:tcbsid and a.eventStatus in :eventStatus and a.eventDueDate between :startDate and :endDate and a.productType in :productType and a.eventType in :eventType)"
            + " or (a.tcbsid=:tcbsid and a.eventStatus in :eventStatus and a.eventDueDate between :startDate and :endDate and a.productType in :productType and a.eventType in :eventType)",
          ICalendarAllDataEntity.class);

      } else {
        query = Dwh.dwhDbConnection.getSession().createQuery(
          "from ICalendarAllDataEntity a " +
            "where (a.rmtcbsid=:tcbsid and a.custodyCd in :custodyCD and a.eventStatus in :eventStatus and a.eventDueDate between :startDate and :endDate and a.productType in :productType and a.eventType in :eventType)"
            + " or (a.tcbsid=:tcbsid and a.eventStatus in :eventStatus and a.eventDueDate between :startDate and :endDate and a.productType in :productType and a.eventType in :eventType)",
          ICalendarAllDataEntity.class);
        query.setParameter("custodyCD", childCus);
      }
      query.setParameter("tcbsid", parentCus);
      query.setParameter("startDate", fromDate);
      query.setParameter("endDate", toDate);
      query.setParameter("eventStatus", eventStatus);
      query.setParameter("productType", productType);
      query.setParameter("eventType", eventType);

      List<ICalendarAllDataEntity> resultList = query.getResultList();
      Dwh.dwhDbConnection.closeSession();
      return resultList;
    } catch (ParseException ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
      return null;
    }
  }
}
