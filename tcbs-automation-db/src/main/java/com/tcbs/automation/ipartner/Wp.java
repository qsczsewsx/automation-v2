package com.tcbs.automation.ipartner;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

import static com.tcbs.automation.config.ipartner.IpartnerConstants.TCBSID_TEXT;

@Entity
@Table(name = "wp")
public class Wp {

  private static Logger logger = LoggerFactory.getLogger(Wp.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wp_id_seq")
  @Column(name = "id")
  private long id;
  @Column(name = "tcbs_id")
  private String tcbsId;
  @Column(name = "status")
  private String status;
  @Column(name = "tnc_status")
  private String tncStatus;
  @Column(name = "learning_status")
  private String learningStatus;
  @Column(name = "start_date")
  private java.sql.Timestamp startDate;
  @Column(name = "end_date")
  private java.sql.Timestamp endDate;
  @Column(name = "created")
  private java.sql.Timestamp created;
  @Column(name = "last_modified")
  private java.sql.Timestamp lastModified;
  @Column(name = "created_by")
  private String createdBy;
  @Column(name = "modified_by")
  private String modifiedBy;
  @Column(name = "channel")
  private String channel;
  @Column(name = "description")
  private String description;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public String getTcbsId() {
    return tcbsId;
  }

  public void setTcbsId(String tcbsId) {
    this.tcbsId = tcbsId;
  }


  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }


  public String getTncStatus() {
    return tncStatus;
  }

  public void setTncStatus(String tncStatus) {
    this.tncStatus = tncStatus;
  }


  public String getLearningStatus() {
    return learningStatus;
  }

  public void setLearningStatus(String learningStatus) {
    this.learningStatus = learningStatus;
  }


  public java.sql.Timestamp getStartDate() {
    return startDate;
  }

  public void setStartDate(java.sql.Timestamp startDate) {
    this.startDate = startDate;
  }


  public java.sql.Timestamp getEndDate() {
    return endDate;
  }

  public void setEndDate(java.sql.Timestamp endDate) {
    this.endDate = endDate;
  }


  public java.sql.Timestamp getCreated() {
    return created;
  }

  public void setCreated(java.sql.Timestamp created) {
    this.created = created;
  }


  public java.sql.Timestamp getLastModified() {
    return lastModified;
  }

  public void setLastModified(java.sql.Timestamp lastModified) {
    this.lastModified = lastModified;
  }


  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }


  public String getModifiedBy() {
    return modifiedBy;
  }

  public void setModifiedBy(String modifiedBy) {
    this.modifiedBy = modifiedBy;
  }


  public String getChannel() {
    return channel;
  }

  public void setChannel(String channel) {
    this.channel = channel;
  }


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  public void insert() {
    Session session = IWpartner.iwpDBConnection.getSession();
    session.clear();
    session.beginTransaction();
    session.save(this);
    session.getTransaction().commit();
  }

  public Wp getWp() throws Exception {
    Query<Wp> query = IWpartner.iwpDBConnection.getSession().createQuery("from Wp where tcbsId=:tcbsId", Wp.class);
    query.setParameter(TCBSID_TEXT, tcbsId);
    List<Wp> result = query.getResultList();
    if (result.size() > 0) {
      return result.get(0);
    } else {
      return null;
    }
  }

  public void delete() {
    try {
      Session session = IWpartner.iwpDBConnection.getSession();
      session.clear();
      Transaction trans = session.beginTransaction();
      Query query = session.createSQLQuery(String.format("DELETE FROM wp WHERE tcbs_id= '%s'", tcbsId));
      query.executeUpdate();
      trans.commit();
    } catch (Exception ex) {
      logger.info(ex.getMessage());
    }
  }

  public boolean isExistedWp() throws Exception {
    Query<Wp> query = IWpartner.iwpDBConnection.getSession().createQuery("from Wp where tcbsId=:tcbsId", Wp.class);
    query.setParameter(TCBSID_TEXT, tcbsId);
    List<Wp> result = query.getResultList();
    if (result.size() > 0) {
      return true;
    } else {
      return false;
    }
  }

  public void insertSampleToDB() {
    try {
      Session session = IWpartner.iwpDBConnection.getSession();
      session.clear();
      Transaction trans = session.beginTransaction();
      Query query = session.createSQLQuery(
        String.format("insert into wp(tcbs_id,description) values(:tcbsId,'IAG-T633 Auto_[Upload DS iWP]Verify that API''s response is correct when import exist iWP in DB')"));
      query.setParameter(TCBSID_TEXT, tcbsId);
      query.executeUpdate();
      trans.commit();
    } catch (Exception ex) {
      logger.info(ex.getMessage());
    }
  }

  public void updateStatusByTcbsId(String status, String tcbsId) {
    Session session = IWpartner.iwpDBConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<Wp> query = session.createQuery(
      "UPDATE Wp SET status=:status WHERE id=(select max(id) from Wp where tcbsId=:tcbsId)");

    query.setParameter("status", status)
      .setParameter("tcbsId", tcbsId)
      .executeUpdate();
    session.getTransaction().commit();
  }

  public void updateModifiedDateByTcbsId(Timestamp lastModifiedDate, String tcbsId) {
    Session session = IWpartner.iwpDBConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<Wp> query = session.createQuery(
      "UPDATE Wp SET lastModified=:lastModifiedDate where tcbsId=:tcbsId");
    query.setParameter("lastModifiedDate", lastModifiedDate)
      .setParameter("tcbsId", tcbsId)
      .executeUpdate();
    session.getTransaction().commit();
  }

  public void dmlPrepareData(String queryString) {
    Session session = IWpartner.iwpDBConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = session.createSQLQuery(queryString);
    query.executeUpdate();
    session.getTransaction().commit();
  }
}