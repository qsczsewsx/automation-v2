package com.tcbs.automation.cas;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.config.ipartner.IpartnerConstants.DATA_REFERRAL_ID_TEXT;

@Entity
@Table(name = "TCBS_REFERRAL_INCENTIVE")
@Getter
@Setter
public class TcbsReferralIncentive {
  private static final String TCBS_ID = "tcbsId";
  private static Logger logger = LoggerFactory.getLogger(TcbsReferral.class);
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @Column(name = "TCBSID")
  private String tcbsid;
  @Column(name = "AMOUNT")
  private BigDecimal amount;
  @Column(name = "REFEREE_TCBSID")
  private String refereeTcbsId;
  @Column(name = "REFERRER_TCBSID")
  private String referrerTcbsId;
  @Column(name = "INCENTIVE_TYPE")
  private String incentiveType;
  @Column(name = "CREATED_DATE")
  private Date createdDate;
  @Column(name = "UPDATED_DATE")
  private Date updatedDate;

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

  public static void deleteDataByTcbsId(String tcbsId) {
    Session session = CAS.casConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = session.createQuery("delete from TcbsReferralIncentive where tcbsid=:tcbsId");
    query.setParameter(TCBS_ID, tcbsId)
      .executeUpdate();
    session.getTransaction().commit();
  }

  public TcbsReferralIncentive getTcbsReferralIncentiveByDataReferralId(String dataReferralId) {
    Session session = CAS.casConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<TcbsReferralIncentive> query = session.createQuery("from TcbsReferralIncentive WHERE dataReferralId=:dataReferralId", TcbsReferralIncentive.class);
    TcbsReferralIncentive result = new TcbsReferralIncentive();
    query.setParameter(DATA_REFERRAL_ID_TEXT, dataReferralId);
    query.setMaxResults(1);
    try {
      result = query.getSingleResult();
    } catch (Exception e) {
      return new TcbsReferralIncentive();
    }
    return result;
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

  public List<TcbsReferralIncentive> getByTcbsId(String tcbsId) {
    Session session = CAS.casConnection.getSession();
    session.clear();
    Query<TcbsReferralIncentive> query = session.createQuery("from TcbsReferralIncentive where tcbsid=:tcbsId", TcbsReferralIncentive.class);
    return query.setParameter(TCBS_ID, tcbsId)
      .getResultList();
  }

  public List<HashMap<String, Object>> getHistoryRefererByTcbsId(String tcbsId) {
    Session session = CAS.casConnection.getSession();
    session.clear();
    Query<TcbsReferralIncentive> query = session.createQuery("from TcbsReferralIncentive where tcbsid=:tcbsId and tcbsid = referrerTcbsId ", TcbsReferralIncentive.class);
    List<TcbsReferralIncentive> lHistoryReferrer = query.setParameter(TCBS_ID, tcbsId).getResultList();
    List<HashMap<String, Object>> rs = new ArrayList<>();
    for (TcbsReferralIncentive tcbsReferralIncentive : lHistoryReferrer) {
      HashMap<String, Object> temp = parameters(tcbsReferralIncentive);
      rs.add(temp);
    }
    return rs;
  }

  public List<HashMap<String, Object>> getTopReferralStock() {
    Session session = CAS.casConnection.getSession();
    session.clear();
    NativeQuery query = session.createSQLQuery(
      "Select ROW_NUMBER() OVER (ORDER BY FREETRANSACTION desc) ORDERID, TCBSID, FREETRANSACTION from( select  TCBSID, sum(AMOUNT) FREETRANSACTION from TCBS_REFERRAL_INCENTIVE where TCBSID = REFERRER_TCBSID and INCENTIVE_TYPE = 3 group by TCBSID) TCBS_INCENTIVE_TOP");
    List<Object[]> rows = query.list();
    List<HashMap<String, Object>> listTopStock = new ArrayList<>();
    for (Object[] row : rows) {
      HashMap<String, Object> mTopStock = new HashMap<>();
      mTopStock.put("orderId", row[0]);
      mTopStock.put(TCBS_ID, row[1]);
      mTopStock.put("freeTransaction", row[2]);
      listTopStock.add(mTopStock);
    }
    return listTopStock;
  }

}
