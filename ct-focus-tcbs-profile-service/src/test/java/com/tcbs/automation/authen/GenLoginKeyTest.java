package com.tcbs.automation.authen;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
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

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.API_KEY;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.GEN_LOGIN_KEY;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/authen/GenLoginKey.csv", separator = '|')
public class GenLoginKeyTest {

  @Getter
  private String authenKey;
  private int statusCode;
  private String testCaseName;
  private String errorMessage;
  private HashMap<String, Object> body;

  @Before
  public void before() {
    if (authenKey.equalsIgnoreCase("gen")) {
      authenKey = CallApiUtils.callGenAuthenKeyApi().jsonPath().get("authenKey");
    } else {
      authenKey = syncData(authenKey);
    }
    body = new HashMap<>();
    body.put("authenKey", authenKey);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api gen login key")
  public void verifyGenLoginKeyTest() {
    System.out.println("TestCaseName : " + testCaseName);
    RequestSpecification requestSpecification = given()
      .baseUri(GEN_LOGIN_KEY)
      .header("x-api-key", API_KEY)
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
      String loginKey = response.jsonPath().get("login_key");
      assertThat("verify login key", loginKey, is(notNullValue()));
      Response resLogin = CallApiUtils.callLoginApi(loginKey);
      assertThat("verify login success", resLogin.jsonPath().get("token"), is(notNullValue()));
    } else if (statusCode == 400) {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }
}