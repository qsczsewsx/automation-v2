package com.automation.cas;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "OB_RETRY")
@Getter
@Setter
public class ObRetry {
  private static Logger logger = LoggerFactory.getLogger(ObRetry.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "USER_ID")
  private BigDecimal userId;
  @Column(name = "TYPE")
  private String type;
  @Column(name = "ACTION")
  private String action;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "REASON")
  private String reason;
  @Column(name = "RETRY_MAX_COUNT")
  private BigDecimal retryMaxCount;
  @Column(name = "RETRY_COUNT")
  private BigDecimal retryCount;
  @Column(name = "ERROR_CODE")
  private String errorCode;
  @Column(name = "ERROR_MESSAGE")
  private String errorMessage;
  @Column(name = "CREATED_DATETIME")
  private Date createdDatetime;
  @Column(name = "UPDATED_DATETIME")
  private Date updatedDatetime;

  @Step
  public static ObRetry getByActionAndErrorCode(String action, String errorCode) {
    CAS.casConnection.getSession().clear();
    Query<ObRetry> query = CAS.casConnection.getSession().createQuery(
      "from ObRetry a where a.action=:action and a.errorCode=:errorCode", ObRetry.class);
    query.setParameter("action", action);
    query.setParameter("errorCode", errorCode);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new ObRetry();
    }
  }

  public static void deleteByActionAndErrorCode(String action, String errorCode) {
    try {
      Session session = CAS.casConnection.getSession();
      session.clear();
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }
      Query<?> query = session.createQuery("DELETE ObRetry where action=:action and errorCode=:errorCode");
      query.setParameter("action", action);
      query.setParameter("errorCode", errorCode);
      query.executeUpdate();
      session.getTransaction().commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }

}
