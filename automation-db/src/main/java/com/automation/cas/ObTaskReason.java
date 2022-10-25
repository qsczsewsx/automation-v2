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
@Table(name = "OB_TASK_REASON")
@Getter
@Setter
public class ObTaskReason {
  private static Logger logger = LoggerFactory.getLogger(ObTaskReason.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "TASK_ID")
  private BigDecimal taskId;
  @Column(name = "REASON_ID")
  private BigDecimal reasonId;

  @Step
  public static List<ObTaskReason> getListByTaskId(String taskId) {
    CAS.casConnection.getSession().clear();
    Query<ObTaskReason> query = CAS.casConnection.getSession().createQuery(
      "from ObTaskReason a where a.taskId=:taskId order by reasonId", ObTaskReason.class);
    query.setParameter("taskId", new BigDecimal(taskId));

    return query.getResultList();

  }

  public static ObTaskReason getByTaskRefId(BigDecimal id) {
    CAS.casConnection.getSession().clear();
    Query<ObTaskReason> query = CAS.casConnection.getSession().createQuery(
      "from ObTask a where a.taskRefId=:id", ObTaskReason.class);
    query.setParameter("id", id);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new ObTaskReason();
    }
  }

  @Step
  public static ObTaskReason getByUserIdAndActionId(String userId, String actionId) {
    CAS.casConnection.getSession().clear();
    Query<ObTaskReason> query = CAS.casConnection.getSession().createQuery(
      "from ObTask a where a.userId=:userId and a.actionId=:actionId", ObTaskReason.class);
    query.setParameter("userId", new BigDecimal(userId));
    query.setParameter("actionId", new BigDecimal(actionId));
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new ObTaskReason();
    }
  }

  @Step
  public static List<ObTaskReason> getByUserIdAndKycStatus(String userId, String kycStatus) {
    CAS.casConnection.getSession().clear();
    Query<ObTaskReason> query = CAS.casConnection.getSession().createQuery(
      "from ObTask a where a.userId=:userId and a.kycStatus=:kycStatus order by id desc", ObTaskReason.class);
    query.setParameter("userId", new BigDecimal(userId));
    query.setParameter("kycStatus", kycStatus);

    return query.getResultList();
  }

  @Step
  public static ObTaskReason getByUserIdAndStatus(String userId, String status) {
    CAS.casConnection.getSession().clear();
    Query<ObTaskReason> query = CAS.casConnection.getSession().createQuery(
      "from ObTask a where a.userId=:userId and a.status=:status", ObTaskReason.class);
    query.setParameter("userId", new BigDecimal(userId));
    query.setParameter("status", status);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new ObTaskReason();
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

  public static List<ObTaskReason> getListByUserId(BigDecimal userId) {
    CAS.casConnection.getSession().clear();
    Query<ObTaskReason> query = CAS.casConnection.getSession().createQuery(
      "from ObTask a where a.userId=:userId order by a.id desc", ObTaskReason.class);
    query.setParameter("userId", userId);
    return query.getResultList();
  }

  public static List<ObTaskReason> getListByStatusNotDone(BigDecimal userId) {
    CAS.casConnection.getSession().clear();
    Query<ObTaskReason> query = CAS.casConnection.getSession().createQuery(
      "from ObTask a where a.userId=:userId and a.status !='DONE' order by a.id desc", ObTaskReason.class);
    query.setParameter("userId", userId);
    return query.getResultList();
  }

  public static List<ObTaskReason> getListByStatus(String typeT, String channelId, String status) {
    CAS.casConnection.getSession().clear();
    Query<ObTaskReason> query = CAS.casConnection.getSession().createQuery(
      "from ObTask a where a.type=:typeT and a.channelId=:channelId and a.status =:status order by a.id asc", ObTaskReason.class);
    query.setParameter("typeT", typeT);
    query.setParameter("status", status);
    query.setParameter("channelId", new BigDecimal(channelId));
    return query.getResultList();
  }

}
