package com.tcbs.automation.newonboarding.tci3;


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

import java.util.Date;
import java.util.HashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static common.CallApiUtils.callPostApiHasBody;
import static common.CallApiUtils.callProfileRegisterApi;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/ProfilePendingActivate.csv", separator = '|')
public class ProfilePendingActivateTest {
  private final HashMap<String, Object> body = new HashMap<>();
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String transactionId;
  private String otp;
  private String token;
  private String bankAccount;
  private String phoneCode;

  @Before
  public void before() {
    phoneCode = syncData(phoneCode);
    String prepareValue = String.valueOf(new Date().getTime() / 10);
    String suffixPhone = "2" + prepareValue.substring(4);
    String phoneConfirm = phoneCode + suffixPhone;
    String code105C = "105C" + prepareValue.substring(6, 11) + "M";
    String accountNumber = "85" + prepareValue;

    if (testCaseName.contains("missing BODY")) {
      transactionId = syncData(transactionId);
        otp = syncData(otp);
      token = syncData(token);
    } else {
      // Call API confirm booking fancy 105C
      HashMap<String, Object> bodyFancy = CommonUtils.getConfirmBookingFancy105CBody(testCaseName, phoneConfirm, code105C);
      callPostApiHasBody(CONFIRM_BOOKING_FANCY_105C, "", "", bodyFancy);

      // Call API Register
      Response res = callProfileRegisterApi(prepareValue, suffixPhone, phoneCode, code105C, accountNumber);

      if (testCaseName.contains("token does not exist")) {
        token = syncData(token);
      } else {
        token = res.jsonPath().get("data.token").toString();
      }
      transactionId = res.jsonPath().get("data.otpId").toString();
      otp = syncData(otp);
    }
    body.put("transactionId", transactionId);
    body.put("otp", otp);
    body.put("token", token);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api profile pending activate")
  public void perfomTest() {

    RequestSpecification requestSpecification = SerenityRest.given()
      .baseUri(TCBSPROFILE_PUB_API + TCBSPROFILE_PENDING + TCBSPROFILE_ACTIVATE)
      .contentType("application/json");

    Response response;

    if (testCaseName.contains("missing BODY")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(body).post();
    }

    if (!(testCaseName.contains("valid request") && response.getStatusCode() != 200)) {
      assertThat(response.getStatusCode(), is(statusCode));
    }

    if (statusCode == 400 || (testCaseName.contains("valid request") && response.getStatusCode() != 200)) {
      assertThat(response.jsonPath().get("message"), is(errorMessage));
    } else {
      assertThat(response.jsonPath().get("message"), is("Actived pending profile success"));
    }
  }
}
