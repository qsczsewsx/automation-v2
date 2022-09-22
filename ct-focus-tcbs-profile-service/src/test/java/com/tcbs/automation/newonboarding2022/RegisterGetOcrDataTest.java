package com.tcbs.automation.newonboarding2022;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.cas.OcrData;
import com.tcbs.automation.cas.TcbsUserOpenAccountQueue;
import common.CallApiUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.REGISTER_OCR_GET_DATA;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding2022/RegisterGetOcrData.csv", separator = '|')
public class RegisterGetOcrDataTest {

  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String referenceId;
  private String authenKey;
  private HashMap<String, Object> body;

  @Before
  public void before() {
    referenceId = syncData(referenceId);
    if (authenKey.equalsIgnoreCase("gen")) {
      authenKey = CallApiUtils.callRegisterConfirmPhoneApi(referenceId);
    } else {
      authenKey = syncData(authenKey);
    }
    body = new HashMap<>();
    body.put("referenceId", referenceId);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify register get ocr data")
  public void verifyRegisterGetOcrData() {

    System.out.println("Test Case: " + testCaseName);

    RequestSpecification requestSpecification = given()
      .baseUri(REGISTER_OCR_GET_DATA)
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
      if (testCaseName.contains("need upload other docs")) {
        assertThat(response.jsonPath().get("needUploadOther"), is(true));
      } else {
        assertThat(response.jsonPath().get("needUploadOther"), is(false));
      }
      Map<String, Object> mapOcrData = response.jsonPath().get("ocrData");
      OcrData ocrData = OcrData.getByTuoqId(TcbsUserOpenAccountQueue.getByPhone(referenceId.substring(0, 12)).getId().toString()).get(0);
      assertEquals(ocrData.getFullName(), mapOcrData.get("fullName"));
      assertEquals(ocrData.getDob(), mapOcrData.get("dob"));
      assertEquals(ocrData.getIdNumber(), mapOcrData.get("idNumber"));
      assertEquals(ocrData.getIssuePlace(), mapOcrData.get("idPlace"));
      assertEquals(ocrData.getIssueDate(), mapOcrData.get("idDate"));
      assertEquals(ocrData.getExpireDate(), mapOcrData.get("expireDate"));
      assertEquals(ocrData.getNationality(), mapOcrData.get("nationality"));
      assertEquals(ocrData.getHometownAddress(), mapOcrData.get("hometownAddress"));
    } else if (statusCode == 400) {
      assertEquals(errorMessage, response.jsonPath().get("message"));
    }
  }

}