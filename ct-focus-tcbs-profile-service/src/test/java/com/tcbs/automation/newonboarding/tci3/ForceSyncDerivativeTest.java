package com.tcbs.automation.newonboarding.tci3;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.login.LoginApi;
import com.tcbs.automation.login.TheUserInfo;
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

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.FORCE_SYNC_DERIVATIVE;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/ForceSyncDerivative.csv", separator = '|')
public class ForceSyncDerivativeTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String referralCode;
  private String userName;
  private HashMap<String, Object> body;
  private String jwt;

  @Before
  public void setup() throws UnsupportedEncodingException {
    Actor actor = Actor.named("haihv");
    LoginApi.withCredentials("105C313993", "abc123").performAs(actor);
    jwt = TheUserInfo.aboutLoginData().answeredBy(actor).getToken();
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api force sync derivative")
  public void performTest() {
    referralCode = syncData(referralCode);
    userName = syncData(userName);
    body = new HashMap<>();
    body.put("referralCode", referralCode);
    body.put("userName", userName);

    Gson gson = new Gson();

    RequestSpecification requestSpecification = given()
      .baseUri(FORCE_SYNC_DERIVATIVE)
      .header("Authorization", "Bearer " + jwt)
      .contentType("application/json");

    Response response;

    if (testCaseName.contains("missing BODY")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(gson.toJson(body)).post();
    }

    assertThat("verify status", response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      assertThat("verify valid", response.jsonPath().get("valid"), is(true));
    } else if (statusCode == 400) {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }
}
