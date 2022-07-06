package com.tcbs.automation.changecustomerinfo;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.API_KEY;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.GEN_MBLK5_WITH_QRCODE;
import static com.tcbs.automation.tools.ConvertUtils.fileTxtToString;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/changecustomerinfo/GenMblk5WithQRCode.csv", separator = '|')
public class GenMblk5WithQRCodeTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String qrCode;
  private String body;

  @Before
  public void setup() {
    qrCode = syncData(qrCode);
    if (qrCode.equalsIgnoreCase("valid")) {
      qrCode = "MBLK5|2072.100008-0001166656|164245445444545445|1.1";
    }
    body = fileTxtToString("src/test/resources/requestBody/GenMblk5WithQrCode.json").replaceAll("#qrCode#", qrCode);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api gen MBLK5 With QRCode")
  public void performTest() {
    RequestSpecification requestSpecification = given()
      .baseUri(GEN_MBLK5_WITH_QRCODE)
      .header("x-api-key", API_KEY)
      .contentType("application/json");
    Response response;

    if (testCaseName.contains("missing BODY")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(body).post();
    }

    assertThat("verify status", response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      assertThat("verify result", response.jsonPath().get("html"), is(notNullValue()));
    } else {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }

  }
}