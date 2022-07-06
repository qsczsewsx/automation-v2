package com.tcbs.automation.rmrbo;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.R3RdElearning;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static net.serenitybdd.rest.RestRequests.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/rmrbo/ApiJobSyncValidateExpired.csv", separator = '|')
public class ApiJobSyncValidateExpiredTest {

  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String tcbsId;
  private String courseCode;
  private String expectStatus;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify API sync validate expired elearning")
  public void apiSyncValidateExpired() {
    System.out.println("Testcase Name : " + testCaseName);

    Response response = given()
      .baseUri(VALIDATE_AND_EXPIRED)
      .header("x-api-key", testCaseName.contains("invalid x-api-key") ? RMRBO_CHECKER_AUTHORIZATION_KEY : RMRBO_API_KEY)
      .contentType("application/json")
      .post();

    assertThat(response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      assertThat("verify update status elearning", response.jsonPath().get("data"), is(true));
      assertThat("verify status", R3RdElearning.getByTcbsIdAndCourseCode(tcbsId, courseCode).getCourseStatus().toString(), is(expectStatus));
    } else {
      assertEquals(errorMessage, response.jsonPath().get("message"));
    }
  }

  @After
  public void teardown() {
    if (statusCode == 200) {
      R3RdElearning.updateStatusByTcbsIdAndCourseCode("0001100066", courseCode, "1");
      R3RdElearning.updateStatusByTcbsIdAndCourseCode("0001067838", courseCode, "1");
      R3RdElearning.updateStatusByTcbsIdAndCourseCode("0001093600", courseCode, "1");
    }
  }
}
