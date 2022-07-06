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
@Table(name = "REPORT_PORTFOLIO_FUND")
public class ReportPortfolioFund {
  public static Session session;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Integer id;

  @Column(name = "NAME")
  private String name;

  @Column(name = "TICKER")
  private String ticker;

  @Column(name = "TYPE")
  private String type;

  @Column(name = "TYPE_NAME")
  private String typeName;

  @Column(name = "LISTED")
  private String listed;

  @Column(name = "VOLUME")
  private Double volume;

  @Column(name = "EXPIRE_DATE")
  @Temporal(TemporalType.DATE)
  private Date expireDate;

  @Column(name = "PAR_VALUE")
  private Double parValue;

  @Column(name = "DIRTY_VALUE")
  private Double dirtyValue;

  @Column(name = "FUND_HLDG")
  private Double fundHldg;

  @Column(name = "REMAIN_OTC")
  private Double remainOtc;

  @Column(name = "REMAIN_COMPANY")
  private Double remainCompany;

  @Column(name = "REMAIN_GROUP")
  private Double remainGroup;

  @Column(name = "MONTH_HLDG")
  private Double monthHldg;

  @Column(name = "TENOR")
  private Double tenor;

  @Column(name = "YIELD")
  private Double yield;

  @Column(name = "REPORT_DATE")
  @Temporal(TemporalType.DATE)
  private Date reportDate;

  @Column(name = "PORTFOLIO_CODE")
  private String portfolioCode;

  @Column(name = "GROUP_CODE")
  private Integer groupCode;

  @Column(name = "VOLUME_IN_TRANSIT")
  private Double volumeInTransit;

  @Column(name = "MARKET_CAP")
  private Double marketCap;

  @Column(name = "MKT_PRICE")
  private Double mktPrice;

  @Column(name = "RSI")
  private Double rsi;

  @Column(name = "ONE_DAY")
  private Double oneDay;

  @Column(name = "SEVEN_DAY")
  private Double sevenDay;

  @Column(name = "THIRTY_DAY")
  private Double thirtyDay;

  @Column(name = "ONE_YEAR")
  private Double oneYear;

  @Column(name = "PE")
  private Double pe;

  @Column(name = "PB")
  private Double pb;

  @Column(name = "DVD_YIELD")
  private Double dvdYield;

  @Column(name = "ROE")
  private Double roe;

  @Column(name = "NET_MARGIN")
  private Double netMargin;

  @Column(name = "DE")
  private Double de;

  @Column(name = "VOLUME_ON_HAND")
  private Double volumeOnHand;

  @Column(name = "AVG_COST")
  private Double avgCost;

  @Column(name = "GAIN_LOSS")
  private Double gainLoss;

  @Column(name = "HALF_YEAR")
  private Double halfYear;


  @SuppressWarnings("unchecked")
  public static List<ReportPortfolioFund> getListReportPortfolioFundByDate(String account, Date reportDate) {
    Query<ReportPortfolioFund> query = session.createQuery("from ReportPortfolioFund where portfolioCode =:accountCode and reportDate =:reportDate");
    query.setParameter("accountCode", account);
    query.setParameter("reportDate", reportDate);
    return query.getResultList();
  }

  public static ReportPortfolioFund getReportPortfolioOfPortfolioCodeBy(String portfolioCode, String ticker) {
    Query<ReportPortfolioFund> query = session.createQuery("from ReportPortfolioFund where portfolioCode =:portfolioCode and ticker =:ticker order by reportDate desc").setFirstResult(0);
    query.setParameter("portfolioCode", portfolioCode);
    query.setParameter("ticker", ticker);
    List<ReportPortfolioFund> result = query.getResultList();
    if (result.size() > 0) {
      return result.get(0);
    }
    return new ReportPortfolioFund();
  }

  public static Date getReportDateLatest(String account) {
    Query<ReportPortfolioFund> query = session.createQuery("from ReportPortfolioFund where portfolioCode =:accountCode order by reportDate desc").setMaxResults(1);
    query.setParameter("accountCode", account);
    List<ReportPortfolioFund> result = query.getResultList();
    if (result.size() > 0) {
      return result.get(0).getReportDate();
    }
    return new Date();
  }
}
