package com.tcbs.automation.cas;

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
@Table(name = "OB_USER_CLOSE_DOCS")
@Getter
@Setter
public class ObUserCloseDocs {
  private static Logger logger = LoggerFactory.getLogger(ObUserCloseDocs.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "FILE_NAME")
  private String fileName;
  @Column(name = "FILE_TYPE")
  private String fileType;
  @Column(name = "ECM_ID")
  private String ecmId;
  @Column(name = "TASK_ID")
  private BigDecimal taskId;
  @Column(name = "CREATE_DATE")
  private Date createDate;
  @Column(name = "ACTOR")
  private String actor;
  @NotNull
  @Column(name = "FILE_SUFFIX")
  private String fileSuffix;
  @Column(name = "DOWNLOAD_ID")
  private String downloadId;
  @Column(name = "USER_ID")
  private String userId;


  public static List<ObUserCloseDocs> getCloseDocs(String taskId) {
    CAS.casConnection.getSession().clear();
    Query<ObUserCloseDocs> query = CAS.casConnection.getSession().createQuery(
      "from ObUserCloseDocs a where a.taskId=:taskId", ObUserCloseDocs.class);
    query.setParameter("taskId", new BigDecimal(taskId));
    return query.getResultList();
  }

  public static void deleteByObTaskCloseId(BigDecimal taskId) {
    try {
      Session session = CAS.casConnection.getSession();
      session.clear();
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }
      Query query = session.createQuery("DELETE ObUserCloseDocs WHERE taskId=:taskId");
      query.setParameter("taskId", taskId);
      query.executeUpdate();
      session.getTransaction().commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }

  public static List<ObUserCloseDocs> getCloseDocsByUserId(String userId) {
    CAS.casConnection.getSession().clear();
    Query<ObUserCloseDocs> query = CAS.casConnection.getSession().createQuery(
      "from ObUserCloseDocs a where a.userId=:userId", ObUserCloseDocs.class);
    query.setParameter("userId", userId);
    return query.getResultList();
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
