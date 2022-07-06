package com.tcbs.automation.coco;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Entity
@Getter
@Setter
@Table(name = "PE_USER_PERFORMANCE_SNAPSHOT")
public class PeUserPerformanceSnapshot {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
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
  @Column(name = "TOTAL_COGS")
  private String totalCogs;
  @Column(name = "TOTAL_ESTIMATED_REVENUE")
  private String totalEstimatedRevenue;
  @Column(name = "RETURN_PCT")
  private Double returnPct;

}
