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

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.FORGOT_PASSWORD_NOTIFY;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/forgotPassword/ForgotPasswordNotify.csv", separator = '|')
public class ForgotPasswordNotifyTest {

  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String transactionId;
  private String token;
  HashMap<String, Object> body;

  @Before
  public void before() {
    Response res = CallApiUtils.callForgotPasswordPhoneApi("105C481234", "21/07/1988");
    if (transactionId.equalsIgnoreCase("valid")) {
      transactionId = res.jsonPath().get("transactionId");
    } else {
      transactionId = syncData(transactionId);
    }
    if (token.equalsIgnoreCase("valid")) {
      token = res.jsonPath().get("token");
    } else {
      token = syncData(token);
    }
    body = new HashMap<>();

    body.put("transactionId", transactionId);
    body.put("token", token);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api forgot password notify")
  public void verifyForgotPasswordNotifyTest() {

    System.out.println("TestCaseName : " + testCaseName);

    RequestSpecification requestSpecification = given()
      .baseUri(FORGOT_PASSWORD_NOTIFY)
      .contentType("application/json");

    Response response;

    if (testCaseName.contains("missing BODY")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(body).post();
    }

    assertThat(response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      assertThat(response.jsonPath().get("otpSession"), is(notNullValue()));
    } else {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }

}