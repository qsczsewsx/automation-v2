package com.tcbs.automation.coman;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.tcbs.automation.config.coman.ComanKey.BOND_CODE_KEY;

@Entity
@Getter
@Setter
@Table(name = "COUPON_TIMELINE")
public class CouponTimeline {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @Column(name = "BOND_CODE")
  private String bondCode;
  @Column(name = "DEFINE_DATE")
  private Timestamp defineDate;
  @Column(name = "START_DATE")
  private Timestamp startDate;
  @Column(name = "END_DATE")
  private Timestamp endDate;
  @Column(name = "RATE_TYPE")
  private Integer rateType;
  @Column(name = "EXPECTED_RATE")
  private double expectedRate;
  @Column(name = "REAL_RATE")
  private float realRate;
  @Column(name = "REFER_RATE")
  private float referRate;
  @Column(name = "REFER_TYPE")
  private Integer referType;
  @Column(name = "MARGIN")
  private Float margin;
  @Column(name = "ACTIVE")
  private String active;
  @Column(name = "IS_MANUAL")
  private String isManual;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "UPDATED_DATE")
  private Timestamp updatedDate;
  @Column(name = "REDEEM")
  private Float redeem;

  public static void deleteCouponTimeLineByBondCode(String bondCode) {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<CouponTimeline> query = session.createQuery(
      "DELETE FROM CouponTimeline a WHERE a.bondCode = :bondCode"
    );
    query.setParameter(BOND_CODE_KEY, bondCode);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  @Step
  public static List<CouponTimeline> getCouponTimelineByBondCode(String bondCode) {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<CouponTimeline> query = session
      .createQuery("from CouponTimeline a where a.bondCode=:bondCode and a.active = 1 order by id", CouponTimeline.class);
    query.setParameter(BOND_CODE_KEY, bondCode);

    return query.getResultList();
  }

  public static CouponTimeline getDetailCouponTimelineByBondCode(String bondCode) {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<CouponTimeline> query = session
      .createQuery("from CouponTimeline a where a.bondCode=:bondCode order by id desc", CouponTimeline.class).setFirstResult(0);
    query.setParameter(BOND_CODE_KEY, bondCode);

    return query.getResultList().get(0);
  }

  public static void deleteZeroPeriodCouponTimeLineByBondCode(String id) {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<CouponTimeline> query = session.createQuery(
      "DELETE FROM CouponTimeline a WHERE a.id = :id"
    );
    query.setParameter("id", id);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static String getReferRate(String date, String bondCode) {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    StringBuilder querySql = new StringBuilder("SELECT DISTINCT(REFER_RATE) FROM COUPON_TIMELINE ");
    querySql.append(String.format("WHERE BOND_CODE IN (%s) AND START_DATE > to_date('%s','YYYY-MM-DD') AND REAL_RATE = 0", bondCode, date));
    Query query = session.createSQLQuery(querySql.toString());
    Object obj = query.getSingleResult();
    if (obj != null) {
      return ((BigDecimal) obj).toString();
    }
    return "0";
  }

  public static String getReferRateId(String date, String bondCodes) {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    StringBuilder querySql = new StringBuilder();
    querySql.append(String.format("SELECT DISTINCT(REFER_TYPE) FROM COUPON_TIMELINE WHERE BOND_CODE IN (%s) AND START_DATE > to_date('%s','YYYY-MM-DD') AND REAL_RATE = 0", bondCodes, date));
    Query query = session.createSQLQuery(querySql.toString());
    Object obj = query.getSingleResult();
    if (obj != null) {
      return ((BigDecimal) obj).toString();
    }
    return "0";
  }

  public static List<String> getFieldDateInPaymentTimeline(String date, String bondCode, String fieldName, boolean testFuture) {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    StringBuilder querySql = new StringBuilder();
    querySql.append(String.format("SELECT %s FROM PAYMENT_TIMELINE WHERE BOND_CODE = '%s' AND IS_ZERO_PERIOD = 0 ", fieldName, bondCode));
    if (testFuture) {
      querySql.append(String.format("AND START_DATE > to_date('%s','YYYY-MM-DD')", date));
    }
    querySql.append(" ORDER BY ID DESC");
    Query query = session.createSQLQuery(querySql.toString());
    List<Object[]> objList = query.getResultList();
    List<String> rs = new ArrayList<>();
    for (Object obj : objList) {
      rs.add(((Timestamp) obj).toString());
    }
    return rs;
  }

  public static List<String> getAffectedBond(String date, Integer referId) {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    StringBuilder querySql = new StringBuilder();
    querySql.append("SELECT DISTINCT(BOND_CODE) FROM COUPON_TIMELINE WHERE REFER_TYPE = :referType AND ACTIVE = 1");
    querySql.append(String.format("AND DEFINE_DATE = to_date('%s','YYYY-MM-DD')", date));

    Query query = session.createSQLQuery(querySql.toString());
    query.setParameter("referType", referId);
    List<Object[]> objList = query.getResultList();
    List<String> rs = new ArrayList<>();
    for (Object obj : objList) {
      rs.add(obj.toString());
    }
    return rs;
  }
}
