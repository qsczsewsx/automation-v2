package com.tcbs.automation.coman;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "COUPON_TEMPLATE")
public class CouponTemplate {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private int id;
  @Column(name = "BOND_CODE")
  private String bondCode;
  @Column(name = "COUPON_FREQ")
  private Integer couponFreq;
  @Column(name = "COUPON_START_DATE")
  private Date couponStartDate;
  @Column(name = "FIXED_RATE")
  private String fixedRate;
  @Column(name = "FIXED_PERIOD")
  private Integer fixedPeriod;
  @Column(name = "DEFINE_SHIFT_DAY")
  private Integer defineShiftDay;
  @Column(name = "COUPON_SHIFT_TYPE")
  private Integer couponShiftType;
  @Column(name = "COUPON_SHIFT_WORKING_DAY")
  private Integer couponShiftWorkingDay;
  @Column(name = "MARGIN")
  private String margin;
  @Column(name = "PAYMENT_FREQ")
  private Integer paymentFreq;
  @Column(name = "PAYMENT_START_DATE")
  private Date paymentStartDate;
  @Column(name = "CONFIRM_SHIFT_DAY")
  private Integer confirmShiftDay;
  @Column(name = "GDKHQ_SHIFT_DAY")
  private Integer gdkhqShiftDay;
  @Column(name = "PAYMENT_SHIFT_WORKING_DAY")
  private Integer paymentShiftWorkingDay;
  @Column(name = "CONFIRM_SHIFT_TYPE")
  private Integer confirmShiftType;
  @Column(name = "GDKHQ_SHIFT_TYPE")
  private Integer gdkhqShiftType;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "UPDATED_DATE")
  private Date updatedDate;
  @Column(name = "MIN_RATE")
  private String minRate;
  @Column(name = "MAX_RATE")
  private String maxRate;

  public static void deleteCouponTemplateByBondCode(String bondCode) {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<CouponTemplate> query = session.createQuery(
      "DELETE CouponTemplate a WHERE a.bondCode = :bondCode"
    );
    query.setParameter("bondCode", bondCode);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  @Step
  public static CouponTemplate getCouponTemplateByBondCode(String bondCode) {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<CouponTemplate> query = session
      .createQuery("from CouponTemplate a where a.bondCode=:bondCode", CouponTemplate.class);
    query.setParameter("bondCode", bondCode);

    return query.getSingleResult();
  }

  @Step
  public static List<CouponTemplate> getListCouponTemplate() {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<CouponTemplate> query = session
      .createQuery("from CouponTemplate a where 1=1", CouponTemplate.class);
    return query.getResultList();
  }
}
