package com.tcbs.automation.otp;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.sql.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "SMS_LOG")
public class SmsLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private Integer id;
  @NotNull
  @Column(name = "CONTENT")
  private String content;
  @Column(name = "RECEIVER")
  private String receiver;
  @NotNull
  @Column(name = "PROVIDER")
  private String provider;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "ERROR_MESSAGE")
  private String errorMessage;
  @Column(name = "REQUEST_ID")
  private String requestId;
  @NotNull
  @Column(name = "REQUEST_TIME")
  private Timestamp requestTime;
  @NotNull
  @Column(name = "RESPONSE_TIME")
  private Timestamp responseTime;

  @Step
  public static SmsLog getSmsLog(String receiver) {
    Session session = OtpRepository.otpDbConnection.getSession();
    session.clear();
    Query<SmsLog> query = session
      .createQuery("from SmsLog a where a.receiver=:receiver order by id desc ", SmsLog.class).setFirstResult(0);
    query.setParameter("receiver", receiver);

    return query.getResultList().get(0);
  }


}
