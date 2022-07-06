package com.tcbs.automation.coco;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "PE_USER_PERFORMANCE_PRESENT")
public class PeUserPerformancePresent {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @Column(name = "USER_ID")
  private String userId;
  @Column(name = "REPORT_DATE")
  private Date reportDate;
  @Column(name = "DAILY_PROFIT")
  private String dailyProfit;
  @Column(name = "ACC_PROFIT")
  private String accProfit;
  @Column(name = "NUMERATOR")
  private String numerator;
  @Column(name = "DENOMINATOR")
  private String denominator;
  @Column(name = "PORTF_INDEX")
  private Double portfIndex;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "TOTAL_COGS")
  private String totalCogs;
  @Column(name = "TOTAL_ESTIMATED_REVENUE")
  private String totalEstimatedRevenue;
  @Column(name = "CASH_BALANCE")
  private String cashBalance;
  @Column(name = "CASH_DEBIT")
  private String cashDebit;
  @Column(name = "CASH_CREDIT")
  private String cashCredit;
  @Column(name = "INDEX_ADJUSTED")
  private String indexAdjusted;

}
