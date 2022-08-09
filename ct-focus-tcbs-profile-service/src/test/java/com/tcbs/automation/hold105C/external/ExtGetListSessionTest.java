package com.tcbs.automation.hold105C.external;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import common.CommonUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.EXT_GET_SESSION_LIST;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/external/ExtGetListSession.csv", separator = '|')
public class ExtGetListSessionTest {

  @Getter
  private String testCaseName;
  @Getter
  private String token;
  private String start;
  private String end;
  private int statusCode;
  private String errorMessage;

  @Before
  public void before() {
    if (token.equalsIgnoreCase("token")) {
      token = CommonUtils.getTokenAuthenSOtp("10000025745");
    } else {
      token = syncData(token);
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api add to black list")
  public void verifyExtGetListSessionTest() {

    System.out.println("TestCaseName : " + testCaseName);
    RequestSpecification requestSpecification = SerenityRest.given()
      .baseUri(EXT_GET_SESSION_LIST)
      .header("Authorization", "Bearer " + token)
      .contentType("application/json");

    start = syncData(start);
    end = syncData(end);
    if (testCaseName.contains("missing end param")) {
      requestSpecification.param("start", start);
    } else if (testCaseName.contains("missing start param")) {
      requestSpecification.param("end", end);
    } else if (!testCaseName.contains("missing all param")) {
      requestSpecification.param("start", start).param("end", end);
    }
    Response response = requestSpecification
      .when()
      .get();

    assertThat(response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      CommonUtils.verifyGetListSession(response);
    } else if (statusCode == 400) {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }
}
