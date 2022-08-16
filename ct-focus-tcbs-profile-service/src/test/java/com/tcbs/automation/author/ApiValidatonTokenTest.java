package com.tcbs.automation.author;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import common.CommonUtils;
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

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.VALIDATION_TOKEN_BLACK_LIST;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.VALIDATION_TOKEN_X_API_KEY;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/author/ApiValidationToken.csv", separator = '|')
public class ApiValidatonTokenTest {

  @Getter
  private String testCaseName;
  @Getter
  private String token;
  private String action;
  private String resource;
  private int statusCode;
  private String errorMessage;

  @Before
  public void before() {
    if (token.equalsIgnoreCase("token")) {
      token = CommonUtils.getToken("105C300126");
    } else {
      token = syncData(token);
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api validation token")
  public void verifyApiValidationTokenTest() {

    System.out.println("TestCaseName : " + testCaseName);
    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    body.put("token", token);
    body.put("action", action);
    body.put("resource", resource);

    RequestSpecification requestSpecification = given()
      .baseUri(VALIDATION_TOKEN_BLACK_LIST)
      .header("x-api-key", VALIDATION_TOKEN_X_API_KEY)
      .contentType("application/json");

    Response response;

    Gson gson = new Gson();
    if (testCaseName.contains("missing body")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(gson.toJson(body)).post();
    }

    assertThat(response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      assertThat("verify validation token", response.jsonPath().get("is_allowed"), is(true));
    } else if (statusCode == 403) {
      assertThat("verify validation token", response.jsonPath().get("is_allowed"), is(false));
    } else {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }
}
