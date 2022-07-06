package com.tcbs.automation.tcbond;

import com.tcbs.automation.functions.PublicConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Bond")
public class Bond {
  @Id
  @Column(name = "ID")
  private Integer id;
  @Column(name = "Name")
  private String name;
  @Column(name = "Code")
  private String code;
  @Column(name = "Price")
  private Double price;
  @Column(name = "PublicDate")
  private Timestamp publicDate;
  @Column(name = "ExpiredDate")
  private Timestamp expiredDate;
  @Column(name = "SeriesContractCode")
  private String seriesContractCode;
  @Column(name = "SeriesBondCode")
  private String seriesBondCode;
  @Column(name = "FirstInterestDate")
  private Timestamp firstInterestDate;
  @Column(name = "InterestBuyBack")
  private Double interestBuyBack;
  @Column(name = "Description")
  private String description;
  @Column(name = "NoteType")
  private String noteType;
  @Column(name = "NoteInterest")
  private String noteInterest;
  @Column(name = "NotePayInterest")
  private String notePayInterest;
  @Column(name = "CreatedDate")
  private Timestamp createdDate;
  @Column(name = "UpdatedDate")
  private Timestamp updatedDate;
  @Column(name = "Active")
  private Long active;
  @Column(name = "IssuerId")
  private Long issuerId;
  @Column(name = "LastPeriodTrading")
  private Timestamp lastPeriodTrading;
  @Column(name = "BaseInterestBottom")
  private Long baseInterestBottom;
  @Column(name = "BaseInterestTop")
  private Long baseInterestTop;
  @Column(name = "IsPaymentCouponEndTerm")
  private Long isPaymentCouponEndTerm;
  @Column(name = "CouponPayment")
  private Long couponPayment;
  @Column(name = "IsCoupon")
  private Long isCoupon;
  @Column(name = "Guarantee")
  private Long guarantee;
  @Column(name = "CouponEffectDate")
  private Timestamp couponEffectDate;
  @Column(name = "FistDatePeriodPl")
  private Timestamp fistDatePeriodPl;
  @Column(name = "LastDatePeriodPl")
  private Timestamp lastDatePeriodPl;
  @Column(name = "FloatBandInterest")
  private Double floatBandInterest;
  @Column(name = "ReferenceRateCoupon")
  private Double referenceRateCoupon;
  @Column(name = "TrustAsset")
  private Long trustAsset;
  @Column(name = "ListedStatus")
  private Long listedStatus;
  @Column(name = "GuaranteeValue")
  private String guaranteeValue;
  @Column(name = "TrustAssetValue")
  private String trustAssetValue;
  @Column(name = "BuildbookStatus")
  private Long buildbookStatus;
  @Column(name = "ListedCode")
  private String listedCode;
  @Column(name = "FrozenStatus")
  private Long frozenStatus;
  @Column(name = "PaymentTerm")
  private String paymentTerm;
  @Column(name = "PublicOffering")
  private Long publicOffering;

  public String getPublicDate() {
    return PublicConstant.dateTimeFormat.format(publicDate);
  }

  public void setPublicDate(String publicDate) {
    this.publicDate = Timestamp.valueOf(publicDate);
  }

  public String getExpiredDate() {
    return PublicConstant.dateTimeFormat.format(expiredDate);
  }

  public void setExpiredDate(String expiredDate) {
    this.expiredDate = Timestamp.valueOf(expiredDate);
  }

  public String getCreatedDate() {
    return PublicConstant.dateTimeFormat.format(createdDate);
  }

  public void setCreatedDate(String createdDate) {
    this.createdDate = Timestamp.valueOf(createdDate);
  }

  public String getUpdatedDate() {
    return PublicConstant.dateTimeFormat.format(updatedDate);
  }

  public void setUpdatedDate(String updatedDate) {
    this.updatedDate = Timestamp.valueOf(updatedDate);
  }

  public String getLastPeriodTrading() {
    return PublicConstant.dateTimeFormat.format(lastPeriodTrading);
  }

  public void setLastPeriodTrading(String lastPeriodTrading) {
    this.lastPeriodTrading = Timestamp.valueOf(lastPeriodTrading);
  }

  public String getCouponEffectDate() {
    return PublicConstant.dateTimeFormat.format(couponEffectDate);
  }

  public void setCouponEffectDate(String couponEffectDate) {
    this.couponEffectDate = Timestamp.valueOf(couponEffectDate);
  }

  public String getFistDatePeriodPl() {
    return PublicConstant.dateTimeFormat.format(fistDatePeriodPl);
  }

  public void setFistDatePeriodPl(String fistDatePeriodPl) {
    this.fistDatePeriodPl = Timestamp.valueOf(fistDatePeriodPl);
  }

  public String getLastDatePeriodPl() {
    return PublicConstant.dateTimeFormat.format(lastDatePeriodPl);
  }

  public void setLastDatePeriodPl(String lastDatePeriodPl) {
    this.lastDatePeriodPl = Timestamp.valueOf(lastDatePeriodPl);
  }

  public String getExpiredDateByFormat(String format) {
    return new SimpleDateFormat(format).format(this.expiredDate);
  }

  public String getPublicDateByFormat(String format) {
    return new SimpleDateFormat(format).format(this.publicDate);
  }

  @Step
  public List<Integer> getListIdBond() {
    Query<Integer> query = TcBond.tcBondDbConnection.getSession().createQuery(
      "select a.id from Bond a order by a.id asc", Integer.class);
    List<Integer> result = query.getResultList();
    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }

  @Step
  public Bond getBondById(Integer id) {
    Query<Bond> query = TcBond.tcBondDbConnection.getSession().createQuery(
      "from Bond a where a.id=:id", Bond.class);
    query.setParameter("id", id);
    List<Bond> result = query.getResultList();
    if (result.size() > 0) {
      return result.get(0);
    } else {
      return null;
    }
  }

  @Step
  public List<Bond> getListOfBonds() {
    Query<Bond> query = TcBond.tcBondDbConnection.getSession().createQuery(
      "from Bond", Bond.class);
    List<Bond> result = query.getResultList();
    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }

  @Step
  public List<Bond> getListOfBondsExpired() {
    Query<Bond> query = TcBond.tcBondDbConnection.getSession().createQuery(
      "from Bond where ExpiredDate < GETDATE()-1", Bond.class);
    List<Bond> result = query.getResultList();
    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }

  @Step
  public List<Bond> getListOfBondsNotExpired() {
    Query<Bond> query = TcBond.tcBondDbConnection.getSession().createQuery(
      "from Bond where ExpiredDate >= current_timestamp", Bond.class);
    List<Bond> result = query.getResultList();
    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }

  @Step
  public Bond getBondByBondCode(String bondCode) {
    Query<Bond> query = TcBond.tcBondDbConnection.getSession().createQuery(
      "from Bond a where a.code=:code", Bond.class);
    query.setParameter("code", bondCode);
    return query.getSingleResult();
  }

  @Step("Get bond by productCode {0}")
  public Bond getBondByProductCode(String productCode) {
    Query<Bond> query = TcBond.tcBondDbConnection.getSession()
      .createNativeQuery("select * from Bond a where a.ID in (select bondId from BondProduct where Code =:productCode)", Bond.class)
      .setParameter("productCode", productCode);
    return query.getSingleResult();
  }
}
















