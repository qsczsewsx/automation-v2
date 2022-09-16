package com.tcbs.automation.newonboarding.tci3;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.publicmatcher.HashMapMatcher;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.publicmatcher.CombinableMatcher.all;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/ApiGetInfoByCondition.csv", separator = '|')
public class ApiGetInfoByConditionTest {

  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMsg;
  private String searchValue;
  private HashMap<String, Object> params;

  @Before
  public void setup() {
    params = new HashMap<>();
    if (StringUtils.isNotEmpty(searchValue)) {
      params.put("searchValue", searchValue);
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api get info by condition test")
  public void verifyGetInfoByConditionTest() {
    System.out.println("TestCaseName : " + testCaseName);

    Response response = given()
      .baseUri(GET_INFO_BY_CONDITION)
      .header("x-api-key", (testCaseName.contains("invalid x-api-key") ? FMB_X_API_KEY : TCBSPROFILE_BACKENDWBLKEY))
      .contentType("application/json")
      .params(params)
      .get();

    assertThat(response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      if (!testCaseName.contains("not exsit")) {
        List<HashMap<String, Object>> listData = response.jsonPath().getList("");
        MatcherAssert.assertThat(listData.size(), is(greaterThan(0)));
        MatcherAssert.assertThat("verify data in response",
          listData.get(0),
          all(HashMapMatcher.has("personalInfo", is(notNullValue())))
            .and(HashMapMatcher.has("basicInfo", is(notNullValue())))
            .and(HashMapMatcher.has("accountStatus", is(notNullValue())))
            .and(HashMapMatcher.has("bankIaAccounts", is(notNullValue())))
        );
      } else if (statusCode == 400) {
        assertThat("verify error message", response.jsonPath().get("message"), is(errorMsg));
      }
    }
  }
}