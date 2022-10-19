package com.tcbs.automation.other;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsUserCheck;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.API_KEY;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.JOB_CHECK_CONTRACT;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/other/JobCheckContract.csv", separator = '|')
public class JobCheckContractTest {

  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMsg;
  private String fromDate;
  private String toDate;
  private String username;
  private String contractOk;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api job check contract")
  public void verifyJobCheckContractTest() throws InterruptedException {

    System.out.println("TestCaseName : " + testCaseName);

    RequestSpecification requestSpecification = given()
      .baseUri(JOB_CHECK_CONTRACT)
      .header("x-api-key", API_KEY);

    Response response;

    if (testCaseName.contains("missing param fromDate")) {
      response = requestSpecification.param("toDate", toDate).param("username", username).get();
    } else if (testCaseName.contains("missing param toDate")) {
      response = requestSpecification.param("fromDate", fromDate).param("username", username).get();
    } else {
      response = requestSpecification.param("fromDate", fromDate).param("toDate", toDate).param("username", username).get();
    }

    assertThat(response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      TimeUnit.SECONDS.sleep(3);
      assertNotNull(TcbsUserCheck.getByUserName(username, contractOk).getId());
    } else {
      assertThat(response.jsonPath().get("message"), containsString(errorMsg));
    }
  }

  @After
  public void clearData() {
    if (statusCode == 200) {
      TcbsUserCheck.deleteByUserName(username, contractOk);
    }
  }
}