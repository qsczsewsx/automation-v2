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

import java.util.HashMap;
import java.util.Objects;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.SIGN_CONTRACT_API;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/SignContract.csv", separator = '|')
public class SignContractTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String typeValue;
  private String accountLogin;
  private String tcbsid;
  private String errorMessage;
  private String token;
  private String otp;
  private String otpId;

  @Before
  public void before() {
    Actor actor = Actor.named("tuanna2");
    LoginApi.withCredentials(accountLogin, "abc123").performAs(actor);
    token = TheUserInfo.aboutLoginData().answeredBy(actor).getToken();

    typeValue = syncData(typeValue);
  }


  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api sign contract")
  public void verifySignContractTest() {
    System.out.println("TestCaseName : " + testCaseName);

    Response response;
    if (testCaseName.contains("check OTP") || testCaseName.contains("tcbsId is not found") || testCaseName.contains("no permission")) {
      if (testCaseName.contains("missing body")) {
        response = callApiSignContract(null, typeValue);
      } else {
        response = callApiSignContract(buildBody(otpId, otp), typeValue);
      }
    } else {
      response = callApiSignContract(buildBody(otpId, otp), typeValue);
      otpId = response.jsonPath().get("data").toString().trim();
      HashMap<String, Object> body = buildBody(otpId, "111111");
      if (testCaseName.contains("missing type_value")) {
        response = callApiSignContract(body, null);
      } else {
        response = callApiSignContract(body, typeValue);
      }
    }

    assertThat("verify status code", response.getStatusCode(), is(statusCode));
    if (statusCode == 400) {
      assertThat("verify error message", response.jsonPath().get("message").toString().trim(), is(errorMessage));
    }
  }

  private HashMap<String, Object> buildBody(String otpId, String otp) {
    HashMap<String, Object> body = new HashMap<>();
    body.put("otp", syncData(otp));
    body.put("otpId", syncData(otpId));
    return body;
  }

  private Response callApiSignContract(Object body, String type) {
    Gson gson = new Gson();
    RequestSpecification requestSpecification = given()
      .baseUri(SIGN_CONTRACT_API + tcbsid)
      .contentType("application/json")
      .header("Authorization", "Bearer " + token);

    if (Objects.isNull(body)) {
      return requestSpecification.queryParam("type", typeValue).post();
    }

    if (Objects.isNull(type)) {
      return requestSpecification.body(gson.toJson(body)).post();
    }

    return requestSpecification.queryParam("type", typeValue).body(gson.toJson(body)).post();
  }
}
