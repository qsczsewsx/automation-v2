package com.tcbs.automation.hth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

import static com.tcbs.automation.hth.HthDb.h2hConnection;

@Entity
@Table(name = "ISOCIAS_TXN_RECONCILE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IsociasTxnReconcile {

  private static final String PAYMENT_TYPE = "paymentType";
  private static final String TRANSACTION_ID = "transactionId";
  private static final String PARTNER_TRANSACTION_ID = "partnerTransactionId";
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "PARTNER_TYPE")
  private String partnerType;
  @Column(name = "TRANSACTION_ID")
  private String transactionId;
  @Column(name = "PAYMENT_TYPE")
  private String paymentType;
  @Column(name = "BANK_CODE")
  private String bankCode;
  @Column(name = "CITAD")
  private String citad;
  @Column(name = "SOURCE_ACCOUNT_NUMBER")
  private String sourceAccountNumber;
  @Column(name = "AMOUNT")
  private BigDecimal amount;
  @Column(name = "CURRENCY")
  private String currency;
  @Column(name = "DESTINATION_ACCOUNT_NUMBER")
  private String destinationAccountNumber;
  @Column(name = "TRANSACTION_DATE")
  private Timestamp transactionDate;
  @Column(name = "PARTNER_TRANSACTION_ID")
  private String partnerTransactionId;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "CUSTODY_ID")
  private String custodyId;
  @Column(name = "STOCK_SUB_ACCOUNT_NUMBER")
  private String stockSubAccountNumber;
  @Column(name = "VALIDATE_STATUS")
  private String validateStatus;
  @Column(name = "PARTNER_STATUS")
  private String partnerStatus;
  @Column(name = "RECONCILE_STATUS")
  private String reconcileStatus;
  @Column(name = "PROPERTIES")
  private String properties;
  @Column(name = "NOTE")
  private String note;
  @Column(name = "CREATED_TIME")
  private Timestamp createdTime;
  @Column(name = "UPDATED_TIME")
  private Timestamp updatedTime;

  public static List<IsociasTxnReconcile> getWithParam(String partnerType, String paymentType, String reconcileDate,
                                                       String reconcileStatus, String transactionId, String partnerTransactionId) {
    Query<IsociasTxnReconcile> query = HthDb.h2hConnection.getSession().createQuery(
      "from IsociasTxnReconcile where partnerType=:partnerType and TO_CHAR(createdTime, 'DD/MM/YYYY')=:createdTime " +
        (StringUtils.isNotEmpty(paymentType) ? " and paymentType=:paymentType " : "") +
        (StringUtils.isNotEmpty(reconcileStatus) ? " and reconcileStatus in (:reconcileStatus) " : "") +
        (StringUtils.isNotEmpty(transactionId) ? " and transactionId=:transactionId " : "") +
        (StringUtils.isNotEmpty(partnerTransactionId) ? " and partnerTransactionId=:partnerTransactionId " : "")
      , IsociasTxnReconcile.class);

    query.setParameter("partnerType", partnerType);
    query.setParameter("createdTime", reconcileDate);
    if (StringUtils.isNotEmpty(paymentType)) {
      query.setParameter(PAYMENT_TYPE, paymentType);
    }
    if (StringUtils.isNotEmpty(reconcileStatus)) {
      query.setParameter("reconcileStatus", Arrays.asList(reconcileStatus.split(",")));
    }
    if (StringUtils.isNotEmpty(transactionId)) {
      query.setParameter(TRANSACTION_ID, transactionId);
    }
    if (StringUtils.isNotEmpty(partnerTransactionId)) {
      query.setParameter(PARTNER_TRANSACTION_ID, partnerTransactionId);
    }

    return query.getResultList();
  }

  /**
   * Author Lybtk
   */
  public static IsociasTxnReconcile getFromId(Long id) {
    try {
      h2hConnection.getSession().clear();
      Query<IsociasTxnReconcile> query = h2hConnection.getSession().createQuery(
        "from IsociasTxnReconcile where id=:Id",
        IsociasTxnReconcile.class
      );
      query.setParameter("Id", BigDecimal.valueOf(id));
      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  /**
   * Author Lybtk
   */
  public static void updateReconcileStatus(Long id, String reconcileStatus) {
    Session session = HthDb.h2hConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = session.createQuery(
      "update IsociasTxnReconcile set reconcileStatus=:reconcileStatus where id=:id ");
    query.setParameter("reconcileStatus", reconcileStatus);
    query.setParameter("id", BigDecimal.valueOf(id));
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static List<IsociasTxnReconcile> getFromTransactionId(String paymentType, String transactionId) {
    try {
      h2hConnection.getSession().clear();
      Query<IsociasTxnReconcile> query = h2hConnection.getSession().createQuery(
        "from IsociasTxnReconcile where paymentType=:paymentType " +
          "AND transactionId like :transactionId order by transactionId asc",
        IsociasTxnReconcile.class
      );

      query.setParameter(PAYMENT_TYPE, paymentType);
      query.setParameter(TRANSACTION_ID, transactionId + "%");
      return query.getResultList();
    } catch (NoResultException e) {
      return null;
    }
  }

  public static List<IsociasTxnReconcile> getFromPartnerTransactionId(String paymentType, String partnerTransactionId) {
    try {
      h2hConnection.getSession().clear();
      Query<IsociasTxnReconcile> query = h2hConnection.getSession().createQuery(
        "from IsociasTxnReconcile where paymentType=:paymentType " +
          "AND partnerTransactionId like :partnerTransactionId order by partnerTransactionId asc",
        IsociasTxnReconcile.class
      );

      query.setParameter(PAYMENT_TYPE, paymentType);
      query.setParameter(PARTNER_TRANSACTION_ID, partnerTransactionId + "%");
      return query.getResultList();
    } catch (NoResultException e) {
      return null;
    }
  }

  public static void deleteReconcileByTransactionId(String transactionId) {
    Session session = h2hConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<?> query = session.createNativeQuery(" DELETE from ISOCIAS_TXN_RECONCILE " +
      " where TRANSACTION_ID like :transactionId");
    query.setParameter("transactionId", transactionId + "%");

    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static void deleteReconcileByPartnerTransactionId(String partnerTransactionId) {
    Session session = h2hConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<?> query = session.createNativeQuery(" DELETE from ISOCIAS_TXN_RECONCILE " +
      " where PARTNER_TRANSACTION_ID like :partnerTransactionId");
    query.setParameter(PARTNER_TRANSACTION_ID, partnerTransactionId + "%");

    query.executeUpdate();
    session.getTransaction().commit();
  }
}
