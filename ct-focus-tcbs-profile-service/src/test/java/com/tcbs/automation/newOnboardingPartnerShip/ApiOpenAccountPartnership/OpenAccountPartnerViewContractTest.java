package com.tcbs.automation.newOnboardingPartnerShip.ApiOpenAccountPartnership;

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
@UseTestDataFrom(value = "data/newOnboardingPartnerShip/ApiOpenAccountPartnership/ApiOpenAccountPartnerViewContract.csv", separator = '|')
public class OpenAccountPartnerViewContractTest {
  @Getter
  private String testCaseName;
  private int statusCode;

  private String errorMessage;
  private String partnerId;
  private String code105C;
  private HashMap<String, Object> body;

  @Before

  public void setup() {

    partnerId = syncData(partnerId);
    code105C = syncData(code105C);

    body = new HashMap<>();

    body.put("partnerId", partnerId);
    body.put("code105C", code105C);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify open account partnership view contract")
  public void partnerShipViewContract() {
    System.out.println("Test Case: " + testCaseName);
    RequestSpecification requestSpecification = given()
      .baseUri(OPEN_ACCOUNT_PARTNER_VIEW_CONTRACT)
      .header("x-api-key", (testCaseName.contains("invalid x-api-key") ? FMB_X_API_KEY : PARTNERSHIP_X_API_KEY));

    Response response;

    if (testCaseName.contains("missing body")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(body).post();
    }

    assertEquals(statusCode, response.getStatusCode());
    if (statusCode == 200) {
      assertThat("verify onboarding contract", response.jsonPath().get("onboarding"), is(notNullValue()));
      assertThat("verify derivative_account contract", response.jsonPath().get("derivative_account"), is(notNullValue()));
    }  else {
      assertEquals(errorMessage, response.jsonPath().get("message"));
    }
  }
}


