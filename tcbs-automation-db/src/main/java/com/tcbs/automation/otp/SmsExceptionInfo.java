package com.tcbs.automation.otp;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "SMS_EXCEPTION_INFO")
public class SmsExceptionInfo {

  @NotNull
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  @Id
  private Long id;
  @NotNull
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "START_DATE")
  private Long startDate;
  @Column(name = "END_DATE")
  private Long endDate;
  @Column(name = "RANGE_VALUE")
  private int rangeValue;
  @NotNull
  @Column(name = "RANGE_TYPE")
  private String rangeType;
  @NotNull
  @Column(name = "NOTE")
  private String note;
  @NotNull
  @Column(name = "CREATED")
  private Long created;

  @Step
  public static SmsExceptionInfo getSmsExceptionByTcbsId(String tcbsId) {
    Session session = OtpRepository.otpDbConnection.getSession();
    session.clear();
    Query<SmsExceptionInfo> query = session
      .createQuery("from SmsExceptionInfo a where a.tcbsId=:tcbsId order by id desc ", SmsExceptionInfo.class);
    query.setMaxResults(1);
    query.setParameter("tcbsId", tcbsId);

    return query.getSingleResult();
  }

  @Step
  public static List<SmsExceptionInfo> getListSmsExceptionByTcbsId(String tcbsId) {
    Session session = OtpRepository.otpDbConnection.getSession();
    session.clear();
    Query<SmsExceptionInfo> query = session
      .createQuery("from SmsExceptionInfo a where a.tcbsId=:tcbsId order by id desc ", SmsExceptionInfo.class);
    query.setParameter("tcbsId", tcbsId);

    return query.getResultList();
  }

  public static void deleteSmsExceptionByTcbsId(String tcbsId) {
    Session session = OtpRepository.otpDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<SmsExceptionInfo> query = session.createQuery(
      "DELETE FROM SmsExceptionInfo a WHERE a.tcbsId = :tcbsId"
    );
    query.setParameter("tcbsId", tcbsId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static void deleteSmsExceptionById(Long id) {
    Session session = OtpRepository.otpDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<SmsExceptionInfo> query = session.createQuery(
      "DELETE FROM SmsExceptionInfo a WHERE a.id = :id"
    );
    query.setParameter("id", id);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static void updateSmsExceptionByTcbsId(Long id) {
    Session session = OtpRepository.otpDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<SmsExceptionInfo> query = session.createQuery(
      "update SmsExceptionInfo a  set startDate = 1607965200000, endDate = 1608051599000 WHERE a.id = :id"
    );
    query.setParameter("id", id);
    query.executeUpdate();
    session.getTransaction().commit();
  }


}
