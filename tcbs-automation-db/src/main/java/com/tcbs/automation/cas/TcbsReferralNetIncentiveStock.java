package com.tcbs.automation.cas;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.sql.Date;
import java.util.HashMap;

@Entity
@Table(name = "TCBS_REFERRAL_NET_INCENTIVE_STOCK")
public class TcbsReferralNetIncentiveStock {
  private static final Logger logger = LoggerFactory.getLogger(TcbsReferralNetIncentiveStock.class);
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @Column(name = "REFERRAL_ID")
  private String referralId;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "APPLY_AMOUNT")
  private String applyAmount;
  @Column(name = "GROSS_INCENTIVE_VALUE")
  private String grossIncentiveValue;
  @Column(name = "TAX_INCENTIVE_VALUE")
  private String taxIncentiveValue;
  @Column(name = "NET_INCENTIVE_VALUE")
  private String netIncentiveValue;
  @Column(name = "VSD_COLLECTION")
  private String vsdCollection;
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

  public HashMap<String, Object> getIncentiveStockWithCondition(String tcbsId, String orderId) {
    Session session = CAS.casConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<TcbsReferralNetIncentiveStock> query = session.createQuery("from TcbsReferralNetIncentiveStock WHERE referralId=:referralId and tcbsId =: tcbsId ", TcbsReferralNetIncentiveStock.class);
    HashMap<String, Object> result = new HashMap<>();
    query.setParameter("referralId", orderId);
    query.setParameter("tcbsId", tcbsId);
    query.setMaxResults(1);
    try {
      TcbsReferralNetIncentiveStock rs = query.getSingleResult();
      result = parameters(rs);
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
    return result;
  }

}
