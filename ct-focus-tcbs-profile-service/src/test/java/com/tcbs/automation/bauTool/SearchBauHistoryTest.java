package com.tcbs.automation.bauTool;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/bauTool/SearchBauHistory.csv", separator = '|')
public class SearchBauHistoryTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String action;
  private String username;
  private String userAction;
  private String fromDate;
  private String toDate;
  private String pageNumber;
  private String pageSize;
  private LinkedHashMap<String, Object> mapParams;
  private int countElements;

  @Before
  public void setup() {
    action = syncData(action);
    username = syncData(username);
    userAction = syncData(userAction);
    fromDate = syncData(fromDate);
    toDate = syncData(toDate);
    pageNumber = syncData(pageNumber);
    pageSize = syncData(pageSize);

    mapParams = new LinkedHashMap<>();
    mapParams.put("action", action);
    mapParams.put("username", username);
    mapParams.put("userAction", userAction);
    mapParams.put("fromDate", fromDate);
    mapParams.put("toDate", toDate);
    mapParams.put("pageNumber", pageNumber);
    mapParams.put("pageSize", pageSize);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api search bau history")
  public void performTest() {

    System.out.println("Test case name: " + testCaseName);

    RequestSpecification requestSpecification = given()
      .baseUri(SEARCH_BAU_HISTORY)
      .header("Authorization", "Bearer " +
        (testCaseName.contains("no permission") ? TCBSPROFILE_AUTHORIZATION : BAU_AUTHORIZATION_TOKEN));

    Response response;

    if (testCaseName.contains("missing all params")) {
      response = requestSpecification.get();
    } else {
      response = requestSpecification.queryParams(mapParams).get();
    }

    assertThat("verify status code", response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      List<Map<String, Object>> result = response.jsonPath().get("data.items");
      if (testCaseName.contains("missing all params")) {
        assertThat(result.size(), greaterThan(countElements));
      } else if (testCaseName.contains("valid return")) {
        assertThat(result.size(), is(countElements));
      } else {
        assertThat(result, is(nullValue()));
      }
    } else if (statusCode == 403) {
      assertThat("verify error message", response.jsonPath().get("errorMessage"), is(errorMessage));
    } else {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }
}