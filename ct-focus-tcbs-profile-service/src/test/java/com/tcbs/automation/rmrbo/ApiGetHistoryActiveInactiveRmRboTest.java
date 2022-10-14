package com.tcbs.automation.rmrbo;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.publicmatcher.HashMapMatcher;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.publicmatcher.CombinableMatcher.all;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/rmrbo/ApiGetHistoryActiveInactiveRmRbo.csv", separator = '|')
public class ApiGetHistoryActiveInactiveRmRboTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String date;
  private String code105c;
  private String tcbsId;
  private HashMap<String, Object> params;

  @Before
  public void setup() {
    params = new HashMap<>();
    if (StringUtils.isNotEmpty(date)) {
      params.put("date", date);
    }
    if (StringUtils.isNotEmpty(tcbsId)) {
      params.put("tcbsId", tcbsId);
    }
    if (StringUtils.isNotEmpty(code105c)) {
      params.put("username", code105c);
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify API get history active inactive rmrbo")
  public void getHistoryActiveInactiveRmRbo() {
    System.out.println("Test Case: " + testCaseName);
    Response response = given()
      .baseUri(GET_HISTORY_ACTIVE_INACTIVE)
      .header("x-api-key", testCaseName.contains("invalid x-api-key") ? FMB_X_API_KEY : PARTNERSHIP_X_API_KEY)
      .contentType("application/json")
      .params(params)
      .get();

    assertEquals(statusCode, response.getStatusCode());
    if (statusCode == 200) {
      if (!testCaseName.contains("not exsit")) {
        List<HashMap<String, Object>> listData = response.jsonPath().getList("");
        assertThat(listData.size(), is(greaterThan(0)));
        assertThat("verify data in response",
          listData.get(0),
          all(HashMapMatcher.has("tcbsId", is(notNullValue())))
            .and(HashMapMatcher.has("role", anyOf(is("RM"), is("RBO"))))
            .and(HashMapMatcher.has("status", anyOf(is(0), is(1))))
            .and(HashMapMatcher.has("createdDate", is(notNullValue())))
        );
      }
    } else {
      assertEquals(errorMessage, response.jsonPath().get("message"));
    }
  }
}


