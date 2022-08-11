package com.tcbs.automation.forgotPassword;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import common.CallApiUtils;
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

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.FORGOT_PASSWORD_CONFIRM_PHONE;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/forgotPassword/ForgotPasswordConfirmPhone.csv", separator = '|')
public class ForgotPasswordConfirmPhoneTest {

  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String transactionId;
  private String token;
  private String otpSession;
  private String password;
  private String otp;
  HashMap<String, Object> body;

  @Before
  public void before() {
    Response resPhone = CallApiUtils.callForgotPasswordPhoneApi("105C481234", "21/07/1988");
    String transactionIdNotify = resPhone.jsonPath().get("transactionId");
    String tokenNotify = resPhone.jsonPath().get("token");
    Response resNotify = CallApiUtils.callForgotPasswordNotifyApi(transactionIdNotify, tokenNotify);

    if (transactionId.equalsIgnoreCase("valid")) {
      transactionId = transactionIdNotify;
    } else {
      transactionId = syncData(transactionId);
    }
    if (token.equalsIgnoreCase("valid")) {
      token = tokenNotify;
    } else {
      token = syncData(token);
    }
    if (otpSession.equalsIgnoreCase("valid")) {
      otpSession = resNotify.jsonPath().get("otpSession");
    } else {
      otpSession = syncData(otpSession);
    }
    password = syncData(password);
    otp = syncData(otp);

    body = new HashMap<>();

    body.put("transactionId", transactionId);
    body.put("token", token);
    body.put("otpSession", otpSession);
    body.put("password", password);
    body.put("otp", otp);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api forgot password confirm phone")
  public void verifyForgotPasswordConfirmPhoneTest() {

    System.out.println("TestCaseName : " + testCaseName);

    RequestSpecification requestSpecification = given()
      .baseUri(FORGOT_PASSWORD_CONFIRM_PHONE)
      .contentType("application/json");

    Response response;

    if (testCaseName.contains("missing BODY")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(body).post();
    }

    assertThat(response.getStatusCode(), is(statusCode));
    if (statusCode == 400) {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }

}