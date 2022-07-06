package com.tcbs.automation.hth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.protocol.types.Field;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static com.tcbs.automation.hth.HthDb.h2hConnection;

@Entity
@Table(name = "COLLECTION_TRANS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CollectionTrans {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_TXN_RECONCILE_GEN")
  @SequenceGenerator(sequenceName = "STOCK_TXN_RECONCILE_SEQ", allocationSize = 1, name = "STOCK_TXN_RECONCILE_GEN")
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @NotNull
  @Column(name = "TRANSACTION_ID")
  private String transactionId;
  @NotNull
  @Column(name = "SOURCE_ACCOUNT_NUMBER")
  private String sourceAccountNumber;
  @Column(name = "SOURCE_ACCOUNT_NAME")
  private String sourceAccountName;
  @Column(name = "STOCK_ACCOUNT_NUMBER")
  private String stockAccountNumber;
  @Column(name = "STOCK_ACCOUNT_NAME")
  private String stockAccountName;
  @Column(name = "STOCK_SUB_ACCOUNT_NUMBER")
  private String stockSubAccountNumber;
  @NotNull
  @Column(name = "DESTINATION_ACCOUNT_NUMBER")
  private String destinationAccountNumber;
  @NotNull
  @Column(name = "DESTINATION_ACCOUNT_NAME")
  private String destinationAccountName;
  @NotNull
  @Column(name = "AMOUNT")
  private BigDecimal amount;
  @Column(name = "FEE")
  private BigDecimal fee;
  @Column(name = "DESCRIPTION")
  private String description;
  @NotNull
  @Column(name = "TRANSACTION_DATE")
  private Timestamp transactionDate;
  @Column(name = "BRANCH_NAME")
  private String branchName;
  @Column(name = "BRANCH_CODE")
  private String branchCode;
  @Column(name = "BANK_NAME")
  private String bankName;
  @Column(name = "BANK_CODE")
  private String bankCode;
  @Column(name = "REFERENCE_ID")
  private String referenceId;
  @Column(name = "ADDITIONAL_INFORMATION")
  private String additionalInformation;
  @NotNull
  @Column(name = "PAYMENT_TYPE")
  private String paymentType;
  @Column(name = "CHANNEL_TYPE")
  private String channelType;
  @Column(name = "SOURCE")
  private String source;
  @Column(name = "SOURCE_TYPE")
  private String sourceType;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "STOCK_TYPE")
  private String stockType;
  @Column(name = "COLLECTION_TYPE ")
  private String collectionType;
  @Column(name = "DETAIL")
  private String detail;
  @Column(name = "CREATED_TIME")
  private Timestamp createdTime;
  @Column(name = "UPDATED_TIME")
  private Timestamp updatedTime;
  @Column(name = "REFUND_TIME")
  private Timestamp refundTime;
  @Column(name = "REFUND_TRANSACTION_ID")
  private String refundTransactionId;
  @Column(name = "ACCOUNTING_TIME")
  private String accountingTime;
  @Transient
  private String coboType;
  @Transient
  private String loanPayBackAccountNumber;
  @Transient
  private String loanPayBackAccountName;
  @Transient
  private String foreign;
  @Transient
  private String key;
  @Transient
  private String comparison;

  public static CollectionTrans getByTxnId(String txnId) {
    h2hConnection.getSession().clear();
    Query<CollectionTrans> query = HthDb.h2hConnection.getSession().createQuery(
      "from CollectionTrans where transactionId=:txnId",
      CollectionTrans.class
    );
    query.setParameter("txnId", txnId);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  public static Long countNumberElementByAttr(String transactionId, String sourceAccountNumber, String stockAccountNumber,
                                              String stockSubAccountNumber, String paymentType, String sourceType, String stockType,
                                              String status, String from, String to) {
    Query<Long> query = HthDb.h2hConnection.getSession().createQuery(
      "select count(*) from CollectionTrans where sourceAccountNumber=:sourceAccountNumber" +
        (StringUtils.isNotEmpty(transactionId) ? " and transactionId=:transactionId " : "") +
        (StringUtils.isNotEmpty(stockAccountNumber) ? " and stockAccountNumber=:stockAccountNumber " : "") +
        (StringUtils.isNotEmpty(stockSubAccountNumber) ? " and stockSubAccountNumber=:stockSubAccountNumber " : "") +
        (StringUtils.isNotEmpty(paymentType) ? " and paymentType=:paymentType " : "") +
        (StringUtils.isNotEmpty(sourceType) ? " and sourceType=:sourceType " : "") +
        (StringUtils.isNotEmpty(stockType) ? " and stockType=:stockType " : "") +
        (StringUtils.isNotEmpty(status) ? " and status=:status " : "") +
        (StringUtils.isNotEmpty(from) ? " and transactionDate >= :from " : "") +
        (StringUtils.isNotEmpty(to) ? " and transactionDate <= :to " : ""),
      Long.class
    );

    query.setParameter("sourceAccountNumber", sourceAccountNumber);
    setParameters(query, transactionId, stockAccountNumber,
      stockSubAccountNumber, paymentType, sourceType, stockType,
      status, from, to);

    return query.getSingleResult();
  }

  private static void setParameters(Query query, String transactionId, String stockAccountNumber,
                                    String stockSubAccountNumber, String paymentType, String sourceType, String stockType,
                                    String status, String from, String to) {
    if (StringUtils.isNotEmpty(transactionId)) {
      query.setParameter("transactionId", transactionId);
    }
    if (StringUtils.isNotEmpty(stockAccountNumber)) {
      query.setParameter("stockAccountNumber", stockAccountNumber);
    }
    if (StringUtils.isNotEmpty(stockSubAccountNumber)) {
      query.setParameter("stockSubAccountNumber", stockSubAccountNumber);
    }
    if (StringUtils.isNotEmpty(paymentType)) {
      query.setParameter("paymentType", paymentType);
    }
    if (StringUtils.isNotEmpty(sourceType)) {
      query.setParameter("sourceType", sourceType);
    }
    if (StringUtils.isNotEmpty(stockType)) {
      query.setParameter("stockType", stockType);
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
  public static void deleteByParternId(String parternId) {
    Session session = HthDb.h2hConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    javax.persistence.Query query = session.createQuery(
      "delete from CollectionTrans where transactionId=:parternId");
    query
      .setParameter("parternId", parternId)
      .executeUpdate();
    session.getTransaction().commit();
  }

  public static void deleteStockTxnReconcileByDesAccountName(String desAccountName) {
    Session session = h2hConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<?> query = session.createNativeQuery(" DELETE from COLLECTION_TRANS " +
      " where DESTINATION_ACCOUNT_NAME = ?");
    query.setParameter(1, desAccountName);

    query.executeUpdate();
    session.getTransaction().commit();
  }

  /****
   *** Author Thuynt53
   **/
  public static List<CollectionTrans> getCollectionTransaction(String source, String sourceAccountNumber, String collectionType, String stockType,
                                                               String destinationAccountNumber, String destinationAccountName, String transactionId,
                                                               Long amount, String stockAccountNumber, String status, String refundTransactionId, String sourceType, String from, String to) {
    Query<CollectionTrans> query;
    query = h2hConnection.getSession().createQuery(

      "FROM CollectionTrans WHERE source is not null " +
        (StringUtils.isNotEmpty(source) ? " and source =:source " : "") +
        (StringUtils.isNotEmpty(sourceAccountNumber) ? " and sourceAccountNumber =:sourceAccountNumber " : "") +
        (StringUtils.isNotEmpty(collectionType) ? " and collectionType IN :listTrans " : "") +
        (stockType != null ? " and stockType=:stockType " : "") +
        (StringUtils.isNotEmpty(destinationAccountNumber) ? " and destinationAccountNumber=:destinationAccountNumber " : "") +
        (StringUtils.isNotEmpty(destinationAccountName) ? " and destinationAccountName=:destinationAccountName " : "") +
        (StringUtils.isNotEmpty(transactionId) ? " and transactionId=:transactionId " : "") +
        (amount != null ? " and amount=:amount " : "") +
        (StringUtils.isNotEmpty(stockAccountNumber) ? " and stockAccountNumber=:stockAccountNumber " : "") +
        (status == null ? " and status IN :listStatus " : "") +
        (status != null ? " and status IN :status " : "") +
        (StringUtils.isNotEmpty(refundTransactionId) ? " and refundTransactionId=:refundTransactionId " : "") +
        (StringUtils.isNotEmpty(sourceType) ? " and sourceType=:sourceType " : "") +
        (StringUtils.isNotEmpty(from) ? " and transactionDate >= :from " : "") +
        (StringUtils.isNotEmpty(to) ? " and transactionDate <= :to " : "") +
        " ORDER BY transactionDate,id DESC",
      CollectionTrans.class

    );
    setParameter(query, source, sourceAccountNumber, collectionType, stockType, destinationAccountNumber,
      destinationAccountName, transactionId, amount, stockAccountNumber, status, refundTransactionId, sourceType, from, to);
    return query.getResultList();
  }

  private static void setParameter(Query query, String source, String sourceAccountNumber, String collectionType, String stockType, String destinationAccountNumber,
                                   String destinationAccountName, String transactionId, Long amount, String stockAccountNumber,
                                   String status, String refundTransactionId, String sourceType, String from, String to) {

    if (StringUtils.isNotEmpty(source)) {
      query.setParameter("source", source);
    }
    if (StringUtils.isNotEmpty(sourceAccountNumber)) {
      query.setParameter("sourceAccountNumber", sourceAccountNumber);
    }

    if (collectionType != null) {
      String[] arrayCollection = {"NORMAL", "DERIVATIVE", "MARGIN", "FOREIGN", "OTHER", "BOND_LOAN"};
      String[] arrayList = {"LIST"};
      List<String> listTrans;
      if (collectionType.equals("COLLECTION")) {
        listTrans = Arrays.asList(arrayCollection);
        query.setParameter("listTrans", listTrans);
      } else {
        query.setParameter("listTrans", collectionType);
      }
    }
    if (StringUtils.isNotEmpty(stockType)) {
      query.setParameter("stockType", stockType);
    }
    if (StringUtils.isNotEmpty(destinationAccountNumber)) {
      query.setParameter("destinationAccountNumber", destinationAccountNumber);
    }
    if (StringUtils.isNotEmpty(destinationAccountName)) {
      query.setParameter("destinationAccountName", destinationAccountName);
    }
    if (StringUtils.isNotEmpty(transactionId)) {
      query.setParameter("transactionId", transactionId);
    }
    if (amount != null) {
      query.setParameter("amount", BigDecimal.valueOf(amount));
    }
    if (StringUtils.isNotEmpty(stockAccountNumber)) {
      query.setParameter("stockAccountNumber", stockAccountNumber);
    }
    String[] arrayStatus = {"DONE", "ERROR", "IDENTIFIED", "UNIDENTIFIED_ACCOUNT",
      "UNIDENTIFIED_DESCRIPTION", "PUSHED", "REFUND",
      "WAITING_REFUND", "WAITING_APPROVE", "IDENTIFIED_ACC_NOT_ACTIVE"};
    if (status == null) {
      List<String> listStatus = Arrays.asList(arrayStatus);
      query.setParameter("listStatus", listStatus);
    } else {
      String[] arrayError = {"IDENTIFIED", "ERROR"};
      if (status.equals("IDENTIFIED") || status.equals("ERROR")) {
        List<String> listError = Arrays.asList(arrayError);
        query.setParameter("status", listError);
      }
      query.setParameter("status", status);
    }
    if (StringUtils.isNotEmpty(refundTransactionId)) {
      query.setParameter("refundTransactionId", refundTransactionId);
    }
    if (StringUtils.isNotEmpty(sourceType)) {
      query.setParameter("sourceType", sourceType);
    }
    if (StringUtils.isNotEmpty(from)) {
      query.setParameter("from", Timestamp.valueOf(from + " 00:00:00"));
    }
    if (StringUtils.isNotEmpty(to)) {
      query.setParameter("to", Timestamp.valueOf(to + " 23:59:59"));
    }
  }

  /****
   *** Author Thompt
   **/
  public static CollectionTrans getById(BigDecimal id) {
    h2hConnection.getSession().clear();
    Query<CollectionTrans> query = HthDb.h2hConnection.getSession().createQuery(
      "from CollectionTrans where id=:id",
      CollectionTrans.class
    );
    query.setParameter("id", id);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  // Update status
  public static void updateStatus(String status, BigDecimal id) {
    Session session = HthDb.h2hConnection.getSession();
    session.beginTransaction();
    javax.persistence.Query query = session.createQuery(
      "update CollectionTrans set status=:status where id=:id"
    );
    query.setParameter("status", status)
      .setParameter("id", id)
      .executeUpdate();
    session.getTransaction().commit();
  }

  public static List<CollectionTrans> getListWithStatus(String status, String source, String stockAccountName, String stockType) {
    try {
      h2hConnection.getSession().clear();
      Query<CollectionTrans> query = h2hConnection.getSession().createQuery(
        "from CollectionTrans where status=:status " +
          (StringUtils.isNotEmpty(source) ? " and source =:source " : "") +
          (StringUtils.isNotEmpty(stockAccountName) ? " and stockAccountName =:stockAccountName " : "") +
          (StringUtils.isNotEmpty(stockType) ? " and stockType =:stockType " : "") +
          " and collectionType NOT IN ('FOREIGN','LIST') " +
          " order by createdTime desc ",

        CollectionTrans.class
      );
      query.setParameter("status", status);
      if (StringUtils.isNotEmpty(source)) {
        query.setParameter("source", source);
      }
      if (StringUtils.isNotEmpty(stockAccountName)) {
        query.setParameter("stockAccountName", stockAccountName);
      }
      if (StringUtils.isNotEmpty(stockType)) {
        query.setParameter("stockType", stockType);
      }
      return query.getResultList();
    } catch (NoResultException e) {
      return null;
    }
  }

  /**
   * Author Lybtk
   */

  public static List<CollectionTrans> getListFromStatus(String collectionType, String status) {
    try {
      h2hConnection.getSession().clear();
      Query<CollectionTrans> query = h2hConnection.getSession().createQuery(
        "from CollectionTrans where collectionType=:collectionType and status=:status order by id desc",
        CollectionTrans.class
      );
      query.setParameter("collectionType", collectionType)
        .setParameter("status", status);
      return query.getResultList();
    } catch (NoResultException e) {
      return null;
    }
  }

  /**
   * Author Lybtk
   */
  public static void updateTransactionStatus(String transactionId, String status) {
    Session session = HthDb.h2hConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = session.createQuery(
      "update CollectionTrans set status=:status where transactionId=:transactionId ");
    query.setParameter("status", status);
    query.setParameter("transactionId", transactionId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static CollectionTrans getByStatus(String stockType) {

    String queryStr = "SELECT * FROM COLLECTION_TRANS CT FULL OUTER JOIN SUB_ACCOUNT_TRANSACTION_LOG SATL" +
      " ON CT.TRANSACTION_ID = SATL.BANK_TRANSACTION_ID " +
      " WHERE CT.STATUS IN ('ERROR','IDENTIFIED') AND CT.SOURCE_TYPE = 'INDIRECT' " +
      (StringUtils.isNotEmpty(stockType) ? " and CT.STOCK_TYPE =:stockType " : "") +
      " AND ROWNUM = 1";

    Query<CollectionTrans> query = h2hConnection.getSession().createNativeQuery(
      queryStr, CollectionTrans.class
    );

    if (StringUtils.isNotEmpty(stockType)) {
      query.setParameter("stockType", stockType);
    }
    try {
      query.getSingleResult();
      return query.getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  public static List<CollectionTrans> getListByStatus(String status) {
    try {
      h2hConnection.getSession().clear();
      Query<CollectionTrans> query = h2hConnection.getSession().createQuery(
        "from CollectionTrans where status=:status order by id desc",
        CollectionTrans.class
      );
      query.setParameter("status", status);
      return query.getResultList();
    } catch (NoResultException e) {
      return null;
    }
  }

  public static List<CollectionTrans> getAllList() {
    try {
      h2hConnection.getSession().clear();
      Query<CollectionTrans> query = h2hConnection.getSession().createQuery(
        "from CollectionTrans order by id desc", CollectionTrans.class);
      return query.getResultList();
    } catch (NoResultException e) {
      return null;
    }
  }

  public void insert() {
    Session session = HthDb.h2hConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    try {
      session.save(this);
      session.getTransaction().commit();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}