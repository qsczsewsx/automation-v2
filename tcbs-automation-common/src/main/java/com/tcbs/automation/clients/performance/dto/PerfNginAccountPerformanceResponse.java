package com.tcbs.automation.clients.performance.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tcbs.automation.constants.coco.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PerfNginAccountPerformanceResponse {
  private List<AccountPerformance> data;

  @Data
  @NoArgsConstructor
  public static class AccountPerformance {
    private String accountNo;
    private AccountPnl pnl30days;
    private AccountPnl pnl6months;
    private AccountPnl pnl12months;
    private AccountPnl pnlAll;
    private RiskScore riskInMonth;
    private Constants.NumeratorRange avgNumerator30Days;
  }

  @Data
  @NoArgsConstructor
  public static class AccountPnl {
    private String actualDate;
    private Double returnPct;
    private Double returnValue;
  }

  @Data
  @NoArgsConstructor
  public static class RiskScore {
    private Integer riskScore;
    private String timeStamp;
  }
}
