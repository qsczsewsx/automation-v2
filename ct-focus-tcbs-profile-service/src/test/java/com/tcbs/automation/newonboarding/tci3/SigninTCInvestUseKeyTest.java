package com.tcbs.automation.newonboarding.tci3;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.cas.TcbsIdentification;
import com.tcbs.automation.cas.TcbsUser;
import com.tcbs.automation.login.LoginApi;
import com.tcbs.automation.login.TheUserInfo;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.screenplay.Actor;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.LinkedHashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.LOGIN_GETKEY;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.LOGIN_USEKEY;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.junit.Assert.assertEquals;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/ApiSigninTcinvestUseKey.csv", separator = '|')
public class SigninTCInvestUseKeyTest {

  private static String token;
  private static String gen_loginKey;
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errMess;
  private String loginKey;

  @Before
  public void before() {

    TcbsIdentification identification = TcbsIdentification.getListByIdPlace("Cục CS QLHC về TTXH").get(0);
    TcbsUser user = TcbsUser.getById(identification.getUserId());

    Actor actor = Actor.named("trangnt88");
    String userName = user.getUsername();
    LoginApi.withCredentials(userName, "abc123").performAs(actor);
    token = TheUserInfo.aboutLoginData().answeredBy(actor).getToken();

    Response response = given()
      .baseUri(LOGIN_GETKEY)
      .header("Authorization", "Bearer " + token)
      .contentType("application/json")
      .when()
      .get();

    if (response.getStatusCode() == 200) {
      gen_loginKey = response.jsonPath().get("login_key");
    } else {
      System.out.println("API login key gets error with status code: " + response.getStatusCode());
    }

    if (loginKey.equalsIgnoreCase("gen")) {
      loginKey = gen_loginKey;
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify API signin TCInvest by login key")
  public void signInTcbsUseKey() {
    System.out.println("Test Case: " + testCaseName);

    Gson gson = new Gson();
    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    body.put("login_key", loginKey);

    Response response = given()
      .baseUri(LOGIN_USEKEY)
      .contentType("application/json")
      .body(gson.toJson(body))
      .urlEncodingEnabled(false)
      .when()
      .post();

    assertEquals(statusCode, response.getStatusCode());
  }
}
