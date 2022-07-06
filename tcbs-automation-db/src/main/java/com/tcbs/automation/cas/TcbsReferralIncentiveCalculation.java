package com.tcbs.automation.cas;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "TCBS_REFERRAL_INCENTIVE_CALCULATION")
@Getter
@Setter
public class TcbsReferralIncentiveCalculation {
  private static Logger logger = LoggerFactory.getLogger(TcbsReferralIncentiveCalculation.class);

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @Column(name = "TCBSID")
  private String tcbsid;
  @Column(name = "TOTAL_AMOUNT")
  private BigDecimal totalAmount;
  @Column(name = "INCENTIVE_TYPE")
  private Integer incentiveType;
  @Column(name = "EFFECTIVE_DATE_START")
  private Date effectiveDateStart;
  @Column(name = "EFFECTIVE_DATE_END")
  private Date effectiveDateEnd;
  @Column(name = "CREATED_DATE")
  private Date createdDate;
  @Column(name = "UPDATED_DATE")
  private Date updatedDate;
  @Column(name = "FREE_TAX_AMOUNT")
  private Date freeTaxAmount;

  public static void deleteDataByTcbsId(String tcbsId) {
    Session session = CAS.casConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = session.createQuery("delete from TcbsReferralIncentiveCalculation where tcbsid=:tcbsId");
    query.setParameter("tcbsId", tcbsId)
      .executeUpdate();
    session.getTransaction().commit();
  }

  public static void updateCalculation(BigDecimal totalAmount, String tcbsId, BigDecimal freeTaxAmount) {
    try {
      Session session = CAS.casConnection.getSession();
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }
      Query query = session.createQuery("update TcbsReferralIncentiveCalculation set totalAmount =: totalAmount and freeTaxAmount =: freeTaxAmount where tcbsid=:tcbsId");
      query.setParameter("tcbsId", tcbsId);
      query.setParameter("totalAmount", totalAmount);
      query.setParameter("freeTaxAmount", freeTaxAmount)
        .executeUpdate();
      session.getTransaction().commit();
    } catch (Exception ex) {
      logger.info("ex " + ex.getMessage());
    }
  }

  public void dmlPrepareData(String queryString) {
    Session session = CAS.casConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = CAS.casConnection.getSession().createSQLQuery(queryString);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public TcbsReferralIncentiveCalculation getReferralIncentiveCalculationByTcbsId(String tcbsId) {
    Session session = CAS.casConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<TcbsReferralIncentiveCalculation> query = session.createQuery("FROM TcbsReferralIncentiveCalculation WHERE tcbsid=:tcbsid", TcbsReferralIncentiveCalculation.class);
    TcbsReferralIncentiveCalculation result = new TcbsReferralIncentiveCalculation();
    query.setParameter("tcbsid", tcbsId);
    query.setMaxResults(1);
    try {
      result = query.getSingleResult();
      session.refresh(result);
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
    return result;
  }
}
