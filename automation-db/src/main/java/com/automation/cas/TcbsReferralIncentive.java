package com.automation.cas;

import com.automation.config.ipartner.IpartnerConstants;
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

@Entity
@Table(name = "xxxx_REFERRAL_INCENTIVE")
@Getter
@Setter
public class xxxxReferralIncentive {
  private static final String xxxx_ID = "xxxxId";
  private static Logger logger = LoggerFactory.getLogger(xxxxReferral.class);
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @Column(name = "xxxxID")
  private String xxxxid;
  @Column(name = "AMOUNT")
  private BigDecimal amount;
  @Column(name = "REFEREE_xxxxID")
  private String refereexxxxId;
  @Column(name = "REFERRER_xxxxID")
  private String referrerxxxxId;
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

  public static void deleteDataByxxxxId(String xxxxId) {
    Session session = CAS.casConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = session.createQuery("delete from xxxxReferralIncentive where xxxxid=:xxxxId");
    query.setParameter(xxxx_ID, xxxxId)
      .executeUpdate();
    session.getTransaction().commit();
  }

  public xxxxReferralIncentive getxxxxReferralIncentiveByDataReferralId(String dataReferralId) {
    Session session = CAS.casConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<xxxxReferralIncentive> query = session.createQuery("from xxxxReferralIncentive WHERE dataReferralId=:dataReferralId", xxxxReferralIncentive.class);
    xxxxReferralIncentive result = new xxxxReferralIncentive();
    query.setParameter(IpartnerConstants.DATA_REFERRAL_ID_TEXT, dataReferralId);
    query.setMaxResults(1);
    try {
      result = query.getSingleResult();
    } catch (Exception e) {
      return new xxxxReferralIncentive();
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

  public List<xxxxReferralIncentive> getByxxxxId(String xxxxId) {
    Session session = CAS.casConnection.getSession();
    session.clear();
    Query<xxxxReferralIncentive> query = session.createQuery("from xxxxReferralIncentive where xxxxid=:xxxxId", xxxxReferralIncentive.class);
    return query.setParameter(xxxx_ID, xxxxId)
      .getResultList();
  }

  public List<HashMap<String, Object>> getHistoryRefererByxxxxId(String xxxxId) {
    Session session = CAS.casConnection.getSession();
    session.clear();
    Query<xxxxReferralIncentive> query = session.createQuery("from xxxxReferralIncentive where xxxxid=:xxxxId and xxxxid = referrerxxxxId ", xxxxReferralIncentive.class);
    List<xxxxReferralIncentive> lHistoryReferrer = query.setParameter(xxxx_ID, xxxxId).getResultList();
    List<HashMap<String, Object>> rs = new ArrayList<>();
    for (xxxxReferralIncentive xxxxReferralIncentive : lHistoryReferrer) {
      HashMap<String, Object> temp = parameters(xxxxReferralIncentive);
      rs.add(temp);
    }
    return rs;
  }

  public List<HashMap<String, Object>> getTopReferralStock() {
    Session session = CAS.casConnection.getSession();
    session.clear();
    NativeQuery query = session.createSQLQuery(
      "Select ROW_NUMBER() OVER (ORDER BY FREETRANSACTION desc) ORDERID, xxxxID, FREETRANSACTION from( select  xxxxID, sum(AMOUNT) FREETRANSACTION from xxxx_REFERRAL_INCENTIVE where xxxxID = REFERRER_xxxxID and INCENTIVE_TYPE = 3 group by xxxxID) xxxx_INCENTIVE_TOP");
    List<Object[]> rows = query.list();
    List<HashMap<String, Object>> listTopStock = new ArrayList<>();
    for (Object[] row : rows) {
      HashMap<String, Object> mTopStock = new HashMap<>();
      mTopStock.put("orderId", row[0]);
      mTopStock.put(xxxx_ID, row[1]);
      mTopStock.put("freeTransaction", row[2]);
      listTopStock.add(mTopStock);
    }
    return listTopStock;
  }

}
