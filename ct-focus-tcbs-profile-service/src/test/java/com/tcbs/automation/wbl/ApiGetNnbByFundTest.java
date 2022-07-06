package com.tcbs.automation.wbl;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.publicmatcher.HashMapMatcher;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.publicmatcher.CombinableMatcher.all;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/wbl/ApiGetNnbByFund.csv", separator = '|')
public class ApiGetNnbByFundTest {

  @Getter
  private String testCaseName;
  private String fundCode;
  private int statusCode;
  private String errorMessage;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api get NNB by Fund")
  public void apiGetNnbByFund() {
    System.out.println("Testcase Name: " + testCaseName);

    RequestSpecification requestSpecification = SerenityRest.given()
      .baseUri(GET_NNB_BY_FUND)
      .header("x-api-key", testCaseName.contains("x-api-key") ? FMB_X_API_KEY : STP_X_API_KEY)
      .header("Cookie", COOKIE_NNB)
      .contentType("application/json");

    if (!testCaseName.contains("missing fundCode param")) {
      requestSpecification.param("fundCode", fundCode);
    }
    Response response = requestSpecification
      .when()
      .get();

    assertThat(response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      if (testCaseName.contains("fundCode as")) {
        List<HashMap<String, Object>> listData = response.jsonPath().getList("");
        assertThat(listData.size(), is(greaterThan(0)));
        assertThat("verify data in response",
          listData.get(0),
          all(HashMapMatcher.has("wblUserId", is(notNullValue())))
            .and(HashMapMatcher.has("policyUserId", is(notNullValue())))
            .and(HashMapMatcher.has("policyCode", is(notNullValue())))
            .and(HashMapMatcher.has("policyUserType", anyOf(is("NNB"), is("NLQ"))))
        );
      }
    } else {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }
}