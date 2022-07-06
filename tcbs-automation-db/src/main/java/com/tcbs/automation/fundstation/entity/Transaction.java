package com.tcbs.automation.fundstation.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.tcbs.automation.config.fundstation.FundStationConstants.ACTION_CASH;
import static com.tcbs.automation.fundstation.entity.Portfolio.getPortfolioByCode;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "TRANSACTION")
public class Transaction {
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
  private String assetBankAccountId;

  @Column(name = "CREATED_TIMESTAMP")
  private Date createdTimestamp;

  @Column(name = "UPDATED_TIMESTAMP")
  private Date updatedTimestamp;

  @Column(name = "TYPE_TRANSACTION")
  private Integer typeTransaction;

  @Column(name = "REPO_DATE")
  private Date repoDate;

  @Column(name = "REPO_PRICE")
  private Double repoPrice;

  @Column(name = "REF_ID")
  private String refId;

  public static List<Transaction> getAllTransOfPortfolioId(Object portfolioId) {
    Query<Transaction> query = session.createQuery("from Transaction where portfolioId =:portfolioId order by transactionDate asc, id asc");
    query.setParameter("portfolioId", portfolioId);
    return query.getResultList();
  }

  public static List<Transaction> transOfPortfolioIdFromTransId(Integer portfolioId, Integer transId) {
    Query<Transaction> query = session.createQuery("from Transaction where portfolioId =:portfolioId and id >=:transId");
    query.setParameter("portfolioId", portfolioId);
    query.setParameter("transId", transId);
    return query.getResultList();
  }

  public static List<Transaction> transRepoNeedGen(Integer portfolioId, Date fromDate, Date toDate) {
    Query<Transaction> query = session.createQuery("from Transaction where portfolioId =:portfolioId and repoDate >=:fromDate and repoDate <=:toDate");
    query.setParameter("portfolioId", portfolioId);
    query.setParameter("fromDate", fromDate);
    query.setParameter("toDate", toDate);
    return query.getResultList();
  }

  public static Transaction transLatestOfAccountId(Integer portfolioId) {
    Query<Transaction> query = session.createQuery("from Transaction where portfolioId =:portfolioId order by id desc ").setMaxResults(1);
    query.setParameter("portfolioId", portfolioId);
    List<Transaction> list = query.getResultList();

    Transaction trans = new Transaction();
    if (list.size() > 0) {
      trans = list.get(0);
    } else {
      trans.setId(0);
    }
    return trans;
  }

  public static Double getCashOfAccountIdByDate(Integer portfolioId, Date date) {
    Query<Transaction> query = session.createQuery("from Transaction where portfolioId =:portfolioId and action =:action and transactionDate =:transDate");
    query.setParameter("portfolioId", portfolioId);
    query.setParameter("action", ACTION_CASH);
    query.setParameter("transDate", date);
    List<Transaction> list = query.getResultList();
    double cash = 0D;
    for (Transaction trans : list) {
      cash += trans.getPrice();
    }
    return cash;
  }

  public static List<Transaction> getTransByReportDate(Integer portfolioId, Date reportDate) {
    Query<Transaction> query = session.createQuery("from Transaction where portfolioId =:portfolioId and transactionDate =:tradingDate and action in (1,2,4, 5,6) order by transactionDate asc");
    query.setParameter("portfolioId", portfolioId);
    query.setParameter("tradingDate", reportDate);
    return query.getResultList();
  }

  public static List<Transaction> listTransHasVolumeInTransit(Integer portfolioId, Date reportDate) {
    Query<Transaction> query = session.createQuery("from Transaction where portfolioId =:portfolioId and paymentDate > :reportDate and transactionDate <=:reportDate and action in (1,6)");
    query.setParameter("portfolioId", portfolioId);
    query.setParameter("reportDate", reportDate);
    return query.getResultList();
  }

  public static List<Transaction> getTransByHasCashInTransit(Integer portfolioId, Date reportDate) {
    Query<Transaction> query = session.createQuery("from Transaction where portfolioId =:portfolioId and paymentDate > :reportDate and transactionDate <=:reportDate and action in (2,5)");
    query.setParameter("portfolioId", portfolioId);
    query.setParameter("reportDate", reportDate);
    return query.getResultList();
  }

  public static Transaction getTransLatestBy(Integer portfolioId, Integer action) {
    Query<Transaction> query = session.createQuery("from Transaction where portfolioId =:portfolioId and action =:action order by transactionDate desc, id desc ").setFirstResult(0);
    query.setParameter("portfolioId", portfolioId);
    query.setParameter("action", action);
    List<Transaction> result = query.getResultList();
    if (result.size() > 0) {
      return result.get(0);
    } else {
      return new Transaction();
    }
  }

  public static Double cashInHand(Integer portfolioId, Date reportDate) {
    Query<Transaction> query = session.createQuery("from Transaction where portfolioId =:portfolioId and transactionDate =:reportDate and action =:action");
    query.setParameter("portfolioId", portfolioId);
    query.setParameter("reportDate", reportDate);
    query.setParameter("action", ACTION_CASH);
    List<Transaction> result = query.getResultList();
    return result.stream().mapToDouble(Transaction::getPrice).sum();
  }

  public static void deleteAllTransOfPortfolio(List<String> portfolioCode) {
    List<Integer> listPortfolioId = new ArrayList<>();
    for (String code : portfolioCode) {
      listPortfolioId.add(getPortfolioByCode(code).getId());
    }
    Query<Transaction> query = session.createQuery("delete Transaction where portfolioId in :listPortfolioId");
    query.setParameter("listPortfolioId", listPortfolioId);
    query.executeUpdate();
  }
}
