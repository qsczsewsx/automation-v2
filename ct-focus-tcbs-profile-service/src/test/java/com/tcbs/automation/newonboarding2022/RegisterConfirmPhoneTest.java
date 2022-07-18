package com.tcbs.automation.newonboarding2022;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.cas.TcbsUserOpenaccountQueue;
import common.CommonUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.REGISTER_CONFIRM_PHONE;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding2022/RegisterConfirmPhone.csv", separator = '|')
public class RegisterConfirmPhoneTest {

  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String phoneCode;
  private String phoneNumber;
  private String otp;
  private String otpId;
  private HashMap<String, Object> body;

  @Before
  public void before() {
    String suffixPhone = CommonUtils.genPhoneNumberByDateTime();

    if (phoneNumber.equalsIgnoreCase("gen")) {
      phoneNumber = suffixPhone;
    } else {
      phoneNumber = syncData(phoneNumber);
    }

    phoneCode = syncData(phoneCode);
    otp = syncData(otp);
    otpId = syncData(otpId);

    body = new HashMap<>();
    body.put("phoneNumber", phoneNumber);
    body.put("phoneCode", phoneCode);
    body.put("otp", otp);
    body.put("otpId", otpId);

    if (testCaseName.contains("case authenKey expired")) {
      Response response = given()
        .baseUri(REGISTER_CONFIRM_PHONE)
        .contentType("application/json")
        .body(body)
        .post();

      body.put("referenceId", response.jsonPath().get("referenceId"));
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify register confirm phone")
  public void verifyRegisterConfirmPhone() {
    System.out.println("Test Case: " + testCaseName);
    RequestSpecification requestSpecification = given()
      .baseUri(REGISTER_CONFIRM_PHONE)
      .contentType("application/json")
      .when();

    Response response;
    Gson gson = new Gson();
    if (testCaseName.contains("missing body")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(gson.toJson(body)).post();
    }

    assertEquals(statusCode, response.getStatusCode());
    if (statusCode == 200) {
      assertNotNull(response.jsonPath().get("authenKey").toString());
      assertEquals(TcbsUserOpenaccountQueue.getByPhone(phoneCode + phoneNumber).getReferenceid(), response.jsonPath().get("referenceId"));
    } else {
      assertEquals(errorMessage, response.jsonPath().get("message"));
    }
  }

  @After
  public void clearData() {
    // Clear data
    if (statusCode == 200) {
      TcbsUserOpenaccountQueue.deleteByPhone(phoneCode + phoneNumber);
    }
  }
}