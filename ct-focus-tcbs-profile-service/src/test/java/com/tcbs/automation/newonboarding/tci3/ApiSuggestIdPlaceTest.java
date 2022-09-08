package com.tcbs.automation.newonboarding.tci3;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.SUGGEST_ID_PLACE;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/ApiSuggestIdPlace.csv", separator = '|')
public class ApiSuggestIdPlaceTest {

  @Getter
  private String testCaseName;
  private int statusCode;
  private String identityNo;
  private String errorMsg;
  private HashMap<String, Object> body;
  private String issueDate;

  @Before
  public void setup() {
    identityNo = syncData((identityNo));
    issueDate = syncData(issueDate);
    body = new HashMap<>();
    body.put("identityNo", identityNo);
    body.put("issueDate", issueDate);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api suggest id place test")
  public void verifySuggestIdPlaceTest() {
    System.out.println("TestCaseName : " + testCaseName);

    RequestSpecification requestSpecification = given()
      .baseUri(SUGGEST_ID_PLACE)
      .contentType("application/json");

    Response response;
    if (testCaseName.contains("missing body")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(body).post();
    }

    assertThat(response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      List<Map> listIdPlace = response.jsonPath().get("");
      assertThat("verify list id place", listIdPlace, is(notNullValue()));
    } else if (statusCode == 400) {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMsg));
    }

  }
}