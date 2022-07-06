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
@Table(name = "PE_PERFORMANCE_PRESENT")
public class PePerformancePresent {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @Column(name = "ACCOUNT_ID")
  private String accountId;
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
  @Column(name = "RETURN_PCT")
  private Double returnPct;
  @Column(name = "PORTF_INDEX_ADJ")
  private Double portfIndexAdj;
  @Column(name = "USER_ID")
  private String userId;

}
