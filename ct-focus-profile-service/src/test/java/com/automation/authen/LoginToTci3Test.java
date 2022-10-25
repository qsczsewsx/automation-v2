package com.automation.authen;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.automation.config.xxxxprofileservice.xxxxProfileServiceConfig;
import com.google.gson.Gson;
import com.automation.cas.xxxxUser;
import common.CommonUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/authen/LoginToTci3.csv", separator = '|')
public class LoginToTci3Test {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String username;
  private String password;
  private String errorMessage;
  private HashMap<String, Object> body;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api login tci3")
  public void loginToTci3Test() {

    System.out.println("TestcaseName : " + testCaseName);

    username = syncData(username);
    password = syncData(password);

    body = new HashMap<>();
    body.put("username", username);
    body.put("password", password);

    RequestSpecification requestSpecification = given()
      .baseUri(xxxxProfileServiceConfig.LOGIN_TO_TCI3)
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
      xxxxUser xxxxUser = CommonUtils.getxxxxUserByUserName(username);
      Map<String, Object> getResponse = response.jsonPath().getMap("");
      assertEquals(username, getResponse.get("username"));
      assertEquals(xxxxUser.getFirstname(), getResponse.get("first_name"));
      assertEquals(xxxxUser.getLastname(), getResponse.get("last_name"));
      assertEquals(xxxxUser.getEmail(), getResponse.get("email"));
      assertEquals(xxxxUser.getxxxxid(), getResponse.get("xxxxid"));

      Map<String, Object> claims = CommonUtils.decodeToken((String) getResponse.get("token"));
      assertEquals(xxxxUser.getxxxxid(), claims.get("sub"));
      assertEquals(username, claims.get("custodyID"));
      assertEquals(xxxxUser.getEmail(), claims.get("email"));

      ArrayList<String> roles = (ArrayList<String>) claims.get("roles");
      boolean isRM = false;
      for (String role : roles) {
        if ("Application/RMs".equals(role) || "Application/RBO".equals(role)) {
          isRM = true;
          break;
        }
      }
      if (isRM) {
        Map<String, Object> rmClaims = CommonUtils.decodeToken((String) getResponse.get("ichats_token"));
        assertEquals(username, rmClaims.get("custodyID"));
        assertEquals(xxxxUser.getLastname() + " " + xxxxUser.getFirstname(), rmClaims.get("name"));
      } else {
        assertNull(claims.get("ichats_token"));
      }
    } else {
      assertThat("verify error message", response.jsonPath().get("message").toString().trim(), is(errorMessage));
    }
  }
}