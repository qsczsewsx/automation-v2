package com.tcbs.automation.hth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import static com.tcbs.automation.hth.HthDb.h2hConnection;

@Entity
@Table(name = "SUB_ACCOUNT_TRANSACTION")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubAccountTransaction {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @NotNull
  @Column(name = "SUB_ACCOUNT_NO")
  private String subAccountNo;
  @Column(name = "CUSTODY_CODE")
  private String custodyCode;
  @NotNull
  @Column(name = "ACTION")
  private String action;
  @Column(name = "INTERACTED_ACCOUNT_NO")
  private String interactedAccountNo;
  @Column(name = "AMOUNT")
  private BigDecimal amount;
  @Column(name = "CLIENT_ID")
  private String clientId;
  @Column(name = "CLIENT_TRANSACTION_ID")
  private String clientTransactionId;
  @Column(name = "CLIENT_BATCH_ID")
  private String clientBatchId;
  @Column(name = "PROCESSED_TRANSACTION_ID")
  private String processedTransactionId;
  @Column(name = "PROCESSED_BATCH_ID")
  private String processedBatchId;
  @Column(name = "REFERENCE_ID")
  private String referenceId;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "CURRENCY")
  private String currency;
  @Column(name = "TRANSACTION_DATE")
  private Timestamp transactionDate;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "MAKER")
  private String maker;
  @Column(name = "CLASSIFY")
  private String classify;
  @Column(name = "MARK")
  private String mark;
  @Column(name = "CREATED_TIME")
  private Timestamp createdTime;
  @Column(name = "UPDATED_TIME")
  private Timestamp updatedTime;
  @Column(name = "REFERENCE_TYPE")
  private String referenceType;
  @Column(name = "BANK_TRANSACTION_ID")
  private String bankTransactionId;

  /**
   * Author Lybtk
   */
  public static SubAccountTransaction getFromTxnidAndClientId(String txnId, String clientId) {
    try {
      h2hConnection.getSession().clear();
      org.hibernate.query.Query<SubAccountTransaction> query = h2hConnection.getSession().createQuery(
        "from SubAccountTransaction where clientId=:clientId and clientTransactionId=:txnId and mark='ACTIVE'",
        SubAccountTransaction.class
      );
      query.setParameter("clientId", clientId)
        .setParameter("txnId", txnId);

      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  /**
   * Author Lybtk
   */
  public static Long getTotalInquiryTransactionForCutPaymentFee(String txnIds, String clientId,
                                                                String custodyCode, String stockSubAccountNumber, String status,
                                                                String from, String to) {
    Query<Long> query = HthDb.h2hConnection.getSession().createQuery(
      "select count (*) from SubAccountTransaction where clientTransactionId!=null " +
        (StringUtils.isNotEmpty(txnIds) ? "and clientTransactionId =:txnIds " : " ") +
        (StringUtils.isNotEmpty(clientId) ? "and clientId =:clientId " : "") +
        (StringUtils.isNotEmpty(custodyCode) ? "and custodyCode =:custodyCode " : "") +
        (StringUtils.isNotEmpty(stockSubAccountNumber) ? "and subAccountNo =:stockSubAccountNumber " : "") +
        (StringUtils.isNotEmpty(status) ? "and status =:status " : "") +
        (StringUtils.isNotEmpty(from) ? "and transactionDate >=:from " : "") +
        (StringUtils.isNotEmpty(to) ? "and transactionDate <=:to " : "")
    );
    setParameter(query, txnIds, clientId, custodyCode, stockSubAccountNumber, status,
      from, to);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  private static void setParameter(Query<Long> query, String txnIds, String clientId, String custodyCode,
                                   String stockSubAccountNumber,
                                   String status, String from, String to) {

    if (StringUtils.isNotEmpty(txnIds)) {
      query.setParameter("txnIds", txnIds);
    }
    if (StringUtils.isNotEmpty(clientId)) {
      query.setParameter("clientId", clientId);
    }
    if (StringUtils.isNotEmpty(custodyCode)) {
      query.setParameter("custodyCode", custodyCode);
    }
    if (StringUtils.isNotEmpty(stockSubAccountNumber)) {
      query.setParameter("stockSubAccountNumber", stockSubAccountNumber);
    }
    if (StringUtils.isNotEmpty(status)) {
      query.setParameter("status", status);
    }
    if (StringUtils.isNotEmpty(from)) {
      query.setParameter("from", Timestamp.valueOf(from + " 00:00:00"));
    }
    if (StringUtils.isNotEmpty(to)) {
      query.setParameter("to", Timestamp.valueOf(to + " 23:59:59"));
    }
  }

  public static SubAccountTransaction getFromBankTransactionId(String bankTransactionId) {
    try {
      h2hConnection.getSession().clear();
      Query<SubAccountTransaction> query = h2hConnection.getSession().createQuery(
        "from SubAccountTransaction where bankTransactionId=:bankTransactionId",
        SubAccountTransaction.class
      );
      query.setParameter("bankTransactionId", bankTransactionId);

      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  /**
   * Author Lybtk
   */
  public static SubAccountTransaction getFromTxnId(String clientId, String clientTransactionId) {
    try {
      h2hConnection.getSession().clear();
      Query<SubAccountTransaction> query = h2hConnection.getSession().createQuery(
        "from SubAccountTransaction where clientId=:clientId and clientTransactionId=:clientTransactionId",
        SubAccountTransaction.class
      );
      query.setParameter("clientId", clientId)
        .setParameter("clientTransactionId", clientTransactionId);
      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  /**
   * Author Lybtk
   */
  public static SubAccountTransaction getFromProcessing(String clientId, String processedTransactionId) {
    try {
      h2hConnection.getSession().clear();
      Query<SubAccountTransaction> query = h2hConnection.getSession().createQuery(
        "from SubAccountTransaction where clientId=:clientId and processedTransactionId=:processedTransactionId",
        SubAccountTransaction.class
      );
      query.setParameter("clientId", clientId)
        .setParameter("processedTransactionId", processedTransactionId);
      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  /**
   * Author Lybtk
   */
  public static Long getTotalInquiryTransactionForFlowExtenalFutureAndCapitalMarket(String txnId, String clientId,
                                                                                    String custodyCode, String sourceAccountNumber, String destinationAccountNumber, String status,
                                                                                    String from, String to) {
    Query<Long> query = HthDb.h2hConnection.getSession().createQuery(
      "select count (*) from SubAccountTransaction where clientTransactionId!=null " +
        (StringUtils.isNotEmpty(txnId) ? "and clientTransactionId =:txnId " : " ") +
        (StringUtils.isNotEmpty(clientId) ? "and clientId =:clientId " : "") +
        (StringUtils.isNotEmpty(custodyCode) ? "and custodyCode =:custodyCode " : "") +
        (StringUtils.isNotEmpty(sourceAccountNumber) ? "and subAccountNo =:sourceAccountNumber " : "") +
        (StringUtils.isNotEmpty(status) ? "and status =:status " : "") +
        (StringUtils.isNotEmpty(destinationAccountNumber) ? "and interactedAccountNo =:destinationAccountNumber " : "") +
        (StringUtils.isNotEmpty(from) ? "and transactionDate >=:from " : "") +
        (StringUtils.isNotEmpty(to) ? "and transactionDate <=:to " : "")
    );
    setParameter(query, txnId, clientId, custodyCode, sourceAccountNumber, destinationAccountNumber, status,
      from, to);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  private static void setParameter(Query<Long> query, String txnId, String clientId, String custodyCode,
                                   String sourceAccountNumber, String destinationAccountNumber,
                                   String status, String from, String to) {

    if (StringUtils.isNotEmpty(txnId)) {
      query.setParameter("txnId", txnId);
    }
    if (StringUtils.isNotEmpty(clientId)) {
      query.setParameter("clientId", clientId);
    }
    if (StringUtils.isNotEmpty(custodyCode)) {
      query.setParameter("custodyCode", custodyCode);
    }
    if (StringUtils.isNotEmpty(sourceAccountNumber)) {
      query.setParameter("sourceAccountNumber", sourceAccountNumber);
    }
    if (StringUtils.isNotEmpty(destinationAccountNumber)) {
      query.setParameter("destinationAccountNumber", destinationAccountNumber);
    }
    if (StringUtils.isNotEmpty(status)) {
      query.setParameter("status", status);
    }
    if (StringUtils.isNotEmpty(from)) {
      query.setParameter("from", Timestamp.valueOf(from + " 00:00:00"));
    }
    if (StringUtils.isNotEmpty(to)) {
      query.setParameter("to", Timestamp.valueOf(to + " 23:59:59"));
    }
  }

  public static List<SubAccountTransaction> getListTransactionByParam(String custodyCode, String clientId, String batchId, List<String> listTransactionIds, String status, String from, String to, Long size) {
    try {
      h2hConnection.getSession().clear();
      String queryStr = "SELECT * FROM (SELECT *" +
        "               FROM SUB_ACCOUNT_TRANSACTION" +
        "               ORDER BY ID asc)" +
        "WHERE CLIENT_ID =:clientId " +
        (StringUtils.isNotEmpty(custodyCode) ? "and CUSTODY_CODE =:custodyCode " : "") +
        (StringUtils.isNotEmpty(batchId) ? "and CLIENT_BATCH_ID=:batchId " : "") +
        (listTransactionIds != null ? "and CLIENT_TRANSACTION_ID IN :listTransactionIds " : "") +
        (StringUtils.isNotEmpty(status) ? "and STATUS =:status " : "") +
        (StringUtils.isNotEmpty(from) ? "and TRANSACTION_DATE>=:from " : "") +
        (StringUtils.isNotEmpty(to) ? "and TRANSACTION_DATE<=:to " : "") +
        "and MARK = 'ACTIVE' " +
        "and ROWNUM<=:size ";
      Query<SubAccountTransaction> query = h2hConnection.getSession().createNativeQuery(
        queryStr, SubAccountTransaction.class
      );
      query.setParameter("clientId", clientId);
      if (StringUtils.isNotEmpty(custodyCode)) {
        query.setParameter("custodyCode", custodyCode);
      }
      if (StringUtils.isNotEmpty(batchId)) {
        query.setParameter("batchId", batchId);
      }
      if (listTransactionIds != null) {
        query.setParameter("listTransactionIds", listTransactionIds);
      }
      if (StringUtils.isNotEmpty(status)) {
        query.setParameter("status", status);
      }
      if (StringUtils.isNotEmpty(from)) {
        query.setParameter("from", Timestamp.valueOf(from + " 00:00:00"));
      }
      if (StringUtils.isNotEmpty(to)) {
        query.setParameter("to", Timestamp.valueOf(to + " 23:59:59"));
      }
      query.setParameter("size", size);
      return query.getResultList();
    } catch (NoResultException e) {
      System.out.println("error : " + e.getMessage());
      return null;
    }
  }

  public static List<SubAccountTransaction> getFromClientBatchId(String clientId, String clientBatchId) {
    try {
      h2hConnection.getSession().clear();
      Query<SubAccountTransaction> query = h2hConnection.getSession().createQuery(
        "from SubAccountTransaction where clientId=:clientId " +
          "and clientBatchId=:clientBatchId " +
          "order by processedTransactionId asc",
        SubAccountTransaction.class
      );
      query.setParameter("clientId", clientId)
        .setParameter("clientBatchId", clientBatchId);
      return query.getResultList();
    } catch (NoResultException e) {
      return null;
    }
  }

  public static void updateMarkByClientBatchId(String clientBatchId) {
    Session session = HthDb.h2hConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<?> query = session.createQuery(
      "update SubAccountTransaction SET mark='ACTIVE' where clientBatchId =:clientBatchId"
    );
    query.setParameter("clientBatchId", clientBatchId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static void deleteByClientTransactionId(String clientTransactionId) {
    Session session = h2hConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<?> query = session.createNativeQuery("delete from SUB_ACCOUNT_TRANSACTION " +
      "where STATUS = 'SUCCESS' and CLIENT_TRANSACTION_ID =:clientTransactionId");
    query.setParameter("clientTransactionId", clientTransactionId);

    query.executeUpdate();
    session.getTransaction().commit();
  }

  /**
   * Author Lybtk
   */
  public static List<SubAccountTransaction> getListFromTransCode(String clientTransactionId) {
    try {
      h2hConnection.getSession().clear();
      Query<SubAccountTransaction> query = h2hConnection.getSession().createQuery(
        "from SubAccountTransaction where clientTransactionId =:clientTransactionId  order by id desc",
        SubAccountTransaction.class
      );
      query.setParameter("clientTransactionId", clientTransactionId);
      return query.getResultList();
    } catch (NoResultException e) {
      return null;
    }
  }

  /**
   * Author Lybtk
   */
  public static List<SubAccountTransaction> getFromReferenceId(String referenceId) {
    try {
      h2hConnection.getSession().clear();
      Query<SubAccountTransaction> query = h2hConnection.getSession().createQuery(
        "from SubAccountTransaction where referenceId=:referenceId order by id desc",
        SubAccountTransaction.class
      );
      query.setParameter("referenceId", referenceId);
      return query.getResultList();
    } catch (NoResultException e) {
      return null;
    }
  }


  public static SubAccountTransaction getByReferenceId(String referenceId, String action) {
    try {
      h2hConnection.getSession().clear();
      Query<SubAccountTransaction> query = h2hConnection.getSession().createQuery(
        "from SubAccountTransaction where referenceId =:referenceId and action =:action ",
        SubAccountTransaction.class
      );
      query.setParameter("referenceId", referenceId);
      query.setParameter("action", action);
      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  /**
   * Author Lybtk
   */
  public static Long getTotalInquiryTransactionForInquiryList(String txnIds, String clientId,
                                                              String custodyCode, String subAccountNo, String status,
                                                              String mark, String processId, String from, String to) {
    Query<Long> query = HthDb.h2hConnection.getSession().createQuery(
      "select count (*) from SubAccountTransaction where id != null " +
        (StringUtils.isNotEmpty(txnIds) ? "and clientTransactionId =:txnIds " : " ") +
        (StringUtils.isNotEmpty(clientId) ? "and clientId =:clientId " : "") +
        (StringUtils.isNotEmpty(custodyCode) ? "and custodyCode =:custodyCode " : "") +
        (StringUtils.isNotEmpty(subAccountNo) ? "and subAccountNo =:subAccountNo " : "") +
        (StringUtils.isNotEmpty(mark) ? "and mark =:mark " : "") +
        (StringUtils.isNotEmpty(processId) ? "and processedTransactionId =:processId " : "") +
        (StringUtils.isNotEmpty(status) ? "and status =:status " : "") +
        (StringUtils.isNotEmpty(from) ? "and transactionDate >=:from " : "") +
        (StringUtils.isNotEmpty(to) ? "and transactionDate <=:to " : "")
    );
    setParameter3(query, txnIds, clientId, custodyCode, subAccountNo, mark, processId, status,
      from, to);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  private static void setParameter3(Query<Long> query, String txnIds, String clientId, String custodyCode,
                                    String subAccountNo, String mark, String processId,
                                    String status, String from, String to) {

    if (StringUtils.isNotEmpty(txnIds)) {
      query.setParameter("txnIds", txnIds);
    }
    if (StringUtils.isNotEmpty(clientId)) {
      query.setParameter("clientId", clientId);
    }
    if (StringUtils.isNotEmpty(custodyCode)) {
      query.setParameter("custodyCode", custodyCode);
    }
    if (StringUtils.isNotEmpty(mark)) {
      query.setParameter("mark", mark);
    }
    if (StringUtils.isNotEmpty(processId)) {
      query.setParameter("processId", processId);
    }
    if (StringUtils.isNotEmpty(subAccountNo)) {
      query.setParameter("subAccountNo", subAccountNo);
    }
    if (StringUtils.isNotEmpty(status)) {
      query.setParameter("status", status);
    }
    if (StringUtils.isNotEmpty(from)) {
      query.setParameter("from", Timestamp.valueOf(from + " 00:00:00"));
    }
    if (StringUtils.isNotEmpty(to)) {
      query.setParameter("to", Timestamp.valueOf(to + " 23:59:59"));
    }
  }

  /**
   * Author Lybtk
   */
  public static Long getTotalInquiryTransactionForIncreaseDecreaseISave(String txnIds, String clientId,
                                                                        String action, String status,
                                                                        String from, String to, String custodyCode,
                                                                        String subAccountNumber, String batchId) {
    Query<Long> query = HthDb.h2hConnection.getSession().createQuery(
      "select count (*) from SubAccountTransaction where clientTransactionId!=null " +
        (StringUtils.isNotEmpty(txnIds) ? "and clientTransactionId =:txnIds " : " ") +
        (StringUtils.isNotEmpty(clientId) ? "and clientId =:clientId " : "") +
        (StringUtils.isNotEmpty(action) ? "and action =:action " : "") +
        (StringUtils.isNotEmpty(status) ? "and status =:status " : "") +
        (StringUtils.isNotEmpty(custodyCode) ? "and custodyCode =:custodyCode " : "") +
        (StringUtils.isNotEmpty(subAccountNumber) ? "and subAccountNo =:subAccountNumber " : "") +
        (StringUtils.isNotEmpty(batchId) ? "and clientBatchId =:batchId " : "") +
        (StringUtils.isNotEmpty(from) ? "and transactionDate >=:from " : "") +
        (StringUtils.isNotEmpty(to) ? "and transactionDate <=:to " : "")
    );
    setParameter2(query, txnIds, clientId, action, custodyCode, subAccountNumber, batchId, status,
      from, to);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  private static void setParameter2(Query<Long> query, String txnIds, String clientId, String action, String custodyCode,
                                    String subAccountNumber, String batchId, String status, String from, String to) {

    if (StringUtils.isNotEmpty(txnIds)) {
      query.setParameter("txnIds", txnIds);
    }
    if (StringUtils.isNotEmpty(clientId)) {
      query.setParameter("clientId", clientId);
    }
    if (StringUtils.isNotEmpty(action)) {
      query.setParameter("action", action);
    }
    if (StringUtils.isNotEmpty(custodyCode)) {
      query.setParameter("custodyCode", custodyCode);
    }
    if (StringUtils.isNotEmpty(subAccountNumber)) {
      query.setParameter("subAccountNumber", subAccountNumber);
    }
    if (StringUtils.isNotEmpty(batchId)) {
      query.setParameter("batchId", batchId);
    }
    if (StringUtils.isNotEmpty(status)) {
      query.setParameter("status", status);
    }
    if (StringUtils.isNotEmpty(from)) {
      query.setParameter("from", Timestamp.valueOf(from + " 00:00:00"));
    }
    if (StringUtils.isNotEmpty(to)) {
      query.setParameter("to", Timestamp.valueOf(to + " 23:59:59"));
    }
  }

  /**
   * Author Lybtk
   */
  public static List<SubAccountTransaction> getListFromISave(String status, String action) {
    try {
      h2hConnection.getSession().clear();
      Query<SubAccountTransaction> query = h2hConnection.getSession().createQuery(
        "from SubAccountTransaction where status =:status and action =: action order by id desc"
        , SubAccountTransaction.class
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
  public static void updateTransactionStatus(String processedTransactionId, String status) {
    Session session = HthDb.h2hConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = session.createQuery(
      "update SubAccountTransaction set status=:status where processedTransactionId =:processedTransactionId ");
    query.setParameter("status", status);
    query.setParameter("processedTransactionId", processedTransactionId);
    query.executeUpdate();
    session.getTransaction().commit();
  }
}
