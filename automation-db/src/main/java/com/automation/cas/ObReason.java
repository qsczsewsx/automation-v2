package com.automation.cas;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "OB_REASON")
@Getter
@Setter
public class ObReason {
  private static Logger logger = LoggerFactory.getLogger(ObReason.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "REASON_NAME")
  private String reasonName;
  @Column(name = "ACTION_CODE")
  private String actionCode;
  @Column(name = "REASON_GROUP")
  private String reasonGroup;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "REASON_CODE")
  private String reasonCode;
  @Column(name = "CHANNEL_CODE")
  private String channelCode;

  @Step
  public static ObReason getById(String id) {
    CAS.casConnection.getSession().clear();
    Query<ObReason> query = CAS.casConnection.getSession().createQuery(
      "from ObReason a where a.id=:id", ObReason.class);
    query.setParameter("id", new BigDecimal(id));
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new ObReason();
    }
  }

  public static ObReason getByTaskRefId(BigDecimal id) {
    CAS.casConnection.getSession().clear();
    Query<ObReason> query = CAS.casConnection.getSession().createQuery(
      "from ObTask a where a.taskRefId=:id", ObReason.class);
    query.setParameter("id", id);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new ObReason();
    }
  }

  @Step
  public static ObReason getByUserIdAndActionId(String userId, String actionId) {
    CAS.casConnection.getSession().clear();
    Query<ObReason> query = CAS.casConnection.getSession().createQuery(
      "from ObTask a where a.userId=:userId and a.actionId=:actionId", ObReason.class);
    query.setParameter("userId", new BigDecimal(userId));
    query.setParameter("actionId", new BigDecimal(actionId));
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new ObReason();
    }
  }

  @Step
  public static List<ObReason> getByUserIdAndKycStatus(String userId, String kycStatus) {
    CAS.casConnection.getSession().clear();
    Query<ObReason> query = CAS.casConnection.getSession().createQuery(
      "from ObTask a where a.userId=:userId and a.kycStatus=:kycStatus order by id desc", ObReason.class);
    query.setParameter("userId", new BigDecimal(userId));
    query.setParameter("kycStatus", kycStatus);

    return query.getResultList();
  }

  @Step
  public static ObReason getByUserIdAndStatus(String userId, String status) {
    CAS.casConnection.getSession().clear();
    Query<ObReason> query = CAS.casConnection.getSession().createQuery(
      "from ObTask a where a.userId=:userId and a.status=:status", ObReason.class);
    query.setParameter("userId", new BigDecimal(userId));
    query.setParameter("status", status);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new ObReason();
    }
  }

  @Step
  public static void deleteByUserId(String userId, String actionId) {
    CAS.casConnection.getSession().clear();
    if (!CAS.casConnection.getSession().getTransaction().isActive()) {
      CAS.casConnection.getSession().beginTransaction();
    }
    Query query = CAS.casConnection.getSession().createQuery(
      "Delete from ObTask a where a.userId=:userId and a.actionId=:actionId");
    query.setParameter("userId", new BigDecimal(userId));
    query.setParameter("actionId", new BigDecimal(actionId));
    query.executeUpdate();
    CAS.casConnection.getSession().getTransaction().commit();
  }

  @Step
  public static void deleteByUserIdAndStatus(String userId, String status) {
    CAS.casConnection.getSession().clear();
    if (!CAS.casConnection.getSession().getTransaction().isActive()) {
      CAS.casConnection.getSession().beginTransaction();
    }
    Query query = CAS.casConnection.getSession().createQuery(
      "Delete from ObTask a where a.userId=:userId and a.status=:status");
    query.setParameter("userId", new BigDecimal(userId));
    query.setParameter("status", status);
    query.executeUpdate();
    CAS.casConnection.getSession().getTransaction().commit();
  }

  public static List<ObReason> getListByUserId(BigDecimal userId) {
    CAS.casConnection.getSession().clear();
    Query<ObReason> query = CAS.casConnection.getSession().createQuery(
      "from ObTask a where a.userId=:userId order by a.id desc", ObReason.class);
    query.setParameter("userId", userId);
    return query.getResultList();
  }

  public static List<ObReason> getListByStatusNotDone(BigDecimal userId) {
    CAS.casConnection.getSession().clear();
    Query<ObReason> query = CAS.casConnection.getSession().createQuery(
      "from ObTask a where a.userId=:userId and a.status !='DONE' order by a.id desc", ObReason.class);
    query.setParameter("userId", userId);
    return query.getResultList();
  }

  public static List<ObReason> getListByStatus(String typeT, String channelId, String status) {
    CAS.casConnection.getSession().clear();
    Query<ObReason> query = CAS.casConnection.getSession().createQuery(
      "from ObTask a where a.type=:typeT and a.channelId=:channelId and a.status =:status order by a.id asc", ObReason.class);
    query.setParameter("typeT", typeT);
    query.setParameter("status", status);
    query.setParameter("channelId", new BigDecimal(channelId));
    return query.getResultList();
  }

}
