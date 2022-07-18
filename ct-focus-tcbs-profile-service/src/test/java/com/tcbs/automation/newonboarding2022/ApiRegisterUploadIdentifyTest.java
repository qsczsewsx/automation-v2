package com.tcbs.automation.newonboarding2022;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.cas.TcbsUserOpenaccountQueue;
import com.tcbs.automation.cas.TcbsUserOpenaccountQueueUpload;
import common.CallApiUtils;
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
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.REGISTER_UPLOAD_IDENTIFY;
import static com.tcbs.automation.tools.ConvertUtils.fileTxtToString;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding2022/ApiRegisterUploadIdentify.csv", separator = '|')
public class ApiRegisterUploadIdentifyTest {

  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String fontIdentity;
  private String backIdentity;
  private String referenceId;
  private String authenKey;
  private HashMap<String, Object> body;

  @Before
  public void before() {

    CallApiUtils.callConfirmOtpApi();
    if (authenKey.equalsIgnoreCase("gen")) {
      authenKey = CallApiUtils.callConfirmOtpApi().jsonPath().get("authenKey");
    } else {
      authenKey = syncData(authenKey);
    }

    if (referenceId.equalsIgnoreCase("gen")) {
      referenceId = CallApiUtils.callConfirmOtpApi().jsonPath().get("referenceId");
    } else {
      authenKey = syncData(authenKey);
    }

    fontIdentity = getFileContents(fontIdentity);
    backIdentity = getFileContents(backIdentity);

    body = new HashMap<>();
    body.put("fontIdentity", fontIdentity);
    body.put("backIdentity", backIdentity);
    body.put("referenceId", referenceId);
    body.put("authenKey", authenKey);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify register upload identify")
  public void verifyRegisterUploadIdentify() {
    System.out.println("Test Case: " + testCaseName);
    RequestSpecification requestSpecification = given()
      .baseUri(REGISTER_UPLOAD_IDENTIFY)
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
//      assertNotNull(response.jsonPath().get("authenKey").toString());
//      assertEquals(TcbsUserOpenaccountQueueUpload.getFileUploadIdentify().getReferenceid(), response.jsonPath().get("referenceId"));
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

  public String getFileContents(String fileContents) {
    if (fileContents.equalsIgnoreCase("front")) {
      fileContents = fileTxtToString("src/test/resources/requestBody/FrontFileContent");
    } else if (fileContents.equalsIgnoreCase("com/tcbs/automation/back")) {
      fileContents = fileTxtToString("src/test/resources/requestBody/BackFileContent");
    }
    return fileContents;
  }


}