package com.tcbs.automation.cas;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.config.ipartner.IpartnerConstants.DATA_REFERRAL_ID_TEXT;
import static com.tcbs.automation.config.ipartner.IpartnerConstants.DATA_REFERRER_TCBS_ID_TEXT;

@Getter
@Setter
@Entity
@Table(name = "TCBS_REFERRAL")
public class TcbsReferral {
  private static Logger logger = LoggerFactory.getLogger(TcbsReferral.class);
  @Id
  @GeneratedValue
  @Column(name = "ID")
  private Long id;

  @Column(name = "REFERREE_TCBS_ID")
  private String referreeTcbsId;


  @Column(name = "REFERRER_TCBS_ID")
  private String referrerTcbsId;


  @Column(name = "DATA_REFERRAL_ID")
  private String dataReferralId;


  @Column(name = "STATUS")
  private Long status;


  @Column(name = "REFERRAL_TYPE")
  private Long referralType;


  @Column(name = "START_REFERRAL_DATE")
  //@Temporal(TemporalType.TIMESTAMP)
  private Date startReferralDate;


  @Column(name = "FINISHED_REFERRAL_DATE")
  //@Temporal(TemporalType.DATE)
  private Date finishedReferralDate;


  @Column(name = "INCENTIVE_RATE_REFERRER")
  private Double incentiveRateReferrer;


  @Column(name = "INCENTIVE_AMOUNT_REFERRER")
  private Double incentiveAmountReferrer;


  @Column(name = "INCENTIVE_RATE_REFERREE")
  private Double incentiveRateReferree;


  @Column(name = "INCENTIVE_AMOUNT_REFERREE")
  private Double incentiveAmountReferree;


  @Column(name = "INCENTIVE_RATE_BASE_REFERREE")
  private Double incentiveRateBaseReferree;


  @Column(name = "INCENTIVE_RATE_BASE_REFERRER")
  private Double incentiveRateBaseReferrer;


  @Column(name = "INCENTIVE_TOTAL_REFERREE")
  private Double incentiveTotalReferree;


  @Column(name = "INCENTIVE_TOTAL_REFERRER")
  private Double incentiveTotalReferrer;


  @Column(name = "UPDATED_DATE")
  private Date updatedDate;

  @Column(name = "REFERRER_ROLE")
  private String referrerRole;

  @Column(name = "REFERREE_CAMPAIGN")
  private String referreeCampaign;

  public static HashMap<String, Object> parameters(Object obj) {
    HashMap<String, Object> map = new HashMap<>();
    for (Field field : obj.getClass().getDeclaredFields()) {
      field.setAccessible(true);
      try {
        map.put(field.getName(), field.get(obj));
      } catch (Exception e) {
        logger.info(e.getMessage());
      }
    }
    return map;
  }

  public void insert() {
    Session session = CAS.casConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    session.save(this);
    session.getTransaction().commit();
  }

  public TcbsReferral getTcbsReferralByDataReferralId(String dataReferralId) {
    Session session = CAS.casConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<TcbsReferral> query = session.createQuery("from TcbsReferral WHERE dataReferralId=:dataReferralId", TcbsReferral.class);
    TcbsReferral result = new TcbsReferral();
    query.setParameter(DATA_REFERRAL_ID_TEXT, dataReferralId);
    query.setMaxResults(1);
    try {
      result = query.getSingleResult();
      session.refresh(result);
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
    return result;
  }

  public List<HashMap<String, Object>> getTcbsReferralBond(String referrerTcbsId) {
    Session session = CAS.casConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<TcbsReferral> query = session.createQuery("from TcbsReferral WHERE referrerTcbsId=:referrerTcbsId and referralType = 2 and status = 1 and referreeCampaign = 'FIRST_BOND_ORDER'",
      TcbsReferral.class);
    TcbsReferral result = new TcbsReferral();
    query.setParameter(DATA_REFERRER_TCBS_ID_TEXT, referrerTcbsId);
    List<TcbsReferral> lHistoryReferrer = query.getResultList();
    List<HashMap<String, Object>> rs = new ArrayList<>();
    for (TcbsReferral tcbsReferral : lHistoryReferrer) {
      HashMap<String, Object> temp = parameters(tcbsReferral);
      rs.add(temp);
    }
    return rs;
  }

  public List<HashMap<String, Object>> getTcbsReferralFund(String referrerTcbsId) {
    Session session = CAS.casConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<TcbsReferral> query = session.createQuery("from TcbsReferral WHERE referrerTcbsId=:referrerTcbsId and referralType = 3 and status = 1 and referreeCampaign = 'FIRST_FUND_ORDER'",
      TcbsReferral.class);
    query.setParameter(DATA_REFERRER_TCBS_ID_TEXT, referrerTcbsId);
    List<TcbsReferral> lHistoryReferrer = query.getResultList();
    List<HashMap<String, Object>> rs = new ArrayList<>();
    for (TcbsReferral tcbsReferral : lHistoryReferrer) {
      HashMap<String, Object> temp = parameters(tcbsReferral);
      rs.add(temp);
    }
    return rs;
  }

  public void updateStatusByDataReferralId(Long status, String dataReferralId) {
    Session session = CAS.casConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<TcbsReferral> query = session.createQuery("update TcbsReferral set status=:status where dataReferralId=:dataReferralId");
    query.setParameter("dataReferralId", dataReferralId);//sometimes if not add apostrophe mark this query will hang
    query.setParameter("status", status);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public void delete() {
    Session session = CAS.casConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    session.delete(this);
    session.getTransaction().commit();
  }

  public void deleteByDataReferralId(String dataReferralId) {
    try {
      Session session = CAS.casConnection.getSession();
      session.clear();
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }
      Query query = session.createQuery("DELETE TcbsReferral WHERE dataReferralId=:dataReferralId");
      query.setParameter(DATA_REFERRAL_ID_TEXT, dataReferralId);
      query.executeUpdate();
      session.getTransaction().commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }

  public void dmlTcbsReferralRows(String queryString) {
    Session session = CAS.casConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = CAS.casConnection.getSession().createSQLQuery(queryString);
    query.executeUpdate();
    session.getTransaction().commit();
  }

}
