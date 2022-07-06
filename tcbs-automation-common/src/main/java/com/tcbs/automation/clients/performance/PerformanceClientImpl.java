package com.tcbs.automation.clients.performance;

import com.tcbs.automation.clients.performance.dto.PerfNginAccountIndicesRequest;
import com.tcbs.automation.clients.performance.dto.PerfNginAccountIndicesResponse;
import com.tcbs.automation.clients.performance.dto.PerfNginAccountPerformanceRequest;
import com.tcbs.automation.clients.performance.dto.PerfNginAccountPerformanceResponse;
import com.tcbs.automation.tools.DateUtils;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.tcbs.automation.config.coco.CocoServiceConfig.*;
import static java.net.HttpURLConnection.HTTP_OK;
import static net.serenitybdd.rest.SerenityRest.given;

public class PerformanceClientImpl implements PerformanceClient {
  @Override
  public Map<String, PerfNginAccountPerformanceResponse.AccountPerformance> getAccountPerformance(List<String> accountNos) {
    String baseUri = COCO_URL + COCO_PERF_NGIN_INTERNAL + COCO_ACCOUNT_PERFORMANCE;

    PerfNginAccountPerformanceRequest request = PerfNginAccountPerformanceRequest.builder()
      .accountNoList(accountNos)
      .build();

    Response response = given()
      .baseUri(baseUri)
      .contentType(ContentType.JSON)
      .header("x-api-key", COCO_X_API_KEY_ACCOUNT_B)
      .body(request)
      .post();
    if (response.statusCode() == HTTP_OK) {
      PerfNginAccountPerformanceResponse res = response.as(PerfNginAccountPerformanceResponse.class);
      return res.getData().stream().collect(Collectors.toMap(e -> e.getAccountNo(), e -> e, (e1, e2) -> e1));
    }
    return null;
  }

  @Override
  public Map<String, PerfNginAccountIndicesResponse.PerfInfo> getAccountIndices(List<String> accountNos) {
    return getAccountIndices(accountNos, null, null);
  }

  @Override
  public Map<String, PerfNginAccountIndicesResponse.PerfInfo> getAccountIndices(List<String> accountNos, Date from, Date to) {
    String baseUri = COCO_URL + COCO_PERF_NGIN_INTERNAL + COCO_ACCOUNT_INDICES;

    PerfNginAccountIndicesRequest request = PerfNginAccountIndicesRequest.builder()
      .accountNoList(accountNos)
      .fromDate(DateUtils.toString(from, null))
      .toDate(DateUtils.toString(to, null))
      .build();

    Response response = given()
      .baseUri(baseUri)
      .contentType(ContentType.JSON)
      .header("x-api-key", COCO_X_API_KEY_ACCOUNT_B)
      .body(request)
      .post();
    if (response.statusCode() == HTTP_OK) {
      PerfNginAccountIndicesResponse res = response.as(PerfNginAccountIndicesResponse.class);
      return res.getData().stream().collect(Collectors.toMap(e -> e.getAccountNo(), e -> e, (e1, e2) -> e1));
    }
    return null;
  }
}
