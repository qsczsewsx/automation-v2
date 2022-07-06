package com.tcbs.automation.authen;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.cas.TcbsUser;
import common.CommonUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.LOGIN_FROM_THIRD_PARTY;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/authen/LoginFromThirdParty.csv", separator = '|')
public class LoginFromThirdPartyTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String username;
  private String password;
  private String errorMessage;
  private HashMap<String, Object> body;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api login from third party")
  public void loginToTci3Test() {

    System.out.println("TestcaseName : " + testCaseName);

    username = syncData(username);
    password = syncData(password);

    body = new HashMap<>();
    body.put("username", username);
    body.put("password", password);

    RequestSpecification requestSpecification = given()
      .baseUri(LOGIN_FROM_THIRD_PARTY)
      .contentType("application/json");

    Response response;

    Gson gson = new Gson();
    if (testCaseName.contains("missing BODY")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(gson.toJson(body)).post();
    }

    assertThat("verify status code", response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      TcbsUser tcbsUser = CommonUtils.getTcbsUserByUserName(username);
      Map<String, Object> getResponse = response.jsonPath().getMap("");
      assertEquals(username, getResponse.get("username"));
      assertEquals(tcbsUser.getFirstname(), getResponse.get("firstname"));
      assertEquals(tcbsUser.getLastname(), getResponse.get("lastname"));
      assertEquals(tcbsUser.getTcbsid(), getResponse.get("tcbsid"));
    } else {
      assertThat("verify error message", response.jsonPath().get("message").toString().trim(), is(errorMessage));
    }
  }
}