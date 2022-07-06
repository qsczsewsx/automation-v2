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
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.RMRBO_API_KEY;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.UPDATE_ROLE_VALIDATE_ROLE_INTO_WSO2;
import static net.serenitybdd.rest.RestRequests.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/rmrbo/ApiUpdateRoleValidateRoleIntoWso2.csv", separator = ',')
public class ApiUpdateRoleValidateRoleIntoWso2Test {

  @Getter
  private String testCaseName;
  private int statusCode;
  private String roleName;
  private String lstUserAdd;
  private String errorMessage;
  private String Expected;
  private String lstUserRemove;
  private static final String APPLICATION_JSON = "application/json";
  private static final String DATA_X_API_KEY = "x-api-key";

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify API active role by username")
  public void apiUpdateRoleValidateRoleIntoWso2() {
    System.out.println("Testcase Name : " + testCaseName);
    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    body.put("roleName", roleName);
    body.put("lstUserAdd", Collections.singletonList(lstUserAdd));
    body.put("lstUserRemove", new ArrayList<>());

    RequestSpecification requestSpecification = given()
      .baseUri(UPDATE_ROLE_VALIDATE_ROLE_INTO_WSO2)
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
    if (statusCode == 200) {
      LinkedHashMap<String, Object> body = new LinkedHashMap<>();
      body.put("roleName", roleName);
      body.put("lstUserAdd", new ArrayList<>());
      body.put("lstUserRemove", Collections.singletonList(lstUserAdd));
      Gson gson = new Gson();
      given()
        .baseUri(UPDATE_ROLE_VALIDATE_ROLE_INTO_WSO2)
        .header(DATA_X_API_KEY, RMRBO_API_KEY)
        .contentType(APPLICATION_JSON)
        .body(gson.toJson(body)).post();
    }
  }
}



