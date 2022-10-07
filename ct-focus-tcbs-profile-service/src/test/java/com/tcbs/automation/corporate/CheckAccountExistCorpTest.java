package com.tcbs.automation.corporate;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.cas.TcbsIdentification;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.LinkedHashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.CORPORATE_CHECK_ACCOUNT_EXIST;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.FMB_X_API_KEY;
import static common.CommonUtils.*;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/corporate/CheckAccountExistCorp.csv", separator = '|')
public class CheckAccountExistCorpTest {

  @Getter
  private String testCaseName;
  @Getter
  private int statusCode;
  private String keys;
  private String value;
  private String errorMsg;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api Check Account Exist Corporate")
  public void verifyCheckAccountExistCorpTest() {

    System.out.println("TestCaseName : " + testCaseName);

    LinkedHashMap<String, Object> body = getCheckAccountCorpExistBody(testCaseName, keys, value);
    Gson gson = new Gson();

    Response response = given()
      .baseUri(CORPORATE_CHECK_ACCOUNT_EXIST)
      .header("x-api-key", FMB_X_API_KEY)
      .contentType("application/json")
      .body(gson.toJson(body))
      .post();

    assertThat(response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      String userId = TcbsIdentification.getByIdNumber(value).getUserId().toString();
      verifyFMBOnboardingStatus(response, "accountStatus.onboardingStatus.", "userId", userId);
      verifyFMBBasicInfo(response, "basicInfo", "userId", userId);
      verifyBankSource(response, "iaBankAccount", "userId", userId);
    } else if (statusCode == 400) {
      assertEquals(errorMsg, response.jsonPath().get("message"));
    }

  }

  public LinkedHashMap<String, Object> getCheckAccountCorpExistBody(String testCaseName, String keys, String value) {

    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    if (testCaseName.contains("missing body")) {
      body = null;
    } else if (testCaseName.contains("missing param key")) {
      body.put("value", value);
    } else if (testCaseName.contains("missing param keysValue")) {
      body.put("keys", keys);
    } else {
      body.put("keys", keys);
      body.put("value", value);
    }
    return body;
  }

}