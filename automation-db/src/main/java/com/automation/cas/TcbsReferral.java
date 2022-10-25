package com.automation.cas;

import com.automation.config.ipartner.IpartnerConstants;
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

@Getter
@Setter
@Entity
@Table(name = "xxxx_REFERRAL")
public class xxxxReferral {
  private static Logger logger = LoggerFactory.getLogger(xxxxReferral.class);
  @Id
  @GeneratedValue
  @Column(name = "ID")
  private Long id;

  @Column(name = "REFERREE_xxxx_ID")
  private String referreexxxxId;


  @Column(name = "REFERRER_xxxx_ID")
  private String referrerxxxxId;


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

  public xxxxReferral getxxxxReferralByDataReferralId(String dataReferralId) {
    Session session = CAS.casConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<xxxxReferral> query = session.createQuery("from xxxxReferral WHERE dataReferralId=:dataReferralId", xxxxReferral.class);
    xxxxReferral result = new xxxxReferral();
    query.setParameter(IpartnerConstants.DATA_REFERRAL_ID_TEXT, dataReferralId);
    query.setMaxResults(1);
    try {
      result = query.getSingleResult();
      session.refresh(result);
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
    return result;
  }

  public List<HashMap<String, Object>> getxxxxReferralBond(String referrerxxxxId) {
    Session session = CAS.casConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<xxxxReferral> query = session.createQuery("from xxxxReferral WHERE referrerxxxxId=:referrerxxxxId and referralType = 2 and status = 1 and referreeCampaign = 'FIRST_BOND_ORDER'",
      xxxxReferral.class);
    xxxxReferral result = new xxxxReferral();
    query.setParameter(IpartnerConstants.DATA_REFERRER_xxxx_ID_TEXT, referrerxxxxId);
    List<xxxxReferral> lHistoryReferrer = query.getResultList();
    List<HashMap<String, Object>> rs = new ArrayList<>();
    for (xxxxReferral xxxxReferral : lHistoryReferrer) {
      HashMap<String, Object> temp = parameters(xxxxReferral);
      rs.add(temp);
    }
    return rs;
  }

  public List<HashMap<String, Object>> getxxxxReferralFund(String referrerxxxxId) {
    Session session = CAS.casConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<xxxxReferral> query = session.createQuery("from xxxxReferral WHERE referrerxxxxId=:referrerxxxxId and referralType = 3 and status = 1 and referreeCampaign = 'FIRST_FUND_ORDER'",
      xxxxReferral.class);
    query.setParameter(IpartnerConstants.DATA_REFERRER_xxxx_ID_TEXT, referrerxxxxId);
    List<xxxxReferral> lHistoryReferrer = query.getResultList();
    List<HashMap<String, Object>> rs = new ArrayList<>();
    for (xxxxReferral xxxxReferral : lHistoryReferrer) {
      HashMap<String, Object> temp = parameters(xxxxReferral);
      rs.add(temp);
    }
    return rs;
  }

  public void updateStatusByDataReferralId(Long status, String dataReferralId) {
    Session session = CAS.casConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<xxxxReferral> query = session.createQuery("update xxxxReferral set status=:status where dataReferralId=:dataReferralId");
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
      Query query = session.createQuery("DELETE xxxxReferral WHERE dataReferralId=:dataReferralId");
      query.setParameter(IpartnerConstants.DATA_REFERRAL_ID_TEXT, dataReferralId);
      query.executeUpdate();
      session.getTransaction().commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }

  public void dmlxxxxReferralRows(String queryString) {
    Session session = CAS.casConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = CAS.casConnection.getSession().createSQLQuery(queryString);
    query.executeUpdate();
    session.getTransaction().commit();
  }

}
