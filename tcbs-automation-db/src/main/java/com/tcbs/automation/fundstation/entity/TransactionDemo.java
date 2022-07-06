package com.tcbs.automation.fundstation.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import static com.tcbs.automation.config.fundstation.FundStationConstants.ACTION_CASH;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "TRANSACTION_DEMO")
public class TransactionDemo {
  public static Session session;
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "ID")
  private int id;

  @Column(name = "TRANSACTION_CODE")
  private String transactionCode;

  @Column(name = "PORTFOLIO_ID")
  private int portfolioId;

  @Column(name = "ACTION")
  private int action;

  @Column(name = "TICKER")
  private String ticker;

  @Column(name = "PRODUCT_ID")
  private String productId;

  @Column(name = "UNIT")
  private Double unit;

  @Column(name = "PRICE")
  private Double price;

  @Column(name = "FEE")
  private Double fee;

  @Column(name = "CURRENCY_ID")
  private String currencyId;

  @Column(name = "YIELD")
  private String yield;

  @Column(name = "BROKER")
  private Integer broker;

  @Column(name = "COUNTER_PARTY")
  private String counterParty;

  @Column(name = "DOMAIN_CODE")
  private String domainCode;

  @Column(name = "PARAMS")
  private String params;

  @Column(name = "TRANSACTION_DATE")
  private Date transactionDate;

  @Column(name = "SETTLEMENT_DATE")
  private Date settlementDate;

  @Column(name = "PAYMENT_DATE")
  private Date paymentDate;

  @Column(name = "CASH_STATUS")
  private String cashStatus;

  @Column(name = "ASSET_BANK_ACCOUNT_ID")
  private String assetBankportfolioId;

  @Column(name = "CREATED_TIMESTAMP")
  private Date createdTimestamp;

  @Column(name = "UPDATED_TIMESTAMP")
  private Date updatedTimestamp;

  public static List<TransactionDemo> getAllTransOfPortfolioTypeDemo(Object portfolioId) {
    Query<TransactionDemo> query = session.createQuery("from TransactionDemo where portfolioId =:portfolioId ");
    query.setParameter("portfolioId", portfolioId);
    return query.getResultList();
  }


  public static List<TransactionDemo> transOfPortfolioIdFromIdDemo(Integer portfolioId, Integer fromId) {
    Query<TransactionDemo> query = session.createQuery("from TransactionDemo where portfolioId =:portfolioId and id >=:fromId");
    query.setParameter("portfolioId", portfolioId);
    query.setParameter("fromId", fromId);
    return query.getResultList();
  }

  public static Double cashInHandForDemo(Integer portfolioId, Date reportDate) {
    Query<TransactionDemo> query = session.createQuery("from TransactionDemo where portfolioId =:portfolioId and transactionDate =:reportDate and action =:action");
    query.setParameter("portfolioId", portfolioId);
    query.setParameter("reportDate", reportDate);
    query.setParameter("action", ACTION_CASH);
    List<TransactionDemo> result = query.getResultList();
    return result.stream().mapToDouble(TransactionDemo::getPrice).sum();
  }

  public static TransactionDemo transLatestOfPortfolioIdDemo(Integer portfolioId) {
    Query<TransactionDemo> query = session.createQuery("from TransactionDemo where portfolioId =:portfolioId order by id desc ").setMaxResults(1);
    query.setParameter("portfolioId", portfolioId);
    List<TransactionDemo> list = query.getResultList();

    TransactionDemo trans = new TransactionDemo();
    if (list.size() > 0) {
      trans = list.get(0);
    } else {
      trans.setId(0);
    }
    return trans;
  }

  public static void deleteAllTransOfPortfolioId(Integer portfolioId) {
    Query<TransactionDemo> query = session.createQuery("delete TransactionDemo where portfolioId =:portfolioId ");
    query.setParameter("portfolioId", portfolioId);
    query.executeUpdate();
  }
}
