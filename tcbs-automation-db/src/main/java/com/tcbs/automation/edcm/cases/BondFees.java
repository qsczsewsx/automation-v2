package com.tcbs.automation.edcm.cases;

import com.tcbs.automation.edcm.EdcmConnection;
import com.tcbs.automation.functions.PublicConstant;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.text.ParseException;

@Entity
@Table(name = "BOND_FEES")
public class BondFees {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "FEE_ID")
  private String feeId;
  @NotNull
  @Column(name = "CASE_BOND_TEMP_ID")
  private String caseBondTempId;
  @NotNull
  @Column(name = "FEE_TYPE_ID")
  private String feeTypeId;
  @NotNull
  @Column(name = "FEE_PAYMENT_TYPE_ID")
  private String feePaymentTypeId;
  @Column(name = "FEE_FREQUENCY")
  private String feeFrequency;
  @Column(name = "FEE_AMORTISATION")
  private String feeAmortisation;
  @Column(name = "TCB_FEE_SHARING")
  private Double tcbFeeSharing;
  @Column(name = "FEE_DUE_BY_DAYS")
  private String feeDueByDays;
  @Column(name = "FEE_DUE_BY_TYPE_ID")
  private String feeDueByTypeId;
  @Column(name = "FEE_DUE_BY_DESCRIPTION")
  private String feeDueByDescription;
  @Column(name = "FEE_DESCRIPTION")
  private String feeDescription;
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
  @Column(name = "FEE_RATE")
  private Double feeRate;
  @Column(name = "DEAL_TYPE")
  private String dealType;


  public String getFeeId() {
    return feeId;
  }

  public void setFeeId(String feeId) {
    this.feeId = feeId;
  }


  public String getCaseBondTempId() {
    return caseBondTempId;
  }

  public void setCaseBondTempId(String caseBondTempId) {
    this.caseBondTempId = caseBondTempId;
  }


  public String getFeeTypeId() {
    return feeTypeId;
  }

  public void setFeeTypeId(String feeTypeId) {
    this.feeTypeId = feeTypeId;
  }


  public String getFeePaymentTypeId() {
    return feePaymentTypeId;
  }

  public void setFeePaymentTypeId(String feePaymentTypeId) {
    this.feePaymentTypeId = feePaymentTypeId;
  }


  public String getFeeFrequency() {
    return feeFrequency;
  }

  public void setFeeFrequency(String feeFrequency) {
    this.feeFrequency = feeFrequency;
  }


  public String getFeeAmortisation() {
    return feeAmortisation;
  }

  public void setFeeAmortisation(String feeAmortisation) {
    this.feeAmortisation = feeAmortisation;
  }


  public Double getTcbFeeSharing() {
    return tcbFeeSharing;
  }

  public void setTcbFeeSharing(Double tcbFeeSharing) {
    this.tcbFeeSharing = tcbFeeSharing;
  }


  public String getFeeDueByDays() {
    return feeDueByDays;
  }

  public void setFeeDueByDays(String feeDueByDays) {
    this.feeDueByDays = feeDueByDays;
  }


  public String getFeeDueByTypeId() {
    return feeDueByTypeId;
  }

  public void setFeeDueByTypeId(String feeDueByTypeId) {
    this.feeDueByTypeId = feeDueByTypeId;
  }


  public String getFeeDueByDescription() {
    return feeDueByDescription;
  }

  public void setFeeDueByDescription(String feeDueByDescription) {
    this.feeDueByDescription = feeDueByDescription;
  }


  public String getFeeDescription() {
    return feeDescription;
  }

  public void setFeeDescription(String feeDescription) {
    this.feeDescription = feeDescription;
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


  public Double getFeeRate() {
    return feeRate;
  }

  public void setFeeRate(Double feeRate) {
    this.feeRate = feeRate;
  }


  public String getDealType() {
    return dealType;
  }

  public void setDealType(String dealType) {
    this.dealType = dealType;
  }


  @Step
  public BondFees getData(String caseBondTempId) {
    EdcmConnection.connection.getSession().clear();
    Query<BondFees> query = EdcmConnection.connection.getSession().createQuery(
      "from BondFees a WHERE a.caseBondTempId=:caseBondTempId", BondFees.class);
    query.setParameter("caseBondTempId", caseBondTempId);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new BondFees();
    }
  }


}
