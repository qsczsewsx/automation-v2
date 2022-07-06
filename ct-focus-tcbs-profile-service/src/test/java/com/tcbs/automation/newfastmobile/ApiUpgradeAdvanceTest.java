package com.tcbs.automation.newfastmobile;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.cas.TcbsUser;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static common.CallApiUtils.clearCache;
import static common.CallApiUtils.getFMBRegisterBetaResponse;
import static common.CommonUtils.*;
import static common.ProfileTools.TOKEN;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newfastmobile/ApiUpgradeAdvance.csv", separator = '|')
public class ApiUpgradeAdvanceTest {

  @Getter
  private String testCaseName;
  @Getter
  private int statusCode;
  private String contactAddress;
  private String province;
  private String frontIdentityFilePath;
  private String backIdentityFilePath;
  private String frontIdentity;
  private String backIdentity;
  private String message;
  private LinkedHashMap<String, Object> body = new LinkedHashMap<>();
  private String userId;
  private String getTcbsId;
  private String tcbsId;
  private String idNumberVal;

  @Before
  public void beforeTest() {
    clearCache(CLEAR_CACHE_REDIS.replace("{phoneNumber}", "0985652565"), "x-api-key", TOKEN);
    String prepareValue = String.valueOf(new Date().getTime());
    idNumberVal = prepareValue.substring(0, 12);
    LinkedHashMap<String, Object> body = getFMBRegisterBetaBody(idNumberVal);
    Response response = getFMBRegisterBetaResponse(body);
    tcbsId = response.jsonPath().getString("basicInfo.tcbsId");
    clearCache(CLEAR_CACHE_REDIS.replace("{phoneNumber}", "0985652565"), "x-api-key", TOKEN);

  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api Upgrade Advance")
  public void verifyUpgradeAdvanceTest() {

    System.out.println("TestCaseName : " + testCaseName);

    getTcbsId = getDesiredData(testCaseName, "invalid tcbsid", "10000017565", tcbsId);

    body = getUpgradeAdvancedBody(testCaseName);
    Gson gson = new Gson();

    Response response = given()
      .baseUri(FMB_UPGRADE_ADVANCED.replace("{tcbsId}", getTcbsId))
      .header("x-api-key", FMB_X_API_KEY)
      .contentType("application/json")
      .body(gson.toJson(body))
      .when()
      .post();

    assertThat(response.getStatusCode(), is(statusCode));

    if (response.statusCode() == 200) {

      verifyBasicInfo(response, tcbsId);
      verifyOnboardingStatus(response, tcbsId);

    } else if (response.statusCode() == 400) {

      String actualMessage = response.jsonPath().get("message");
      assertEquals(message, actualMessage);
    }
  }

  public LinkedHashMap<String, Object> getUpgradeAdvancedBody(String testCaseName) {

    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    LinkedHashMap<String, Object> personalInfo = new LinkedHashMap<>();
    LinkedHashMap<String, Object> identityCard = new LinkedHashMap<>();
    if (testCaseName.contains("missing Body")) {
      body = new LinkedHashMap<>();
    } else {
      frontIdentity = getFileContents(frontIdentity);
      backIdentity = getFileContents(backIdentity);
      identityCard.put("frontIdentityFilePath", frontIdentityFilePath);
      identityCard.put("backIdentityFilePath", backIdentityFilePath);
      identityCard.put("frontIdentity", frontIdentity);
      identityCard.put("backIdentity", backIdentity);

      personalInfo.put("contactAddress", contactAddress);
      personalInfo.put("province", province);
      personalInfo.put("identityCard", identityCard);
      body.put("personalInfo", personalInfo);
    }
    return body;
  }

  public void verifyOnboardingStatus(Response response, String tcbsId) {
    userId = TcbsUser.getByTcbsId(tcbsId).getId().toString();
    verifyFMBOnboardingStatus(response, "accountStatus.onboardingStatus.", "userId", userId);
  }

  public void verifyBasicInfo(Response response, String tcbsId) {

    Map<String, Object> getResponse = response.jsonPath().getMap("basicInfo");
    TcbsUser tcbsUser = TcbsUser.getByTcbsId(tcbsId);
    assertEquals(tcbsUser.getTcbsid(), getResponse.get("tcbsId"));
    assertEquals(tcbsUser.getUsername(), getResponse.get("code105C"));
    String status = getUserStatus(tcbsUser.getAccountStatus().toString());
    assertEquals(status, getResponse.get("status"));
    String type = getUserType(tcbsUser.getCustype().toString());
    assertEquals(type, getResponse.get("type"));
    String accountType = getAccountType(tcbsUser.getUsername());
    assertEquals(accountType, getResponse.get("accountType"));
  }

  @After
  public void afterTest() {
    deleteFMBRegisterBetaData("0985652565", idNumberVal, "nguyenvana@gmail.com");
    clearCache(CLEAR_CACHE_REDIS.replace("{phoneNumber}", "0985652565"), "x-api-key", TOKEN);
    clearCache(CLEAR_CACHE, "x-api-key", API_KEY);
  }
}
