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

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.FORGOT_PASSWORD_REGISTRY;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/forgotpassword/ForgotPasswordRegistry.csv", separator = '|')
public class ForgotPasswordRegistryTest {

  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String code105C;
  private String birthday;
  HashMap<String, Object> body;

  @Before
  public void before() {
    code105C = syncData(code105C);
    birthday = syncData(birthday);
    body = new HashMap<>();

    body.put("code105C", code105C);
    body.put("birthday", birthday);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api forgot password registry")
  public void verifyForgotPasswordRegistryTest() {

    System.out.println("TestCaseName : " + testCaseName);

    RequestSpecification requestSpecification = given()
      .baseUri(FORGOT_PASSWORD_REGISTRY)
      .contentType("application/json");

    Response response;

    if (testCaseName.contains("missing BODY")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(body).post();
    }

    assertThat(response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      assertThat("verify error message", response.jsonPath().get("abc"), is(errorMessage));
    } else {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }

}