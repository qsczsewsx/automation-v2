package com.automation.cas;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "OB_USER_OPENACCOUNT_QUEUE")
@Getter
@Setter
public class ObUserOpenaccountQueue {

  private static Logger logger = LoggerFactory.getLogger(ObUserOpenaccountQueue.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "FIRSTNAME")
  private String firstname;
  @Column(name = "LASTNAME")
  private String lastname;
  @Column(name = "BIRTHDAY")
  private Date birthday;
  @Column(name = "IDNUMBER")
  private String idnumber;
  @Column(name = "IDDATE")
  private Date iddate;
  @Column(name = "IDPLACE")
  private String idplace;
  @Column(name = "CREATED_DATE")
  private Date createdDate;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "GENDER")
  private BigDecimal gender;
  @Column(name = "USER_ID")
  private BigDecimal userId;
  @Column(name = "EXPIRE_DATE")
  private String expireDate;
  @Column(name = "TASK_ID")
  private BigDecimal taskId;
  @Column(name = "UPDATED_DATE")
  private Date updatedDate;
  @Column(name = "EDIT_TYPE")
  private String editType;
  @Column(name = "COUNT_MODIFY")
  private BigDecimal countModify;
  @Column(name = "ADDRESS")
  private String address;

  @Step
  public static ObUserOpenaccountQueue getObUserOpenaccountQueueByTaskId(BigDecimal taskId) {
    CAS.casConnection.getSession().clear();
    Query<ObUserOpenaccountQueue> query = CAS.casConnection.getSession().createQuery(
      "from ObUserOpenaccountQueue a where a.taskId=:taskId", ObUserOpenaccountQueue.class);
    query.setParameter("taskId", taskId);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new ObUserOpenaccountQueue();
    }

  }

  public static void deleteByTaskid(String taskId) {
    CAS.casConnection.getSession().clear();
    if (!CAS.casConnection.getSession().getTransaction().isActive()) {
      CAS.casConnection.getSession().beginTransaction();
    }
    org.hibernate.query.Query query = CAS.casConnection.getSession().createQuery(
      "Delete from ObUserOpenaccountQueue a where a.taskId=:taskId");
    query.setParameter("taskId", new BigDecimal(taskId));
    query.executeUpdate();
    CAS.casConnection.getSession().getTransaction().commit();
  }

}
