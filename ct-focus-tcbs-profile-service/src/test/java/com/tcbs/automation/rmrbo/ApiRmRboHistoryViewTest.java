package com.tcbs.automation.rmrbo;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.R3RdAuditLog;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Map;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static net.serenitybdd.rest.RestRequests.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/rmrbo/ApiRmRboHistoryView.csv", separator = '|')
public class ApiRmRboHistoryViewTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String tcbsId;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify API RM RBO History View")
  public void apiRmRboHistoryView() {
    System.out.println("TestCaseName : " + testCaseName);

    Response response = given()
      .baseUri(RM_RBO_HISTORY_VIEW.replace("{tcbsId}", tcbsId))
      .header("Authorization", "Bearer " + (testCaseName.contains("invalid token") ? FMB_X_API_KEY : RMRBO_VIEW_TOKEN))
      .contentType("application/json")
      .get();

    assertThat(response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      List<Map<String, Object>> resDataList = response.jsonPath().getList("");
      if (testCaseName.contains("all data is returned")) {
        List<R3RdAuditLog> resultDb = R3RdAuditLog.getHistoryByTcbsId(tcbsId);
        for (int i = 0; i < resDataList.size(); i++) {
          assertThat("verify action", resDataList.get(i).get("action"), is(resultDb.get(i).getAction()));
          assertThat("verify userName", resDataList.get(i).get("userName"), is(resultDb.get(i).getUsername()));
          assertThat("verify data", resDataList.get(i).get("data"), is(resultDb.get(i).getData()));
        }
      } else {
        assertThat("verify size", resDataList.size(), is(0));
      }
    } else {
      assertTrue(response.jsonPath().get("errorMessage").toString().contains(errorMessage));
    }
  }
}