package com.tcbs.automation.cas;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.tcbs.automation.cas.CAS.casConnection;

@Entity
@Table(name = "OB_TASK")
@Getter
@Setter
public class ObTask {
  private static Logger logger = LoggerFactory.getLogger(ObTask.class);
  private static final String USERID = "userId";
  private static final String STATUS = "status";
  private static final String TASKID = "taskId";
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "TYPE")
  private String type;
  @Column(name = "CHANNEL_ID")
  private BigDecimal channelId;
  @Column(name = "ACTION_ID")
  private BigDecimal actionId;
  @Column(name = "CREATED_DATETIME")
  private Timestamp createdDatetime;
  @Column(name = "UPDATED_DATETIME")
  private Timestamp updatedDatetime;
  @Column(name = "TASK_REF_ID")
  private BigDecimal taskRefId;
  @Column(name = "USER_ID")
  private BigDecimal userId;
  @Column(name = "UO_QUEUE_ID")
  private BigDecimal uoQueueId;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "RETRY_COUNT")
  private BigDecimal retryCount;
  @Column(name = "START_DATETIME")
  private Timestamp startDatetime;
  @Column(name = "END_DATETIME")
  private Timestamp endDatetime;
  @Column(name = "PRIORITY")
  private BigDecimal priority;
  @Column(name = "REASON")
  private String reason;
  @Column(name = "NEXT_ACTION_ID")
  private BigDecimal nextActionId;
  @Column(name = "REASON_ID")
  private BigDecimal reasonId;
  @Column(name = "KYC_STATUS")
  private String kycStatus;
  @Column(name = "ACTOR")
  private String actor;
  @Column(name = "SLA_START_DATETIME")
  private Timestamp slaStartDatetime;
  @Column(name = "SLA_END_DATETIME")
  private Timestamp slaEndDatetime;
  @Column(name = "KYC_GROUP_DATETIME")
  private Timestamp kycGroupDatetime;
  @Column(name = "SLA_TIMESHEET_CODE")
  private String slaTimesheetCode;
  @Column(name = "KYC_FLOW")
  private String kycFlow;

  @Step
  public static ObTask getByTaskId(String id) {
    CAS.casConnection.getSession().clear();
    Query<ObTask> query = CAS.casConnection.getSession().createQuery(
      "from ObTask a where a.id=:id", ObTask.class);
    query.setParameter("id", new BigDecimal(id));
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new ObTask();
    }
  }

  public static ObTask getByTaskRefId(BigDecimal id) {
    CAS.casConnection.getSession().clear();
    Query<ObTask> query = CAS.casConnection.getSession().createQuery(
      "from ObTask a where a.taskRefId=:id", ObTask.class);
    query.setParameter("id", id);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new ObTask();
    }
  }

  @Step
  public static ObTask getByUserIdAndActionId(String userId, String actionId) {
    CAS.casConnection.getSession().clear();
    Query<ObTask> query = CAS.casConnection.getSession().createQuery(
      "from ObTask a where a.userId=:userId and a.actionId=:actionId", ObTask.class);
    query.setParameter(USERID, new BigDecimal(userId));
    query.setParameter("actionId", new BigDecimal(actionId));
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new ObTask();
    }
  }

  @Step
  public static List<ObTask> getByUserIdAndKycStatus(String userId, String kycStatus) {
    CAS.casConnection.getSession().clear();
    Query<ObTask> query = CAS.casConnection.getSession().createQuery(
      "from ObTask a where a.userId=:userId and a.kycStatus=:kycStatus order by id desc", ObTask.class);
    query.setParameter(USERID, new BigDecimal(userId));
    query.setParameter("kycStatus", kycStatus);

    return query.getResultList();
  }

  @Step
  public static ObTask getByUserIdAndStatus(String userId, String status) {
    CAS.casConnection.getSession().clear();
    Query<ObTask> query = CAS.casConnection.getSession().createQuery(
      "from ObTask a where a.userId=:userId and a.status=:status", ObTask.class);
    query.setParameter(USERID, new BigDecimal(userId));
    query.setParameter(STATUS, status);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new ObTask();
    }
  }

  @Step
  public static ObTask getByUserIdAndType(String userId, String type) {
    CAS.casConnection.getSession().clear();
    Query<ObTask> query = CAS.casConnection.getSession().createQuery(
      "from ObTask a where a.userId=:userId and a.type=:type", ObTask.class);
    query.setParameter(USERID, new BigDecimal(userId));
    query.setParameter("type", type);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new ObTask();
    }
  }

  @Step
  public static void deleteById(String taskId) {
    casConnection.getSession().clear();
    if (!casConnection.getSession().getTransaction().isActive()) {
      casConnection.getSession().beginTransaction();
    }
    org.hibernate.query.Query query = casConnection.getSession().createQuery(
      "Delete from ObTask a where a.id=:taskId");
    query.setParameter(TASKID, new BigDecimal(taskId));
    query.executeUpdate();
    casConnection.getSession().getTransaction().commit();
  }

  @Step
  public static void deleteByUserId(String userId, String actionId) {
    casConnection.getSession().clear();
    if (!casConnection.getSession().getTransaction().isActive()) {
      casConnection.getSession().beginTransaction();
    }
    org.hibernate.query.Query query = casConnection.getSession().createQuery(
      "Delete from ObTask a where a.userId=:userId and a.actionId=:actionId");
    query.setParameter(USERID, new BigDecimal(userId));
    query.setParameter("actionId", new BigDecimal(actionId));
    query.executeUpdate();
    casConnection.getSession().getTransaction().commit();
  }

  @Step
  public static void deleteByUserIdAndStatus(String userId, String status) {
    casConnection.getSession().clear();
    if (!casConnection.getSession().getTransaction().isActive()) {
      casConnection.getSession().beginTransaction();
    }
    org.hibernate.query.Query query = casConnection.getSession().createQuery(
      "Delete from ObTask a where a.userId=:userId and a.status=:status");
    query.setParameter(USERID, new BigDecimal(userId));
    query.setParameter(STATUS, status);
    query.executeUpdate();
    casConnection.getSession().getTransaction().commit();
  }

  public static List<ObTask> getListByUserId(BigDecimal userId) {
    CAS.casConnection.getSession().clear();
    Query<ObTask> query = CAS.casConnection.getSession().createQuery(
      "from ObTask a where a.userId=:userId order by a.id desc", ObTask.class);
    query.setParameter(USERID, userId);
    return query.getResultList();
  }

  public static List<ObTask> getListByStatusNotDone(BigDecimal userId) {
    CAS.casConnection.getSession().clear();
    Query<ObTask> query = CAS.casConnection.getSession().createQuery(
      "from ObTask a where a.userId=:userId and a.status !='DONE' order by a.id desc", ObTask.class);
    query.setParameter(USERID, userId);
    return query.getResultList();
  }

  public static List<ObTask> getListByStatus(String typeT, String channelId, String status) {
    CAS.casConnection.getSession().clear();
    Query<ObTask> query = CAS.casConnection.getSession().createQuery(
      "from ObTask a where a.type=:typeT and a.channelId=:channelId and a.status =:status order by a.id asc", ObTask.class);
    query.setParameter("typeT", typeT);
    query.setParameter(STATUS, status);
    query.setParameter("channelId", new BigDecimal(channelId));
    return query.getResultList();
  }

  public static void updateStatusAndNullActor(String taskId, String status) {
    casConnection.getSession().clear();
    if (!casConnection.getSession().getTransaction().isActive()) {
      casConnection.getSession().beginTransaction();
    }
    org.hibernate.query.Query query = casConnection.getSession().createQuery(
      "Update ObTask a set a.status =:status, a.actor =:actor where a.id =:taskId");
    query.setParameter(TASKID, new BigDecimal(taskId));
    query.setParameter(STATUS, status);
    query.setParameter("actor", null);
    query.executeUpdate();
    casConnection.getSession().getTransaction().commit();
  }

  public static void updateCreatedDateByUserIdAndType(String userId, String type, String createdDate) {
    casConnection.getSession().clear();
    if (!casConnection.getSession().getTransaction().isActive()) {
      casConnection.getSession().beginTransaction();
    }
    org.hibernate.query.Query query = casConnection.getSession().createQuery(
      "Update ObTask a set a.createdDatetime =:createdDate where a.userId =:userId and a.type=:type");

    query.setParameter("createdDate", Timestamp.valueOf(LocalDateTime.parse(createdDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
    query.setParameter(USERID, new BigDecimal(userId));
    query.setParameter("type", type);
    query.executeUpdate();
    casConnection.getSession().getTransaction().commit();
  }

  @Step
  public static List<ObTask> getByUserIdAndKycFlow(String userId, String kycFlow) {
    CAS.casConnection.getSession().clear();
    Query<ObTask> query = CAS.casConnection.getSession().createQuery(
      "from ObTask a where a.userId=:userId and a.kycFlow=:kycFlow order by id desc", ObTask.class);
    query.setParameter(USERID, new BigDecimal(userId));
    query.setParameter("kycFlow", kycFlow);

    return query.getResultList();
  }

  public void insert() {
    Session session = CAS.casConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    try {
      session.save(this);
      session.getTransaction().commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }

  public static void updateStatusById(String taskId, String status) {
    try {
      Session session = CAS.casConnection.getSession();
      session.getTransaction().begin();
      Query query = session.createQuery(
        "Update ObTask a set a.status =:status where a.id =:taskId");
      query.setParameter(TASKID, new BigDecimal(taskId));
      query.setParameter(STATUS, status);
      query.executeUpdate();
      casConnection.getSession().getTransaction().commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }
}

