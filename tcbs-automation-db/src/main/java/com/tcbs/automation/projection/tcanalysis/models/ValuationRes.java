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
public class ValuationRes {
  @Id
  private String ticker;
  @Value("Valuation")
  private Double valuation;

  @Value("P/E")
  private Double pe;

  @Value("P/B")
  private Double pb;

  @Value("P/S")
  private Double ps;

  @Value("EV/EBITDA")
  private Double evebitda;

  @Value("Dividend_Rate")
  private Double dividendRate;
}
