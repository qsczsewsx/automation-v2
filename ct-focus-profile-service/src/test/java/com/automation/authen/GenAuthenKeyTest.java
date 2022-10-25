package com.automation.authen;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.automation.config.xxxxprofileservice.xxxxProfileServiceConfig;
import com.google.gson.Gson;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static com.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/authen/GenAuthenKey.csv", separator = '|')
public class GenAuthenKeyTest {

  @Getter

  private String phoneNumber;
  private String otp;
  private String otpId;
  private String referenceId;
  private int statusCode;
  private String testCaseName;
  private String errorMessage;
  private HashMap<String, Object> body;

  @Before
  public void before() {
    phoneNumber = syncData(phoneNumber);
    otp = syncData(otp);
    otpId = syncData(otpId);
    referenceId = syncData(referenceId);
    body = new HashMap<>();
    body.put("phoneNumber", phoneNumber);
    body.put("otp", otp);
    body.put("otpId", otpId);
    body.put("referenceId", referenceId);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api gen authen key")
  public void verifyGetAuthenKeyTest() {
    System.out.println("TestCaseName : " + testCaseName);
    RequestSpecification requestSpecification = given()
      .baseUri(xxxxProfileServiceConfig.GEN_AUTHEN_KEY)
      .header("x-api-key", xxxxProfileServiceConfig.API_KEY)
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
      assertThat("verify authen key", response.jsonPath().get("authenKey"), is(notNullValue()));
    } else if (statusCode == 400) {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }
}