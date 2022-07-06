package com.tcbs.automation.coman;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tcbs.automation.config.coman.ComanKey.BOND_CODE_KEY;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "PAYMENT_TIMELINE")
public class PaymentTimeline {

  private final static Logger logger = LoggerFactory.getLogger(AppliedBond.class);
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @Column(name = "BOND_CODE")
  private String bondCode;
  @Column(name = "START_DATE")
  private Timestamp startDate;
  @Column(name = "END_DATE")
  private Timestamp endDate;
  @Column(name = "GDKHQ")
  private Timestamp gdkhq;
  @Column(name = "CONFIRM_DATE")
  private Timestamp confirmDate;
  @Column(name = "PAYMENT_DATE")
  private Timestamp paymentDate;
  @Column(name = "ACTIVE")
  private String active;

  public static void deletePaymentTimeLineByBondCode(String bondCode) {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<PaymentTimeline> query = session.createQuery(
      "DELETE FROM PaymentTimeline a WHERE a.bondCode = :bondCode"
    );
    query.setParameter(BOND_CODE_KEY, bondCode);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  @Step
  public static List<PaymentTimeline> getPaymentTimelineByBondCode(String bondCode) {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<PaymentTimeline> query = session
      .createQuery("from PaymentTimeline a where a.bondCode=:bondCode order by id", PaymentTimeline.class);
    query.setParameter(BOND_CODE_KEY, bondCode);
    return query.getResultList();
  }

  @Step
  public static PaymentTimeline getZeroPeriodPaymentTimelineByBondCode(String bondCode) {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<PaymentTimeline> query = session
      .createQuery("from PaymentTimeline a where a.bondCode=:bondCode order by id desc ", PaymentTimeline.class).setFirstResult(0);
    query.setParameter(BOND_CODE_KEY, bondCode);
    return query.getResultList().get(0);
  }

  public static void deleteZeroPeriodPaymentTimeLineByBondCode(String id) {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<PaymentTimeline> query = session.createQuery(
      "DELETE FROM PaymentTimeline a WHERE a.id = :id"
    );
    query.setParameter("id", id);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static List<String> getBondCodebyDate(HashMap<String, Object> params) {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    StringBuilder querySql = new StringBuilder("SELECT DISTINCT(BOND_CODE) FROM PAYMENT_TIMELINE ");
    for (
      Map.Entry<String, Object> entry : params.entrySet()) {
      querySql.append(String.format("WHERE " + entry.getKey() + " = to_date('" + entry.getValue() + "','YYYY-MM-DD')"));
    }
    try {
      List<String> query = Connection.comanDbConnection.getSession().createNativeQuery(querySql.toString()).getResultList();
      return query;
    } catch (Exception ex) {
      logger.warn("Exception!{}", ex);
      return new ArrayList<>();
    }
  }

  public static void main(String[] args) {
    HashMap b = new HashMap();
    b.put("PAYMENT_DATE", "2022-02-28");
    List<PaymentTimeline> a = PaymentTimeline.getBondCodebyDate(b);
    System.out.println(a);
  }

}
