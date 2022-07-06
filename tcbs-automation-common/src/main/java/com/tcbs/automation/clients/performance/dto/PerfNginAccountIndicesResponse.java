package com.tcbs.automation.clients.performance.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PerfNginAccountIndicesResponse {
  private List<PerfInfo> data;

  @Data
  @NoArgsConstructor
  public static class PerfInfo {
    private String accountNo;
    private List<PerfIndex> indices;
  }

  @Data
  @NoArgsConstructor
  public static class PerfIndex {
    private String date;
    private Double index;
  }
}
