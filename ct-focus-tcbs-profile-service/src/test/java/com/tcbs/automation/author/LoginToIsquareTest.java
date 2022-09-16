package com.tcbs.automation.author;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.isquare.IsAuthenInfoEntity;
import com.tcbs.automation.isquare.UmUserEntity;
import common.CommonUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.LOGIN_TO_ISQUARE;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/author/LoginToIsquareTest.csv", separator = '|')
public class LoginToIsquareTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String username;
  private String password;
  private String token;
  private String errorMessage;
  private String userId;
  private String sessionId;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api login isquare")
  public void loginToIsquareTest() {

    System.out.println("TestcaseName : " + testCaseName);

    username = syncData(username);
    password = syncData(password);

    HashMap<String, Object> body = new HashMap<>();
    body.put("username", username);
    body.put("password", password);

    RequestSpecification requestSpecification = given()
      .baseUri(LOGIN_TO_ISQUARE)
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
      Map<String, Object> getResponse = response.jsonPath().getMap("");
      Map<String, Object> claims = CommonUtils.decodeToken((String) getResponse.get("token"));
      Optional<UmUserEntity> umUserEntity = UmUserEntity.getUserByUsername(username);
      umUserEntity.ifPresent(userEntity -> userId = userEntity.getId().toString());
      sessionId = IsAuthenInfoEntity.getListBySessionId(userId).get(0).getSessionId();
      assertThat(claims.get("sessionID"), is(sessionId));
    } else {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }

  @After
  public void clearData() {
    IsAuthenInfoEntity.deleteByUserIdAndSessionId(userId, sessionId);
  }
}