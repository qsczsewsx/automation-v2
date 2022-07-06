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
@Table(name = "INVEST_PORTFOLIO")
public class InvestPortfolio {
  public static Session session;
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "ID")
  private int id;

  @Column(name = "PORTFOLIO_CODE")
  private String portfolioCode;

  @Column(name = "TICKER")
  private String ticker;

  @Column(name = "TARGET")
  private Double target;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "FROM_DATE")
  @Temporal(TemporalType.DATE)
  private Date fromDate;

  @Column(name = "TO_DATE")
  @Temporal(TemporalType.DATE)
  private Date toDate;

  @Column(name = "CREATED_TIMESTAMP")
  @Temporal(TemporalType.DATE)
  private Date createdTimestamp;

  @Column(name = "UPDATED_TIMESTAMP")
  @Temporal(TemporalType.DATE)
  private Date updatedTimestamp;

  @Column(name = "AMOUNT_CHANGE")
  private Double amountChange;

  @Column(name = "LAST_PRICE")
  private Double lastPrice;

  @Column(name = "TYPE")
  private Integer type;

  @Column(name = "EXPECTED_RETURN")
  private Double expectedReturn;

  @Column(name = "REF_CODE")
  private String refCode;

  public static List<InvestPortfolio> getInvestPortfolio(String portfolioCode) {
    Query<InvestPortfolio> query = session.createQuery("from InvestPortfolio where portfolioCode =:portfolioCode and status = 'ACTIVE'");
    query.setParameter("portfolioCode", portfolioCode);
    return query.getResultList();
  }

  public static List<InvestPortfolio> getInvestPortfolioByPortfolioCodeAndType(String portfolioCode, Integer type) {
    Query<InvestPortfolio> query = session.createQuery("from InvestPortfolio where portfolioCode =:portfolioCode and status = 'ACTIVE' and type =:type");
    query.setParameter("portfolioCode", portfolioCode);
    query.setParameter("type", type);
    return query.getResultList();
  }

  public static void deleteInvestPortfolio(List<String> listPortfolio) {
    if (!listPortfolio.isEmpty()) {
      Query<InvestPortfolio> query = session.createQuery("delete InvestPortfolio where portfolioCode in :listPortfolio");
      query.setParameter("listPortfolio", listPortfolio);
      query.executeUpdate();
    }
  }
}
