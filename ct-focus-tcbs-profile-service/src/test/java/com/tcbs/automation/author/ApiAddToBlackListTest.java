package com.tcbs.automation.author;

import com.adaptavist.tm4j.junit.annotation.TestCase;
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

import java.util.*;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.ADD_BLACKLIST_X_API_KEY;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.ADD_BLACK_LIST;
import static com.tcbs.automation.tools.FormatUtils.syncData;
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
  private LinkedHashMap<String, Object> body;
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
    body = new LinkedHashMap<>();
    body.put("sessionIDs", sessionIDList);

    RequestSpecification requestSpecification = given()
      .baseUri(ADD_BLACK_LIST)
      .header("x-api-key", ADD_BLACKLIST_X_API_KEY)
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
