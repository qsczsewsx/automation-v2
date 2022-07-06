package com.tcbs.automation.projection.tcanalysis.recommend;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HisBuyItem {
  private String d;
  private String ticker;
  private Double recommendValue;
  private Integer recommendType;
}
