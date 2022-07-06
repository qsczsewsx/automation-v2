package com.tcbs.automation.clients.performance;

import com.tcbs.automation.clients.performance.dto.PerfNginAccountIndicesResponse;
import com.tcbs.automation.clients.performance.dto.PerfNginAccountPerformanceResponse;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface PerformanceClient {
  Map<String, PerfNginAccountPerformanceResponse.AccountPerformance> getAccountPerformance(List<String> accountNos);

  Map<String, PerfNginAccountIndicesResponse.PerfInfo> getAccountIndices(List<String> accountNos);

  Map<String, PerfNginAccountIndicesResponse.PerfInfo> getAccountIndices(List<String> accountNos, Date from, Date to);
}
