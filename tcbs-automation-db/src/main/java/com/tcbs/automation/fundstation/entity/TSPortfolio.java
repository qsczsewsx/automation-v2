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
@Table(name = "TS_PORTFOLIO")
public class TSPortfolio {
  public static Session session;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Integer id;

  @Column(name = "PLAN_ID")
  private Integer planId;

  @Column(name = "PRODUCT_CODE")
  private String productCode;

  @Column(name = "TRANSACTION_DATE")
  private Date transactionDate;

  @Column(name = "CHANNEL_ID")
  private Integer chanelId;

  @Column(name = "VALUE")
  private Double value;

  @Column(name = "CREATED_TIMESTAMP")
  private Date createdTimestamp;

  @Column(name = "UPDATED_TIMESTAMP")
  private Date updatedTimestamp;

  @Column(name = "IS_BOOK_BUILD")
  private Integer isBookBuild;

  @Column(name = "ISSUE_DATE")
  private Date issueDate;

  @Column(name = "PERIOD")
  private String period;

  @Column(name = "STATUS")
  private String status;

  public static List<TSPortfolio> getListTSPortfolioWithPlanIdAndProductCode(Integer planId, String productCode) {
    Query<TSPortfolio> query = session.createQuery("from TSPortfolio where planId =:planId and productCode =: productCode order by id");
    query.setParameter("planId", planId);
    query.setParameter("productCode", productCode);
    return query.getResultList();
  }

  public static List<TSPortfolio> getRootTSPortfolioOfAIssues(Integer planId, String productCode, Date issueDate, Integer channelId) {
    Query<TSPortfolio> query = session.createQuery(
      "from TSPortfolio where planId =:planId and productCode =: productCode and issueDate =:issueDate and transactionDate=:issueDate and chanelId=:channelId");
    query.setParameter("planId", planId);
    query.setParameter("productCode", productCode);
    query.setParameter("issueDate", issueDate);
    query.setParameter("channelId", channelId);
    return query.getResultList();
  }

  public static List<TSPortfolio> getListTSPortfolioWithPlanIdAndIssueDate(Integer planId, String productCode, Date issueDate) {
    Query<TSPortfolio> query = session.createQuery("from TSPortfolio where planId =:planId and productCode =: productCode and issueDate=:issueDate");
    query.setParameter("planId", planId);
    query.setParameter("productCode", productCode);
    query.setParameter("issueDate", issueDate);
    return query.getResultList();
  }

  public static void deleteTSPortfolioWithPlanId(Integer planId) {
    Session session2 = sendSessionDBAssets();
    Query<TSPlanDetail> query = session2.createQuery("delete TSPortfolio where planId =:planId ");
    query.setParameter("planId", planId);
    int row = query.executeUpdate();
    session2.getTransaction().commit();
  }

  public static void insertIntoPortfolio(Integer planId, String productCode, Date transactionDate, Integer channelId, Double value, Integer isBookBuild) {
    Session session2 = sendSessionDBAssets();
    TSPortfolio tsPortfolio = new TSPortfolio();
    tsPortfolio.setPlanId(planId);
    tsPortfolio.setProductCode(productCode);
    tsPortfolio.setTransactionDate(transactionDate);
    tsPortfolio.setChanelId(channelId);
    tsPortfolio.setValue(value);
    tsPortfolio.setIsBookBuild(isBookBuild);
    session2.save(tsPortfolio);
    session2.getTransaction().commit();
  }

  public static void insertIntoPortfolioWithIssueDate(Integer planId, String productCode, Date transactionDate, Integer channelId, Double value, Integer isBookBuild, Date issueDate) {
    Session session2 = sendSessionDBAssets();
    TSPortfolio tsPortfolio = new TSPortfolio();
    tsPortfolio.setPlanId(planId);
    tsPortfolio.setProductCode(productCode);
    tsPortfolio.setTransactionDate(transactionDate);
    tsPortfolio.setChanelId(channelId);
    tsPortfolio.setValue(value);
    tsPortfolio.setIsBookBuild(isBookBuild);
    tsPortfolio.setIssueDate(issueDate);
    session2.save(tsPortfolio);
    session2.getTransaction().commit();
  }

  public static TSPortfolio getPortfolioToMovement(Integer planId, String productCode, Date transactionDate, Date issueDate, Integer channelId) {
    Query<TSPortfolio> query = session.createQuery(
      "from TSPortfolio where planId =:planId and productCode =: productCode and issueDate =:issueDate and transactionDate=:transactionDate and chanelId=:channelId");
    query.setParameter("planId", planId);
    query.setParameter("productCode", productCode);
    query.setParameter("issueDate", issueDate);
    query.setParameter("channelId", channelId);
    query.setParameter("transactionDate", transactionDate);
    List<TSPortfolio> list = query.getResultList();
    if (list.size() != 0) {
      return list.get(0);
    } else {
      return null;
    }
  }

  public static List<TSPortfolio> getTsPortfolioWithTransactionDateAndChannelIdAndIssueDate(Integer planId, String productCode, Date issueDate, Integer channelId, Date transactionDate, String status) {
    session.clear();
    Query<TSPortfolio> query = session.createQuery(
      "from TSPortfolio where planId =:planId and productCode =: productCode and issueDate=:issueDate and chanelId=:channelId and transactionDate=: transactionDate and status=:status");
    query.setParameter("planId", planId);
    query.setParameter("productCode", productCode);
    query.setParameter("issueDate", issueDate);
    query.setParameter("channelId", channelId);
    query.setParameter("transactionDate", transactionDate);
    query.setParameter("status", status);
    return query.getResultList();
  }
}
