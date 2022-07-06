package com.tcbs.automation.newonboarding.newBE;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.cas.ObTask;
import com.tcbs.automation.cas.TcbsApplicationUser;
import com.tcbs.automation.cas.TcbsNewOnboardingStatus;
import com.tcbs.automation.cas.TcbsUser;
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

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/ApiMakerApproveAccountScannedByOCR.csv", separator = '|')
public class ApiMakerApproveOCRTest {

  private final String USERID = "5285550";
  @Getter
  private String testCaseName;
  private String authorization;
  private String taskId;
  private String reasonNode;
  private String resignContract;
  private int changeInfo;
  private int statusCode;
  private String errorMsg;

  @Before
  public void before() {
    taskId = syncData(taskId);
    reasonNode = syncData(reasonNode);
    resignContract = syncData(resignContract);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api Maker approves OCR result")
  public void verifyApiMakerApprovesScannedByOCRTest() {

    System.out.println("Test Case : " + testCaseName);
    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    body.put("taskId", taskId.isEmpty() ? null : taskId);
    body.put("responseNode", reasonNode.isEmpty() ? null : reasonNode);
    body.put("resignContract", resignContract.isEmpty() ? null : Integer.valueOf(resignContract));
    ArrayList<Integer> changeInfoList = new ArrayList<>();
    changeInfoList.add(changeInfo);
    body.put("changeInfo", changeInfoList);

    RequestSpecification requestSpecification = given()
      .baseUri(MAKER_APPROVE_AFTER_OCR_SCAN)
      .header("Authorization", "Bearer " +
        (authorization.equalsIgnoreCase("InvalidMaker") ? TCBSPROFILE_AUTHORIZATION : ASSIGN_TASK_TO_MAKER_KEY))
      .contentType("application/json");

    Response response;
    Gson gson = new Gson();
    if (testCaseName.contains("missing body")) {
      response = requestSpecification.put();
    } else {
      response = requestSpecification.body(gson.toJson(body)).put();
    }
    assertThat(response.getStatusCode(), is(statusCode));
    System.out.println(response.getStatusCode());

    if (statusCode == 200) {
      if (resignContract.equalsIgnoreCase("1")) {
        assertThat(TcbsApplicationUser.getByTcbsApplicationUserAppId2(USERID, "4").getStatus().toString(), anyOf(is("1"), is("2")));
        assertThat(TcbsApplicationUser.getByTcbsApplicationUserAppId2(USERID, "2").getStatus().toString(), anyOf(is("1"), is("2")));
      } else if (resignContract.equalsIgnoreCase("2")) {
        assertThat(TcbsNewOnboardingStatus.getByTcbsIdAndStatusKey("10000025964", "RESIGN_STATUS").getStatusValue(), is("WAIT_CHANGE_INFO"));
      }
      assertThat(response.jsonPath().get("code"), is("200"));
      assertThat(response.jsonPath().get("message"), is(nullValue()));
      assertThat(response.jsonPath().get("data"), is(true));
    } else if (statusCode == 400) {
      assertEquals(errorMsg, response.jsonPath().get("message"));
    } else if (statusCode == 403) {
      assertEquals(errorMsg, response.jsonPath().get("errorMessage"));
    }
  }

  @After
  public void teardown() {
    TcbsUser.updateTransferStatusById(USERID, "0");
    ObTask.updateStatusById("25442", "PROCESSING");
    String WAIT_VERIFY = "WAIT_FOR_VERIFY";
    TcbsNewOnboardingStatus.updateStatusValueByUserId(USERID, "RESIGN_STATUS", WAIT_VERIFY);
    TcbsNewOnboardingStatus.updateStatusValueByUserId(USERID, "ID_STATUS", WAIT_VERIFY);
    TcbsNewOnboardingStatus.updateStatusValueByUserId(USERID, "BANK_INFO_STATUS", WAIT_VERIFY);
    TcbsNewOnboardingStatus.updateStatusValueByUserId(USERID, "ECONTRACT_STATUS", WAIT_VERIFY);
    TcbsNewOnboardingStatus.updateStatusValueByUserId(USERID, "EKYC_STATUS", "VERIFIED");
    TcbsApplicationUser.updateStatusApp(USERID, "4", "0");
  }
}
