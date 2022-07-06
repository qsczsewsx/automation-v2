package com.tcbs.automation.profileadmin;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.UserChangeInforRecordEntity;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.TRACKING_POINT_CHANGE_INFO_PROCESS;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.junit.Assert.assertEquals;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/profileadmin/TrackingPointChangeInfoProcess.csv", separator = '|')
public class TrackingPointChangeInfoProcessTest {
  @Getter
  private String testcaseName;
  private int statusCode;
  private String cookie;
  private String csrfToken;
  private String tcbsId;
  private int status;
  private String errMsg;

  @Before
  public void before() throws Exception {
    if (statusCode == 200) {
      if (status == 0) {
        if (testcaseName.contains("case valid")) {
          UserChangeInforRecordEntity.updateStatusDoneForAllByTcbsid(tcbsId);
        } else {
          UserChangeInforRecordEntity.insert(tcbsId, 0);
        }
      } else {
        if (testcaseName.contains("case valid")) {
          UserChangeInforRecordEntity.insert(tcbsId, 0);
        } else {
          UserChangeInforRecordEntity.updateStatusDoneForAllByTcbsid(tcbsId);
        }
      }
    }

  }

  @Test
  @TestCase(name = "#testcaseName#")
  @Title("Verify API tracking point change info process")
  public void trackingPointChangeInfoProcess() {
    System.out.println("Testcase Name: " + testcaseName);
    Response response = given()
      .urlEncodingEnabled(false)
      .baseUri(TRACKING_POINT_CHANGE_INFO_PROCESS)
      .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
      .header("Cookie", cookie)
      .header("X-CSRF-TOKEN", csrfToken)
      .body("tcbsId=" + tcbsId + "&status=" + status)
      .when()
      .post();

    assertEquals(statusCode, response.getStatusCode());

    if (response.getStatusCode() == 200) {
      assertEquals(errMsg, response.jsonPath().get("msg"));
    }
  }
}
