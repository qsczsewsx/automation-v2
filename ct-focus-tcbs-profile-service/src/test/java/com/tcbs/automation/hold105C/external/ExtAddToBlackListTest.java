package com.tcbs.automation.hold105C.external;

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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.EXT_ADD_BLACK_LIST;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/external/ExtAddToBlackList.csv", separator = '|')
public class ExtAddToBlackListTest {

  @Getter
  private String testCaseName;
  @Getter
  private String sessionId;
  private String token;
  private int statusCode;
  private String errorMessage;
  private LinkedHashMap<String, Object> body;
  private List<String> sessionIDList;

  @Before
  public void before() {
    token = CommonUtils.getTokenAuthenSOtp("10000025745");
    sessionIDList = CommonUtils.getSessionID(sessionId);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api add to black list")
  public void verifyExtAddToBlackListTest() {

    System.out.println("TestCaseName : " + testCaseName);
    body = new LinkedHashMap<>();
    body.put("sessionId", sessionIDList);

    RequestSpecification requestSpecification = given()
      .baseUri(EXT_ADD_BLACK_LIST)
      .header("Authorization", "Bearer " + token)
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
      assertThat("verify add black list", response.jsonPath().get(""), is(sessionIDList));
    } else {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }


}
