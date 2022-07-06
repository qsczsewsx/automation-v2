package com.tcbs.automation.projection.tcanalysis.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialHealthRes {
  @Id
  private String ticker;
  @Value("Financial_Health")
  private Double financialHealth;

  @Value("NetDebt/Equity")
  private Double netDebtEquity;

  @Value("Current_ratio")
  private Double currentRatio;

  @Value("Quick_ratio")
  private Double quickRatio;

  @Value("Interest_Coverage")
  private Double interestCoverage;

  @Value("NetDebt/EBITDA")
  private Double netDebtEbitda;
}
