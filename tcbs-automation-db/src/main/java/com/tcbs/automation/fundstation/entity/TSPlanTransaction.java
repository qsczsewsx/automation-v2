package com.tcbs.automation.fundstation.entity;

import lombok.Data;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import static com.tcbs.automation.fundstation.entity.TcAssets.sendSessionDBAssets;

@Data
@Entity
@Table(name = "TS_PLAN_TRANSACTION")
public class TSPlanTransaction {
  public static Session session;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "SOURCE")
  private Long source;

  @Column(name = "DESTINATION")
  private Long destination;

  @Column(name = "TRANSACTION_DATE")
  private Date transactionDate;

  @Column(name = "UNIT")
  private Integer unit;

  @Column(name = "PRODUCT_CODE")
  private String productCode;

  @Column(name = "PRICE")
  private Double price;

  @Column(name = "PLAN_ID")
  private Long planId;

  @Column(name = "USER_NAME")
  private String userName;

  @Column(name = "PRODUCT_TYPE")
  private String productType;

  @Column(name = "REPO_DATE")
  private Date repoDate;

  @Column(name = "REF_ID")
  private Long refId;

  @Column(name = "TRANSACTION_TYPE")
  private String transactionType;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "VALUE")
  private Integer value;

  @Column(name = "CREATED_TIMESTAMP")
  private Date createdTimestamp;

  @Column(name = "UPDATED_TIMESTAMP")
  private Date updatedTimestamp;

  @Column(name = "REPO_CHANNEL_ID")
  private Long repoChannelId;

  @Column(name = "ISSUE_DATE")
  private Date issueDate;

  public static List<TSPlanTransaction> getListActiveTSPlanTransactionWithProductCodeAndPlanId(Long planId, String productCode) {
    session.clear();
    Query<TSPlanTransaction> query = session.createQuery("from TSPlanTransaction where planId =:planId and productCode =: productCode and status ='ACTIVE'");
    query.setParameter("planId", planId);
    query.setParameter("productCode", productCode);
    return query.getResultList();
  }

  public static List<TSPlanTransaction> getListDraftTSPlanTransactionWithProductCodeAndPlanId(Long planId, String productCode, Date issueDate) {
    session.clear();
    Query<TSPlanTransaction> query = session.createQuery("from TSPlanTransaction where planId =:planId and productCode =: productCode and issueDate=:issueDate and status ='DRAFT'");
    query.setParameter("planId", planId);
    query.setParameter("productCode", productCode);
    query.setParameter("issueDate", issueDate);
    return query.getResultList();
  }

  public static List<TSPlanTransaction> getListActiveTSPlanTransactionWithProductCodeAndPlanId(Long planId, String productCode, Date issueDate, Date transactionDate, String transactionType, Integer source, Integer des) {
    session.clear();
    Query<TSPlanTransaction> query = session.createQuery(
      "from TSPlanTransaction where planId =:planId and productCode =: productCode and issueDate=:issueDate and status ='active' and transactionDate=:transactionDate and transactionType=:transactionType and source=:source and destination=:des ");
    query.setParameter("planId", planId);
    query.setParameter("productCode", productCode);
    query.setParameter("issueDate", issueDate);
    return query.getResultList();
  }

  public static List<TSPlanTransaction> getListTSPlanTransactionWithProductCodeAndPlanIdAndType(Long planId, String productCode, List<String> list) {
    session.clear();
    Query<TSPlanTransaction> query = session.createQuery("from TSPlanTransaction where planId =:planId and productCode =: productCode and status='ACTIVE' order by transactionDate DESC");
    query.setParameter("planId", planId);
    query.setParameter("productCode", productCode);
    return query.getResultList();
  }

  public static void insertIntoTsPlanTransaction(Long destination, Date transactionDate, Integer unit, String productCode, Long planId, String username, String status, Integer value, Double price, Integer repoChannelId) {
    Session session2 = sendSessionDBAssets();
    TSPlanTransaction tsPlanTransaction = new TSPlanTransaction();
    tsPlanTransaction.setDestination(destination);
    tsPlanTransaction.setTransactionDate(transactionDate);
    tsPlanTransaction.setUnit(unit);
    tsPlanTransaction.setUserName(username);
    tsPlanTransaction.setStatus(status);
    tsPlanTransaction.setProductCode(productCode);
    tsPlanTransaction.setPlanId(planId);
    tsPlanTransaction.setValue(value);
    tsPlanTransaction.setPrice(price);
    if (repoChannelId != null) {
      tsPlanTransaction.setRepoChannelId(Long.valueOf(repoChannelId));
    }
    session2.save(tsPlanTransaction);
    session2.getTransaction().commit();
  }

  public static void insertIntoTsPlanTransaction(Long destination, Date transactionDate, Integer unit, String productCode, Long planId, String username, String status, Integer value, Double price, Integer repoChannelId, Date issueDate) {
    Session session2 = sendSessionDBAssets();
    TSPlanTransaction tsPlanTransaction = new TSPlanTransaction();
    tsPlanTransaction.setDestination(destination);
    tsPlanTransaction.setTransactionDate(transactionDate);
    tsPlanTransaction.setUnit(unit);
    tsPlanTransaction.setUserName(username);
    tsPlanTransaction.setStatus(status);
    tsPlanTransaction.setProductCode(productCode);
    tsPlanTransaction.setPlanId(planId);
    tsPlanTransaction.setValue(value);
    tsPlanTransaction.setPrice(price);
    tsPlanTransaction.setIssueDate(issueDate);
    if (repoChannelId != null) {
      tsPlanTransaction.setRepoChannelId(Long.valueOf(repoChannelId));
    }
    session2.save(tsPlanTransaction);
    session2.getTransaction().commit();
  }

  public static void deleteTsPlanTransactionWithPlanIdAndUnderlyingId(Long planId, String productCode) {
    Session session2 = sendSessionDBAssets();
    Query<TSPlanTransaction> query = session2.createQuery("delete TSPlanTransaction where planId =:planId and productCode =: productCode");
    query.setParameter("planId", planId);
    query.setParameter("productCode", productCode);
    int row = query.executeUpdate();
    session2.getTransaction().commit();
  }

}
