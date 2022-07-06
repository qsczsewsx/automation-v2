package com.tcbs.automation.paymentprocess;

import com.tcbs.automation.ops.OPS;
import lombok.*;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "H2H_TRANSACTION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "batch")
public class Transaction extends CRUEntity {

  @Id
  @Column(name = "ID", updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "REFERENCE_ID")
  private String referenceId;

  @Column(name = "RETRY_COUNT")
  private Integer retryCount;

  @Column(name = "TRANSACTION_TYPE")
  private String transactionType;

  @Column(name = "PAYMENT_DATE")
  private String paymentDate;

  @Column(name = "CITAD")
  private String citad;

  @Column(name = "BANK_NAME")
  private String bkNm;

  @Column(name = "BANK_CODE")
  private String bkCd;

  @Column(name = "BRANCH_NAME")
  private String branchNm;

  @Column(name = "STATE_PROVINCE")
  private String statPrvc;

  @Column(name = "CREDIT_ACCOUNT_NO")
  private String creditAccountNo;

  @Column(name = "CREDIT_ACCOUNT_NAME")
  private String creditAccountName;

  @Column(name = "TCBS_ID")
  private String tcbsId;

  @Column(name = "CUSTODY_CODE")
  private String custodyCd;

  @Column(name = "CUSTOMER_TYPE")
  private String customerType;

  @Column(name = "CCY")
  private String ccy;

  @Column(name = "AMOUNT")
  private BigDecimal amount;

  @Column(name = "DESCRIPTION")
  private String desc;

  @Column(name = "BOND_CODE")
  private String bondCode;

  @Column(name = "COUPON_PERIOD")
  private String couponPeriod;

  @Column(name = "PAYMENT_PURPOSE")
  private String paymentPurpose;

  @Column(name = "PROCESSING_STATUS")
  private String processingStatus;

  @Column(name = "PICKUP_STATUS")
  private Boolean pickUpStatus;

  @Column(name = "BPM_SUB_INSTANCE_ID")
  private String bpmSubInstanceId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "BATCH_ID")
  private Batch batch;

  @Transient
  private String batchId;

  public static Transaction getTransactionById(String id) {
    Session session = OPS.opsConnection.getSession();
    session.clear();
    Query<Transaction> query = session.createQuery("select t from Transaction t where t.id =:id",
      Transaction.class);
    query.setParameter("id", id);
    List<Transaction> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }
    return results.get(0);
  }

  public static void deleteTransactionsByBatchId(String batchId) {
    Session session = OPS.opsConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<?> query = session.createNativeQuery(
      "DELETE FROM H2H_TRANSACTION t WHERE t.BATCH_ID = :batchId"
    );
    query.setParameter("batchId", batchId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static List<Transaction> getTransactionsByIdList(List<String> ids) {
    Session session = OPS.opsConnection.getSession();
    session.clear();
    Query<Transaction> query = session.createQuery("select t from Transaction t where t.id in :ids",
      Transaction.class);
    query.setParameter("ids", ids);
    List<Transaction> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }
    return results;
  }

  public static void insertTransaction(Transaction transaction) {
    Session session = OPS.opsConnection.getSession();
    session.clear();
    org.hibernate.Transaction trans = session.beginTransaction();
    session.save(transaction);
    trans.commit();
  }

  public static void insertTransactionList(List<Transaction> transactions) {
    if (transactions != null && !transactions.isEmpty()) {
      Session session = OPS.opsConnection.getSession();
      session.clear();
      org.hibernate.Transaction trans = session.beginTransaction();
      transactions.forEach(t -> {
        session.save(t);
      });
      trans.commit();
    }
  }

  public static void deleteByReferenceId(String referenceId) {
    Session session = OPS.opsConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<?> query = session.createQuery(
      "DELETE FROM Transaction t WHERE t.referenceId = :referenceId"
    );
    query.setParameter("referenceId", referenceId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public void setTransientValues() {
    this.batchId = batch.getId();
  }

}
