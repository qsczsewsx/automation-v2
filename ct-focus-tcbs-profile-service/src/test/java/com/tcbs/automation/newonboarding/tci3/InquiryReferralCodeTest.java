package com.tcbs.automation.newonboarding.tci3;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.INQUIRY_REFERRAL_CODE;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/InquiryReferralCode.csv", separator = '|')
public class InquiryReferralCodeTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String referralCode;
  private String campaignCode;
  private HashMap<String, Object> body;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api inquiry referral code")
  public void performTest() {
    body = new HashMap<>();
    body.put("referralCode", referralCode);

    Gson gson = new Gson();

    RequestSpecification requestSpecification = given()
      .baseUri(INQUIRY_REFERRAL_CODE)
      .contentType("application/json");

    Response response;

    if (testCaseName.contains("missing BODY")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(gson.toJson(body)).post();
    }

    assertThat("verify status", response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      assertThat("verify valid", response.jsonPath().get("valid"), is(true));
      assertThat("verify campaignCode", response.jsonPath().get("campaignCode"), is(campaignCode));
    } else if (statusCode == 400) {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
      if (!testCaseName.contains("missing BODY")) {
        assertThat("verify invalid", response.jsonPath().get("valid"), is(false));
      }
    }
  }
}
