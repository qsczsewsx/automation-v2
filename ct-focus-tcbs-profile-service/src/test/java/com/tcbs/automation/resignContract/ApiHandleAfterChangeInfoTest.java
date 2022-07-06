package com.tcbs.automation.resignContract;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsNewOnboardingStatus;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/resignContract/ApiHandleAfterChangeInfo.csv", separator = '|')
public class ApiHandleAfterChangeInfoTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private int status;
  private String tcbsId;
  private String statusKey;
  private String statusValue;
  private String errMess;

  @Before
  public void before() {
    if ((StringUtils.isNotBlank(statusKey) && StringUtils.isNotBlank(statusValue)) || testCaseName.contains("not in resign list")) {
      List<String> listTcbsId = new ArrayList<>();
      listTcbsId.add(tcbsId);
      TcbsNewOnboardingStatus.deleteByListAndStatusKey("RESIGN_STATUS", listTcbsId, "TCBSID");
      if (StringUtils.isNotBlank(statusKey) && StringUtils.isNotBlank(statusValue)) {
        TcbsNewOnboardingStatus.insertData(statusKey, statusValue, tcbsId, "CMND/CCCD,họ tên và ngày sinh");
      }
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api handle after change info")
  public void verifySignContractTest() {
    System.out.println("Test Case: " + testCaseName);
    Response response = given()
      .contentType(ContentType.JSON)
      .baseUri(HANDLE_AFTER_CHANGE_INFO + tcbsId)
      .urlEncodingEnabled(false)
      .header("x-api-key", testCaseName.contains("invalid x-api-key") ? FMB_X_API_KEY : API_KEY)
      .post();

    assertThat("verify status code", response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      assertEquals(status, (int) response.jsonPath().get("status"));
      if (testCaseName.contains("that is updating information")) {
        assertNull(response.jsonPath().get("message"));
      } else {
        assertEquals(errMess, response.jsonPath().get("message"));
      }
      if (status == 200) {
        TcbsNewOnboardingStatus tcbsNewOnboardingStatus = TcbsNewOnboardingStatus.getByTcbsIdAndStatusKey(tcbsId, statusKey);
        if (testCaseName.contains("that is updating information")) {
          assertEquals("HAS_CHANGE_INFO", tcbsNewOnboardingStatus.getStatusValue());
          assertNull(tcbsNewOnboardingStatus.getRejectContent());
        } else if (!testCaseName.contains("status value as null")) {
          assertEquals(tcbsNewOnboardingStatus.getStatusValue(), statusValue);
        }
      }
    }
  }
}