package com.tcbs.automation.back;

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

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.BACK_ADD_BLACK_LIST;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.BACK_ADD_TO_BLACK_LIST;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/back/BackAddToBlackList.csv", separator = '|')
public class BackAddToBlackListTest {

  @Getter
  private String testCaseName;
  @Getter
  private String sessionIDs;
  private String tcbsId;
  private int statusCode;
  private String errorMessage;
  private LinkedHashMap<String, Object> body;
  private List<String> sessionIDList;

  @Before
  public void before() {
    sessionIDList = CommonUtils.getSessionID(sessionIDs);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api add to black list")
  public void verifyBackAddToBlackListTest() {

    System.out.println("TestCaseName : " + testCaseName);
    body = new LinkedHashMap<>();
    body.put("tcbsId", tcbsId);
    body.put("sessionId", sessionIDList);

    RequestSpecification requestSpecification = given()
      .baseUri(BACK_ADD_BLACK_LIST)
      .header("Authorization", "Bearer " + BACK_ADD_TO_BLACK_LIST)
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
      assertThat("verify add black list", response.jsonPath().get(""), is(sessionIDList));
    } else {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }
}
