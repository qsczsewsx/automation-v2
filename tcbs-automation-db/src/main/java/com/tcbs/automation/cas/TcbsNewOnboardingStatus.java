package com.tcbs.automation.cas;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.tcbs.automation.cas.CAS.casConnection;

@Entity
@Table(name = "TCBS_NEW_ONBOARDING_STATUS")
@Getter
@Setter
public class TcbsNewOnboardingStatus {

  private static Logger logger = LoggerFactory.getLogger(TcbsReferralData.class);

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @NotNull
  @Column(name = "USER_ID")
  private BigDecimal userId;
  @Column(name = "GROUP_STATUS")
  private String groupStatus;
  @Column(name = "STATUS_KEY")
  private String statusKey;
  @Column(name = "STATUS_VALUE")
  private String statusValue;
  @Column(name = "REJECT_REASON")
  private String rejectReason;
  @Column(name = "REJECT_CONTENT")
  private String rejectContent;
  @Column(name = "REJECT_PERSON")
  private String rejectPerson;
  @Column(name = "CREATED_DATE")
  private Date createdDate;
  @Column(name = "UPDATED_DATE")
  private Timestamp updatedDate;

  @Step
  public static TcbsNewOnboardingStatus getByUserIdAndStatusKey(String userId, String statusKey) {
    Query<TcbsNewOnboardingStatus> query = casConnection.getSession().createQuery(
      "from TcbsNewOnboardingStatus a where a.userId=:userId and a.statusKey=:statusKey", TcbsNewOnboardingStatus.class);
    query.setParameter("userId", new BigDecimal(userId));
    query.setParameter("statusKey", statusKey);
    return query.getSingleResult();
  }

  public static TcbsNewOnboardingStatus getByTcbsIdAndStatusKey(String tcbsId, String statusKey) {
    casConnection.getSession().clear();
    if (!casConnection.getSession().getTransaction().isActive()) {
      casConnection.getSession().beginTransaction();
    }
    Query<TcbsNewOnboardingStatus> query = casConnection.getSession().createNativeQuery(
      "SELECT * FROM TCBS_NEW_ONBOARDING_STATUS WHERE STATUS_KEY =:statusKey AND USER_ID IN (SELECT ID FROM TCBS_USER WHERE TCBSID =:tcbsid)", TcbsNewOnboardingStatus.class);
    query.setParameter("tcbsid", tcbsId);
    query.setParameter("statusKey", statusKey);
    return query.getSingleResult();
  }

  public static List<TcbsNewOnboardingStatus> getByUserId(BigDecimal userId) {
    Query<TcbsNewOnboardingStatus> query = casConnection.getSession().createQuery(
      "from TcbsNewOnboardingStatus a where a.userId=:userId", TcbsNewOnboardingStatus.class);
    query.setParameter("userId", userId);
    return query.getResultList();
  }

  public static List<TcbsNewOnboardingStatus> getByStatusKeyAndStatusValue(String sttKey, String sttValue) {
    Query<TcbsNewOnboardingStatus> query = casConnection.getSession().createQuery(
      "from TcbsNewOnboardingStatus a where a.statusKey=:sttKey and a.statusValue=:sttValue", TcbsNewOnboardingStatus.class);
    query.setParameter("sttKey", sttKey);
    query.setParameter("sttValue", sttValue);

    return query.getResultList();
  }

  public static void updateStatusValueByUserId(String userId, String statusKey, String statusValue) {
    casConnection.getSession().clear();
    if (!casConnection.getSession().getTransaction().isActive()) {
      casConnection.getSession().beginTransaction();
    }
    org.hibernate.query.Query query = casConnection.getSession().createQuery(
      "Update TcbsNewOnboardingStatus a set a.statusValue =:statusValue where a.userId=:userId and a.statusKey =:statusKey ");
    query.setParameter("userId", new BigDecimal(userId));
    query.setParameter("statusKey", statusKey);
    query.setParameter("statusValue", statusValue);
    query.executeUpdate();
    casConnection.getSession().getTransaction().commit();
  }

  public static void updateByStatusKeyAndList105C(List<String> list105code, String statusKey, String statusValue) {
    try {
      casConnection.getSession().clear();
      Session session = casConnection.getSession();
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }
      Query<?> query = session.createNativeQuery(
        "UPDATE TCBS_NEW_ONBOARDING_STATUS SET STATUS_VALUE=:statusValue WHERE STATUS_KEY=:statusKey AND USER_ID IN (SELECT ID FROM TCBS_USER WHERE USERNAME IN (:list105code))"
      );
      query.setParameter("statusKey", statusKey);
      query.setParameter("statusValue", statusValue);
      query.setParameter("list105code", list105code);
      query.executeUpdate();
      casConnection.getSession().getTransaction().commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }

  public static void deleteByUserId(String userId) {
    try {
      Session session = CAS.casConnection.getSession();
      Transaction trans = session.beginTransaction();

      Query<?> query = session.createQuery("DELETE TcbsNewOnboardingStatus WHERE userId=:userId");
      query.setParameter("userId", new BigDecimal(userId));
      query.executeUpdate();
      trans.commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }

  public static void deleteByListAndStatusKey(String statusKey, List<String> code105CList, String type) {
    try {
      Session session = casConnection.getSession();
      session.clear();
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }
      type = Objects.nonNull(type) ? type : "USERNAME";
      Query<?> query = session.createNativeQuery(
        "DELETE FROM TCBS_NEW_ONBOARDING_STATUS WHERE STATUS_KEY = :statusKey AND USER_ID IN (SELECT ID FROM TCBS_USER WHERE " + type + " IN (:list105C))");
      query.setParameter("statusKey", statusKey);
      query.setParameter("list105C", code105CList);
      query.executeUpdate();
      session.getTransaction().commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }

  public static void insertData(String statusKey, String statusValue, String tcbsId, String rejectContent) {
    try {
      Session session = casConnection.getSession();
      session.clear();
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }
      Query<?> query = session.createNativeQuery(
        "INSERT INTO TCBS_NEW_ONBOARDING_STATUS " +
          "(ID, USER_ID, GROUP_STATUS, STATUS_KEY, STATUS_VALUE, REJECT_REASON, REJECT_CONTENT, REJECT_PERSON, CREATED_DATE, UPDATED_DATE) " +
          "VALUES(TCBS_NO_STATUS_SEQUENCE.nextval, (SELECT ID FROM TCBS_USER WHERE TCBSID = :tcbsId), 'ONLINE_ACTIVATION', :statusKey, :statusValue, '', :rejectContent, '', SYSDATE, SYSDATE)");
      query.setParameter("tcbsId", tcbsId);
      query.setParameter("statusKey", statusKey);
      query.setParameter("statusValue", statusValue);
      query.setParameter("rejectContent", rejectContent);
      query.executeUpdate();
      session.getTransaction().commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }

  public void insert() {
    Session session = casConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    try {
      session.save(this);
      session.getTransaction().commit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
