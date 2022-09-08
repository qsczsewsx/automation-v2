package com.tcbs.automation.newonboarding.tci3;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.publicmatcher.HashMapMatcher;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.publicmatcher.CombinableMatcher.all;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/ApiSearchByCondition.csv", separator = '|')
public class ApiSearchByConditionTest {

  @Getter
  private String testCaseName;
  private int statusCode;
  private String condition;
  private String errorMsg;
  private HashMap<String, Object> body;

  @Before
  public void setup() {
    condition = syncData((condition));
    body = new HashMap<>();
    body.put("condition", condition);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api search by condition test")
  public void verifySearchByConditionTest() {
    System.out.println("TestCaseName : " + testCaseName);

    RequestSpecification requestSpecification = given()
      .baseUri(SEARCH_BY_CONDITION)
      .header("x-api-key", (testCaseName.contains("invalid x-api-key") ? FMB_X_API_KEY : TCBSPROFILE_BACKENDWBLKEY))
      .contentType("application/json");

    Response response;
    if (testCaseName.contains("missing body")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(body).post();
    }

    assertThat(response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      if (!testCaseName.contains("not exsit")) {
        List<HashMap<String, Object>> listData = response.jsonPath().getList("content");
        MatcherAssert.assertThat(listData.size(), is(greaterThan(0)));
        MatcherAssert.assertThat("verify data in response",
          listData.get(0),
          all(HashMapMatcher.has("personalInfo", is(notNullValue())))
            .and(HashMapMatcher.has("basicInfo", is(notNullValue())))
            .and(HashMapMatcher.has("accountStatus", is(notNullValue())))
        );
      } else if (statusCode == 400) {
        assertThat("verify error message", response.jsonPath().get("message"), is(errorMsg));
      }
    }
  }
}