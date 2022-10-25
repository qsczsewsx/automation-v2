package com.automation.author;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.automation.config.xxxxprofileservice.xxxxProfileServiceConfig;
import com.google.gson.Gson;
import common.CommonUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.LinkedHashMap;
import java.util.List;

import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/author/ApiAddToBlackList.csv", separator = '|')
public class ApiAddToBlackListTest {

  @Getter
  private String testCaseName;
  @Getter
  private String sessionIDs;
  private int statusCode;
  private String errorMessage;
  private List<String> sessionIDList;

  @Before
  public void before() {
    sessionIDList = CommonUtils.getSessionIDFromLogin(sessionIDs);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api add to black list")
  public void verifyApiAddToBlackListTest() {

    System.out.println("TestCaseName : " + testCaseName);
    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    body.put("sessionIDs", sessionIDList);

    RequestSpecification requestSpecification = given()
      .baseUri(xxxxProfileServiceConfig.ADD_BLACK_LIST)
      .header("x-api-key", xxxxProfileServiceConfig.ADD_BLACKLIST_X_API_KEY)
      .contentType("application/json");

    Response response;
    Gson gson = new Gson();
    if (testCaseName.contains("missing body")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(gson.toJson(body)).post();
    }

    assertThat(response.getStatusCode(), is(statusCode));
    if (response.statusCode() == 200) {
      assertThat("verify add black list", response.jsonPath().get("message"), is("Success"));
    } else {
      assertThat("verify error message", response.jsonPath().get("message"), is(notNullValue()));
    }
  }

}
