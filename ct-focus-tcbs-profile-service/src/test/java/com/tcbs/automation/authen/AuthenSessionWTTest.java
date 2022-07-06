package com.tcbs.automation.authen;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.login.LoginApi;
import com.tcbs.automation.login.TheUserInfo;
import common.CommonUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.screenplay.Actor;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.UPDATE_SESSION_WT;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.UPDATE_SESSION_WT_TOKEN;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/authen/AuthenWtSession.csv", separator = '|')
public class AuthenSessionWTTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String token;
  private String stepupExp;
  private String sotpSign;
  private HashMap<String, Object> body;
  private final String STEP_UP_EXP = "stepup_exp";

  @Before
  public void before() {
    if (token.equalsIgnoreCase("success")) {
      Actor actor = Actor.named("haihv");
      LoginApi.withCredentials("105C793997", "abc123").performAs(actor);
      token = TheUserInfo.aboutLoginData().answeredBy(actor).getToken();
    } else {
      token = syncData(token);
    }
    stepupExp = syncData(stepupExp);
    sotpSign = syncData(sotpSign);

    body = new HashMap<>();
    body.put("token", token);
    body.put("sotp_sign", sotpSign);

    if (stepupExp.equalsIgnoreCase("") || stepupExp.equalsIgnoreCase("abc")) {
      body.put(STEP_UP_EXP, stepupExp);
    } else {
      body.put(STEP_UP_EXP, Long.parseLong(stepupExp));
    }

  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api authen session for WT")
  public void updateSessionWTTest() {
    System.out.println("TestcaseName : " + testCaseName);

    Response response = callApiUpdateSessionWT(body, testCaseName);
    Map<String, Object> getResponse = response.jsonPath().getMap("");
    assertThat("verify status code", response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      Map<String, Object> oldClaims = CommonUtils.decodeToken(token);
      Map<String, Object> newClaims = CommonUtils.decodeToken((String) getResponse.get("token"));
      assertEquals("WT", newClaims.get("otpSource"));
      assertEquals(Double.parseDouble(stepupExp), newClaims.get(STEP_UP_EXP));
      assertEquals(sotpSign, newClaims.get("sotp_sign"));
      assertEquals(oldClaims.get("sub"), newClaims.get("sub"));
      assertEquals(oldClaims.get("iss"), newClaims.get("iss"));
      assertEquals(oldClaims.get("custodyID"), newClaims.get("custodyID"));
      assertEquals(oldClaims.get("email"), newClaims.get("email"));
      assertEquals(oldClaims.get("roles"), newClaims.get("roles"));
    } else {
      assertThat("verify error message", response.jsonPath().get("message").toString().trim(), is(errorMessage));
    }
  }

  public static Response callApiUpdateSessionWT(HashMap body, String testCaseName) {
    RequestSpecification requestSpecification = given()
      .baseUri(UPDATE_SESSION_WT)
      .header("x-api-key", UPDATE_SESSION_WT_TOKEN)
      .contentType("application/json");

    Response response;

    Gson gson = new Gson();
    if (testCaseName.contains("missing BODY")) {
      response = requestSpecification.put();
    } else {
      response = requestSpecification.body(gson.toJson(body)).put();
    }

    return response;
  }
}
