package com.tcbs.automation.cas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

import static com.tcbs.automation.cas.CAS.casConnection;

@Entity
@Table(name = "TCBS_REMINDER")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TcbsReminder {
  @Id
  @SequenceGenerator(name = "TCBS_REMINDER_ID_GENERATOR", sequenceName = "TCBS_REMINDER_SEQUENCE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TCBS_REMINDER_ID_GENERATOR")
  @Column(name = "ID")
  private BigDecimal id;

  @Column(name = "NAME")
  private String name;

  @Column(name = "STATUS")
  private BigDecimal status;

  @Column(name = "FREQUENCY")
  private String frequency;

  @Column(name = "DURATION_PER_SESSION")
  private Integer durationPerSession;

  @Column(name = "TEMPLATE")
  private String template;

  @Column(name = "NOTI_TYPE")
  private String notiType;

  @Column(name = "CREATED_DATE")
  private Date createdDate;

  @Column(name = "UPDATED_DATE")
  private Date updatedDate;

  @Column(name = "START_TIME")
  private Date startTime;

  @Column(name = "END_TIME")
  private Date endTime;

  public static void updateStatusAndEndTimeById(int id, int status, Date endTime) {
    casConnection.getSession().clear();
    if (!casConnection.getSession().getTransaction().isActive()) {
      casConnection.getSession().beginTransaction();
    }
    Query query = casConnection.getSession().createNativeQuery(
      "UPDATE TCBS_REMINDER SET STATUS = ?1, END_TIME = ?2 WHERE ID = ?3");
    query.setParameter(1, status);
    query.setParameter(2, endTime);
    query.setParameter(3, id);
    query.executeUpdate();
    casConnection.getSession().getTransaction().commit();
  }

  public static TcbsReminder getById(BigDecimal id) {
    casConnection.getSession().clear();
    if (!casConnection.getSession().getTransaction().isActive()) {
      casConnection.getSession().beginTransaction();
    }
    try {
      Query<TcbsReminder> query = CAS.casConnection.getSession().createQuery(
        "from TcbsReminder where id =:id", TcbsReminder.class);
      query.setParameter("id", id);
      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }
}
