package com.tcbs.automation.silkgate;

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

import static com.tcbs.automation.silkgate.SilkgateDb.silkgateDbConnection;

@Entity
@Table(name = "STOCK_TXN_RECONCILE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockTxnReconcile {

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
  @NotNull
  @Column(name = "PAYMENT_TYPE")
  private String paymentType;
  @Column(name = "TRANSACTION_TYPE")
  private String transactionType;
  @Column(name = "TRANSACTION_TYPE_DESCRIPTION")
  private String transactionTypeDescription;
  @Column(name = "TRANSACTION_CODE")
  private String transactionCode;
  @Column(name = "TRANSACTION_CODE_DESCRIPTION")
  private String transactionCodeDescription;
  @NotNull
  @Column(name = "IS_INTERNAL")
  private BigDecimal isInternal;
  @Column(name = "STATEMENT_ENTRY")
  private String statementEntry;
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
  @Column(name = "SOURCE_TYPE")
  private String sourceType;
  @Column(name = "CREATED_TIME")
  private Timestamp createdTime;
  @Column(name = "UPDATED_TIME")
  private Timestamp updatedTime;


  public void insert() {
    Session session = silkgateDbConnection.getSession();
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

  public static StockTxnReconcile getByTransactionId(String transactionId) {
    silkgateDbConnection.getSession().clear();
    Query<StockTxnReconcile> query = silkgateDbConnection.getSession().createQuery(
            " FROM StockTxnReconcile WHERE transactionId=:transactionId ",
            StockTxnReconcile.class
    );
    query.setParameter("transactionId", transactionId);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }
}
