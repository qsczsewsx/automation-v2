package com.tcbs.automation.newonboarding.ia_channel;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.ObTask;
import com.tcbs.automation.cas.OcrData;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.OCR_SIGNATURE_TOKEN;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.OCR_WEBHOOK;
import static com.tcbs.automation.tools.ConvertUtils.fileTxtToString;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/OcrWebHook.csv", separator = '|')
public class OcrWebHookTest {

  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMsg;
  private String request_id;
  private String fullName;
  private String dob;
  private String identity_number;
  private String issue_date;
  private String issue_place;
  private String expireDate;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api ocr webhook")
  public void verifyOcrWebHookTest() {
    String body = fileTxtToString("src/test/resources/requestBody/CreateOcrTask.json")
      .replaceAll("#requestId#", request_id)
      .replaceAll("#fullName#", fullName).replaceAll("#birthday#", dob)
      .replaceAll("#idNumber#", identity_number).replaceAll("#idDate#", issue_date)
      .replaceAll("#idPlace#", issue_place).replaceAll("#expireDate#", expireDate);

    RequestSpecification requestSpecification = given()
      .baseUri(OCR_WEBHOOK)
      .contentType("application/json");
    Response response;

    if (testCaseName.contains("missing BODY")) {
      response = requestSpecification.header("Is-Signature", OCR_SIGNATURE_TOKEN).get();
    } else if (testCaseName.contains("missing Is-Signature")) {
      response = requestSpecification.body(body).get();
    } else {
      response = requestSpecification.header("Is-Signature", OCR_SIGNATURE_TOKEN).body(body).get();
    }

    assertThat(response.getStatusCode(), is(statusCode));

    if (response.statusCode() == 200) {
      BigDecimal taskIdActual = ObTask.getByUserIdAndStatus("5280842", "TODO").getId();

      if (testCaseName.contains("do not gen task") || testCaseName.contains("missing format of expire_date")) {
        assertThat(taskIdActual, is(nullValue()));
      } else {
        assertThat(taskIdActual, is(notNullValue()));
      }
    } else {
      assertThat(response.jsonPath().get("message"), is(errorMsg));
    }
  }

  @After
  public void clearData() {
    ObTask.deleteByUserIdAndStatus("5280842", "TODO");
    OcrData.updateStatus(request_id, null);
  }
}
