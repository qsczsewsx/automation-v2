package com.tcbs.automation.cas;

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
@Table(name = "TCBS_REFERRAL_NET_INCENTIVE_BOND_FUND")
public class TcbsReferralNetIncentiveBondFund {
  private static Logger logger = LoggerFactory.getLogger(TcbsReferralNetIncentiveBondFund.class);

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
  @Column(name = "REFEREE_TCBSID")
  private String refereeTcbsid;
  @Column(name = "REFERRER_TCBSID")
  private String referrerTcbsid;
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

  public TcbsReferralNetIncentiveBondFund getTcbsReferralNetIncentiveBondFundByDataReferralId(String dataReferralId) {
    Session session = CAS.casConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<TcbsReferralNetIncentiveBondFund> query = session.createQuery("from TcbsReferralNetIncentiveBondFund WHERE orderId=:orderId", TcbsReferralNetIncentiveBondFund.class);
    TcbsReferralNetIncentiveBondFund result = new TcbsReferralNetIncentiveBondFund();
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