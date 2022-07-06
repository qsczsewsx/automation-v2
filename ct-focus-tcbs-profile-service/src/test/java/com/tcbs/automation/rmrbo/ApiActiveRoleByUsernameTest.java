package com.tcbs.automation.rmrbo;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.cas.R3RdUser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.LinkedHashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static net.serenitybdd.rest.RestRequests.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/rmrbo/ApiActiveRoleByUsername.csv", separator = '|')
public class ApiActiveRoleByUsernameTest {

  @Getter
  private String testCaseName;
  private int statusCode;
  private String username;
  private String role;
  private String errorMessage;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify API active role by username")
  public void apiActiveRoleByUserName() {
    System.out.println("Testcase Name : " + testCaseName);
    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    body.put("username", username);
    body.put("role", role);

    RequestSpecification requestSpecification = given()
      .baseUri(ACTIVE_ROLE_BY_USERNAME)
      .header("Authorization", "Bearer " + (testCaseName.contains("Authorization key") ? FMB_X_API_KEY : RMRBO_MAKER_AUTHORIZATION_KEY))
      .contentType("application/json");

    Response response;
    Gson gson = new Gson();
    if (testCaseName.contains("missing body")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(gson.toJson(body)).post();
    }

    assertThat(response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      assertThat("verify receive data", response.jsonPath().get("data"), is(true));
      assertThat("verify status", R3RdUser.getByCustodyCode(username).getRoleId().toString(), is("2"));
    } else {
      assertTrue(response.jsonPath().get("message").toString().contains(errorMessage));
    }
  }

  @After
  public void teardown() {
    if (statusCode == 200) {
      R3RdUser.updateRoleIdByCustodyCode("105C100103", "1");
    }
  }
}



