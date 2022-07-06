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
public class LiquidityRes {
  @Id
  private String ticker;

  @Value("Liquidity")
  private Double liquidity;

  @Value("Shortterm_Liquidity")
  private Double shortTermLiquidity;

  @Value("Shortterm_Liquidity_Change")
  private Double shortTermLiquidityChange;

  @Value("Longterm_Liquidity")
  private Double longTermLiquidity;

  @Value("Longterm_Liquidity_Change")
  private Double longTermLiquidityChange;
}
