package com.tcbs.automation.tcbond;


import com.tcbs.automation.functions.PublicConstant;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Coupon")
public class Coupon {
  @Id
  @Column(name = "ID")
  private Long id;
  @Column(name = "BondId")
  private Long bondId;
  @Column(name = "CouponDate")
  private Timestamp couponDate;
  @Column(name = "CouponRate")
  private Double couponRate;
  @Column(name = "CreatedDate")
  private Timestamp createdDate;
  @Column(name = "UpdatedDate")
  private Timestamp updatedDate;
  @Column(name = "Active")
  private Long active;
  @Column(name = "CloseCouponDate")
  private Timestamp closeCouponDate;
  @Column(name = "AdvancedCoupon")
  private Long advancedCoupon;


  public Coupon(Long id, Long bondId, Timestamp couponDate,
                Double couponRate, Timestamp createdDate,
                Timestamp updatedDate, Long active,
                Timestamp closeCouponDate, Long advancedCoupon) {
    this.id = id;
    this.bondId = bondId;
    this.couponDate = couponDate;
    this.couponRate = couponRate;
    this.createdDate = createdDate;
    this.updatedDate = updatedDate;
    this.active = active;
    this.closeCouponDate = closeCouponDate;
    this.advancedCoupon = advancedCoupon;
  }

  public Coupon() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getBondId() {
    return bondId;
  }

  public void setBondId(Long bondId) {
    this.bondId = bondId;
  }

  public Date getCouponDate() {
    return new Date(couponDate.getTime());
  }

  public void setCouponDate(String couponDate) throws ParseException {
    this.couponDate = new Timestamp(PublicConstant.dateTimeFormat.parse(couponDate).getTime());
  }

  public Double getCouponRate() {
    return couponRate;
  }

  public void setCouponRate(Double couponRate) {
    this.couponRate = couponRate;
  }

  public String getCreatedDate() {
    return PublicConstant.dateTimeFormat.format(createdDate);
  }

  public void setCreatedDate(String createdDate) throws ParseException {
    this.createdDate = new Timestamp(PublicConstant.dateTimeFormat.parse(createdDate).getTime());
  }

  public String getUpdatedDate() {
    return PublicConstant.dateTimeFormat.format(updatedDate);
  }

  public void setUpdatedDate(String updatedDate) throws ParseException {
    this.updatedDate = new Timestamp(PublicConstant.dateTimeFormat.parse(updatedDate).getTime());
  }

  public Long getActive() {
    return active;
  }

  public void setActive(Long active) {
    this.active = active;
  }

  public String getCloseCouponDate() {
    return closeCouponDate == null ? null : PublicConstant.dateTimeFormat.format(closeCouponDate);
  }

  public void setCloseCouponDate(String closeCouponDate) throws ParseException {
    this.closeCouponDate = new Timestamp(PublicConstant.dateTimeFormat.parse(closeCouponDate).getTime());
  }

  public Long getAdvancedCoupon() {
    return advancedCoupon;
  }

  public void setAdvancedCoupon(Long advancedCoupon) {
    this.advancedCoupon = advancedCoupon;
  }

  @Step
  public List<Coupon> getListCouponDataFromBondId(Long bondId) {
    Query<Coupon> query = TcBond.tcBondDbConnection.getSession().createQuery(
      "from Coupon a where a.bondId=:bondId order by CouponDate asc", Coupon.class
    );
    query.setParameter("bondId", bondId);
    List<Coupon> result = query.getResultList();
    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }
}

















