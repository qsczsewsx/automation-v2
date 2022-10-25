package com.automation.cas;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Getter
@Setter
@Entity
@Table(name = "xxxx_REFERRAL_NET_INCENTIVE_BOND_FUND")
public class xxxxReferralNetIncentiveBondFund {
  private static Logger logger = LoggerFactory.getLogger(xxxxReferralNetIncentiveBondFund.class);

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @Column(name = "ORDER_ID")
  private String orderId;
  @Column(name = "TRADING_CODE")
  private String tradingCode;
  @Column(name = "TRADING_VALUE")
  private String tradingValue;
  @Column(name = "REFEREE_xxxxID")
  private String refereexxxxid;
  @Column(name = "REFERRER_xxxxID")
  private String referrerxxxxid;
  @Column(name = "REFEREE_GROSS_INCENTIVE_VALUE")
  private String refereeGrossIncentiveValue;
  @Column(name = "REFEREE_TAX_INCENTIVE_VALUE")
  private String refereeTaxIncentiveValue;
  @Column(name = "REFEREE_NET_INCENTIVE_VALUE")
  private String refereeNetIncentiveValue;
  @Column(name = "REFERRER_GROSS_INCENTIVE_VALUE")
  private String referrerGrossIncentiveValue;
  @Column(name = "REFERRER_TAX_INCENTIVE_VALUE")
  private String referrerTaxIncentiveValue;
  @Column(name = "REFERRER_NET_INCENTIVE_VALUE")
  private String referrerNetIncentiveValue;
  @Column(name = "ORDER_TYPE")
  private String orderType;
  @Column(name = "CREATED_DATE")
  private Date createdDate;
  @Column(name = "UPDATED_DATE")
  private Date updatedDate;

  public void dmlPrepareData(String queryString) {
    Session session = CAS.casConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = CAS.casConnection.getSession().createSQLQuery(queryString);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public xxxxReferralNetIncentiveBondFund getxxxxReferralNetIncentiveBondFundByDataReferralId(String dataReferralId) {
    Session session = CAS.casConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<xxxxReferralNetIncentiveBondFund> query = session.createQuery("from xxxxReferralNetIncentiveBondFund WHERE orderId=:orderId", xxxxReferralNetIncentiveBondFund.class);
    xxxxReferralNetIncentiveBondFund result = new xxxxReferralNetIncentiveBondFund();
    query.setParameter("orderId", dataReferralId);
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
