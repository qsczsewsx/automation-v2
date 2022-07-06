package com.tcbs.automation.cas;

import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "TCBS_USER_INSTRUMENT")
public class TcbsUserInstrument {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @Column(name = "CITIZENSHIP")
  private String citizenship;
  @Column(name = "JOB")
  private String job;
  @Column(name = "POSITION")
  private String position;
  @Column(name = "USER_ID")
  private String userId;

  @Step
  public static TcbsUserInstrument getByTcbsInstrument(String userId) {
    Query<TcbsUserInstrument> query = CAS.casConnection.getSession().createQuery(
      "from TcbsUserInstrument a where a.userId=:userId", TcbsUserInstrument.class);
    query.setParameter("userId", userId);
    return query.getSingleResult();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCitizenship() {
    return citizenship;
  }

  public void setCitizenship(String citizenship) {
    this.citizenship = citizenship;
  }

  public String getJob() {
    return job;
  }

  public void setJob(String job) {
    this.job = job;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }
}
