package com.automation.cas;

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
@Table(name = "xxxx_REFERRAL_INCENTIVE_CALCULATION")
@Getter
@Setter
public class xxxxReferralIncentiveCalculation {
  private static Logger logger = LoggerFactory.getLogger(xxxxReferralIncentiveCalculation.class);

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @Column(name = "xxxxID")
  private String xxxxid;
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

  public static void deleteDataByxxxxId(String xxxxId) {
    Session session = CAS.casConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = session.createQuery("delete from xxxxReferralIncentiveCalculation where xxxxid=:xxxxId");
    query.setParameter("xxxxId", xxxxId)
      .executeUpdate();
    session.getTransaction().commit();
  }

  public static void updateCalculation(BigDecimal totalAmount, String xxxxId, BigDecimal freeTaxAmount) {
    try {
      Session session = CAS.casConnection.getSession();
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }
      Query query = session.createQuery("update xxxxReferralIncentiveCalculation set totalAmount =: totalAmount and freeTaxAmount =: freeTaxAmount where xxxxid=:xxxxId");
      query.setParameter("xxxxId", xxxxId);
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

  public xxxxReferralIncentiveCalculation getReferralIncentiveCalculationByxxxxId(String xxxxId) {
    Session session = CAS.casConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<xxxxReferralIncentiveCalculation> query = session.createQuery("FROM xxxxReferralIncentiveCalculation WHERE xxxxid=:xxxxid", xxxxReferralIncentiveCalculation.class);
    xxxxReferralIncentiveCalculation result = new xxxxReferralIncentiveCalculation();
    query.setParameter("xxxxid", xxxxId);
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
