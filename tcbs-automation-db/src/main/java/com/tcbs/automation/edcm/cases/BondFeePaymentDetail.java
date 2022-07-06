package com.tcbs.automation.edcm.cases;


import com.tcbs.automation.edcm.EdcmConnection;
import com.tcbs.automation.functions.PublicConstant;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;

@Entity
@Table(name = "BOND_FEE_PAYMENT_DETAIL")
public class BondFeePaymentDetail {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @NotNull
  @Column(name = "FEE_ID")
  private String feeId;
  @Column(name = "FEE_PERIOD_STARTDATE")
  private Date feePeriodStartdate;
  @Column(name = "FEE_PERIOD_ENDDATE")
  private Date feePeriodEnddate;
  @Column(name = "FEE_RATE")
  private Double feeRate;
  @Column(name = "FEE_AMOUNT")
  private String feeAmount;
  @Column(name = "INVOICE_DATE")
  private Date invoiceDate;
  @Column(name = "PAYMENT_DATE")
  private Date paymentDate;
  @Column(name = "REVENUE_RECOGNITION_DATE")
  private Date revenueRecognitionDate;
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
  @Column(name = "ISSUANCE_VOLUME")
  private String issuanceVolume;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  public String getFeeId() {
    return feeId;
  }

  public void setFeeId(String feeId) {
    this.feeId = feeId;
  }


  public Date getFeePeriodStartdate() {
    return feePeriodStartdate;
  }

  public void setFeePeriodStartdate(Date feePeriodStartdate) {
    this.feePeriodStartdate = feePeriodStartdate;
  }


  public Date getFeePeriodEnddate() {
    return feePeriodEnddate;
  }

  public void setFeePeriodEnddate(Date feePeriodEnddate) {
    this.feePeriodEnddate = feePeriodEnddate;
  }


  public Double getFeeRate() {
    return feeRate;
  }

  public void setFeeRate(Double feeRate) {
    this.feeRate = feeRate;
  }


  public String getFeeAmount() {
    return feeAmount;
  }

  public void setFeeAmount(String feeAmount) {
    this.feeAmount = feeAmount;
  }


  public Date getInvoiceDate() {
    return invoiceDate;
  }

  public void setInvoiceDate(Date invoiceDate) {
    this.invoiceDate = invoiceDate;
  }


  public Date getPaymentDate() {
    return paymentDate;
  }

  public void setPaymentDate(Date paymentDate) {
    this.paymentDate = paymentDate;
  }


  public Date getRevenueRecognitionDate() {
    return revenueRecognitionDate;
  }

  public void setRevenueRecognitionDate(Date revenueRecognitionDate) {
    this.revenueRecognitionDate = revenueRecognitionDate;
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


  public String getIssuanceVolume() {
    return issuanceVolume;
  }

  public void setIssuanceVolume(String issuanceVolume) {
    this.issuanceVolume = issuanceVolume;
  }

  @Step
  public BondFeePaymentDetail getData(String feeId) {
    EdcmConnection.connection.getSession().clear();
    Query<BondFeePaymentDetail> query = EdcmConnection.connection.getSession().createQuery(
      "from BondFeePaymentDetail a where a.feeId= :feeId", BondFeePaymentDetail.class);
    query.setParameter("feeId", feeId);

    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new BondFeePaymentDetail();
    }

  }

}

