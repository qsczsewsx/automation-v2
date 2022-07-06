package com.tcbs.automation.cas;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import static com.tcbs.automation.cas.CAS.casConnection;

@Entity
@SuppressWarnings("unchecked")
@Table(name = "TCBS_FANCY_105C")
@Getter
@Setter
public class TcbsFancy105C {
  private static Logger logger = LoggerFactory.getLogger(TcbsFancy105C.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "CODE_105C")
  private String code105C;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "CREATE_DATE")
  private Timestamp createDate;
  @Column(name = "UPDATE_DATE")
  private Timestamp updateDate;
  @Column(name = "OWNER_BY")
  private String ownerBy;
  @Column(name = "REF_CODE")
  private String refCode;
  @Column(name = "EXPIRED_DATE")
  private Timestamp expiredDate;
  @Column(name = "OWNER_TYPE")
  private String ownerType;

  @Step

  public static List<TcbsFancy105C> getListByCode105C(String code105C) {
    CAS.casConnection.getSession().clear();
    Query<TcbsFancy105C> query = CAS.casConnection.getSession().createQuery(
      "from TcbsFancy105C a where a.code105C=:code105C", TcbsFancy105C.class);
    query.setParameter("code105C", code105C);
    return query.getResultList();

  }

  public static TcbsFancy105C getByReferCode(String refCode) {
    CAS.casConnection.getSession().clear();
    Query<TcbsFancy105C> query = CAS.casConnection.getSession().createQuery(
      "from TcbsFancy105C a where a.refCode=:refCode", TcbsFancy105C.class);
    query.setParameter("refCode", refCode);
    return query.getSingleResult();
  }

  public void deleteByCode105C(String code105C) {
    try {
      Session session = CAS.casConnection.getSession();
      session.clear();
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }
      Query<TcbsFancy105C> query = session.createQuery("DELETE FROM TcbsFancy105C tf WHERE tf.code105C=:code105C");
      query.setParameter(code105C, code105C);
      query.executeUpdate();
      session.getTransaction().commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }

  public static void deleteByOwnerBy(String ownerBy) {
    casConnection.getSession().clear();
    if (!casConnection.getSession().getTransaction().isActive()) {
      casConnection.getSession().beginTransaction();
    }
    Query<TcbsFancy105C> query = casConnection.getSession().createQuery(
      "DELETE FROM TcbsFancy105C tf WHERE tf.ownerBy=:ownerBy");
    query.setParameter("ownerBy", ownerBy);
    query.executeUpdate();
    casConnection.getSession().getTransaction().commit();
  }

  public void dmlPrepareData(String queryString) {
    Session session = CAS.casConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<TcbsFancy105C> query = CAS.casConnection.getSession().createSQLQuery(queryString);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public void setPhoneNoForTcbsId(String username, String phoneNo) {
    try {
      Session session = CAS.casConnection.getSession();
      session.getTransaction().begin();

      Query<TcbsFancy105C> query = session.createQuery("UPDATE TcbsUser a SET a.phone=:phoneNo WHERE a.username=:username");
      query.setParameter("username", username);
      query.setParameter("phoneNo", phoneNo);
      query.executeUpdate();
      session.getTransaction().commit();

    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }


  public void insert() {
    Session session = CAS.casConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    try {
      session.save(this);
      session.getTransaction().commit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
