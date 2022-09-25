package com.tcbs.automation.author;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.authen.AuthenSessionHBTest;
import com.tcbs.automation.authen.AuthenSessionTest;
import com.tcbs.automation.authen.AuthenSessionWTTest;
import com.tcbs.automation.login.LoginApi;
import com.tcbs.automation.login.TheUserInfo;
import common.CommonUtils;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.screenplay.Actor;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.tcbs.automation.config.iotp.IOTPConfig.IOTP_AUTHEN;
import static com.tcbs.automation.config.iotp.IOTPConfig.IOTP_DATAPOWER_DOMAIN;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/author/AuthorValidation.csv", separator = '|')
public class AuthorValidationTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String token;
  private String type;
  private String otp;
  private String otpType;
  private String otpSource;
  private String action;
  private String resource;
  private HashMap<String, Object> body;
  private final String S_OTP_SIGN = "sotp_sign";

  private final String HB = "HB";

  private final String WT = "WT";

  private final String IS_ALLOWED = "is_allowed";


  @Before
  public void before() {
    action = syncData(action);
    resource = syncData(resource);
    token = getTokenCaseByCase(token);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api validation of author")
  public void validationTest() {
    System.out.println("TestcaseName : " + testCaseName);

    Response response = callApi();
    if (testCaseName.contains("case success")) {
      assertThat("verify is_allowed", response.jsonPath().get(IS_ALLOWED).toString().trim(), is("false"));
      assertThat("verify X-Author", response.getHeader("X-Author"), is(type));
      assertThat("verify X-Otp-Source", response.getHeader("X-Otp-Source"), is(otpSource));
      token = getTokenCaseByCase("success");
      response = callApi();
      assertThat("verify status code", response.getStatusCode(), is(statusCode));
      assertThat("verify is_allowed", response.jsonPath().get(IS_ALLOWED).toString().trim(), is("true"));
    } else {
      assertThat("verify status code", response.getStatusCode(), is(statusCode));
      if (statusCode == 400) {
        assertThat("verify error message", response.jsonPath().get("message").toString().trim(), is(errorMessage));
      } else {
        assertThat("verify is_allowed", response.jsonPath().get(IS_ALLOWED).toString().trim(), is("false"));
        assertThat("verify X-Author", response.getHeader("X-Author"), is(type));
      }
    }
  }

  private Response callApi() {
    RequestSpecification requestSpecification = given()
            .baseUri(AUTHOR_VALIDATION)
            .header("x-api-key", TCBSPROFILE_INQUIRYGROUPINFOKEY)
            .contentType("application/json");

    Response response;

    body = new HashMap<>();
    body.put("token", token);
    body.put("action", action);
    body.put("resource", resource);
    Gson gson = new Gson();
    if (testCaseName.contains("missing BODY")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(gson.toJson(body)).post();
    }
    System.out.println(gson.toJson(body));
    return response;
  }

  private String getSotpSign(String token) {
    HashMap<String, Object> authenInfo = new HashMap<>();
    authenInfo.put("otp", otp);
    authenInfo.put("otpTypeName", otpType);
    authenInfo.put("tcbsId", "0001693997");
    String domain;
    if (HB.equals(otpSource)) {
      domain = OTP_HB_DOMAIN;
      authenInfo.put("source", StringUtils.lowerCase(HB));
    } else if (WT.equals(otpSource)) {
      domain = OTP_WT_DOMAIN;
    } else {
      domain = IOTP_DATAPOWER_DOMAIN;
    }

    Response resp = given()
      .baseUri(domain + IOTP_AUTHEN)
      .contentType(ContentType.JSON)
      .header("Authorization", "Bearer " + token)
      .body(authenInfo)
      .post();
    Map<String, Object> getResponse = resp.jsonPath().getMap("");
    String tmp = getResponse.get("token").toString();
    Map<String, Object> claims = CommonUtils.decodeToken(tmp);
    return (String) claims.get(S_OTP_SIGN);
  }

  private String getTokenCaseByCase(String token) {
    if (token.equalsIgnoreCase("success")) {
      Actor actor = Actor.named("haihv");
      LoginApi.withCredentials("105C793997", "abc123").performAs(actor);
      token = TheUserInfo.aboutLoginData().answeredBy(actor).getToken();

      body = new HashMap<>();
      body.put("token", token);
      if (HB.equals(otpSource)) {
        body.put("source", StringUtils.lowerCase(HB));
      } else if (WT.equals(otpSource)) {
        body.put("source", StringUtils.lowerCase(WT));
      }
      String STEP_UP_EXP = "stepup_exp";

      if ("stepup_required".equals(type)) {
        pushTypeStep(STEP_UP_EXP);
      }
      if ("sotp_required".equals(type) || "".equals(type)) {
        pushTypeSotp(STEP_UP_EXP, token);
      }
      Response response = getResponse();
      Map<String, Object> getResponse = response.jsonPath().getMap("");
      token = (String) getResponse.get("token");

    } else if (token.equalsIgnoreCase("default")) {
      Actor actor = Actor.named("haihv");
      LoginApi.withCredentials("105C793997", "abc123").performAs(actor);
      token = TheUserInfo.aboutLoginData().answeredBy(actor).getToken();
    } else {
      token = syncData(token);
    }
    return token;
  }

  private void pushTypeStep(String STEP_UP_EXP) {
    body.put(S_OTP_SIGN, "");
    if (testCaseName.contains("expired stepup_exp")) {
      body.put(STEP_UP_EXP, 1646395626);
    } else if (testCaseName.contains("stepup_exp as negative digit")) {
      body.put(STEP_UP_EXP, -1);
    } else {
      body.put(STEP_UP_EXP, TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + 28800);
    }
  }

  private void pushTypeSotp(String STEP_UP_EXP, String token) {
    body.put(STEP_UP_EXP, 0);
    if (testCaseName.contains("expired sotp_sign")) {
      body.put(S_OTP_SIGN, "abc");
    } else {
      body.put(S_OTP_SIGN, getSotpSign(token));
    }
  }

  private Response getResponse() {
    Response response;
    if (HB.equals(otpSource)) {
      response = AuthenSessionHBTest.callApiUpdateSessionHB(body, testCaseName);
    } else if (WT.equals(otpSource)) {
      response = AuthenSessionWTTest.callApiUpdateSessionWT(body, testCaseName);
    } else {
      response = AuthenSessionTest.callApiUpdateSession(body, testCaseName);
    }
    return response;
  }
}
