package com.tcbs.automation.stpfund;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.publicmatcher.HashMapMatcher;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.publicmatcher.CombinableMatcher.all;
import static net.serenitybdd.rest.RestRequests.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/stpfund/ApiGetAccountActiveVsd.csv", separator = '|')
public class ApiGetAccountActiveVsdTest {
  @Getter
  private String testCaseName;
  private String date;
  private int statusCode;
  private String errorMsg;
  private String appId;
  private String pageNumber;
  private String pageSize;

  @Test
  @TestCase(name = "#testcaseName")
  @Title("Verify API get account active vsd")

  public void ApiGetAccountActiveVsd() {
    RequestSpecification requestSpecification = given()
      .baseUri(GET_ACCOUNT_ACTIVE_VSD)
      .header("x-api-key", testCaseName.contains("x-api-key is invalid") ? FMB_X_API_KEY : MULTIIA_TCBSID_X_API_KEY)
      .contentType("application/json");

    Response response;
    String PAGE_NUMBER = "pageNumber";
    String PAGE_SIZE = "pageSize";
    String APP_ID = "appId";
    String DATE = "date";

    if (testCaseName.contains("missing param pageSize")) {
      response = requestSpecification
        .param(PAGE_NUMBER, pageNumber)
        .param(APP_ID, appId)
        .param(DATE, date)
        .get();
    } else if (testCaseName.contains("missing param pageNumber")) {
      response = requestSpecification
        .param(PAGE_SIZE, pageSize)
        .param(APP_ID, appId)
        .param(DATE, date)
        .get();
    } else if (testCaseName.contains("missing param date")) {
      response = requestSpecification
        .param(PAGE_SIZE, pageSize)
        .param(PAGE_NUMBER, pageNumber)
        .param(APP_ID, appId)
        .get();
    } else if (testCaseName.contains("missing param appId")) {
      response = requestSpecification
        .param(PAGE_SIZE, pageSize)
        .param(PAGE_NUMBER, pageNumber)
        .param(DATE, date)
        .get();
    } else {
      response = requestSpecification
        .param(PAGE_NUMBER, pageNumber)
        .param(PAGE_SIZE, pageSize)
        .param(APP_ID, appId)
        .param(DATE, date)
        .get();
    }

    assertThat("verify status", response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      if (!(testCaseName.contains("param date") || testCaseName.contains("appId not exsit") || testCaseName.contains("date is greater") || testCaseName.contains("pageNumber is greater than 1000"))) {
        List<HashMap<String, Object>> responseList = response.jsonPath().getList("data.items");
        assertThat(responseList.size(), is(greaterThan(0)));
        MatcherAssert.assertThat("verify data in response",
          responseList.get(0),
          all(HashMapMatcher.has("tcbsId", is(notNullValue())))
            .and(HashMapMatcher.has("code105c", is(notNullValue())))
            .and(HashMapMatcher.has("fullName", is(notNullValue())))
            .and(HashMapMatcher.has("activeDate", is(notNullValue())))
        );
      }
    } else {
      assertTrue(response.jsonPath().get("message").toString().contains(errorMsg));
    }
  }
}
