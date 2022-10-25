package com.automation.cas;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "OB_TASK_CLOSE")
@Getter
@Setter
public class ObTaskClose {
  private static Logger logger = LoggerFactory.getLogger(ObTaskClose.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "TYPE")
  private String type;
  @Column(name = "CREATED_DATETIME")
  private Date createdDatetime;
  @Column(name = "UPDATED_DATETIME")
  private Date updatedDatetime;
  @Column(name = "TASK_REF_ID")
  private BigDecimal taskRefId;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "START_DATETIME")
  private Date startDatetime;
  @Column(name = "END_DATETIME")
  private Date endDatetime;
  @Column(name = "PRIORITY")
  private BigDecimal priority;
  @Column(name = "NOTE")
  private String note;
  @Column(name = "ACTOR")
  private String actor;
  @Column(name = "CREATED_BY_USER")
  private String createdByUser;
  @Column(name = "ONLY")
  private BigDecimal only;

  public static ObTaskClose getByTaskId(BigDecimal id) {
    CAS.casConnection.getSession().clear();
    Query<ObTaskClose> query = CAS.casConnection.getSession().createQuery(
      " from ObTaskClose a where a.id=:id", ObTaskClose.class);
    query.setParameter("id", id);
    return query.getSingleResult();
  }

  public static List<ObTaskClose> getLatestTaskId(String status, String actor) {
    CAS.casConnection.getSession().clear();
    Query<ObTaskClose> query = CAS.casConnection.getSession().createQuery(
      "from ObTaskClose a where a.status=:status and a.actor=:actor order by id desc", ObTaskClose.class);
    query.setParameter("status", status);
    query.setParameter("actor", actor);
    return query.getResultList();
  }

  public static void deleteByObTaskCloseId(BigDecimal id) {
    try {
      Session session = CAS.casConnection.getSession();
      session.clear();
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }
      Query query = session.createQuery("DELETE ObTaskClose WHERE id=:id");
      query.setParameter("id", id);
      query.executeUpdate();
      session.getTransaction().commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }

  public void insert() {
    Session session = CAS.casConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    try {
      session.save(this);
      session.getTransaction().commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }

}
