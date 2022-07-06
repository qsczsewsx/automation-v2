package com.tcbs.automation.dwh.biz;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "Stg_tci_INV_BOND_STATIC", schema = "dbo", catalog = "tcbs-dwh")
public class StgTciInvBondStaticEntity {
  private String name;
  private String code;
  private Integer par;
  private String currency;
  private Timestamp issueDate;
  private Timestamp expiredDate;
  private String notePaymentInterest;
  private String noteType;
  private String noteInterest;
  private String baseInterestTop;
  private String baseInterestBottom;
  private Integer bondIssuerId;
  private String seriesContractCode;
  private String seriesBondCode;
  private Integer isCoupon;
  private Integer couponPaymentEndTerm;
  private Integer couponRate;
  private String couponFreq;
  private Timestamp couponDatePayment;
  private String trustAsset;
  private String trustAssetValue;
  private String guarantee;
  private String guaranteeName;
  private Timestamp firstDatePeriodPl;
  private Timestamp lastDatePeriodPl;
  private Integer floatBandInterest;
  private Integer referenceRateCoupon;
  private Integer buildBook;
  private Integer listedStatus;
  private Integer active;
  private Integer status;
  private Timestamp updatedDate;
  private Timestamp createdDate;
  private Timestamp paymentDate;
  private String listedCode;
  private Integer frozenStatus;
  private String paymentTerm;
  private Integer bondStaticId;
  private Integer etlCurDate;
  private Timestamp etlRunDateTime;

  @Basic
  @Column(name = "NAME", nullable = true, length = 150)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Basic
  @Column(name = "CODE", nullable = true, length = 50)
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Basic
  @Column(name = "PAR", nullable = true, precision = 0)
  public Integer getPar() {
    return par;
  }

  public void setPar(Integer par) {
    this.par = par;
  }

  @Basic
  @Column(name = "CURRENCY", nullable = true, length = 5)
  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  @Basic
  @Column(name = "ISSUE_DATE", nullable = true)
  public Timestamp getIssueDate() {
    return issueDate;
  }

  public void setIssueDate(Timestamp issueDate) {
    this.issueDate = issueDate;
  }

  @Basic
  @Column(name = "EXPIRED_DATE", nullable = true)
  public Timestamp getExpiredDate() {
    return expiredDate;
  }

  public void setExpiredDate(Timestamp expiredDate) {
    this.expiredDate = expiredDate;
  }

  @Basic
  @Column(name = "NOTE_PAYMENT_INTEREST", nullable = true, length = 2000)
  public String getNotePaymentInterest() {
    return notePaymentInterest;
  }

  public void setNotePaymentInterest(String notePaymentInterest) {
    this.notePaymentInterest = notePaymentInterest;
  }

  @Basic
  @Column(name = "NOTE_TYPE", nullable = true, length = 2000)
  public String getNoteType() {
    return noteType;
  }

  public void setNoteType(String noteType) {
    this.noteType = noteType;
  }

  @Basic
  @Column(name = "NOTE_INTEREST", nullable = true, length = 2000)
  public String getNoteInterest() {
    return noteInterest;
  }

  public void setNoteInterest(String noteInterest) {
    this.noteInterest = noteInterest;
  }

  @Basic
  @Column(name = "BASE_INTEREST_TOP", nullable = true, length = 15)
  public String getBaseInterestTop() {
    return baseInterestTop;
  }

  public void setBaseInterestTop(String baseInterestTop) {
    this.baseInterestTop = baseInterestTop;
  }

  @Basic
  @Column(name = "BASE_INTEREST_BOTTOM", nullable = true, length = 15)
  public String getBaseInterestBottom() {
    return baseInterestBottom;
  }

  public void setBaseInterestBottom(String baseInterestBottom) {
    this.baseInterestBottom = baseInterestBottom;
  }

  @Basic
  @Column(name = "BOND_ISSUER_ID", nullable = true, precision = 0)
  public Integer getBondIssuerId() {
    return bondIssuerId;
  }

  public void setBondIssuerId(Integer bondIssuerId) {
    this.bondIssuerId = bondIssuerId;
  }

  @Basic
  @Column(name = "SERIES_CONTRACT_CODE", nullable = true, length = 80)
  public String getSeriesContractCode() {
    return seriesContractCode;
  }

  public void setSeriesContractCode(String seriesContractCode) {
    this.seriesContractCode = seriesContractCode;
  }

  @Basic
  @Column(name = "SERIES_BOND_CODE", nullable = true, length = 80)
  public String getSeriesBondCode() {
    return seriesBondCode;
  }

  public void setSeriesBondCode(String seriesBondCode) {
    this.seriesBondCode = seriesBondCode;
  }

  @Basic
  @Column(name = "IS_COUPON", nullable = true, precision = 0)
  public Integer getIsCoupon() {
    return isCoupon;
  }

  public void setIsCoupon(Integer isCoupon) {
    this.isCoupon = isCoupon;
  }

  @Basic
  @Column(name = "COUPON_PAYMENT_END_TERM", nullable = true, precision = 0)
  public Integer getCouponPaymentEndTerm() {
    return couponPaymentEndTerm;
  }

  public void setCouponPaymentEndTerm(Integer couponPaymentEndTerm) {
    this.couponPaymentEndTerm = couponPaymentEndTerm;
  }

  @Basic
  @Column(name = "COUPON_RATE", nullable = true, precision = 0)
  public Integer getCouponRate() {
    return couponRate;
  }

  public void setCouponRate(Integer couponRate) {
    this.couponRate = couponRate;
  }

  @Basic
  @Column(name = "COUPON_FREQ", nullable = true, length = 30)
  public String getCouponFreq() {
    return couponFreq;
  }

  public void setCouponFreq(String couponFreq) {
    this.couponFreq = couponFreq;
  }

  @Basic
  @Column(name = "COUPON_DATE_PAYMENT", nullable = true)
  public Timestamp getCouponDatePayment() {
    return couponDatePayment;
  }

  public void setCouponDatePayment(Timestamp couponDatePayment) {
    this.couponDatePayment = couponDatePayment;
  }

  @Basic
  @Column(name = "TRUST_ASSET", nullable = true, length = 5)
  public String getTrustAsset() {
    return trustAsset;
  }

  public void setTrustAsset(String trustAsset) {
    this.trustAsset = trustAsset;
  }

  @Basic
  @Column(name = "TRUST_ASSET_VALUE", nullable = true, length = 2000)
  public String getTrustAssetValue() {
    return trustAssetValue;
  }

  public void setTrustAssetValue(String trustAssetValue) {
    this.trustAssetValue = trustAssetValue;
  }

  @Basic
  @Column(name = "GUARANTEE", nullable = true, length = 5)
  public String getGuarantee() {
    return guarantee;
  }

  public void setGuarantee(String guarantee) {
    this.guarantee = guarantee;
  }

  @Basic
  @Column(name = "GUARANTEE_NAME", nullable = true, length = 2000)
  public String getGuaranteeName() {
    return guaranteeName;
  }

  public void setGuaranteeName(String guaranteeName) {
    this.guaranteeName = guaranteeName;
  }

  @Basic
  @Column(name = "FIRST_DATE_PERIOD_PL", nullable = true)
  public Timestamp getFirstDatePeriodPl() {
    return firstDatePeriodPl;
  }

  public void setFirstDatePeriodPl(Timestamp firstDatePeriodPl) {
    this.firstDatePeriodPl = firstDatePeriodPl;
  }

  @Basic
  @Column(name = "LAST_DATE_PERIOD_PL", nullable = true)
  public Timestamp getLastDatePeriodPl() {
    return lastDatePeriodPl;
  }

  public void setLastDatePeriodPl(Timestamp lastDatePeriodPl) {
    this.lastDatePeriodPl = lastDatePeriodPl;
  }

  @Basic
  @Column(name = "FLOAT_BAND_INTEREST", nullable = true, precision = 0)
  public Integer getFloatBandInterest() {
    return floatBandInterest;
  }

  public void setFloatBandInterest(Integer floatBandInterest) {
    this.floatBandInterest = floatBandInterest;
  }

  @Basic
  @Column(name = "REFERENCE_RATE_COUPON", nullable = true, precision = 0)
  public Integer getReferenceRateCoupon() {
    return referenceRateCoupon;
  }

  public void setReferenceRateCoupon(Integer referenceRateCoupon) {
    this.referenceRateCoupon = referenceRateCoupon;
  }

  @Basic
  @Column(name = "BUILD_BOOK", nullable = true, precision = 0)
  public Integer getBuildBook() {
    return buildBook;
  }

  public void setBuildBook(Integer buildBook) {
    this.buildBook = buildBook;
  }

  @Basic
  @Column(name = "LISTED_STATUS", nullable = true, precision = 0)
  public Integer getListedStatus() {
    return listedStatus;
  }

  public void setListedStatus(Integer listedStatus) {
    this.listedStatus = listedStatus;
  }

  @Basic
  @Column(name = "ACTIVE", nullable = true, precision = 0)
  public Integer getActive() {
    return active;
  }

  public void setActive(Integer active) {
    this.active = active;
  }

  @Basic
  @Column(name = "STATUS", nullable = true, precision = 0)
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Basic
  @Column(name = "UPDATED_DATE", nullable = true)
  public Timestamp getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(Timestamp updatedDate) {
    this.updatedDate = updatedDate;
  }

  @Basic
  @Column(name = "CREATED_DATE", nullable = true)
  public Timestamp getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Timestamp createdDate) {
    this.createdDate = createdDate;
  }

  @Basic
  @Column(name = "PAYMENT_DATE", nullable = true)
  public Timestamp getPaymentDate() {
    return paymentDate;
  }

  public void setPaymentDate(Timestamp paymentDate) {
    this.paymentDate = paymentDate;
  }

  @Basic
  @Column(name = "LISTED_CODE", nullable = true, length = 50)
  public String getListedCode() {
    return listedCode;
  }

  public void setListedCode(String listedCode) {
    this.listedCode = listedCode;
  }

  @Basic
  @Column(name = "FROZEN_STATUS", nullable = true, precision = 0)
  public Integer getFrozenStatus() {
    return frozenStatus;
  }

  public void setFrozenStatus(Integer frozenStatus) {
    this.frozenStatus = frozenStatus;
  }

  @Basic
  @Column(name = "PAYMENT_TERM", nullable = true, length = 500)
  public String getPaymentTerm() {
    return paymentTerm;
  }

  public void setPaymentTerm(String paymentTerm) {
    this.paymentTerm = paymentTerm;
  }

  @Basic
  @Column(name = "BOND_STATIC_ID", nullable = true, precision = 0)
  public Integer getBondStaticId() {
    return bondStaticId;
  }

  public void setBondStaticId(Integer bondStaticId) {
    this.bondStaticId = bondStaticId;
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
    StgTciInvBondStaticEntity that = (StgTciInvBondStaticEntity) o;
    return Objects.equals(name, that.name) &&
      Objects.equals(code, that.code) &&
      Objects.equals(par, that.par) &&
      Objects.equals(currency, that.currency) &&
      Objects.equals(issueDate, that.issueDate) &&
      Objects.equals(expiredDate, that.expiredDate) &&
      Objects.equals(notePaymentInterest, that.notePaymentInterest) &&
      Objects.equals(noteType, that.noteType) &&
      Objects.equals(noteInterest, that.noteInterest) &&
      Objects.equals(baseInterestTop, that.baseInterestTop) &&
      Objects.equals(baseInterestBottom, that.baseInterestBottom) &&
      Objects.equals(bondIssuerId, that.bondIssuerId) &&
      Objects.equals(seriesContractCode, that.seriesContractCode) &&
      Objects.equals(seriesBondCode, that.seriesBondCode) &&
      Objects.equals(isCoupon, that.isCoupon) &&
      Objects.equals(couponPaymentEndTerm, that.couponPaymentEndTerm) &&
      Objects.equals(couponRate, that.couponRate) &&
      Objects.equals(couponFreq, that.couponFreq) &&
      Objects.equals(couponDatePayment, that.couponDatePayment) &&
      Objects.equals(trustAsset, that.trustAsset) &&
      Objects.equals(trustAssetValue, that.trustAssetValue) &&
      Objects.equals(guarantee, that.guarantee) &&
      Objects.equals(guaranteeName, that.guaranteeName) &&
      Objects.equals(firstDatePeriodPl, that.firstDatePeriodPl) &&
      Objects.equals(lastDatePeriodPl, that.lastDatePeriodPl) &&
      Objects.equals(floatBandInterest, that.floatBandInterest) &&
      Objects.equals(referenceRateCoupon, that.referenceRateCoupon) &&
      Objects.equals(buildBook, that.buildBook) &&
      Objects.equals(listedStatus, that.listedStatus) &&
      Objects.equals(active, that.active) &&
      Objects.equals(status, that.status) &&
      Objects.equals(updatedDate, that.updatedDate) &&
      Objects.equals(createdDate, that.createdDate) &&
      Objects.equals(paymentDate, that.paymentDate) &&
      Objects.equals(listedCode, that.listedCode) &&
      Objects.equals(frozenStatus, that.frozenStatus) &&
      Objects.equals(paymentTerm, that.paymentTerm) &&
      Objects.equals(bondStaticId, that.bondStaticId) &&
      Objects.equals(etlCurDate, that.etlCurDate) &&
      Objects.equals(etlRunDateTime, that.etlRunDateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, code, par, currency, issueDate, expiredDate, notePaymentInterest, noteType, noteInterest, baseInterestTop, baseInterestBottom, bondIssuerId, seriesContractCode,
      seriesBondCode, isCoupon, couponPaymentEndTerm, couponRate, couponFreq, couponDatePayment, trustAsset, trustAssetValue, guarantee, guaranteeName, firstDatePeriodPl, lastDatePeriodPl,
      floatBandInterest, referenceRateCoupon, buildBook, listedStatus, active, status, updatedDate, createdDate, paymentDate, listedCode, frozenStatus, paymentTerm, bondStaticId, etlCurDate,
      etlRunDateTime);
  }
}
