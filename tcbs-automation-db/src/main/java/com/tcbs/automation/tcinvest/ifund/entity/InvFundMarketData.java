package com.tcbs.automation.tcinvest.ifund.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuppressWarnings("unchecked")
@Table(name = "INV_FUND_MARKET_DATA")
public class InvFundMarketData {
  public static Session session;
  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @Column(name = "NAV_CURRENT")
  private String navCurrent;
  @Column(name = "FUND_CODE")
  private String fundCode;
  @Column(name = "SUBSCRIPTION_FEE_FROM")
  private String subscriptionFeeFrom;
  @Column(name = "SUBSCRIPTION_FEE_TO")
  private String subscriptionFeeTo;
  @Column(name = "REDEMPTION_FEE_FROM")
  private String redemptionFeeFrom;
  @Column(name = "REDEMPTION_FEE_TO")
  private String redemptionFeeTo;
  @Column(name = "NAV_HIGHEST_LEVEL")
  private String navHighestLevel;
  @Column(name = "NAV_LOWEST_LEVEL")
  private String navLowestLevel;
  @Column(name = "NUMBER_OF_UNIT")
  private String numberOfUnit;
  @Column(name = "TOTAL_VALUE")
  private String totalValue;
  @Column(name = "FOREIGN_NUMBER_OF_UNIT")
  private String foreignNumberOfUnit;
  @Column(name = "FOREIGN_TOTAL_VALUE")
  private String foreignTotalValue;
  @Column(name = "FOREIGN_OWNERSHIP_RATIO")
  private String foreignOwnershipRatio;
  @Column(name = "VALUATION_DATE")
  private String valuationDate;
  @Column(name = "DEALING_DATE")
  private String dealingDate;
  @Column(name = "TIMESTAMP")
  private Date timestamp;
  @Column(name = "TRADING_DATE")
  private Date tradingDate;
}
