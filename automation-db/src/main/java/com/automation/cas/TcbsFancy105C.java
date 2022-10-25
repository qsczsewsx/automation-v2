package com.automation.cas;

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

@Entity
@SuppressWarnings("unchecked")
@Table(name = "xxxx_FANCY_105C")
@Getter
@Setter
public class xxxxFancy105C {
  private static Logger logger = LoggerFactory.getLogger(xxxxFancy105C.class);
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

  public static List<xxxxFancy105C> getListByCode105C(String code105C) {
    CAS.casConnection.getSession().clear();
    Query<xxxxFancy105C> query = CAS.casConnection.getSession().createQuery(
      "from xxxxFancy105C a where a.code105C=:code105C", xxxxFancy105C.class);
    query.setParameter("code105C", code105C);
    return query.getResultList();

  }

  public static xxxxFancy105C getByReferCode(String refCode) {
    CAS.casConnection.getSession().clear();
    Query<xxxxFancy105C> query = CAS.casConnection.getSession().createQuery(
      "from xxxxFancy105C a where a.refCode=:refCode", xxxxFancy105C.class);
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
      Query<xxxxFancy105C> query = session.createQuery("DELETE FROM xxxxFancy105C tf WHERE tf.code105C=:code105C");
      query.setParameter(code105C, code105C);
      query.executeUpdate();
      session.getTransaction().commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }

  public static void deleteByOwnerBy(String ownerBy) {
    CAS.casConnection.getSession().clear();
    if (!CAS.casConnection.getSession().getTransaction().isActive()) {
      CAS.casConnection.getSession().beginTransaction();
    }
    Query<xxxxFancy105C> query = CAS.casConnection.getSession().createQuery(
      "DELETE FROM xxxxFancy105C tf WHERE tf.ownerBy=:ownerBy");
    query.setParameter("ownerBy", ownerBy);
    query.executeUpdate();
    CAS.casConnection.getSession().getTransaction().commit();
  }

  public void dmlPrepareData(String queryString) {
    Session session = CAS.casConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<xxxxFancy105C> query = CAS.casConnection.getSession().createSQLQuery(queryString);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public void setPhoneNoForxxxxId(String username, String phoneNo) {
    try {
      Session session = CAS.casConnection.getSession();
      session.getTransaction().begin();

      Query<xxxxFancy105C> query = session.createQuery("UPDATE xxxxUser a SET a.phone=:phoneNo WHERE a.username=:username");
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
