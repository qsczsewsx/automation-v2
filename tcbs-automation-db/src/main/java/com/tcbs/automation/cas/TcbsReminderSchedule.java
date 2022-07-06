package com.tcbs.automation.cas;

import lombok.*;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

import static com.tcbs.automation.cas.CAS.casConnection;

@Entity
@Table(name = "TCBS_REMINDER_SCHEDULE")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TcbsReminderSchedule {
  @Id
  @SequenceGenerator(name = "TCBS_REMINDER_SCHEDULE_ID_GENERATOR", sequenceName = "TCBS_REMINDER_SCHEDULE_SEQUENCE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TCBS_REMINDER_SCHEDULE_ID_GENERATOR")
  @Column(name = "ID")
  private BigDecimal id;

  @Column(name = "SOURCE_ID")
  private BigDecimal sourceId;

  @Column(name = "STATUS")
  private BigDecimal status;

  @Column(name = "CONTENT")
  private String content;

  @Column(name = "INTEND_TIME")
  private Date intendTime;

  @Column(name = "EXECUTION_TIME")
  private Date executionTime;

  @Column(name = "CREATED_DATE")
  private Date createdDate;

  public static void deleteByTcbsIdAndReminderIdAndStatus(String tcbsId, int reminderId, int status) {
    casConnection.getSession().clear();
    if (!casConnection.getSession().getTransaction().isActive()) {
      casConnection.getSession().beginTransaction();
    }
    Query query = casConnection.getSession().createNativeQuery(
      "DELETE TCBS_REMINDER_SCHEDULE WHERE SOURCE_ID IN (SELECT ID FROM TCBS_REMINDER_SOURCE WHERE TCBSID = ?1 AND REMINDER_ID = ?2) AND STATUS = ?3");
    query.setParameter(1, tcbsId);
    query.setParameter(2, reminderId);
    query.setParameter(3, status);
    query.executeUpdate();
    casConnection.getSession().getTransaction().commit();
  }

  public static void deleteByTcbsIdAndReminderId(String tcbsId, int reminderId) {
    casConnection.getSession().clear();
    if (!casConnection.getSession().getTransaction().isActive()) {
      casConnection.getSession().beginTransaction();
    }
    Query query = casConnection.getSession().createNativeQuery(
      "DELETE TCBS_REMINDER_SCHEDULE WHERE SOURCE_ID IN (SELECT ID FROM TCBS_REMINDER_SOURCE WHERE TCBSID = ?1 AND REMINDER_ID = ?2)");
    query.setParameter(1, tcbsId);
    query.setParameter(2, reminderId);
    query.executeUpdate();
    casConnection.getSession().getTransaction().commit();
  }

  public static TcbsReminderSchedule getByTcbsIdAndReminderIdAndStatus(String tcbsId, int reminderId, int status) {
    casConnection.getSession().clear();
    if (!casConnection.getSession().getTransaction().isActive()) {
      casConnection.getSession().beginTransaction();
    }
    try {
      Query<TcbsReminderSchedule> query = CAS.casConnection.getSession().createQuery(
        "from TcbsReminderSchedule where status =:status and sourceId IN (select id from TcbsReminderSource where tcbsId =:tcbsId and reminderId =:reminderId)", TcbsReminderSchedule.class);
      query.setParameter("reminderId", new BigDecimal(reminderId));
      query.setParameter("tcbsId", tcbsId);
      query.setParameter("status", new BigDecimal(status));
      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public static TcbsReminderSchedule getById(BigDecimal id) {
    casConnection.getSession().clear();
    if (!casConnection.getSession().getTransaction().isActive()) {
      casConnection.getSession().beginTransaction();
    }
    try {
      Query<TcbsReminderSchedule> query = CAS.casConnection.getSession().createQuery(
        "from TcbsReminderSchedule where id =: id", TcbsReminderSchedule.class);
      query.setParameter("id", id);
      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
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
