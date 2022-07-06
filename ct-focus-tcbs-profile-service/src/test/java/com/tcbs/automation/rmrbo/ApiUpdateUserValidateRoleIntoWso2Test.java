package com.tcbs.automation.rmrbo;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static net.serenitybdd.rest.RestRequests.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/rmrbo/ApiUpdateUserValidateRoleIntoWso2.csv", separator = '|')
public class ApiUpdateUserValidateRoleIntoWso2Test {

  @Getter
  private String testCaseName;
  private int statusCode;
  private String lstRoleAdd;
  private String lstRoleRemove;
  private String userName;
  private String errorMessage;
  private String Expected;
  private static final String APPLICATION_JSON = "application/json";
  private static final String DATA_X_API_KEY = "x-api-key";

  @Before
  public void before() {
    if (testCaseName.contains("RMs is active")) {
      // add RM
      callWso2("Application/RMs", Collections.singletonList(userName), new ArrayList<>());
    } else if (testCaseName.contains("RBO is active")) {
      // add RBO
      callWso2("Application/RBO", Collections.singletonList(userName), new ArrayList<>());
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify API active role by username")
  public void apiUpdateUserValidateRoleIntoWso2() {
    System.out.println("Testcase Name : " + testCaseName);
    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    body.put("userName", userName);
    body.put("lstRoleAdd", Collections.singletonList(lstRoleAdd));
    body.put("lstRoleRemove", new ArrayList<>());

    RequestSpecification requestSpecification = given()
      .baseUri(UPDATE_USER_VALIDATE_ROLE_INTO_WSO2)
      .header(DATA_X_API_KEY, RMRBO_API_KEY)
      .contentType(APPLICATION_JSON);

    Response response;
    Gson gson = new Gson();
    if (testCaseName.contains("missing body")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(gson.toJson(body)).post();
    }

    assertThat(response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      assertThat("verify update success", response.jsonPath().get("message"), is("SUCCESS"));
    } else {
      assertTrue(response.jsonPath().get("message").toString().contains(errorMessage));
    }
  }

  @After
  public void teardown() {
    if (testCaseName.contains("RMs is active")) {
      // remove RM
      callWso2("Application/RMs", new ArrayList<>(), Collections.singletonList(userName));
    } else if (testCaseName.contains("RBO is active")) {
      // remove RBO
      callWso2("Application/RBO", new ArrayList<>(), Collections.singletonList(userName));
    }

    if (statusCode == 200) {
      LinkedHashMap<String, Object> body = new LinkedHashMap<>();
      body.put("userName", userName);
      body.put("lstRoleAdd", new ArrayList<>());
      body.put("lstRoleRemove", Collections.singletonList(lstRoleAdd));
      Gson gson = new Gson();
      given()
        .baseUri(UPDATE_USER_VALIDATE_ROLE_INTO_WSO2)
        .header(DATA_X_API_KEY, RMRBO_API_KEY)
        .contentType(APPLICATION_JSON)
        .body(gson.toJson(body))
        .post();
    }
  }

  private void callWso2(String roleName, List<String> lstUserAdd, List<String> lstUserRemove) {
    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    body.put("roleName", roleName);
    body.put("lstUserAdd", lstUserAdd);
    body.put("lstUserRemove", lstUserRemove);
    Gson gson = new Gson();
    given()
      .baseUri(UPDATE_ROLE_VALIDATE_ROLE_INTO_WSO2)
      .header(DATA_X_API_KEY, RMRBO_API_KEY)
      .contentType(APPLICATION_JSON)
      .body(gson.toJson(body))
      .post();
  }
}



