package com.automation.cas;

import lombok.*;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "xxxx_REMINDER_SCHEDULE")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class xxxxReminderSchedule {
  @Id
  @SequenceGenerator(name = "xxxx_REMINDER_SCHEDULE_ID_GENERATOR", sequenceName = "xxxx_REMINDER_SCHEDULE_SEQUENCE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "xxxx_REMINDER_SCHEDULE_ID_GENERATOR")
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

  public static void deleteByxxxxIdAndReminderIdAndStatus(String xxxxId, int reminderId, int status) {
    CAS.casConnection.getSession().clear();
    if (!CAS.casConnection.getSession().getTransaction().isActive()) {
      CAS.casConnection.getSession().beginTransaction();
    }
    Query query = CAS.casConnection.getSession().createNativeQuery(
      "DELETE xxxx_REMINDER_SCHEDULE WHERE SOURCE_ID IN (SELECT ID FROM xxxx_REMINDER_SOURCE WHERE xxxxID = ?1 AND REMINDER_ID = ?2) AND STATUS = ?3");
    query.setParameter(1, xxxxId);
    query.setParameter(2, reminderId);
    query.setParameter(3, status);
    query.executeUpdate();
    CAS.casConnection.getSession().getTransaction().commit();
  }

  public static void deleteByxxxxIdAndReminderId(String xxxxId, int reminderId) {
    CAS.casConnection.getSession().clear();
    if (!CAS.casConnection.getSession().getTransaction().isActive()) {
      CAS.casConnection.getSession().beginTransaction();
    }
    Query query = CAS.casConnection.getSession().createNativeQuery(
      "DELETE xxxx_REMINDER_SCHEDULE WHERE SOURCE_ID IN (SELECT ID FROM xxxx_REMINDER_SOURCE WHERE xxxxID = ?1 AND REMINDER_ID = ?2)");
    query.setParameter(1, xxxxId);
    query.setParameter(2, reminderId);
    query.executeUpdate();
    CAS.casConnection.getSession().getTransaction().commit();
  }

  public static xxxxReminderSchedule getByxxxxIdAndReminderIdAndStatus(String xxxxId, int reminderId, int status) {
    CAS.casConnection.getSession().clear();
    if (!CAS.casConnection.getSession().getTransaction().isActive()) {
      CAS.casConnection.getSession().beginTransaction();
    }
    try {
      Query<xxxxReminderSchedule> query = CAS.casConnection.getSession().createQuery(
        "from xxxxReminderSchedule where status =:status and sourceId IN (select id from xxxxReminderSource where xxxxId =:xxxxId and reminderId =:reminderId)", xxxxReminderSchedule.class);
      query.setParameter("reminderId", new BigDecimal(reminderId));
      query.setParameter("xxxxId", xxxxId);
      query.setParameter("status", new BigDecimal(status));
      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public static xxxxReminderSchedule getById(BigDecimal id) {
    CAS.casConnection.getSession().clear();
    if (!CAS.casConnection.getSession().getTransaction().isActive()) {
      CAS.casConnection.getSession().beginTransaction();
    }
    try {
      Query<xxxxReminderSchedule> query = CAS.casConnection.getSession().createQuery(
        "from xxxxReminderSchedule where id =: id", xxxxReminderSchedule.class);
      query.setParameter("id", id);
      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
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
