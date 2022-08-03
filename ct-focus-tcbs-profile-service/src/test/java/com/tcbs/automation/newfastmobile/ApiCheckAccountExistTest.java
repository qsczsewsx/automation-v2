package com.tcbs.automation.newfastmobile;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.cas.TcbsIdentification;
import com.tcbs.automation.cas.TcbsUser;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.LinkedHashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.FMB_CHECK_ACCOUNT_EXIST;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.FMB_X_API_KEY;
import static common.CommonUtils.*;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newfastmobile/ApiCheckAccountExist.csv", separator = '|')
public class ApiCheckAccountExistTest {

  @Getter
  private String testCaseName;
  @Getter
  private int statusCode;
  private String keys;
  private String value;
  private String erroMsg;
  private String userId;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api Check Account Exist")
  public void verifyCheckAccountExistTest() {

    System.out.println("TestCaseName : " + testCaseName);

    LinkedHashMap<String, Object> body = getCheckAccountExistBody(testCaseName, keys, value);
    Gson gson = new Gson();

    Response response = given()
      .baseUri(FMB_CHECK_ACCOUNT_EXIST)
      .header("x-api-key", FMB_X_API_KEY)
      .contentType("application/json")
      .body(gson.toJson(body))
      .when()
      .post();

    assertThat(response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {

      verifyOnboardingStatus(response, value);
      verifyFMBBasicInfo(response, "basicInfo", "userId", userId);
      verifyBankSource(response, "iaBankAccount", "userId", userId);
      verifyTncStatus(response);

    } else if (statusCode == 400) {
      assertEquals(erroMsg, response.jsonPath().get("message"));
    }
  }

  public LinkedHashMap<String, Object> getCheckAccountExistBody(String testCaseName, String keys, String value) {

    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    if (testCaseName.contains("missing Body ")) {
      body = null;
    } else if (testCaseName.contains("missing param key")) {
      body.put("value", value);
    } else if (testCaseName.contains("missing param keysValue")) {
      String[] keysValue = keys.split(",");
      body.put("keys", keysValue);
    } else {
      String[] keysValue = keys.split(",");
      body.put("keys", keysValue);
      body.put("value", value);
    }
    return body;
  }

  public void verifyOnboardingStatus(Response response, String value) {

    if (TcbsIdentification.getListByIdNumber(value).size() != 0) {
      userId = TcbsIdentification.getByIdNumber(value).getUserId().toString();
    } else {
      userId = TcbsUser.getByPhoneNumber(value).getId().toString();
    }
    verifyFMBOnboardingStatus(response, "accountStatus.onboardingStatus.", "userId", userId);
  }

  public void verifyTncStatus(Response response) {
    assertThat(response.jsonPath().get("accountStatus.tncTcb"), anyOf(is("Y"), is("N")));
  }

}
