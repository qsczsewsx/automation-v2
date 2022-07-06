package com.tcbs.automation.dwh;

import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

import static com.tcbs.automation.DataBaseUtils.beginTransaction;

@Entity
@Table(name = "Stg_tcb_Bond")
public class StgTcbBondEntity {
  private Integer id;
  private String name;
  private String code;
  private Double price;
  private Timestamp publicDate;
  private Timestamp expiredDate;
  private String seriesContractCode;
  private String seriesBondCode;
  private Timestamp firstInterestDate;
  private BigDecimal interestBuyBack;
  private String description;
  private String noteType;
  private String noteInterest;
  private String notePayInterest;
  private Timestamp createdDate;
  private Timestamp updatedDate;
  private Byte active;
  private Integer issuerId;
  private Timestamp lastPeriodTrading;
  private Byte baseInterestBottom;
  private Byte baseInterestTop;
  private Byte isPaymentCouponEndTerm;
  private Integer couponPayment;
  private Integer isCoupon;
  private Byte guarantee;
  private Timestamp couponEffectDate;
  private Timestamp fistDatePeriodPl;
  private Timestamp lastDatePeriodPl;
  private Double floatBandInterest;
  private Double referenceRateCoupon;
  private Byte trustAsset;
  private Byte listedStatus;
  private String guaranteeValue;
  private String trustAssetValue;
  private Byte buildbookStatus;
  private String listedCode;
  private Byte frozenStatus;
  private String paymentTerm;
  private Integer etlCurDate;
  private Timestamp etlRunDateTime;

  @Id
  @Column(name = "ID")
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @Basic
  @Column(name = "Name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Basic
  @Column(name = "Code")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Basic
  @Column(name = "Price")
  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  @Basic
  @Column(name = "PublicDate")
  public Timestamp getPublicDate() {
    return publicDate;
  }

  public void setPublicDate(Timestamp publicDate) {
    this.publicDate = publicDate;
  }

  @Basic
  @Column(name = "ExpiredDate")
  public Timestamp getExpiredDate() {
    return expiredDate;
  }

  public void setExpiredDate(Timestamp expiredDate) {
    this.expiredDate = expiredDate;
  }

  @Basic
  @Column(name = "SeriesContractCode")
  public String getSeriesContractCode() {
    return seriesContractCode;
  }

  public void setSeriesContractCode(String seriesContractCode) {
    this.seriesContractCode = seriesContractCode;
  }

  @Basic
  @Column(name = "SeriesBondCode")
  public String getSeriesBondCode() {
    return seriesBondCode;
  }

  public void setSeriesBondCode(String seriesBondCode) {
    this.seriesBondCode = seriesBondCode;
  }

  @Basic
  @Column(name = "FirstInterestDate")
  public Timestamp getFirstInterestDate() {
    return firstInterestDate;
  }

  public void setFirstInterestDate(Timestamp firstInterestDate) {
    this.firstInterestDate = firstInterestDate;
  }

  @Basic
  @Column(name = "InterestBuyBack")
  public BigDecimal getInterestBuyBack() {
    return interestBuyBack;
  }

  public void setInterestBuyBack(BigDecimal interestBuyBack) {
    this.interestBuyBack = interestBuyBack;
  }

  @Basic
  @Column(name = "Description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Basic
  @Column(name = "NoteType")
  public String getNoteType() {
    return noteType;
  }

  public void setNoteType(String noteType) {
    this.noteType = noteType;
  }

  @Basic
  @Column(name = "NoteInterest")
  public String getNoteInterest() {
    return noteInterest;
  }

  public void setNoteInterest(String noteInterest) {
    this.noteInterest = noteInterest;
  }

  @Basic
  @Column(name = "NotePayInterest")
  public String getNotePayInterest() {
    return notePayInterest;
  }

  public void setNotePayInterest(String notePayInterest) {
    this.notePayInterest = notePayInterest;
  }

  @Basic
  @Column(name = "CreatedDate")
  public Timestamp getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Timestamp createdDate) {
    this.createdDate = createdDate;
  }

  @Basic
  @Column(name = "UpdatedDate")
  public Timestamp getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(Timestamp updatedDate) {
    this.updatedDate = updatedDate;
  }

  @Basic
  @Column(name = "Active")
  public Byte getActive() {
    return active;
  }

  public void setActive(Byte active) {
    this.active = active;
  }

  @Basic
  @Column(name = "IssuerId")
  public Integer getIssuerId() {
    return issuerId;
  }

  public void setIssuerId(Integer issuerId) {
    this.issuerId = issuerId;
  }

  @Basic
  @Column(name = "LastPeriodTrading")
  public Timestamp getLastPeriodTrading() {
    return lastPeriodTrading;
  }

  public void setLastPeriodTrading(Timestamp lastPeriodTrading) {
    this.lastPeriodTrading = lastPeriodTrading;
  }

  @Basic
  @Column(name = "BaseInterestBottom")
  public Byte getBaseInterestBottom() {
    return baseInterestBottom;
  }

  public void setBaseInterestBottom(Byte baseInterestBottom) {
    this.baseInterestBottom = baseInterestBottom;
  }

  @Basic
  @Column(name = "BaseInterestTop")
  public Byte getBaseInterestTop() {
    return baseInterestTop;
  }

  public void setBaseInterestTop(Byte baseInterestTop) {
    this.baseInterestTop = baseInterestTop;
  }

  @Basic
  @Column(name = "IsPaymentCouponEndTerm")
  public Byte getIsPaymentCouponEndTerm() {
    return isPaymentCouponEndTerm;
  }

  public void setIsPaymentCouponEndTerm(Byte isPaymentCouponEndTerm) {
    this.isPaymentCouponEndTerm = isPaymentCouponEndTerm;
  }

  @Basic
  @Column(name = "CouponPayment")
  public Integer getCouponPayment() {
    return couponPayment;
  }

  public void setCouponPayment(Integer couponPayment) {
    this.couponPayment = couponPayment;
  }

  @Basic
  @Column(name = "IsCoupon")
  public Integer getIsCoupon() {
    return isCoupon;
  }

  public void setIsCoupon(Integer isCoupon) {
    this.isCoupon = isCoupon;
  }

  @Basic
  @Column(name = "Guarantee")
  public Byte getGuarantee() {
    return guarantee;
  }

  public void setGuarantee(Byte guarantee) {
    this.guarantee = guarantee;
  }

  @Basic
  @Column(name = "CouponEffectDate")
  public Timestamp getCouponEffectDate() {
    return couponEffectDate;
  }

  public void setCouponEffectDate(Timestamp couponEffectDate) {
    this.couponEffectDate = couponEffectDate;
  }

  @Basic
  @Column(name = "FistDatePeriodPl")
  public Timestamp getFistDatePeriodPl() {
    return fistDatePeriodPl;
  }

  public void setFistDatePeriodPl(Timestamp fistDatePeriodPl) {
    this.fistDatePeriodPl = fistDatePeriodPl;
  }

  @Basic
  @Column(name = "LastDatePeriodPl")
  public Timestamp getLastDatePeriodPl() {
    return lastDatePeriodPl;
  }

  public void setLastDatePeriodPl(Timestamp lastDatePeriodPl) {
    this.lastDatePeriodPl = lastDatePeriodPl;
  }

  @Basic
  @Column(name = "FloatBandInterest")
  public Double getFloatBandInterest() {
    return floatBandInterest;
  }

  public void setFloatBandInterest(Double floatBandInterest) {
    this.floatBandInterest = floatBandInterest;
  }

  @Basic
  @Column(name = "ReferenceRateCoupon")
  public Double getReferenceRateCoupon() {
    return referenceRateCoupon;
  }

  public void setReferenceRateCoupon(Double referenceRateCoupon) {
    this.referenceRateCoupon = referenceRateCoupon;
  }

  @Basic
  @Column(name = "TrustAsset")
  public Byte getTrustAsset() {
    return trustAsset;
  }

  public void setTrustAsset(Byte trustAsset) {
    this.trustAsset = trustAsset;
  }

  @Basic
  @Column(name = "ListedStatus")
  public Byte getListedStatus() {
    return listedStatus;
  }

  public void setListedStatus(Byte listedStatus) {
    this.listedStatus = listedStatus;
  }

  @Basic
  @Column(name = "GuaranteeValue")
  public String getGuaranteeValue() {
    return guaranteeValue;
  }

  public void setGuaranteeValue(String guaranteeValue) {
    this.guaranteeValue = guaranteeValue;
  }

  @Basic
  @Column(name = "TrustAssetValue")
  public String getTrustAssetValue() {
    return trustAssetValue;
  }

  public void setTrustAssetValue(String trustAssetValue) {
    this.trustAssetValue = trustAssetValue;
  }

  @Basic
  @Column(name = "BuildbookStatus")
  public Byte getBuildbookStatus() {
    return buildbookStatus;
  }

  public void setBuildbookStatus(Byte buildbookStatus) {
    this.buildbookStatus = buildbookStatus;
  }

  @Basic
  @Column(name = "ListedCode")
  public String getListedCode() {
    return listedCode;
  }

  public void setListedCode(String listedCode) {
    this.listedCode = listedCode;
  }

  @Basic
  @Column(name = "FrozenStatus")
  public Byte getFrozenStatus() {
    return frozenStatus;
  }

  public void setFrozenStatus(Byte frozenStatus) {
    this.frozenStatus = frozenStatus;
  }

  @Basic
  @Column(name = "PaymentTerm")
  public String getPaymentTerm() {
    return paymentTerm;
  }

  public void setPaymentTerm(String paymentTerm) {
    this.paymentTerm = paymentTerm;
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
    StgTcbBondEntity that = (StgTcbBondEntity) o;
    return id == that.id &&
      Double.compare(that.price, price) == 0 &&
      issuerId == that.issuerId &&
      Objects.equals(name, that.name) &&
      Objects.equals(code, that.code) &&
      Objects.equals(publicDate, that.publicDate) &&
      Objects.equals(expiredDate, that.expiredDate) &&
      Objects.equals(seriesContractCode, that.seriesContractCode) &&
      Objects.equals(seriesBondCode, that.seriesBondCode) &&
      Objects.equals(firstInterestDate, that.firstInterestDate) &&
      Objects.equals(interestBuyBack, that.interestBuyBack) &&
      Objects.equals(description, that.description) &&
      Objects.equals(noteType, that.noteType) &&
      Objects.equals(noteInterest, that.noteInterest) &&
      Objects.equals(notePayInterest, that.notePayInterest) &&
      Objects.equals(createdDate, that.createdDate) &&
      Objects.equals(updatedDate, that.updatedDate) &&
      Objects.equals(active, that.active) &&
      Objects.equals(lastPeriodTrading, that.lastPeriodTrading) &&
      Objects.equals(baseInterestBottom, that.baseInterestBottom) &&
      Objects.equals(baseInterestTop, that.baseInterestTop) &&
      Objects.equals(isPaymentCouponEndTerm, that.isPaymentCouponEndTerm) &&
      Objects.equals(couponPayment, that.couponPayment) &&
      Objects.equals(isCoupon, that.isCoupon) &&
      Objects.equals(guarantee, that.guarantee) &&
      Objects.equals(couponEffectDate, that.couponEffectDate) &&
      Objects.equals(fistDatePeriodPl, that.fistDatePeriodPl) &&
      Objects.equals(lastDatePeriodPl, that.lastDatePeriodPl) &&
      Objects.equals(floatBandInterest, that.floatBandInterest) &&
      Objects.equals(referenceRateCoupon, that.referenceRateCoupon) &&
      Objects.equals(trustAsset, that.trustAsset) &&
      Objects.equals(listedStatus, that.listedStatus) &&
      Objects.equals(guaranteeValue, that.guaranteeValue) &&
      Objects.equals(trustAssetValue, that.trustAssetValue) &&
      Objects.equals(buildbookStatus, that.buildbookStatus) &&
      Objects.equals(listedCode, that.listedCode) &&
      Objects.equals(frozenStatus, that.frozenStatus) &&
      Objects.equals(paymentTerm, that.paymentTerm) &&
      Objects.equals(etlCurDate, that.etlCurDate) &&
      Objects.equals(etlRunDateTime, that.etlRunDateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, code, price, publicDate, expiredDate, seriesContractCode, seriesBondCode, firstInterestDate, interestBuyBack, description, noteType, noteInterest, notePayInterest,
      createdDate, updatedDate, active, issuerId, lastPeriodTrading, baseInterestBottom, baseInterestTop, isPaymentCouponEndTerm, couponPayment, isCoupon, guarantee, couponEffectDate,
      fistDatePeriodPl,
      lastDatePeriodPl, floatBandInterest, referenceRateCoupon, trustAsset, listedStatus, guaranteeValue, trustAssetValue, buildbookStatus, listedCode, frozenStatus, paymentTerm, etlCurDate,
      etlRunDateTime);
  }

  @Step("insert data")
  public boolean saveBondInfo(StgTcbBondEntity bondInfo) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.beginTransaction();
    Integer id = (Integer) session.save(bondInfo);
    session.getTransaction().commit();
    return true;
  }

  @Step("delete data by key")
  public void deleteByCode(String code) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    beginTransaction(session);
    Query<StgTcbBondEntity> query = session.createQuery(
      "DELETE FROM StgTcbBondEntity i WHERE i.code=:code"
    );
    query.setParameter("code", code);
    query.executeUpdate();
    session.getTransaction().commit();
  }
}
