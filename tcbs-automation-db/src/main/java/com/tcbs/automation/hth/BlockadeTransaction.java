package com.tcbs.automation.hth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import static com.tcbs.automation.hth.HthDb.h2hConnection;

@Entity
@Table(name = "BLOCKADE_TRANSACTION")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BlockadeTransaction {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @NotNull
  @Column(name = "BANK_ACCOUNT_NO")
  private String bankAccountNo;
  @Column(name = "PARTNER")
  private String partner;
  @Column(name = "SUB_ACCOUNT_NO")
  private String subAccountNo;
  @Column(name = "CUSTODY_CODE")
  private String custodyCode;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "HOLD_KEY")
  private String holdKey;
  @NotNull
  @Column(name = "ACTION")
  private String action;
  @Column(name = "AMOUNT")
  private BigDecimal amount;
  @Column(name = "STATUS")
  private String status;
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
  @Column(name = "TRANSACTION_DATE")
  private Timestamp transactionDate;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "ACTION_RESPONSE_MESSAGE")
  private String actionResponseMessage;
  @Column(name = "INQUIRY_RESPONSE_MESSAGE")
  private String inquiryResponseMessage;
  @Column(name = "CREATED_TIME")
  private Timestamp createdTime;
  @Column(name = "UPDATED_TIME")
  private Timestamp updatedTime;

  /**
   * Author Lybtk
   */
  public static BlockadeTransaction getFromClientAndTxnId(String clientId, String clientTransactionId) {
    try {
      h2hConnection.getSession().clear();
      Query<BlockadeTransaction> query = h2hConnection.getSession().createQuery(
        "from BlockadeTransaction where clientId=:clientId and clientTransactionId=:clientTransactionId ",
        BlockadeTransaction.class
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
  public static Long getTotalInquiryHoldAndUnhold(String clientId, String action,
                                                  String bankAccountNumber, String clientTransactionId, String holdKey, String custodyCode,
                                                  String subAccountNo, String status, String from, String to) {
    Query<Long> query = h2hConnection.getSession().createQuery(
      "select count (*) from BlockadeTransaction where clientTransactionId!=null " +
        (StringUtils.isNotEmpty(clientTransactionId) ? "and clientTransactionId =:clientTransactionId " : " ") +
        (StringUtils.isNotEmpty(clientId) ? "and clientId =:clientId " : "") +
        (StringUtils.isNotEmpty(action) ? "and action =:action " : "") +
        (StringUtils.isNotEmpty(bankAccountNumber) ? "and bankAccountNo =:bankAccountNumber " : "") +
        (StringUtils.isNotEmpty(holdKey) ? "and holdKey =:holdKey " : "") +
        (StringUtils.isNotEmpty(custodyCode) ? "and custodyCode =:custodyCode " : "") +
        (StringUtils.isNotEmpty(subAccountNo) ? "and subAccountNo =:subAccountNo " : "") +
        (StringUtils.isNotEmpty(status) ? "and status =:status " : "") +
        (StringUtils.isNotEmpty(from) ? "and transactionDate >=:from " : "") +
        (StringUtils.isNotEmpty(to) ? "and transactionDate <=:to " : "")
    );
    if (StringUtils.isNotEmpty(clientId)) {
      query.setParameter("clientId", clientId);
    }
    if (StringUtils.isNotEmpty(action)) {
      query.setParameter("action", action);
    }
    if (StringUtils.isNotEmpty(bankAccountNumber)) {
      query.setParameter("bankAccountNumber", bankAccountNumber);
    }
    if (StringUtils.isNotEmpty(clientTransactionId)) {
      query.setParameter("clientTransactionId", clientTransactionId);
    }
    if (StringUtils.isNotEmpty(holdKey)) {
      query.setParameter("holdKey", holdKey);
    }
    if (StringUtils.isNotEmpty(custodyCode)) {
      query.setParameter("custodyCode", custodyCode);
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
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  public static List<BlockadeTransaction> getTransactionHoldAndUnhold(String clientId, String action,
                                                                      String bankAccountNumber, List<String> clientTransactionId, String holdKey, String custodyCode,
                                                                      String subAccountNo, String status,String partner, String from, String to) {
    Query<BlockadeTransaction> query = h2hConnection.getSession().createQuery(
      " FROM BlockadeTransaction where clientId != null " +
        (clientTransactionId != null ? " and clientTransactionId IN :clientTransactionId " : "") +
        (StringUtils.isNotEmpty(clientId) ? " and clientId =:clientId " : "") +
        (StringUtils.isNotEmpty(action) ? " and action =:action " : "") +
        (StringUtils.isNotEmpty(bankAccountNumber) ? " and bankAccountNo =:bankAccountNumber " : "") +
        (StringUtils.isNotEmpty(holdKey) ? " and holdKey =:holdKey " : "") +
        (StringUtils.isNotEmpty(custodyCode) ? " and custodyCode =:custodyCode " : "") +
        (StringUtils.isNotEmpty(subAccountNo) ? " and subAccountNo =:subAccountNo " : "") +
        (StringUtils.isNotEmpty(status) ? " and status =:status " : "") +
        (StringUtils.isNotEmpty(partner) ? " and partner =:partner " : "") +
        (StringUtils.isNotEmpty(from) ? " and transactionDate >=:from " : "") +
        (StringUtils.isNotEmpty(to) ? "and transactionDate <=:to " : "") +
            " ORDER BY ID DESC",
      BlockadeTransaction.class
    );
    setParameter(query, clientId, action, bankAccountNumber, clientTransactionId, holdKey, custodyCode, subAccountNo, status,partner, from, to);
    System.out.println(query.getQueryString());
    return query.getResultList();
  }

  private static void setParameter(Query query, String clientId, String action,
                                   String bankAccountNumber, List<String> clientTransactionId, String holdKey, String custodyCode,
                                   String subAccountNo, String status,String partner, String from, String to) {

    if (StringUtils.isNotEmpty(clientId)) {
      query.setParameter("clientId", clientId);
    }
    if (StringUtils.isNotEmpty(action)) {
      query.setParameter("action", action);
    }
    if (StringUtils.isNotEmpty(bankAccountNumber)) {
      query.setParameter("bankAccountNumber", bankAccountNumber);
    }
    if (clientTransactionId != null) {
      query.setParameter("clientTransactionId", clientTransactionId);
    }
    if (StringUtils.isNotEmpty(holdKey)) {
      query.setParameter("holdKey", holdKey);
    }
    if (StringUtils.isNotEmpty(custodyCode)) {
      query.setParameter("custodyCode", custodyCode);
    }
    if (StringUtils.isNotEmpty(subAccountNo)) {
      query.setParameter("subAccountNo", subAccountNo);
    }
    if (StringUtils.isNotEmpty(status)) {
      query.setParameter("status", status);
    }
    if (StringUtils.isNotEmpty(partner)) {
      query.setParameter("partner", partner);
    }
    if (StringUtils.isNotEmpty(from)) {
      query.setParameter("from", Timestamp.valueOf(from + " 00:00:00"));
    }
    if (StringUtils.isNotEmpty(to)) {
      query.setParameter("to", Timestamp.valueOf(to + " 23:59:59"));
    }
  }
}
