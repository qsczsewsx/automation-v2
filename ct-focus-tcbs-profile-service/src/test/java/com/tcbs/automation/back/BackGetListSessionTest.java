package com.tcbs.automation.back;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import common.CommonUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.BACK_GET_SESSION_LIST;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static common.ProfileTools.MAKER_TOKEN;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/back/BackGetListSession.csv", separator = '|')
public class BackGetListSessionTest {

  @Getter
  private String testCaseName;
  @Getter
  private String start;
  private String end;
  private String tcbsId;
  private int statusCode;
  private String errorMessage;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api add to black list")
  public void verifyExtGetListSessionTest() {

    System.out.println("TestCaseName : " + testCaseName);
    RequestSpecification requestSpecification = SerenityRest.given()
      .baseUri(BACK_GET_SESSION_LIST)
      .header("Authorization", "Bearer " + MAKER_TOKEN)
      .contentType("application/json");

    final String TCBSID = "tcbsId";
    final String START = "start";
    final String END = "end";
    start = syncData(start);
    end = syncData(end);
    tcbsId = syncData(tcbsId);
    if (testCaseName.contains("missing end param")) {
      requestSpecification.param(TCBSID, tcbsId).param(START, start);
    } else if (testCaseName.contains("missing start param")) {
      requestSpecification.param(TCBSID, tcbsId).param(END, end);
    } else if (testCaseName.contains("missing tcbsId param")) {
      requestSpecification.param(START, start).param(END, end);
    } else if (!testCaseName.contains("missing all param")) {
      requestSpecification.param(TCBSID, tcbsId).param(START, start).param(END, end);
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
