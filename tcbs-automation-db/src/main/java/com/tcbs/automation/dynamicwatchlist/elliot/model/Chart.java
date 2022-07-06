package com.tcbs.automation.dynamicwatchlist.elliot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mongodb.morphia.annotations.Id;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Chart {
  @Id
  private Double valueBucKet;
  private Integer totalTicker;
}
