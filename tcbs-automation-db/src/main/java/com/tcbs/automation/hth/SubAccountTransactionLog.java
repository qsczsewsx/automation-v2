package com.tcbs.automation.hth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import static com.tcbs.automation.hth.HthDb.h2hConnection;

@Entity
@Table(name = "SUB_ACCOUNT_TRANSACTION_LOG")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubAccountTransactionLog {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "CUSTODY_CODE")
  private String custodyCode;
  @Column(name = "SUB_ACCOUNT_NO")
  private String subAccountNo;
  @NotNull
  @Column(name = "ACTION")
  private String action;
  @Column(name = "INTERACTED_ACCOUNT_NO")
  private String interactedAccountNo;
  @Column(name = "AMOUNT")
  private BigDecimal amount;
  @Column(name = "REFERENCE_ID")
  private String referenceId;
  @Column(name = "BANK_TRANSACTION_ID")
  private String bankTransactionId;
  @Column(name = "CURRENCY")
  private String currency;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "ACTION_RESPONSE_MESSAGE")
  private String actionResponseMessage;
  @Column(name = "INQUIRY_RESPONSE_MESSAGE")
  private String inquiryResponseMessage;
  @Column(name = "ERROR_CODES")
  private String errorCodes;
  @Column(name = "CREATED_TIME")
  private Timestamp createdTime;
  @Column(name = "UPDATED_TIME")
  private Timestamp updatedTime;

  public static List<SubAccountTransactionLog> getFromReferenceId(String referenceId) {
    try {
      h2hConnection.getSession().clear();
      Query<SubAccountTransactionLog> query = h2hConnection.getSession().createQuery(

        "from SubAccountTransactionLog where referenceId=:referenceId order by REFERENCE_ID desc",

        SubAccountTransactionLog.class
      );
      query.setParameter("referenceId", referenceId);
      return query.getResultList();
    } catch (NoResultException e) {
      return null;
    }
  }

  /**
   * Author Lybtk
   */
  public static SubAccountTransactionLog getByReferenceId(String referenceId) {
    try {
      h2hConnection.getSession().clear();
      org.hibernate.query.Query<SubAccountTransactionLog> query = h2hConnection.getSession().createQuery(
        "from SubAccountTransactionLog where referenceId=:referenceId ",
        SubAccountTransactionLog.class
      );
      query.setParameter("referenceId", referenceId);
      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  /**
   * Author Lybtk
   */
  public static SubAccountTransactionLog getByStatus(String status, String referenceId) {
    try {
      h2hConnection.getSession().clear();
      org.hibernate.query.Query<SubAccountTransactionLog> query = h2hConnection.getSession().createQuery(
        "from SubAccountTransactionLog where status=:status and referenceId=:referenceId ",
        SubAccountTransactionLog.class
      );
      query.setParameter("status", status);
      query.setParameter("referenceId", referenceId);
      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  /**
   * Author Lybtk
   */
  public static List<SubAccountTransactionLog> getFromStatus(String status, String action) {
    try {
      h2hConnection.getSession().clear();
      Query<SubAccountTransactionLog> query = h2hConnection.getSession().createQuery(

        "from SubAccountTransactionLog where status=:status and action =: action  order by REFERENCE_ID desc",

        SubAccountTransactionLog.class
      );
      query.setParameter("status", status);
      query.setParameter("action", action);
      return query.getResultList();
    } catch (NoResultException e) {
      return null;
    }
  }

  /**
   * Author Lybtk
   */
  public static void updateTransactionStatus(String referenceId, String status) {
    Session session = HthDb.h2hConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = session.createQuery(
      "update SubAccountTransactionLog set status=:status where referenceId =:referenceId ");
    query.setParameter("status", status);
    query.setParameter("referenceId", referenceId);
    query.executeUpdate();
    session.getTransaction().commit();
  }
}
