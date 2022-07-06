package com.tcbs.automation.tcwealth;

import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "WEALTH_DATA")
public class WealthData {

  public static Session session;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private Long id;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "SOCIAL_ID")
  private String socialId;
  @NotNull
  @Column(name = "DATA")
  private String data;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }


  public String getTcbsId() {
    return tcbsId;
  }

  public void setTcbsId(String tcbsId) {
    this.tcbsId = tcbsId;
  }


  public String getSocialId() {
    return socialId;
  }

  public void setSocialId(String socialId) {
    this.socialId = socialId;
  }

  @Step
  public WealthData getDataById(String tcbsId) {
    Tcwealth.tcWealthDbConnection.getSession().clear();
    Query<WealthData> query = Tcwealth.tcWealthDbConnection.getSession().createQuery(
      "from WealthData Where tcbsId=:tcbsId", WealthData.class);
    query.setParameter("tcbsId", tcbsId);
    return query.getSingleResult();
  }


  @Step
  public void removeAllWealthData(String tcbsId) {
    Tcwealth.tcWealthDbConnection.getSession().clear();
    Tcwealth.tcWealthDbConnection.getSession().beginTransaction();
    Query query = Tcwealth.tcWealthDbConnection.getSession().createQuery(
      "delete from WealthData Where tcbsId=:tcbsId");
    query.setParameter("tcbsId", tcbsId);
    int numberRowDeleted = query.executeUpdate();
    Tcwealth.tcWealthDbConnection.getSession().getTransaction().commit();
    System.out.println("so data bi xoa: " + numberRowDeleted);
  }
}
