package com.tcbs.automation.fundstation.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@SuppressWarnings("unchecked")
@Table(name = "REPORT_TRANS_FUND")
public class ReportTransFund {
  public static Session session;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Integer id;

  @Column(name = "TRANSACTION_ID")
  private Integer transactionId;

  @Column(name = "PORTFOLIO_ID")
  private Integer portfolioId;

  @Column(name = "ACTION")
  private Integer action;

  @Column(name = "TYPE_CODE")
  private String typeCode;

  @Column(name = "TICKER")
  private String ticker;

  @Column(name = "PRODUCT_ID")
  private Integer productId;

  @Column(name = "UNIT")
  private Double unit;

  @Column(name = "PRICE")
  private Double price;

  @Column(name = "FEE")
  private Double fee;

  @Column(name = "CURRENCY_ID")
  private String currencyId;

  @Column(name = "BROKER")
  private Integer broker;

  @Column(name = "TRANSACTION_DATE")
  private Date transactionDate;

  @Column(name = "ACCUMULATED_VOLUME")
  private Double accumulatedVolume;

  @Column(name = "TRANSACTION_CLEAN_PRICE")
  private Double transactionCleanPrice;

  @Column(name = "COGS_CLEAN")
  private Double cogsClean;

  @Column(name = "TOTAL_CLEAN")
  private Double totalClean;

  @Column(name = "BROKER_NAME")
  private String brokerName;

  @Column(name = "COGS_VALUE")
  private Double cosgValue;

  @Column(name = "TRANSACTION_CLEAN_VALUE")
  private Double transCleanValue;

  @Column(name = "GAIN_LOSS")
  private Double gainLoss;

  @Column(name = "COGS_CLEAN_CALC")
  private Double cogsCleanCal;

  public static List<ReportTransFund> getListReportTransFundByDate(Integer accountId, Date reportDate) {
    Query<ReportTransFund> query = session.createQuery("from ReportTransFund where portfolioId =:accountId and transactionDate =:reportDate order by id asc");
    query.setParameter("accountId", accountId);
    query.setParameter("reportDate", reportDate);
    return query.getResultList();
  }

  public static ReportTransFund calculateTransLatestBy(Integer portfolioId, String ticker, Date reportDate) {
    Query<ReportTransFund> query = session.createQuery(
      "from ReportTransFund where portfolioId =:portfolioId and ticker =:ticker and transactionDate <=:reportDate order by transactionDate desc, transactionId desc").setFirstResult(0);
    query.setParameter("portfolioId", portfolioId);
    query.setParameter("ticker", ticker);
    query.setParameter("reportDate", reportDate);
    List<ReportTransFund> list = query.getResultList();
    if (list.size() > 0) {
      return list.get(0);
    }
    return new ReportTransFund();
  }
}
