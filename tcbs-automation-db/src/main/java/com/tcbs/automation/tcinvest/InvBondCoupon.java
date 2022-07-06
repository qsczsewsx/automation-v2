package com.tcbs.automation.tcinvest;

import com.tcbs.automation.coman.PaymentTimeline;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import static com.tcbs.automation.config.coman.ComanKey.BOND_STATIC_ID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "INV_BOND_COUPON")
public class InvBondCoupon {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @Column(name = "BOND_STATIC_ID")
  private String bondStaticId;
  @Column(name = "COUPON_FIX_DATE")
  private Date couponFixDate;
  @Column(name = "COUPON_DATE")
  private Date couponDate;
  @Column(name = "COUPON")
  private String coupon;
  @Column(name = "ACTIVE")
  private String active;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "UPDATED_DATE")
  private Timestamp updatedDate;
  @Column(name = "ADVANCED_COUPON")
  private String advancedCoupon;
  @NotNull
  @Column(name = "REDEEM")
  private String redeem;

//  public static final Session session = TcInvest.tcInvestDbConnection.getSession();

  @Step
  public static List<InvBondCoupon> getBondCouponTimeLineInv(String bondStaticId) {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    Query<InvBondCoupon> query = session.createQuery("from InvBondCoupon a where a.bondStaticId=:bondStaticId and a.active = 1 order by couponDate", InvBondCoupon.class);
    query.setParameter(BOND_STATIC_ID, bondStaticId);

    return query.getResultList();
  }

  @Step
  public static InvBondCoupon getZeroBondCouponTimeLineInv(String bondStaticId) {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    Query<InvBondCoupon> query = session.createQuery("from InvBondCoupon a where a.bondStaticId=:bondStaticId and a.active = 1 order by couponDate asc ", InvBondCoupon.class).setFirstResult(0);
    query.setParameter(BOND_STATIC_ID, bondStaticId);

    return query.getResultList().get(0);
  }

  public static void deleteZeroPeriodBondTimeLineByid(String id) {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<InvBondCoupon> query = session.createQuery(
      "DELETE FROM InvBondCoupon a WHERE a.id = :id"
    );
    query.setParameter("id", id);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static void deleteTimeLineByBondCode(String bondStaticId) {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<PaymentTimeline> query = session.createQuery(
      "DELETE FROM InvBondCoupon a WHERE a.bondStaticId = :bondStaticId"
    );
    query.setParameter(BOND_STATIC_ID, bondStaticId);
    query.executeUpdate();
    session.getTransaction().commit();
  }
}
