package com.automation.cas;

import lombok.*;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "xxxx_REMINDER_SOURCE")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class xxxxReminderSource {
  @Id
  @SequenceGenerator(name = "xxxx_REMINDER_SOURCE_ID_GENERATOR", sequenceName = "xxxx_REMINDER_SOURCE_SEQUENCE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "xxxx_REMINDER_SOURCE_ID_GENERATOR")
  @Column(name = "ID")
  private BigDecimal id;

  @Column(name = "REMINDER_ID")
  private BigDecimal reminderId;

  @Column(name = "STATUS")
  private BigDecimal status;

  @Column(name = "xxxxID")
  private String xxxxId;

  @Column(name = "CONTENT")
  private String content;

  @Column(name = "EXPIRE_TIME")
  private Date expireTime;

  @Column(name = "CREATED_DATE")
  private Date createdDate;

  @Column(name = "UPDATED_DATE")
  private Date updatedDate;

  @Step
  public static void deleteByxxxxIdAndReminderId(String xxxxId, int reminderId) {
    CAS.casConnection.getSession().clear();
    if (!CAS.casConnection.getSession().getTransaction().isActive()) {
      CAS.casConnection.getSession().beginTransaction();
    }
    Query query = CAS.casConnection.getSession().createNativeQuery(
      "DELETE xxxx_REMINDER_SOURCE WHERE xxxxID = ?1 AND REMINDER_ID = ?2");
    query.setParameter(1, xxxxId);
    query.setParameter(2, reminderId);
    query.executeUpdate();
    CAS.casConnection.getSession().getTransaction().commit();
  }

  public static xxxxReminderSource getByxxxxIdAndReminderId(String xxxxId, int reminderId) {
    CAS.casConnection.getSession().clear();
    if (!CAS.casConnection.getSession().getTransaction().isActive()) {
      CAS.casConnection.getSession().beginTransaction();
    }
    try {
      Query<xxxxReminderSource> query = CAS.casConnection.getSession().createQuery(
        "from xxxxReminderSource where xxxxId =:xxxxId and reminderId =:reminderId", xxxxReminderSource.class);
      query.setParameter("reminderId", new BigDecimal(reminderId));
      query.setParameter("xxxxId", xxxxId);
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
