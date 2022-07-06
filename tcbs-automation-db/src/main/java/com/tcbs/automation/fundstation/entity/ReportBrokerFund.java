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
@Table(name = "REPORT_BROKER_FUND")
public class ReportBrokerFund {
  public static Session session;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Integer id;

  @Column(name = "PORTFOLIO_ID")
  private Integer portfolioId;

  @Column(name = "BROKER_CODE")
  private String brokerCode;

  @Column(name = "BROKER_NAME")
  private String brokerName;

  @Column(name = "BUY_VALUE")
  private Double buy;

  @Column(name = "SELL_VALUE")
  private Double sell;

  @Column(name = "TOTAL_VALUE")
  private Double total;

  @Column(name = "PERCENT_VALUE")
  private Double percentCurrent;

  @Column(name = "REPORT_DATE")
  private Date reportDate;

  public static List<ReportBrokerFund> getListReportBrokerBy(Integer portfolioId, Date reportDate) {
    Query<ReportBrokerFund> query = session.createQuery("from ReportBrokerFund where portfolioId =:portfolioId and reportDate =:reportDate order by  percentCurrent desc");
    query.setParameter("portfolioId", portfolioId);
    query.setParameter("reportDate", reportDate);
    return query.getResultList();
  }
}
