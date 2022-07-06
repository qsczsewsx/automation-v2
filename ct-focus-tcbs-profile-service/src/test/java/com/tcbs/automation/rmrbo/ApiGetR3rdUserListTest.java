package com.tcbs.automation.rmrbo;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.publicmatcher.HashMapMatcher;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.publicmatcher.CombinableMatcher.all;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/rmrbo/ApiGetR3rdUserList.csv", separator = '|')
public class ApiGetR3rdUserListTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String pageSize;
  private String pageNumber;
  private String fromDate;
  private String toDate;
  private String errorMessage;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api get r3rd user list")
  public void verifyGetR3rdUserListTest() {
    System.out.println(testCaseName);
    pageNumber = syncData(pageNumber);
    pageSize = syncData(pageSize);
    fromDate = syncData(fromDate);
    toDate = syncData(toDate);

    RequestSpecification requestSpecification = given()
      .baseUri(GET_R3RD_USER_LIST)
      .contentType("application/json")
      .header("x-api-key", testCaseName.contains("invalid x-api-key") ? FMB_X_API_KEY : RMRBO_API_KEY);

    Response response;

    String PAGE_NUMBER = "pageNumber";
    String PAGE_SIZE = "pageSize";
    String FROM_DATE = "fromDate";
    String TO_DATE = "toDate";

    if (testCaseName.contains("missing param pageSize")) {
      response = requestSpecification
        .param(PAGE_NUMBER, pageNumber)
        .param(FROM_DATE, fromDate)
        .param(TO_DATE, toDate)
        .get();
    } else if (testCaseName.contains("missing param pageNumber")) {
      response = requestSpecification
        .param(PAGE_SIZE, pageSize)
        .param(FROM_DATE, fromDate)
        .param(TO_DATE, toDate)
        .get();
    } else {
      response = requestSpecification
        .param(PAGE_NUMBER, pageNumber)
        .param(PAGE_SIZE, pageSize)
        .param(FROM_DATE, fromDate)
        .param(TO_DATE, toDate)
        .get();
    }

    assertThat("verify status", response.getStatusCode(), is(statusCode));

    if (statusCode == 200 && testCaseName.contains("return valid data")) {
      List<HashMap<String, Object>> listData = response.jsonPath().getList("data.content");
      assertThat(listData.size(), is(greaterThan(0)));
      assertThat("verify data in response",
        listData.get(0),
        all(HashMapMatcher.has("id", is(notNullValue())))
          .and(HashMapMatcher.has("tcbsId", is(notNullValue())))
          .and(HashMapMatcher.has("title", is(notNullValue())))
          .and(HashMapMatcher.has("role", anyOf(is("RM"), is("RBO"))))
      );
    } else {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }
}