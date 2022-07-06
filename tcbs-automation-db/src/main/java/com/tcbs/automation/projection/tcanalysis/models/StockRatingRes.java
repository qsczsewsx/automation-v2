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
public class StockRatingRes {
  @Id
  private String ticker;

  @Value("Stock_Rating")
  private Double stockRating;

  @Value("Business_Model")
  private Double businessModel;

  @Value("Business_Operation")
  private Double businessOperation;

  @Value("Financial_Health")
  private Double financialHealth;

  @Value("Liquidity")
  private Double liquidity;

  @Value("Price_Change")
  private Double priceChange;

  @Value("Valuation")
  private Double valuation;

  @Value("TAScore")
  private Double taScore;

  @Value("RSRating")
  private Double rsRating;
}
