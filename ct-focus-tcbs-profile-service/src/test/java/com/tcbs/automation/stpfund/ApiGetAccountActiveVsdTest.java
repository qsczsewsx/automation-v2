package com.tcbs.automation.stpfund;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.publicmatcher.HashMapMatcher;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
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
  private String expectedData;
  private HashMap<String, Object> params;

  @Before
  public void before() {
    String PAGE_NUMBER = "pageNumber";
    String PAGE_SIZE = "pageSize";
    String APP_ID = "appId";
    String DATE = "date";

    params = new HashMap<>();
    if (StringUtils.isNotEmpty(pageSize)) {
      params.put(PAGE_SIZE, pageSize);
    }
    if (StringUtils.isNotEmpty(pageNumber)) {
      params.put(PAGE_NUMBER, pageNumber);
    }
    if (StringUtils.isNotEmpty(date)) {
      params.put(DATE, date);
    }
    if (StringUtils.isNotEmpty(appId)) {
      params.put(APP_ID, appId);
    }
    System.out.println(params);
  }

  @Test
  @TestCase(name = "#testcaseName")
  @Title("Verify API get account active vsd")

  public void ApiGetAccountActiveVsd() {
    Response response = given()
      .baseUri(GET_ACCOUNT_ACTIVE_VSD)
      .header("x-api-key", testCaseName.contains("x-api-key is invalid") ? FMB_X_API_KEY : MULTIIA_TCBSID_X_API_KEY)
      .contentType("application/json")
      .params(params)
      .get();

    assertThat("verify status", response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      if (!expectedData.contains("0")) {
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

