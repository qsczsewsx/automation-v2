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
public class BusinessOperationRes {
  @Id
  private String ticker;

  @Value("Business_Operation")
  private Double businessOperation;

  @Value("RevenueGrowth")
  private Double last5yearsRevenueGrowth;

  @Value("OperatingProfitGrowth")
  private Double last5yearsOperatingProfitGrowth;

  @Value("EBITDAGrowth")
  private Double last5yearsEbitdaGrowth;

  @Value("FCFFGrowth")
  private Double last5yearsFCFFGrowth;

  @Value("NetProfitGrowth")
  private Double last5yearsNetProfitGrowth;

  @Value("Lastyear_GrossProfitMargin")
  private Double lastYearGrossProfitMargin;

  @Value("Lastyear_OperatingProfitMargin")
  private Double lastYearOperatingProfitMargin;

  @Value("Lastyear_NetProfitMargin")
  private Double lastYearNetProfitMargin;

  @Value("Avg_ROE")
  private Double avgROE;

  @Value("Avg_ROA")
  private Double avgROA;
}
