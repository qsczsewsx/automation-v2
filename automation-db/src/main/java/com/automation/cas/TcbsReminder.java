package com.automation.cas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "xxxx_REMINDER")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class xxxxReminder {
  @Id
  @SequenceGenerator(name = "xxxx_REMINDER_ID_GENERATOR", sequenceName = "xxxx_REMINDER_SEQUENCE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "xxxx_REMINDER_ID_GENERATOR")
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
    CAS.casConnection.getSession().clear();
    if (!CAS.casConnection.getSession().getTransaction().isActive()) {
      CAS.casConnection.getSession().beginTransaction();
    }
    Query query = CAS.casConnection.getSession().createNativeQuery(
      "UPDATE xxxx_REMINDER SET STATUS = ?1, END_TIME = ?2 WHERE ID = ?3");
    query.setParameter(1, status);
    query.setParameter(2, endTime);
    query.setParameter(3, id);
    query.executeUpdate();
    CAS.casConnection.getSession().getTransaction().commit();
  }

  public static xxxxReminder getById(BigDecimal id) {
    CAS.casConnection.getSession().clear();
    if (!CAS.casConnection.getSession().getTransaction().isActive()) {
      CAS.casConnection.getSession().beginTransaction();
    }
    try {
      Query<xxxxReminder> query = CAS.casConnection.getSession().createQuery(
        "from xxxxReminder where id =:id", xxxxReminder.class);
      query.setParameter("id", id);
      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }
}
