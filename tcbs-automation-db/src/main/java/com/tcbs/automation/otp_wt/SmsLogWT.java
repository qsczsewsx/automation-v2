package com.tcbs.automation.otp_wt;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.sql.*;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "SMS_LOG")
public class SmsLogWT {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
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
  public static List<SmsLogWT> getSmsLog(String receiver) {
    Session session = OtpWTRepositoryWT.otpWTDbConnection.getSession();
    session.clear();
    Query<SmsLogWT> query = session
      .createQuery("from SmsLogWT a where a.receiver=:receiver order by id desc", SmsLogWT.class);
    query.setParameter("receiver", receiver);
    return query.getResultList();
  }
}
