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
import java.sql.Timestamp;

@Entity
@Table(name = "OB_USER_CLOSE")
@Getter
@Setter

public class ObUserClose {
  private static Logger logger = LoggerFactory.getLogger(ObUserCloseDocs.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @NotNull
  @Column(name = "USER_ID")
  private BigDecimal userId;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "REASON")
  private String reason;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "CREATED_BY")
  private String createdBy;
  @Column(name = "UPDATED_DATE")
  private Timestamp updatedDate;
  @Column(name = "UPDATED_BY")
  private String updatedBy;
  @Column(name = "TASK_CLOSE_ID")
  private BigDecimal taskCloseId;
  @Column(name = "BOOK_ID")
  private BigDecimal bookId;

  public static void deleteByObTaskCloseId(BigDecimal taskCloseId) {
    try {
      Session session = CAS.casConnection.getSession();
      session.clear();
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }
      Query query = session.createQuery("DELETE ObUserClose WHERE taskCloseId=:taskCloseId");
      query.setParameter("taskCloseId", taskCloseId);
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
      logger.info(e.getMessage());
    }
  }

}
