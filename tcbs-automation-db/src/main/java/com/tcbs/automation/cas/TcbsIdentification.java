package com.tcbs.automation.cas;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static com.tcbs.automation.cas.CAS.casConnection;

@Getter
@Setter
@Entity
@Table(name = "TCBS_IDENTIFICATION")
public class TcbsIdentification {

  private static Logger logger = LoggerFactory.getLogger(TcbsReferralData.class);

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "ID_TYPE")
  private String idType;
  @Column(name = "ID_NUMBER")
  private String idNumber;
  @Column(name = "ID_DATE")
  private Timestamp idDate;
  @Column(name = "ID_PLACE")
  private String idPlace;
  @Column(name = "USER_ID")
  private BigDecimal userId;
  @Column(name = "EXPIRE_DATE")
  private String expireDate;
  @Column(name = "EXTEND_REASON")
  private String extendReason;
  @Column(name = "EXCEPTION_DATE")
  private Date exceptionDate;
  @Column(name = "EXTEND_DATE")
  private String extendDate;
  @Column(name = "REASON")
  private String reason;

  @Step
  public static TcbsIdentification getByTcbsIdentification(String userId) {
    Query<TcbsIdentification> query = CAS.casConnection.getSession().createQuery(
      "from TcbsIdentification a where a.userId=:userId", TcbsIdentification.class);
    query.setParameter("userId", new BigDecimal(userId));
    return query.getSingleResult();
  }

  public static TcbsIdentification getByIdNumber(String idNumber) {
    Query<TcbsIdentification> query = CAS.casConnection.getSession().createQuery(
      "from TcbsIdentification a where a.idNumber=:idNumber order by id desc", TcbsIdentification.class);
    query.setParameter("idNumber", idNumber);
    return query.getSingleResult();
  }

  public static List<TcbsIdentification> getListByIdPlace(String idPlace) {
    Query<TcbsIdentification> query = CAS.casConnection.getSession().createQuery(
      "from TcbsIdentification a where LOWER(a.idPlace) LIKE LOWER('%' || :idPlace  || '%') order by ID", TcbsIdentification.class);
    query.setParameter("idPlace", idPlace);
    return query.getResultList();
  }

  public static List<TcbsIdentification> getListByIdNumber(String idNumber) {
    Query<TcbsIdentification> query = CAS.casConnection.getSession().createQuery(
      "from TcbsIdentification a where a.idNumber=:idNumber", TcbsIdentification.class);
    query.setParameter("idNumber", idNumber);
    return query.getResultList();
  }

  public static TcbsIdentification getByUserId(String userId) {
    Query<TcbsIdentification> query = CAS.casConnection.getSession().createQuery(
      "from TcbsIdentification a where a.userId=:userId", TcbsIdentification.class);
    query.setParameter("userId", new BigDecimal(userId));
    return query.getSingleResult();
  }

  public static void deleteByIdNumber(String idNumber) {
    try {
      Session session = CAS.casConnection.getSession();
      Transaction trans = session.beginTransaction();

      Query<?> query = session.createQuery("DELETE TcbsIdentification WHERE idNumber=:idNumber");
      query.setParameter("idNumber", idNumber);
      query.executeUpdate();
      trans.commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }

  public static void deleteByUserId(String userId) {
    try {
      Session session = CAS.casConnection.getSession();
      Transaction trans = session.beginTransaction();

      Query<?> query = session.createQuery("DELETE TcbsIdentification WHERE userId=:userId");
      query.setParameter("userId", new BigDecimal(userId));
      query.executeUpdate();
      trans.commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }

  public static void updateIdPlace(String userId, String idPlace) {
    casConnection.getSession().clear();
    if (!casConnection.getSession().getTransaction().isActive()) {
      casConnection.getSession().beginTransaction();
    }
    org.hibernate.query.Query query = casConnection.getSession().createQuery(
      "Update TcbsIdentification a set a.idPlace =:idPlace where a.userId=:userId");
    query.setParameter("userId", new BigDecimal(userId));
    query.setParameter("idPlace", idPlace);
    query.executeUpdate();
    casConnection.getSession().getTransaction().commit();
  }

  public void insert() {
    Session session = casConnection.getSession();
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

