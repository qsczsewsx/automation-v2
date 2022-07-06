package com.tcbs.automation.ipartner;

import com.tcbs.automation.ipartner.iwp.TcbsEmployee;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.List;

import static com.tcbs.automation.config.ipartner.IpartnerConstants.PARTNER_ID_TEXT;
import static com.tcbs.automation.config.ipartner.IpartnerConstants.SUBSCRIBER_ID_TEXT;
import static com.tcbs.automation.ipartner.IWpartner.iwpDBConnection;

@Entity
@Getter
@Setter
@Table(name = "wp_relation")
public class WpRelation {
  private static Logger logger = LoggerFactory.getLogger(TcbsEmployee.class);
  @Id
  @SequenceGenerator(name = "wp_relation_id_seq", sequenceName = "wp_relation_id_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wp_relation_id_seq")
  @Column(name = "id")
  private long id;
  @Column(name = "partner_id")
  private String partnerId;
  @Column(name = "subscriber_id")
  private String subscriberId;
  @Column(name = "status")
  private String status;
  @Column(name = "effective_date")
  private java.sql.Timestamp effectiveDate;
  @Column(name = "ineffective_date")
  private java.sql.Timestamp ineffectiveDate;
  @Column(name = "is_shown")
  private boolean isShown;
  @Column(name = "created_date")
  private java.sql.Timestamp createdDate;
  @Column(name = "modified_date")
  private java.sql.Timestamp modifiedDate;
  @Column(name = "created_by")
  private String createdBy;
  @Column(name = "modified_by")
  private String modifiedBy;
  @Column(name = "description")
  private String description;
  @Column(name = "channel")
  private String channel;

  @SuppressWarnings("unchecked")
  public void deleteBySubscriberAndPartner() {
    try {
      Session session = iwpDBConnection.getSession();
      session.clear();
      Transaction trans = session.beginTransaction();
      Query query = session.createSQLQuery("DELETE FROM wp_relation WHERE partner_id =:partnerId and subscriber_id =:subscriberId");
      query.setParameter(PARTNER_ID_TEXT, partnerId);
      query.setParameter(SUBSCRIBER_ID_TEXT, subscriberId);
      query.executeUpdate();
      trans.commit();
    } catch (Exception ex) {
      logger.info(ex.getMessage());
    }
  }

  public void deleteBySubscriber() {
    try {
      Session session = iwpDBConnection.getSession();
      session.clear();
      Transaction trans = session.beginTransaction();
      Query query = session.createSQLQuery("DELETE FROM wp_relation WHERE subscriber_id =:subscriberId");
      query.setParameter(SUBSCRIBER_ID_TEXT, subscriberId);
      query.executeUpdate();
      trans.commit();
    } catch (Exception ex) {
      logger.info(ex.getMessage());
    }
  }

  public void insert() {
    Session session = iwpDBConnection.getSession();
    session.clear();
    session.beginTransaction();
    session.save(this);
    session.getTransaction().commit();
  }

  public WpRelation getWpRelation() throws Exception {
    Session session = iwpDBConnection.getSession();
    session.clear();
    Query<WpRelation> query = session.createQuery("from WpRelation where partnerId=:partnerId and subscriberId =:subscriberId", WpRelation.class);
    query.setParameter(PARTNER_ID_TEXT, partnerId);
    query.setParameter(SUBSCRIBER_ID_TEXT, subscriberId);
    List<WpRelation> result = query.getResultList();
    if (result.size() > 0) {
      WpRelation rs = (WpRelation) result.get(0);
      return result.get(0);
    } else {
      return null;
    }
  }

  public WpRelation getWpRelationByUserIdAndPartnerId(String partnerId, String subscriberId) {
    try {
      Session session = iwpDBConnection.getSession();
      session.clear();
      Query<WpRelation> query = session.createQuery("from WpRelation where partnerId=:partnerId and subscriberId =:subscriberId", WpRelation.class);
      query.setParameter(PARTNER_ID_TEXT, partnerId);
      query.setParameter(SUBSCRIBER_ID_TEXT, subscriberId);
      List<WpRelation> result = query.getResultList();
      if (result.size() > 0) {
        WpRelation rs = (WpRelation) result.get(0);
        return result.get(0);
      } else {
        return null;
      }
    } catch (Exception e) {
      return null;
    }
  }

  public List<WpRelation> getWpRelationByPartnerId(String partnerId) {
    try {
      Session session = iwpDBConnection.getSession();
      session.clear();
      Query<WpRelation> query = session.createQuery("from WpRelation where partnerId=:partnerId", WpRelation.class);
      query.setParameter(PARTNER_ID_TEXT, partnerId);
      List<WpRelation> result = query.getResultList();
      return result;
    } catch (Exception e) {
      return null;
    }
  }

  public void setWpRelationStatusByUserIdAndPartnerId(String statusWant, String partnerId, String subscriberId) {
    try {
      Session session = iwpDBConnection.getSession();
      session.clear();
      Transaction trans = session.beginTransaction();
      Query query = session.createSQLQuery(
        "UPDATE wp_relation SET STATUS=:status  WHERE partner_id =:partnerId and subscriber_id =:subscriberId and id=(select max(id) from wp_relation where partner_id =:partnerId and subscriber_id =:subscriberId)");
      query.setParameter("status", statusWant);
      query.setParameter(PARTNER_ID_TEXT, partnerId);
      query.setParameter(SUBSCRIBER_ID_TEXT, subscriberId);
      query.executeUpdate();
      trans.commit();
    } catch (Exception ex) {
      logger.info(ex.getMessage());
    }
  }

  public void dmlPrepareData(String queryString) {
    Session session = iwpDBConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = iwpDBConnection.getSession().createSQLQuery(queryString);
    query.executeUpdate();
    session.getTransaction().commit();
  }

}