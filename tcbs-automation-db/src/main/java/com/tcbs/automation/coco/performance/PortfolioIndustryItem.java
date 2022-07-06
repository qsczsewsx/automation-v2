package com.tcbs.automation.coco.performance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioIndustryItem {
  private String symbolAdj;

  private Double value;
}
