package com.automation.authen;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.automation.config.xxxxprofileservice.xxxxProfileServiceConfig;
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

import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/authen/ApiAddSOtp.csv", separator = '|')
public class ApiAddSOtpTest {

  @Getter
  private String testCaseName;
  @Getter
  private int duration;
  private String otp;
  private String otpId;
  private String otpTypeName;
  private String xxxxId;
  private int statusCode;
  private String errorMessage;
  private String token;

  @Before
  public void before() {
    token = CommonUtils.getToken("105C300126");
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api add sotp")
  public void verifyApiAddSOtpTest() {

    System.out.println("TestCaseName : " + testCaseName);
    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    body.put("duration", duration);
    body.put("otp", otp);
    body.put("otpId", otpId);
    body.put("otpTypeName", otpTypeName);
    body.put("xxxxId", xxxxId);

    RequestSpecification requestSpecification = given()
      .baseUri(xxxxProfileServiceConfig.ADD_OTP)
      .header("Authorization", "Bearer " + token)
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
      assertThat("verify add sotp", response.jsonPath().get("isValid"), is(true));
    } else if (statusCode == 400) {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }
}
