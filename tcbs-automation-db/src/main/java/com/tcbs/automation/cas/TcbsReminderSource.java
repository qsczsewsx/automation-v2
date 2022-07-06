package com.tcbs.automation.cas;

import lombok.*;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

import static com.tcbs.automation.cas.CAS.casConnection;

@Entity
@Table(name = "TCBS_REMINDER_SOURCE")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TcbsReminderSource {
  @Id
  @SequenceGenerator(name = "TCBS_REMINDER_SOURCE_ID_GENERATOR", sequenceName = "TCBS_REMINDER_SOURCE_SEQUENCE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TCBS_REMINDER_SOURCE_ID_GENERATOR")
  @Column(name = "ID")
  private BigDecimal id;

  @Column(name = "REMINDER_ID")
  private BigDecimal reminderId;

  @Column(name = "STATUS")
  private BigDecimal status;

  @Column(name = "TCBSID")
  private String tcbsId;

  @Column(name = "CONTENT")
  private String content;

  @Column(name = "EXPIRE_TIME")
  private Date expireTime;

  @Column(name = "CREATED_DATE")
  private Date createdDate;

  @Column(name = "UPDATED_DATE")
  private Date updatedDate;

  @Step
  public static void deleteByTcbsIdAndReminderId(String tcbsId, int reminderId) {
    casConnection.getSession().clear();
    if (!casConnection.getSession().getTransaction().isActive()) {
      casConnection.getSession().beginTransaction();
    }
    Query query = casConnection.getSession().createNativeQuery(
      "DELETE TCBS_REMINDER_SOURCE WHERE TCBSID = ?1 AND REMINDER_ID = ?2");
    query.setParameter(1, tcbsId);
    query.setParameter(2, reminderId);
    query.executeUpdate();
    casConnection.getSession().getTransaction().commit();
  }

  public static TcbsReminderSource getByTcbsIdAndReminderId(String tcbsId, int reminderId) {
    casConnection.getSession().clear();
    if (!casConnection.getSession().getTransaction().isActive()) {
      casConnection.getSession().beginTransaction();
    }
    try {
      Query<TcbsReminderSource> query = CAS.casConnection.getSession().createQuery(
        "from TcbsReminderSource where tcbsId =:tcbsId and reminderId =:reminderId", TcbsReminderSource.class);
      query.setParameter("reminderId", new BigDecimal(reminderId));
      query.setParameter("tcbsId", tcbsId);
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
