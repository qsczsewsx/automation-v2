package com.automation.cas;

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

@Getter
@Setter
@Entity
@Table(name = "xxxx_IDENTIFICATION")
public class xxxxIdentification {

  private static Logger logger = LoggerFactory.getLogger(xxxxReferralData.class);

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
  public static xxxxIdentification getByxxxxIdentification(String userId) {
    Query<xxxxIdentification> query = CAS.casConnection.getSession().createQuery(
      "from xxxxIdentification a where a.userId=:userId", xxxxIdentification.class);
    query.setParameter("userId", new BigDecimal(userId));
    return query.getSingleResult();
  }

  public static xxxxIdentification getByIdNumber(String idNumber) {
    Query<xxxxIdentification> query = CAS.casConnection.getSession().createQuery(
      "from xxxxIdentification a where a.idNumber=:idNumber order by id desc", xxxxIdentification.class);
    query.setParameter("idNumber", idNumber);
    return query.getSingleResult();
  }

  public static List<xxxxIdentification> getListByIdPlace(String idPlace) {
    Query<xxxxIdentification> query = CAS.casConnection.getSession().createQuery(
      "from xxxxIdentification a where LOWER(a.idPlace) LIKE LOWER('%' || :idPlace  || '%') order by ID", xxxxIdentification.class);
    query.setParameter("idPlace", idPlace);
    return query.getResultList();
  }

  public static List<xxxxIdentification> getListByIdNumber(String idNumber) {
    Query<xxxxIdentification> query = CAS.casConnection.getSession().createQuery(
      "from xxxxIdentification a where a.idNumber=:idNumber", xxxxIdentification.class);
    query.setParameter("idNumber", idNumber);
    return query.getResultList();
  }

  public static xxxxIdentification getByUserId(String userId) {
    Query<xxxxIdentification> query = CAS.casConnection.getSession().createQuery(
      "from xxxxIdentification a where a.userId=:userId", xxxxIdentification.class);
    query.setParameter("userId", new BigDecimal(userId));
    return query.getSingleResult();
  }

  public static void deleteByIdNumber(String idNumber) {
    try {
      Session session = CAS.casConnection.getSession();
      Transaction trans = session.beginTransaction();

      Query<?> query = session.createQuery("DELETE xxxxIdentification WHERE idNumber=:idNumber");
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

      Query<?> query = session.createQuery("DELETE xxxxIdentification WHERE userId=:userId");
      query.setParameter("userId", new BigDecimal(userId));
      query.executeUpdate();
      trans.commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }

  public static void updateIdPlace(String userId, String idPlace) {
    CAS.casConnection.getSession().clear();
    if (!CAS.casConnection.getSession().getTransaction().isActive()) {
      CAS.casConnection.getSession().beginTransaction();
    }
    org.hibernate.query.Query query = CAS.casConnection.getSession().createQuery(
      "Update xxxxIdentification a set a.idPlace =:idPlace where a.userId=:userId");
    query.setParameter("userId", new BigDecimal(userId));
    query.setParameter("idPlace", idPlace);
    query.executeUpdate();
    CAS.casConnection.getSession().getTransaction().commit();
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

