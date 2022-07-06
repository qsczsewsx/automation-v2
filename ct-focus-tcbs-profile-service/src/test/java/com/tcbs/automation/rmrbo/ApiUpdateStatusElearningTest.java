package com.tcbs.automation.rmrbo;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.cas.R3RdElearning;
import common.CommonUtils;
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

import java.util.LinkedHashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static net.serenitybdd.rest.RestRequests.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/rmrbo/ApiUpdateStatusElearning.csv", separator = '|')
public class ApiUpdateStatusElearningTest {

  @Getter
  private String testCaseName;
  private int statusCode;
  private String userName;
  private String tcbsId;
  private String courseCode;
  private String courseStatus;
  private String partner;
  private String role;
  private String errorMessage;
  private String note;

  @Before
  public void creatUserELearning() {
    CommonUtils.createTcbsIdLearning("0001100103");
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify API update status elearning")
  public void apiUpdateStatusELearning() {
    System.out.println("Testcase Name : " + testCaseName);
    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    body.put("userName", userName);
    body.put("courseCode", courseCode);
    body.put("courseStatus", courseStatus);
    body.put("partner", partner);
    body.put("role", role);
    body.put("note", note);

    RequestSpecification requestSpecification = given()
      .baseUri(UPDATE_STATUS_ELEARNING)
      .header("Authorization", "Bearer " + (testCaseName.contains("invalid token") ? FMB_X_API_KEY : AMLOCK_KEY))
      .contentType("application/json");

    Response response;
    Gson gson = new Gson();
    if (testCaseName.contains("missing body")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(gson.toJson(body)).post();
    }

    assertThat(response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      assertThat("verify receive data", response.jsonPath().get("data"), is(true));
      if (testCaseName.contains("200 inactive")) {
        assertThat("verify status", R3RdElearning.getByTcbsIdAndCourseCode(tcbsId, courseCode).getCourseStatus().toString(), is("0"));
      } else {
        assertThat("verify status", R3RdElearning.getByTcbsIdAndCourseCode(tcbsId, courseCode).getCourseStatus().toString(), is("1"));
      }
    } else if (statusCode == 403) {
      assertEquals(errorMessage, response.jsonPath().get("errorMessage"));
    } else {
      assertEquals(errorMessage, response.jsonPath().get("message"));
    }
  }

  @After
  public void teardown() {
    R3RdElearning.deleteByTcbsId("0001100103");
    R3RdElearning.updateStatusByTcbsIdAndCourseCode("0001900002", "EL00001", "0");
    R3RdElearning.updateStatusByTcbsIdAndCourseCode("0001559929", "EL0123123", "1");
  }
}
