package com.automation.cas;

import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "xxxx_USER_INSTRUMENT")
public class xxxxUserInstrument {

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
  public static xxxxUserInstrument getByxxxxInstrument(String userId) {
    Query<xxxxUserInstrument> query = CAS.casConnection.getSession().createQuery(
      "from xxxxUserInstrument a where a.userId=:userId", xxxxUserInstrument.class);
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
