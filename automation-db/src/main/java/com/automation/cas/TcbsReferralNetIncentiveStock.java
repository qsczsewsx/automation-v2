package com.automation.cas;

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
@Table(name = "xxxx_REFERRAL_NET_INCENTIVE_STOCK")
public class xxxxReferralNetIncentiveStock {
  private static final Logger logger = LoggerFactory.getLogger(xxxxReferralNetIncentiveStock.class);
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @Column(name = "REFERRAL_ID")
  private String referralId;
  @Column(name = "xxxx_ID")
  private String xxxxId;
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

  public HashMap<String, Object> getIncentiveStockWithCondition(String xxxxId, String orderId) {
    Session session = CAS.casConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<xxxxReferralNetIncentiveStock> query = session.createQuery("from xxxxReferralNetIncentiveStock WHERE referralId=:referralId and xxxxId =: xxxxId ", xxxxReferralNetIncentiveStock.class);
    HashMap<String, Object> result = new HashMap<>();
    query.setParameter("referralId", orderId);
    query.setParameter("xxxxId", xxxxId);
    query.setMaxResults(1);
    try {
      xxxxReferralNetIncentiveStock rs = query.getSingleResult();
      result = parameters(rs);
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
    return result;
  }

}
