package com.tcbs.automation.otp;

import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "TOTP")
public class Totp implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private Long id;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "USER_ID")
  private String userId;
  @Column(name = "SECRET")
  private String secret;

  @Step("Get entry from table")
  public static Totp getByUserId(String id) {
    Session session = OtpRepository.otpDbConnection.getSession();
    session.clear();

    Query<Totp> query = session.createQuery("from Totp where userId=:id", Totp.class);
    query.setParameter("id", id);

    try {
      Totp result = query.getSingleResult();
      return result;
    } catch (Exception e) {
      return null;
    }
  }

  public static List<Totp> getTcbsId() {
    Session session = OtpRepository.otpDbConnection.getSession();
    session.clear();
    Query<Totp> query = session.createQuery("from Totp where 1 = 1", Totp.class);
    return query.getResultList();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

}
