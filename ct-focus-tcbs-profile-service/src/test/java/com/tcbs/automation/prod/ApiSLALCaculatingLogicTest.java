package com.tcbs.automation.prod;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.ObTask;
import com.tcbs.automation.cas.TcbsNewOnboardingStatus;
import com.tcbs.automation.cas.TcbsUser;
import com.tcbs.automation.login.LoginApi;
import com.tcbs.automation.login.TheUserInfo;
import common.DatesUtils;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.screenplay.Actor;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.Timestamp;
import java.text.ParseException;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.SLA_CACULATE_LOGIC;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/*
API calculate SLA to display for customer, that relates to:
  ECONTRACT_STATUS
  ID_STATUS
  EKYC_STATUS
in table TCBS_NEW_ONBOARDING_STATUS , and table OB_TASK
 */

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/prod/ApiSLACaculatingLogic.csv")
public class ApiSLALCaculatingLogicTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String tcbsId;
  private String statusKey;
  private String statusValue;
  private String errMess;

  private String jwt;
  private String username;
  private String userId;

  @Before
  public void before() {
    tcbsId = syncData(tcbsId);
    username = TcbsUser.getByTcbsId("10000023610").getUsername();

    Actor actor = Actor.named("Customer Login");
    LoginApi.withCredentials("105C80109C", "abc123").performAs(actor);
    jwt = TheUserInfo.aboutLoginData().answeredBy(actor).getToken();

    if (tcbsId.equalsIgnoreCase("10000023610")) {
      //set up data test
      userId = TcbsUser.getByTcbsId(tcbsId).getId().toString();
      TcbsNewOnboardingStatus.updateStatusValueByUserId(userId, "ECONTRACT_STATUS", "VERIFIED");
      TcbsNewOnboardingStatus.updateStatusValueByUserId(userId, "ID_STATUS", "WAIT_FOR_VERIFY");
      TcbsNewOnboardingStatus.updateStatusValueByUserId(userId, "EKYC_STATUS", "WAIT_FOR_VERIFY");
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify API SLA Caculating Logic")
  public void apiCalculateSla() throws ParseException {
    System.out.println("Test case: " + testCaseName);

    if (!statusKey.isEmpty()) {
      TcbsNewOnboardingStatus.updateStatusValueByUserId(userId, statusKey, statusValue);
    }
    if (testCaseName.contains("maker's task date is before")) {
      ObTask.updateCreatedDateByUserIdAndType(userId, "MAKER", "2019-01-01 10:29:40");
    }
    Response response = given()
      .baseUri(SLA_CACULATE_LOGIC.replaceAll("#tcbsId#", tcbsId.equalsIgnoreCase("null") ? null : tcbsId))
      .header("Authorization", "Bearer " + jwt)
      .when()
      .get();

    assertEquals(statusCode, response.getStatusCode());
    if (statusCode == 200 && testCaseName.contains("return null")) {
      assertEquals("", response.jsonPath().get("data"));
    } else if (statusCode == 200 && testCaseName.contains("full data")) {
      assertThat(response.jsonPath().get("data"), is(notNullValue()));
      Timestamp createdDateTime = ObTask.getByUserIdAndType(userId, "MAKER").getCreatedDatetime();
      Timestamp responseDateTime = DatesUtils.convertStringtoTimestampDayMonthYear(response.jsonPath().get("data"));
      assertTrue("Datetime in response is after datetime in DB", responseDateTime.after(createdDateTime));

    } else if (statusCode == 404) {
      assertEquals(errMess, response.jsonPath().get("message"));
    }
  }
}
