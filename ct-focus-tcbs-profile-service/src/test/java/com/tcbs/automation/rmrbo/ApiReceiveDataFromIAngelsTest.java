package com.tcbs.automation.rmrbo;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.cas.R3RdElearning;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.RestRequests.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/rmrbo/ApiReceiveDataFromIAngels.csv", separator = '|')
public class ApiReceiveDataFromIAngelsTest {

  HashMap<String, Object> body;
  @Getter
  private String testCaseName;
  private int statusCode;
  private String tcbsId;
  private String role;
  private String courseCode;
  private String courseStatus;
  private String partner;
  private String errorMessage;
  private final String DTTCBSID = "tcbsId";
  private final String DTROLE = "role";
  private final String DTCOURSECODE = "courseCode";
  private final String DTCOURSESTATUS = "courseStatus";
  private final String DTPARTNER = "partner";

  @Before
  public void before() {
    tcbsId = syncData(tcbsId);
    role = syncData(role);
    courseCode = syncData(courseCode);
    courseStatus = syncData(courseStatus);
    partner = syncData(partner);
    body = getELearningBody();
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify API receive data from iAngels")
  public void apiAddCustomerToList() {
    System.out.println("Testcase Name : " + testCaseName);

    RequestSpecification requestSpecification = given()
      .baseUri(RECEIVE_DATA_FROM_IANGELS)
      .header("x-api-key", testCaseName.contains("invalid x-api-key") ? FMB_X_API_KEY : RMRBO_API_KEY)
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
      if (testCaseName.contains("tcbsId exist")) {
        assertThat(R3RdElearning.getByTcbsId(tcbsId), is(notNullValue()));
      }
    } else {
      assertEquals(errorMessage, response.jsonPath().get("message"));
    }
  }

  @After
  public void teardown() {
    if (testCaseName.contains("multi tcbsId exist")) {
      R3RdElearning.deleteByTcbsId("0001190988");
      R3RdElearning.deleteByTcbsId("0001541562");
    } else {
      R3RdElearning.deleteByTcbsId(tcbsId);
    }
  }

  public HashMap<String, Object> getELearningBody() {
    List<HashMap<String, Object>> eLearnings = new ArrayList<>();

    HashMap<String, Object> eLearningFirst = new HashMap<>();
    HashMap<String, Object> eLearningSecond = new HashMap<>();

    if (!(testCaseName.contains("multi"))) {
      eLearningFirst.put(DTTCBSID, tcbsId);
      eLearningFirst.put(DTROLE, role);
      eLearningFirst.put(DTCOURSECODE, courseCode);
      eLearningFirst.put(DTCOURSESTATUS, courseStatus);
      eLearningFirst.put(DTPARTNER, partner);
    } else {
      List<String> listTcbsId = new ArrayList<>(Arrays.asList(tcbsId.split(",")));
      eLearningFirst.put(DTTCBSID, listTcbsId.get(0));
      eLearningSecond.put(DTTCBSID, listTcbsId.get(1));
      List<String> listRole = new ArrayList<>(Arrays.asList(role.split(",")));
      eLearningFirst.put(DTROLE, listRole.get(0));
      eLearningSecond.put(DTROLE, listRole.get(1));
      List<String> listCourseCode = new ArrayList<>(Arrays.asList(courseCode.split(",")));
      eLearningFirst.put(DTCOURSECODE, listCourseCode.get(0));
      eLearningSecond.put(DTCOURSECODE, listCourseCode.get(1));
      List<String> listCourseStatus = new ArrayList<>(Arrays.asList(courseStatus.split(",")));
      eLearningFirst.put(DTCOURSESTATUS, listCourseStatus.get(0));
      eLearningSecond.put(DTCOURSESTATUS, listCourseStatus.get(1));
      List<String> listPartner = new ArrayList<>(Arrays.asList(partner.split(",")));
      eLearningFirst.put(DTPARTNER, listCourseStatus.get(0));
      eLearningSecond.put(DTPARTNER, listCourseStatus.get(1));
      eLearnings.add(eLearningSecond);
    }
    eLearnings.add(eLearningFirst);

    HashMap<String, Object> body = new HashMap<>();
    body.put("eLearnings", eLearnings);

    return body;
  }

}
