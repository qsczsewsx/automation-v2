package com.tcbs.automation.newOnboardingPartnerShip;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.login.LoginApi;
import com.tcbs.automation.login.TheUserInfo;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.screenplay.Actor;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newOnboardingPartnerShip/ApiPartnerShipViewContract.csv", separator = '|')
public class PartnerShipViewContractTest {
  @Getter
  private String testCaseName;
  private int statusCode;

  private String errorMessage;
  private String partnerId;
  private String tcbsId;
  private String custodyCode;
  private String authoToken;
  private HashMap<String, Object> body;


  @Before

  public void setup() {

    Actor actor = Actor.named("logintoken");
    LoginApi.withCredentials(custodyCode, "abc123").performAs(actor);
    authoToken = TheUserInfo.aboutLoginData().answeredBy(actor).getToken();

    partnerId = syncData(partnerId);
    tcbsId = syncData(tcbsId);
    body = new HashMap<>();

    body.put("partnerId", partnerId);
    body.put("tcbsId", tcbsId);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify partnership view contract TNC")
  public void partnerShipViewContract() {
    System.out.println("Test Case: " + testCaseName);
    RequestSpecification requestSpecification = given()
      .baseUri(PARTNERSHIP_VIEW_CONTRACT)
      .header("Authorization", "Bearer " + (testCaseName.contains("invalid Authorization") ? FMB_X_API_KEY : authoToken));

    Response response;

    if (testCaseName.contains("missing body")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(body).post();
    }

    assertEquals(statusCode, response.getStatusCode());
    if (statusCode == 200 && testCaseName.contains("case successful")) {
      assertThat("verify", response.jsonPath().get(partnerId + "_LINK_ACCOUNT"), is(notNullValue()));
    } else if (testCaseName.contains("invalid Authorization")) {
      assertEquals(statusCode, response.statusCode());
    } else {
      assertEquals(errorMessage, response.jsonPath().get("message"));
    }
  }
}


