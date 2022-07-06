package com.tcbs.automation.wbl;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsUser;
import com.tcbs.automation.cas.WblReportDoc;
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

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/wbl/AddReportDocInt.csv", separator = '|')
public class AddReportDocIntTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String policyCode;
  private String actionCode;
  private String userName;
  private String docNO;
  private String refDocNO;
  private String orgCode;
  private String startDatetime;
  private String endDatetime;
  private String tradingAction;
  private String volume;
  private HashMap<String, Object> body;

  @Before
  public void setup() {
    policyCode = syncData(policyCode);
    actionCode = syncData(actionCode);
    docNO = syncData(docNO);
    refDocNO = syncData(refDocNO);
    orgCode = syncData(orgCode);
    tradingAction = syncData(tradingAction);
    volume = syncData(volume);
    startDatetime = syncData(startDatetime);
    endDatetime = syncData(endDatetime);
    userName = syncData(userName);

    HashMap<String, Object> metaData = new HashMap<>();
    metaData.put("tradingAction", tradingAction);
    metaData.put("volume", volume);

    body = new HashMap<>();
    body.put("policyCode", policyCode);
    body.put("actionCode", actionCode);
    body.put("userName", userName);
    body.put("docNO", docNO);
    body.put("refDocNO", refDocNO);
    body.put("orgCode", orgCode);
    body.put("metaData", metaData);
    body.put("startDatetime", startDatetime);
    body.put("endDatetime", endDatetime);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api add report doc")
  public void perfomTest() {
    RequestSpecification requestSpecification = given()
      .baseUri(ADD_REPORT_DOC_INT)
      .contentType("application/json")
      .header("X-Api-Key", (testCaseName.contains("invalid x-api-key") ? TCBSPROFILE_AUTHORIZATION :
        TCBSPROFILE_BACKENDWBLKEY));
    Response response;

    if (testCaseName.contains("missing BODY")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(body).post();
    }
    assertThat("verify status code", response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      WblReportDoc wblReportDoc = WblReportDoc.getByDocNo(docNO);
      assertThat(wblReportDoc.getUserId(), is(TcbsUser.getByUserName(userName).getId()));
      assertThat(wblReportDoc.getOrgCode(), is(orgCode));
      assertThat(wblReportDoc.getStartDatetime(), is(Timestamp.valueOf(
        LocalDateTime.parse(startDatetime,
          DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")))));
      assertThat(wblReportDoc.getEndDatetime(), is(Timestamp.valueOf(
        LocalDateTime.parse(endDatetime,
          DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")))));
      if (!testCaseName.contains("param refDocNO")) {
        assertThat(wblReportDoc.getDocNo(), is(docNO));
      }
    } else {
      assertThat("verify error message", response.jsonPath().get("message"), is(containsString(errorMessage)));
    }
  }

  @After
  public void clearData() {
    if (statusCode == 200) {
      WblReportDoc.deleteByDocNo(docNO);
    }
  }
}
