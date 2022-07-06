package com.tcbs.automation.dwh;

import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

import static com.tcbs.automation.DataBaseUtils.beginTransaction;

@Entity
@Table(name = "Stg_tci_INV_BOND_STATIC")
public class StgTciInvBondStaticEntity {
  private Integer bondStaticId;
  private String name;
  private String code;
  private Double par;
  private String currency;
  private Date issueDate;
  private Date expiredDate;
  private String notePaymentInterest;
  private String noteType;
  private String noteInterest;
  private String baseInterestTop;
  private String baseInterestBottom;
  private Integer bondIssuerId;
  private String seriesContractCode;
  private String seriesBondCode;
  private Byte isCoupon;
  private Byte couponPaymentEndTerm;
  private Double couponRate;
  private String couponFreq;
  private Date couponDatePayment;
  private String trustAsset;
  private String trustAssetValue;
  private String guarantee;
  private String guaranteeName;
  private Date firstDatePeriodPl;
  private Date lastDatePeriodPl;
  private Double floatBandInterest;
  private Double referenceRateCoupon;
  private Byte buildBook;
  private Byte listedStatus;
  private Byte active;
  private Byte status;
  private Timestamp updatedDate;
  private Timestamp createdDate;
  private Date paymentDate;
  private String listedCode;
  private Integer etlCurDate;
  private Timestamp etlRunDateTime;

  @Id
  @Column(name = "BOND_STATIC_ID")
  public Integer getBondStaticId() {
    return bondStaticId;
  }

  public void setBondStaticId(Integer bondStaticId) {
    this.bondStaticId = bondStaticId;
  }

  @Basic
  @Column(name = "NAME")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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
  @Column(name = "PAR")
  public Double getPar() {
    return par;
  }

  public void setPar(Double par) {
    this.par = par;
  }

  @Basic
  @Column(name = "CURRENCY")
  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  @Basic
  @Column(name = "ISSUE_DATE")
  public Date getIssueDate() {
    return issueDate;
  }

  public void setIssueDate(Date issueDate) {
    this.issueDate = issueDate;
  }

  @Basic
  @Column(name = "EXPIRED_DATE")
  public Date getExpiredDate() {
    return expiredDate;
  }

  public void setExpiredDate(Date expiredDate) {
    this.expiredDate = expiredDate;
  }

  @Basic
  @Column(name = "NOTE_PAYMENT_INTEREST")
  public String getNotePaymentInterest() {
    return notePaymentInterest;
  }

  public void setNotePaymentInterest(String notePaymentInterest) {
    this.notePaymentInterest = notePaymentInterest;
  }

  @Basic
  @Column(name = "NOTE_TYPE")
  public String getNoteType() {
    return noteType;
  }

  public void setNoteType(String noteType) {
    this.noteType = noteType;
  }

  @Basic
  @Column(name = "NOTE_INTEREST")
  public String getNoteInterest() {
    return noteInterest;
  }

  public void setNoteInterest(String noteInterest) {
    this.noteInterest = noteInterest;
  }

  @Basic
  @Column(name = "BASE_INTEREST_TOP")
  public String getBaseInterestTop() {
    return baseInterestTop;
  }

  public void setBaseInterestTop(String baseInterestTop) {
    this.baseInterestTop = baseInterestTop;
  }

  @Basic
  @Column(name = "BASE_INTEREST_BOTTOM")
  public String getBaseInterestBottom() {
    return baseInterestBottom;
  }

  public void setBaseInterestBottom(String baseInterestBottom) {
    this.baseInterestBottom = baseInterestBottom;
  }

  @Basic
  @Column(name = "BOND_ISSUER_ID")
  public Integer getBondIssuerId() {
    return bondIssuerId;
  }

  public void setBondIssuerId(Integer bondIssuerId) {
    this.bondIssuerId = bondIssuerId;
  }

  @Basic
  @Column(name = "SERIES_CONTRACT_CODE")
  public String getSeriesContractCode() {
    return seriesContractCode;
  }

  public void setSeriesContractCode(String seriesContractCode) {
    this.seriesContractCode = seriesContractCode;
  }

  @Basic
  @Column(name = "SERIES_BOND_CODE")
  public String getSeriesBondCode() {
    return seriesBondCode;
  }

  public void setSeriesBondCode(String seriesBondCode) {
    this.seriesBondCode = seriesBondCode;
  }

  @Basic
  @Column(name = "IS_COUPON")
  public Byte getIsCoupon() {
    return isCoupon;
  }

  public void setIsCoupon(Byte isCoupon) {
    this.isCoupon = isCoupon;
  }

  @Basic
  @Column(name = "COUPON_PAYMENT_END_TERM")
  public Byte getCouponPaymentEndTerm() {
    return couponPaymentEndTerm;
  }

  public void setCouponPaymentEndTerm(Byte couponPaymentEndTerm) {
    this.couponPaymentEndTerm = couponPaymentEndTerm;
  }

  @Basic
  @Column(name = "COUPON_RATE")
  public Double getCouponRate() {
    return couponRate;
  }

  public void setCouponRate(Double couponRate) {
    this.couponRate = couponRate;
  }

  @Basic
  @Column(name = "COUPON_FREQ")
  public String getCouponFreq() {
    return couponFreq;
  }

  public void setCouponFreq(String couponFreq) {
    this.couponFreq = couponFreq;
  }

  @Basic
  @Column(name = "COUPON_DATE_PAYMENT")
  public Date getCouponDatePayment() {
    return couponDatePayment;
  }

  public void setCouponDatePayment(Date couponDatePayment) {
    this.couponDatePayment = couponDatePayment;
  }

  @Basic
  @Column(name = "TRUST_ASSET")
  public String getTrustAsset() {
    return trustAsset;
  }

  public void setTrustAsset(String trustAsset) {
    this.trustAsset = trustAsset;
  }

  @Basic
  @Column(name = "TRUST_ASSET_VALUE")
  public String getTrustAssetValue() {
    return trustAssetValue;
  }

  public void setTrustAssetValue(String trustAssetValue) {
    this.trustAssetValue = trustAssetValue;
  }

  @Basic
  @Column(name = "GUARANTEE")
  public String getGuarantee() {
    return guarantee;
  }

  public void setGuarantee(String guarantee) {
    this.guarantee = guarantee;
  }

  @Basic
  @Column(name = "GUARANTEE_NAME")
  public String getGuaranteeName() {
    return guaranteeName;
  }

  public void setGuaranteeName(String guaranteeName) {
    this.guaranteeName = guaranteeName;
  }

  @Basic
  @Column(name = "FIRST_DATE_PERIOD_PL")
  public Date getFirstDatePeriodPl() {
    return firstDatePeriodPl;
  }

  public void setFirstDatePeriodPl(Date firstDatePeriodPl) {
    this.firstDatePeriodPl = firstDatePeriodPl;
  }

  @Basic
  @Column(name = "LAST_DATE_PERIOD_PL")
  public Date getLastDatePeriodPl() {
    return lastDatePeriodPl;
  }

  public void setLastDatePeriodPl(Date lastDatePeriodPl) {
    this.lastDatePeriodPl = lastDatePeriodPl;
  }

  @Basic
  @Column(name = "FLOAT_BAND_INTEREST")
  public Double getFloatBandInterest() {
    return floatBandInterest;
  }

  public void setFloatBandInterest(Double floatBandInterest) {
    this.floatBandInterest = floatBandInterest;
  }

  @Basic
  @Column(name = "REFERENCE_RATE_COUPON")
  public Double getReferenceRateCoupon() {
    return referenceRateCoupon;
  }

  public void setReferenceRateCoupon(Double referenceRateCoupon) {
    this.referenceRateCoupon = referenceRateCoupon;
  }

  @Basic
  @Column(name = "BUILD_BOOK")
  public Byte getBuildBook() {
    return buildBook;
  }

  public void setBuildBook(Byte buildBook) {
    this.buildBook = buildBook;
  }

  @Basic
  @Column(name = "LISTED_STATUS")
  public Byte getListedStatus() {
    return listedStatus;
  }

  public void setListedStatus(Byte listedStatus) {
    this.listedStatus = listedStatus;
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
  @Column(name = "STATUS")
  public Byte getStatus() {
    return status;
  }

  public void setStatus(Byte status) {
    this.status = status;
  }

  @Basic
  @Column(name = "UPDATED_DATE")
  public Timestamp getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(Timestamp updatedDate) {
    this.updatedDate = updatedDate;
  }

  @Basic
  @Column(name = "CREATED_DATE")
  public Timestamp getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Timestamp createdDate) {
    this.createdDate = createdDate;
  }

  @Basic
  @Column(name = "PAYMENT_DATE")
  public Date getPaymentDate() {
    return paymentDate;
  }

  public void setPaymentDate(Date paymentDate) {
    this.paymentDate = paymentDate;
  }

  @Basic
  @Column(name = "LISTED_CODE")
  public String getListedCode() {
    return listedCode;
  }

  public void setListedCode(String listedCode) {
    this.listedCode = listedCode;
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
    StgTciInvBondStaticEntity that = (StgTciInvBondStaticEntity) o;
    return bondStaticId == that.bondStaticId &&
      Double.compare(that.par, par) == 0 &&
      bondIssuerId == that.bondIssuerId &&
      Objects.equals(name, that.name) &&
      Objects.equals(code, that.code) &&
      Objects.equals(currency, that.currency) &&
      Objects.equals(issueDate, that.issueDate) &&
      Objects.equals(expiredDate, that.expiredDate) &&
      Objects.equals(notePaymentInterest, that.notePaymentInterest) &&
      Objects.equals(noteType, that.noteType) &&
      Objects.equals(noteInterest, that.noteInterest) &&
      Objects.equals(baseInterestTop, that.baseInterestTop) &&
      Objects.equals(baseInterestBottom, that.baseInterestBottom) &&
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
      Objects.equals(etlCurDate, that.etlCurDate) &&
      Objects.equals(etlRunDateTime, that.etlRunDateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bondStaticId, name, code, par, currency, issueDate, expiredDate, notePaymentInterest, noteType, noteInterest, baseInterestTop, baseInterestBottom, bondIssuerId,
      seriesContractCode, seriesBondCode, isCoupon, couponPaymentEndTerm, couponRate, couponFreq, couponDatePayment, trustAsset, trustAssetValue, guarantee, guaranteeName, firstDatePeriodPl,
      lastDatePeriodPl, floatBandInterest, referenceRateCoupon, buildBook, listedStatus, active, status, updatedDate, createdDate, paymentDate, listedCode, etlCurDate, etlRunDateTime);
  }

  @Step("insert data")
  public boolean saveBondStatic(StgTciInvBondStaticEntity bondInfo) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.beginTransaction();
    Integer id = (Integer) session.save(bondInfo);
    session.getTransaction().commit();
    return true;
  }

  @Step("delete data by key")
  public void deleteByBondCode(String bondCode) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    beginTransaction(session);
    Query<StgTciInvBondStaticEntity> query = session.createQuery(
      "DELETE FROM StgTciInvBondStaticEntity i WHERE i.code=:code"
    );
    query.setParameter("code", bondCode);
    query.executeUpdate();
    session.getTransaction().commit();
  }
}
