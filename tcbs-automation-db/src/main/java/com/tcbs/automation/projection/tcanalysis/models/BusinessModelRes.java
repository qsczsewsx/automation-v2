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
public class BusinessModelRes {
  @Id
  private String ticker;

  @Value("Business_Model")
  private Double businessModel;

  @Value("Business_Efficiency")
  private Double businessEfficiency;

  @Value("Asset_Quality")
  private Double assetQuality;

  @Value("CashFlow_Quality")
  private Double cashFlowQuality;

  @Value("BOM")
  private Double bom;

  @Value("Business_Administration")
  private Double businessAdministration;

  @Value("Product_Service")
  private Double productService;

  @Value("Business_Advantage")
  private Double businessAdvantage;

  @Value("Company_Position")
  private Double companyPosition;

  @Value("Industry")
  private Double industry;

  @Value("Operation_Risk")
  private Double operationRisk;
}
