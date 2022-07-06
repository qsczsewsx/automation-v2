package com.tcbs.automation.fundstation.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.tcbs.automation.fundstation.entity.TcAssets.sendSessionDBAssets;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "CASH_FUND")
public class CashFund {
  public static Session session;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Integer id;

  @Column(name = "PORTFOLIO_CODE")
  private String portfolioCode;

  @Column(name = "NAV")
  private Double nav;

  @Column(name = "TOTAL_BUY_VALUE")
  private Double totalBuyValue;

  @Column(name = "TOTAL_SELL_VOLUME")
  private Double totalSellVolume;

  @Column(name = "CASH_SUB_REDEM")
  private Double cashSubRedem;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "REPORT_DATE")
  @Temporal(TemporalType.DATE)
  private Date reportDate;

  @Column(name = "CREATED_TIMESTAMP")
  @Temporal(TemporalType.DATE)
  private Date createdTimestamp;

  @Column(name = "UPDATED_TIMESTAMP")
  @Temporal(TemporalType.DATE)
  private Date updatedTimestamp;

  public static List<CashFund> getCashInTransit(String portfolioCode, Date reportDate) {
    Query<CashFund> query = session.createQuery("from CashFund where portfolioCode =:portfolioCode and reportDate =:reportDate");
    query.setParameter("portfolioCode", portfolioCode);
    query.setParameter("reportDate", reportDate);
    List<CashFund> resultList = query.getResultList();
    return resultList;
  }

  public static List<CashFund> getCashFund(String portfolioCode, Date fromDate, Date toDate) {
    Query<CashFund> query = session.createQuery("from CashFund where portfolioCode =:portfolioCode and reportDate between :fromDate and :toDate");
    query.setParameter("portfolioCode", portfolioCode);
    query.setParameter("fromDate", fromDate);
    query.setParameter("toDate", toDate);
    List<CashFund> resultList = query.getResultList();
    return resultList;
  }

  public static void deleteCashInTransitOfDate(Date reportDate) {
    Query<CashFund> query = session.createQuery("delete CashFund where  reportDate =:reportDate");
    query.setParameter("reportDate", reportDate);
    query.executeUpdate();
  }

  public static void deleteCashInTransitOfDate(String portfolioCode, Date fromDate, Date toDate) {
    Session session2 = sendSessionDBAssets();
    Query<CashFund> query = session2.createQuery("delete CashFund where portfolioCode=:portfolioCode and reportDate between :fromDate and :toDate");
    query.setParameter("portfolioCode", portfolioCode);
    query.setParameter("fromDate", fromDate);
    query.setParameter("toDate", toDate);
    query.executeUpdate();
    session2.getTransaction().commit();
  }

  public static void insertIntoCashFund(String portfolioCode, Double nav, Double totalBuyValue, Double totalSellVolume, Double cashSubRedem, String status, Date reportDate) {
    CashFund cashFund = new CashFund();
    cashFund.setPortfolioCode(portfolioCode);
    cashFund.setNav(nav);
    cashFund.setTotalBuyValue(totalBuyValue);
    cashFund.setTotalSellVolume(totalSellVolume);
    cashFund.setCashSubRedem(cashSubRedem);
    cashFund.setStatus(status);
    cashFund.setReportDate(reportDate);
    Session session2 = sendSessionDBAssets();
    session2.save(cashFund);
    session2.getTransaction().commit();
  }

  public static BigDecimal getSuggestedNetInflow(String portfolioCode, String fromDate, String toDate) {
    Query query = session.createSQLQuery(String.format(
      "select AVG(cf.TOTAL_BUY_VALUE -cf.TOTAL_SELL_VOLUME*cf.NAV) as tb from CASH_FUND cf where REPORT_DATE between TO_DATE('%s', 'yyyy-MM-dd') and TO_DATE('%s', 'yyyy-MM-dd') and PORTFOLIO_CODE='%s'",
      fromDate, toDate, portfolioCode));
    if (query.getResultList().get(0) == null) {
      return BigDecimal.valueOf(0);
    } else {
      return (BigDecimal) query.getResultList().get(0);
    }
  }
}
