package com.tcbs.automation.newfastmobile;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.cas.TcbsUser;
import com.tcbs.automation.cas.TcbsUserTnc;
import common.CommonUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static common.CallApiUtils.*;
import static common.CommonUtils.*;
import static common.ProfileTools.TOKEN;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newfastmobile/ApiSignContract.csv", separator = '|')
public class ApiSignContractTest {

  private static String getTcbsIdAcc;
  private static String idNumberVal;
  @Getter
  private String testCaseName;
  @Getter
  private int statusCode;
  private String tcbsId;
  private String type;
  private String accountNo;
  private String accountName;
  private String bankCode;
  private String bankName;
  private String bankProvince;
  private String branchCode;
  private String bankType;
  private String autoTransfer;
  private String isIAPaid;
  private String isIA;
  private String message;
  private String action;

  @BeforeClass
  public static void beforeTest() {
    clearCache(CLEAR_CACHE_REDIS.replace("{phoneNumber}", "0985652565"), "x-api-key", TOKEN);
    String prepareValue = String.valueOf(new Date().getTime());
    idNumberVal = prepareValue.substring(0, 12);
    LinkedHashMap<String, Object> body = getFMBRegisterBetaBody(idNumberVal);
    Response response = getFMBRegisterBetaResponse(body);
    getTcbsIdAcc = response.jsonPath().getString("basicInfo.tcbsId");
    LinkedHashMap<String, Object> bodyAdvance = getUpgradeAdvancedBody();
    getFMBUpgradeAdvanceResponse(bodyAdvance, getTcbsIdAcc);
  }

  @AfterClass
  public static void afterTest() {
    deleteFMBRegisterBetaData("0985652565", idNumberVal, "nguyenvana@gmail.com");
    clearCache(CLEAR_CACHE_REDIS.replace("{phoneNumber}", "0985652565"), "x-api-key", TOKEN);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api Sign Contract")
  public void verifySignContractTest() {

    System.out.println("TestCaseName : " + testCaseName);

    tcbsId = CommonUtils.getDesiredTcbsId(tcbsId, getTcbsIdAcc);

    LinkedHashMap<String, Object> body = getSignContractBody(testCaseName, tcbsId, type);
    Gson gson = new Gson();

    RequestSpecification requestSpecification = given()
      .baseUri(FMB_SIGN_CONTRACT)
      .header("x-api-key", FMB_X_API_KEY)
      .contentType("application/json");

    Response response;

    if (testCaseName.contains("missing BODY")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(gson.toJson(body)).post();
    }

    assertThat(response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      String getResponse = response.getBody().prettyPrint();
      assertEquals(getResponse, message);
      if (type.equalsIgnoreCase("4")) {
        assertThat(TcbsUserTnc.getByUserId(TcbsUser.getByTcbsId(tcbsId).getId().toString()).getTncTcb(), is("Y"));
      }

    } else {
      assertEquals(message, response.jsonPath().get("message"));
    }
  }

  public LinkedHashMap<String, Object> getSignContractBody(String testCaseName, String tcbsId, String type) {

    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    LinkedHashMap<String, Object> bankAccount = new LinkedHashMap<>();
    LinkedHashMap<String, Object> regIA = new LinkedHashMap<>();
    List<LinkedHashMap<String, Object>> bankAccounts = new ArrayList<>();
    bankAccount.put("accountNo", accountNo);
    bankAccount.put("accountName", accountName);
    bankAccount.put("bankCode", bankCode);
    bankAccount.put("bankName", bankName);
    bankAccount.put("bankProvince", null);
    bankAccount.put("branchCode", null);
    bankAccount.put("bankType", bankType);
    bankAccounts.add(bankAccount);

    regIA.put("autoTransfer", autoTransfer);
    regIA.put("isIAPaid", isIAPaid);
    regIA.put("isIA", isIA);

    if (testCaseName.contains("missing param tcbsId")) {
      body.put("type", type);
      body.put("iaBankAccount", bankAccounts);
      body.put("action", action);
      body.put("iaStatus", regIA);
    } else if (testCaseName.contains("missing param type")) {
      body.put("tcbsId", tcbsId);
      body.put("iaBankAccount", bankAccounts);
      body.put("action", action);
      body.put("iaStatus", regIA);
    } else if (testCaseName.contains("only TNC contract")) {
      body.put("tcbsId", tcbsId);
      body.put("type", type);
    } else {
      body.put("tcbsId", tcbsId);
      body.put("type", type);
      body.put("iaBankAccount", bankAccounts);
      body.put("action", action);
      body.put("iaStatus", regIA);
    }
    return body;
  }
}
