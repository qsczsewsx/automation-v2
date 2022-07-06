package com.tcbs.automation.rmrbo;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static net.serenitybdd.rest.RestRequests.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/rmrbo/ApiGetCourseCodeByRole.csv", separator = '|')
public class ApiGetCourseCodeByRoleTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String role;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify API get course code by role")
  public void apiGetRoleByPartner() {
    System.out.println("Testcase Name : " + testCaseName);

    Response response = given()
      .baseUri(GET_COURSE_CODE.replace("{role}", role))
      .header("Authorization", "Bearer " + (testCaseName.contains("invalid token") ? FMB_X_API_KEY : RMRBO_VIEW_TOKEN))
      .contentType("application/json")
      .get();

    assertThat(response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      if (!testCaseName.contains("non-exist")) {
        assertThat("verify get list course code", response.jsonPath().getList("").size(), is(greaterThan(0)));
      }
    } else if (statusCode == 403) {
      assertTrue(response.jsonPath().get("errorMessage").toString().contains(errorMessage));
    } else {
      assertTrue(response.jsonPath().get("message").toString().contains(errorMessage));
    }
  }
}
