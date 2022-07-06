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

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.API_KEY;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.GEN_CHANGE_INFO_REPORT;
import static com.tcbs.automation.tools.ConvertUtils.fileTxtToString;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/changecustomerinfo/GenChangeInfoReport.csv", separator = '|')
public class GenChangeInfoReportTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String reportType;
  private String oldData;
  private String newData;
  private String body;
  private String timeStamp;

  @Before
  public void setup() {
    reportType = syncData(reportType);
    if (testCaseName.contains("missing")) {
      if (testCaseName.contains("missing param oldData")) {
        body = fileTxtToString("src/test/resources/requestBody/InvalidGenChangeInfoReport1.json")
          .replaceAll("#reportType#", reportType);
      } else if (testCaseName.contains("missing param newData")) {
        body = fileTxtToString("src/test/resources/requestBody/InvalidGenChangeInfoReport2.json")
          .replaceAll("#reportType#", reportType);
      } else {
        body = fileTxtToString("src/test/resources/requestBody/InvalidGenChangeInfoReport3.json");
      }
    } else {
      body = fileTxtToString("src/test/resources/requestBody/GenChangeInfoReport.json").replaceAll("#reportType#", reportType);
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api gen change info report")
  public void perfomTest() {
    RequestSpecification requestSpecification = given()
      .baseUri(GEN_CHANGE_INFO_REPORT)
      .header("x-api-key", API_KEY)
      .contentType("application/json");
    Response response;

    if (testCaseName.contains("missing BODY")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(body).post();
    }

    assertThat("verify status", response.getStatusCode(), is(statusCode));

    response.getHeader("Content-Disposition");
    if (statusCode == 200) {
      timeStamp = new SimpleDateFormat("ddMMyyyy").format(Calendar.getInstance().getTime());
      if (reportType.equalsIgnoreCase("HNX_REPORT")) {
        assertThat(response.getHeader("Content-Disposition"), is("attachment; filename=\"HNX_" + timeStamp + ".xlsx\""));
      } else {
        assertThat(response.getHeader("Content-Disposition"), is("attachment; filename=\"HSX_" + timeStamp + ".xlsx\""));
      }
    } else {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }
}
