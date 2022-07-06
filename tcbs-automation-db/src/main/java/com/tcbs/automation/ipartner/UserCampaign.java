package com.tcbs.automation.ipartner;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user_campaign")
public class UserCampaign {
  @Id
  @SequenceGenerator(name = "user_campaign_id_seq", sequenceName = "user_campaign_id_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_campaign_id_seq")
  @Column(name = "id")
  private long id;
  @Column(name = "tcbsid")
  private String tcbsid;
  @Column(name = "campaign_code")
  private String campaignCode;
  @Column(name = "status")
  private String status;
  @Column(name = "created_date")
  private java.sql.Timestamp createdDate;
  @Column(name = "last_modified_date")
  private java.sql.Timestamp lastModifiedDate;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public String getTcbsid() {
    return tcbsid;
  }

  public void setTcbsid(String tcbsid) {
    this.tcbsid = tcbsid;
  }


  public String getCampaignCode() {
    return campaignCode;
  }

  public void setCampaignCode(String campaignCode) {
    this.campaignCode = campaignCode;
  }


  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }


  public java.sql.Timestamp getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(java.sql.Timestamp createdDate) {
    this.createdDate = createdDate;
  }


  public java.sql.Timestamp getLastModifiedDate() {
    return lastModifiedDate;
  }

  public void setLastModifiedDate(java.sql.Timestamp lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }


  public void delete() {
    try {
      Session session = IWpartner.iwpDBConnection.getSession();
      session.clear();
      Transaction trans = session.beginTransaction();
      Query query = session.createSQLQuery(String.format("DELETE FROM user_campaign WHERE tcbsid= '%s' AND campaign_code = '%s'", tcbsid, campaignCode));
      query.executeUpdate();
      trans.commit();
    } catch (Exception ex) {
      System.out.println("Exception " + ex.toString());
      ;
    }
  }

  public void insert() {
    Session session = IWpartner.iwpDBConnection.getSession();
    session.clear();
    session.beginTransaction();
    session.save(this);
    session.getTransaction().commit();
  }


  public UserCampaign getListUserCampaign() throws Exception {
    Session session = IWpartner.iwpDBConnection.getSession();
    session.clear();
    Query<UserCampaign> query = session.createQuery("from UserCampaign where tcbsid=:tcbsid and campaignCode =:campaignCode", UserCampaign.class);
    query.setParameter("tcbsid", tcbsid);
    query.setParameter("campaignCode", campaignCode);
    List<UserCampaign> result = query.getResultList();
    if (result.size() > 0) {
      return result.get(0);
    } else {
      return null;
    }
  }
}
