package com.tcbs.automation.newonboarding.newBE;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.ASSIGN_TASK_TO_MAKER_KEY;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.CHECK_AM_LOCK;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/CheckAMLock.csv", separator = '|')
public class CheckAMLockTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String tcbsId;
  private String idNumber;
  private String birthDate;
  private String name;
  private String expectedStatus;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api check AM Lock")
  public void performTest() {

    tcbsId = syncData(tcbsId);
    idNumber = syncData(idNumber);
    birthDate = syncData(birthDate);
    name = syncData(name);

    HashMap<String, Object> body = new HashMap<>();
    body.put("tcbsId", tcbsId);
    body.put("idNumber", idNumber);
    body.put("birthDate", birthDate);
    body.put("name", name);

    Gson gson = new Gson();

    RequestSpecification requestSpecification = given()
      .baseUri(CHECK_AM_LOCK)
      .header("Authorization", "Bearer " + ASSIGN_TASK_TO_MAKER_KEY)
      .contentType("application/json");

    Response response;

    if (testCaseName.contains("missing BODY")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(gson.toJson(body)).post();
    }

    assertThat("verify status code", response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      assertThat(response.jsonPath().get("status").toString(), is(expectedStatus));
      assertThat(response.jsonPath().get("scanAmLockDate").toString(), is(notNullValue()));
    } else {
      assertThat("verify error message", response.jsonPath().get("message"), is(containsString(errorMessage)));
    }
  }
}