package com.tcbs.automation.newonboarding2022;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.cas.OcrData;
import com.tcbs.automation.cas.TcbsUserOpenAccountQueue;
import com.tcbs.automation.cas.TcbsUserOpenAccountQueueUpload;
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

import java.math.BigDecimal;
import java.util.HashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.REGISTER_UPLOAD_IDENTIFY;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding2022/ApiRegisterUploadIdentify.csv", separator = '|')
public class ApiRegisterUploadIdentifyTest {

  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String frontIdentity;
  private String backIdentity;
  private String referenceId;
  private String authenKey;
  private BigDecimal tuoqId;
  private HashMap<String, Object> body;


  @Before
  public void before() {
    referenceId = syncData(referenceId);
    if (authenKey.equalsIgnoreCase("gen")) {
      if (testCaseName.contains("referenceId")) {
        String refIdPp = "+84784357139F22092022122727";
        authenKey = CallApiUtils.callRegisterConfirmPhoneApi(refIdPp);
      } else {
        authenKey = CallApiUtils.callRegisterConfirmPhoneApi(referenceId);
      }
    } else {
      authenKey = syncData(authenKey);
    }

    frontIdentity = CommonUtils.getFileContents(frontIdentity);
    backIdentity = CommonUtils.getFileContents(backIdentity);

    body = new HashMap<>();
    body.put("frontIdentity", frontIdentity);
    body.put("backIdentity", backIdentity);
    body.put("referenceId", referenceId);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify register upload identify")
  public void verifyRegisterUploadIdentify() {
    System.out.println("Test Case: " + testCaseName);
    RequestSpecification requestSpecification = given()
      .baseUri(REGISTER_UPLOAD_IDENTIFY)
      .contentType("application/json")
      .header("Authorization", "Bearer " + authenKey);

    Response response;
    Gson gson = new Gson();
    if (testCaseName.contains("missing body")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(gson.toJson(body)).post();
    }

    assertEquals(statusCode, response.getStatusCode());
    if (statusCode == 200) {
      tuoqId = TcbsUserOpenAccountQueue.getByReferenceId(referenceId).getId();

      assertThat("verify exist data queueUpload table: frontIdentity", TcbsUserOpenAccountQueueUpload.getByTuoqIdAndFileType(tuoqId.toString(), "SCAN_ID_IMAGE_FRONT").get(0).getId(),
        is(notNullValue()));
      assertThat("verify exist data queueUpload table: backIdentity", TcbsUserOpenAccountQueueUpload.getByTuoqIdAndFileType(tuoqId.toString(), "SCAN_ID_IMAGE_BACK").get(0).getId(),
        is(notNullValue()));
//      assertThat("verify exist data ocrData table", OcrData.getByTuoqId(tuoqId).getId(), is(notNullValue())); //OCR not apply env sit
//      assertThat("verify exist data ocrDataHis table", OcrDataHis.getByTuoqId(tuoqId).getId(), is(notNullValue()));//OCR not apply env sit
    } else {
      assertEquals(errorMessage, response.jsonPath().get("message"));
    }

  }

  @After
  public void clearData() {
    // Clear data
    if (statusCode == 200) {
      TcbsUserOpenAccountQueueUpload.deleteByTuoqID(tuoqId);
      OcrData.deleteByTuoqId(tuoqId);
    }
  }
}