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
public class PriceChangeRes {
  @Id
  private String ticker;
  @Value("Price_Change")
  private Double priceChange;

  @Value("Shortterm_Price_Change")
  private Double shortTermPriceChange;

  @Value("Longterm_Price_Change")
  private Double longTermPriceChange;

  @Value("VAR")
  private Double var;
}
