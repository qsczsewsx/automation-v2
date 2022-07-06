package com.tcbs.automation.tcbond;

import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "BondCategory")
public class BondCategory {

  @Id
  @Column(name = "ID")
  private long id;
  @Column(name = "Name")
  private String name;
  @Column(name = "Description")
  private String description;
  @Column(name = "Code")
  private String code;
  @Column(name = "MaxTimeInvest")
  private Double maxTimeInvest;
  @Column(name = "MaxInvest")
  private Double maxInvest;
  @Column(name = "MinTimeInvest")
  private Double minTimeInvest;
  @Column(name = "MinInvest")
  private Double minInvest;
  @Column(name = "IsCoupon")
  private Long isCoupon;
  @Column(name = "CouponPayment")
  private Long couponPayment;
  @Column(name = "SellBackMethod")
  private Long sellBackMethod;
  @Column(name = "FeeBroker")
  private Double feeBroker;
  @Column(name = "DateFeeBroker")
  private Timestamp dateFeeBroker;
  @Column(name = "FeeFixBroker")
  private Double feeFixBroker;
  @Column(name = "DateFeeFixBroker")
  private Timestamp dateFeeFixBroker;
  @Column(name = "CancelSell")
  private Long cancelSell;
  @Column(name = "CancelBuy")
  private Long cancelBuy;
  @Column(name = "TotalSellBack")
  private Double totalSellBack;
  @Column(name = "TotalBuyBack")
  private Double totalBuyBack;
  @Column(name = "UpdatedDate")
  private Timestamp updatedDate;
  @Column(name = "IsDefault")
  private Long isDefault;
  @Column(name = "Active")
  private Long active;
  @Column(name = "ApplyRateDate")
  private Timestamp applyRateDate;
  @Column(name = "CreatedDate")
  private Timestamp createdDate;
  @Column(name = "ExpiredRateDate")
  private Timestamp expiredRateDate;
  @Column(name = "RecurringData")
  private String recurringData;
  @Column(name = "Type")
  private Long type;
  @Column(name = "LimitValue")
  private Double limitValue;
  @Column(name = "TotalLimitValue")
  private Double totalLimitValue;

  public BondCategory() {
  }

  public static List<BondCategory> getBuyCode(String code) {
    Session session = TcBond.tcBondDbConnection.getSession();
    session.clear();
    Query<BondCategory> query = session.createQuery("from BondCategory where code =: code");
    query.setParameter("code", code);
    List<BondCategory> result = query.getResultList();

    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }

  public static void deleteFinancialTermByCodeOnTCBond(String code) {
    Session session = TcBond.tcBondDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<BondCategory> query = session.createQuery(
      "DELETE FROM BondCategory WHERE code like :code"
    );
    query.setParameter("code", "%" + code + "%");
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Double getMaxTimeInvest() {
    return maxTimeInvest;
  }

  public void setMaxTimeInvest(Double maxTimeInvest) {
    this.maxTimeInvest = maxTimeInvest;
  }

  public Double getMaxInvest() {
    return maxInvest;
  }

  public void setMaxInvest(Double maxInvest) {
    this.maxInvest = maxInvest;
  }

  public Double getMinTimeInvest() {
    return minTimeInvest;
  }

  public void setMinTimeInvest(Double minTimeInvest) {
    this.minTimeInvest = minTimeInvest;
  }

  public Double getMinInvest() {
    return minInvest;
  }

  public void setMinInvest(Double minInvest) {
    this.minInvest = minInvest;
  }

  public Long getIsCoupon() {
    return isCoupon;
  }

  public void setIsCoupon(Long isCoupon) {
    this.isCoupon = isCoupon;
  }

  public Long getCouponPayment() {
    return couponPayment;
  }

  public void setCouponPayment(Long couponPayment) {
    this.couponPayment = couponPayment;
  }

  public Long getSellBackMethod() {
    return sellBackMethod;
  }

  public void setSellBackMethod(Long sellBackMethod) {
    this.sellBackMethod = sellBackMethod;
  }

  public Double getFeeBroker() {
    return feeBroker;
  }

  public void setFeeBroker(Double feeBroker) {
    this.feeBroker = feeBroker;
  }

  public Timestamp getDateFeeBroker() {
    return dateFeeBroker;
  }

  public void setDateFeeBroker(Timestamp dateFeeBroker) {
    this.dateFeeBroker = dateFeeBroker;
  }

  public Double getFeeFixBroker() {
    return feeFixBroker;
  }

  public void setFeeFixBroker(Double feeFixBroker) {
    this.feeFixBroker = feeFixBroker;
  }

  public Timestamp getDateFeeFixBroker() {
    return dateFeeFixBroker;
  }

  public void setDateFeeFixBroker(Timestamp dateFeeFixBroker) {
    this.dateFeeFixBroker = dateFeeFixBroker;
  }

  public Long getCancelSell() {
    return cancelSell;
  }

  public void setCancelSell(Long cancelSell) {
    this.cancelSell = cancelSell;
  }

  public Long getCancelBuy() {
    return cancelBuy;
  }

  public void setCancelBuy(Long cancelBuy) {
    this.cancelBuy = cancelBuy;
  }

  public Double getTotalSellBack() {
    return totalSellBack;
  }

  public void setTotalSellBack(Double totalSellBack) {
    this.totalSellBack = totalSellBack;
  }

  public Double getTotalBuyBack() {
    return totalBuyBack;
  }

  public void setTotalBuyBack(Double totalBuyBack) {
    this.totalBuyBack = totalBuyBack;
  }

  public Timestamp getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(Timestamp updatedDate) {
    this.updatedDate = updatedDate;
  }

  public Long getIsDefault() {
    return isDefault;
  }

  public void setIsDefault(Long isDefault) {
    this.isDefault = isDefault;
  }

  public Long getActive() {
    return active;
  }

  public void setActive(Long active) {
    this.active = active;
  }

  public Timestamp getApplyRateDate() {
    return applyRateDate;
  }

  public void setApplyRateDate(Timestamp applyRateDate) {
    this.applyRateDate = applyRateDate;
  }

  public Timestamp getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Timestamp createdDate) {
    this.createdDate = createdDate;
  }

  public Timestamp getExpiredRateDate() {
    return expiredRateDate;
  }

  public void setExpiredRateDate(Timestamp expiredRateDate) {
    this.expiredRateDate = expiredRateDate;
  }

  public String getRecurringData() {
    return recurringData;
  }

  public void setRecurringData(String recurringData) {
    this.recurringData = recurringData;
  }

  public Long getType() {
    return type;
  }

  public void setType(Long type) {
    this.type = type;
  }

  public Double getLimitValue() {
    return limitValue;
  }

  public void setLimitValue(Double limitValue) {
    this.limitValue = limitValue;
  }

  public Double getTotalLimitValue() {
    return totalLimitValue;
  }

  public void setTotalLimitValue(Double totalLimitValue) {
    this.totalLimitValue = totalLimitValue;
  }

  @Step("Get all bond categories from TCbond.BondCategory")
  public List<BondCategory> getAllCategories() throws Exception {
    Query<BondCategory> query = TcBond.tcBondDbConnection.getSession().createQuery("from BondCategory", BondCategory.class);
    List<BondCategory> result = query.getResultList();

    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }

  @Step("Get active bond categories from TCbond.BondCategory")
  public List<BondCategory> getActiveCategories() {
    Query<BondCategory> query = TcBond.tcBondDbConnection.getSession().createQuery("from BondCategory where active = 1", BondCategory.class);
    List<BondCategory> result = query.getResultList();

    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }
}
