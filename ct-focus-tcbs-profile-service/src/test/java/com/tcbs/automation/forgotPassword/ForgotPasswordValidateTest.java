package com.tcbs.automation.forgotPassword;

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

import java.util.HashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.FORGOT_PASSWORD_VALIDATE;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/forgotPassword/ForgotPasswordValidate.csv", separator = '|')
public class ForgotPasswordValidateTest {

  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String password;
  HashMap<String, Object> body;

  @Before
  public void before() {
    password = syncData(password);
    body = new HashMap<>();
    body.put("password", password);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api forgot password validate")
  public void verifyForgotPasswordValidateTest() {

    System.out.println("TestCaseName : " + testCaseName);

    RequestSpecification requestSpecification = given()
      .baseUri(FORGOT_PASSWORD_VALIDATE)
      .contentType("application/json");

    Response response;

    if (testCaseName.contains("missing BODY")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(body).post();
    }

    assertThat(response.getStatusCode(), is(statusCode));
    assertThat("verify message", response.jsonPath().get("message"), is(errorMessage));

  }

}