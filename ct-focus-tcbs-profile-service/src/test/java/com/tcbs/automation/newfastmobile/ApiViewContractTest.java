package com.tcbs.automation.newfastmobile;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static common.CallApiUtils.*;
import static common.CommonUtils.*;
import static common.ProfileTools.TOKEN;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newfastmobile/ApiViewContract.csv", separator = '|')
public class ApiViewContractTest {

  private static String tcbsId;
  private static String idNumberVal;
  private final HashMap<String, Object> params = new HashMap<>();
  @Getter
  private String testCaseName;
  @Getter
  private int statusCode;
  private String typeValue;
  private String erroMsg;

  @BeforeClass
  public static void beforeTest() {
    clearCache(CLEAR_CACHE_REDIS.replace("{phoneNumber}", "0985652565"), "x-api-key", TOKEN);
    String prepareValue = String.valueOf(new Date().getTime());
    idNumberVal = prepareValue.substring(0, 12);
    LinkedHashMap<String, Object> body = getFMBRegisterBetaBody(idNumberVal);
    Response response = getFMBRegisterBetaResponse(body);
    tcbsId = response.jsonPath().getString("basicInfo.tcbsId");
    LinkedHashMap<String, Object> bodyAdvance = getUpgradeAdvancedBody();
    getFMBUpgradeAdvanceResponse(bodyAdvance, tcbsId);

  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api View Contract")
  public void verifyViewContractTest() {

    System.out.println("TestCaseName : " + testCaseName);
    String getTcbsId = getDesiredData(testCaseName, "tcbsId not exist in database", "10000017565", tcbsId);
    HashMap<String, Object> params = getViewContractApiParams(testCaseName, typeValue);
    Response response = callGetApiHasParams(FMB_VIEW_CONTRACT.replace("{tcbsId}", getTcbsId), "x-api-key", FMB_X_API_KEY, params);

    assertThat(response.getStatusCode(), is(statusCode));

    if (response.statusCode() == 200) {
      Map<String, Object> getResponse = response.jsonPath().getMap("");
      assertTrue(getResponse.containsKey("onboarding"));

    } else if (response.statusCode() == 400) {
      String actualMessage = response.jsonPath().get("message");
      assertEquals(erroMsg, actualMessage);
    }
  }

  public HashMap<String, Object> getViewContractApiParams(String testCaseName, String typeValue) {
    HashMap<String, Object> params = new HashMap<>();
    if (testCaseName.contains("missing param typeValue")) {
      params.put("", "");
    } else {
      params.put("type", typeValue);
    }
    return params;
  }

  @After
  public void afterTest() {
    deleteFMBRegisterBetaData("0985652565", idNumberVal, "nguyenvana@gmail.com");
    clearCache(CLEAR_CACHE_REDIS.replace("{phoneNumber}", "0985652565"), "x-api-key", TOKEN);
  }
}
