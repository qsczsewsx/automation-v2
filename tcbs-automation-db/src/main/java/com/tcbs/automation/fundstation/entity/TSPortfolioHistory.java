package com.tcbs.automation.fundstation.entity;

import lombok.Data;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;

import static com.tcbs.automation.fundstation.entity.TcAssets.sendSessionDBAssets;

@Data
@Entity
@Table(name = "TS_PORTFOLIO_HISTORY")
public class TSPortfolioHistory {
  public static Session session;
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
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

  public static void deleteTSPortfolioHistoryWithPlanId(Integer planId) {
    Session session2 = sendSessionDBAssets();
    Query<TSPortfolioHistory> query = session2.createQuery("delete TSPortfolioHistory where planId =:planId ");
    query.setParameter("planId", planId);
    int row = query.executeUpdate();
    session2.getTransaction().commit();
  }
}
