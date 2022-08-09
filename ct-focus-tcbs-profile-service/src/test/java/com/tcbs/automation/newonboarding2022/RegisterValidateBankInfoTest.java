package com.tcbs.automation.newonboarding2022;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import common.CommonUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.rest.RestRequests;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.API_VALIDATE_PHONE_EMAIL;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.REGISTER_VALIDATE_BANK_INFO;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static org.junit.Assert.assertEquals;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding2022/RegisterValidateBankInfo.csv", separator = '|')
public class RegisterValidateBankInfoTest {

  @Getter
  private String testCaseName;
  private int statusCode;

  private String errorMessage;
  private String accountNo;
  private String accountName;
  private String bankCode;
  private String bankName;
  private String bankProvince;
  private String bankType;
  private String branchCode;
  private HashMap<String, Object> body;


  @Before
  public void setup() {
    accountNo = syncData(accountNo);
    accountName = syncData(accountName);
    bankCode = syncData(bankCode);
    bankName = syncData(bankName);
    bankProvince = syncData(bankProvince);
    bankType = syncData(bankType);
    branchCode = syncData(branchCode);

    body = new HashMap<>();
    body.put("accountNo", accountNo);
    body.put("accountName", accountName);
    body.put("bankCode", bankCode);
    body.put("bankName", bankName);
    body.put("bankProvince", bankProvince);
    body.put("bankType", bankType);
    body.put("branchCode", branchCode);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify validate bank info")
  public void validateBankInfo() {

    System.out.println("Test Case: " + testCaseName);

    RequestSpecification requestSpecification = RestRequests.given()
      .baseUri(REGISTER_VALIDATE_BANK_INFO)
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
      if (testCaseName.contains("success")) {
        assertEquals("true", response.jsonPath().get("isValid").toString());
      } else {
        assertEquals("false", response.jsonPath().get("isValid").toString());
      }
    }
    assertEquals(errorMessage, response.jsonPath().get("message"));
  }
}
