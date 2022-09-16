package com.tcbs.automation.newOnboardingPartnerShip;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Map;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newOnboardingPartnerShip/PartnerShipGetHistoryIa.csv", separator = '|')
public class PartnerShipGetHistoryIaTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMsg;
  private String partnerId;
  private String username;
  private String channel;
  private String status;
  private String linkType;
  private String pageNumber;
  private String pageSize;
  private int countElements;

  @Before
  public void setup() {
    partnerId = syncData(partnerId);
    username = syncData(username);
    channel = syncData(channel);
    status = syncData(status);
    pageNumber = syncData(pageNumber);
    pageSize = syncData(pageSize);
    linkType = syncData(linkType);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api partnership get history IA")
  public void verifyPartnerShipGetHistoryIaTest() {
    System.out.println("TestcaseName: " + testCaseName);
    String keys = "items,totalCount,totalPage,pageNumber,pageSize";

    RequestSpecification requestSpecification = given()
      .baseUri(PARTNERSHIP_GET_HISTORY_IA)
      .header("Authorization", "Bearer " +
        (testCaseName.contains("has no permission") ? TCBSPROFILE_AUTHORIZATION : ASSIGN_TASK_TO_MAKER_KEY));

    if (testCaseName.contains("missing param partnerId")) {
      requestSpecification
        .param("username", username)
        .param("pageNumber", pageNumber)
        .param("pageSize", pageSize);
    } else if (testCaseName.contains("missing param username")) {
      requestSpecification
        .param("partnerId", partnerId)
        .param("pageNumber", pageNumber)
        .param("pageSize", pageSize);
    } else {
      requestSpecification.param("partnerId", partnerId)
        .param("username", username)
        .param("pageNumber", pageNumber)
        .param("pageSize", pageSize);
      if (testCaseName.contains("with channel")) {
        requestSpecification.param("channel", channel);
      } else if (testCaseName.contains("with status")) {
        requestSpecification.param("status", status);
      } else if (testCaseName.contains("with linkType")) {
        requestSpecification.param("linkType", linkType);
      }
    }

    Response response = requestSpecification.get();

    assertThat("verify status code", response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      for (String item : keys.split(",", -1)) {
        assertThat(response.jsonPath().get("data"), hasKey(item));
      }
      List<Map<String, Object>> listResult = response.jsonPath().get("data.items");
      assertThat("verify count elements", listResult.size(), is(countElements));
    } else if (statusCode == 400) {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMsg));
    } else {
      assertThat("verify error message", response.jsonPath().get("errorMessage"), is(errorMsg));
    }
  }
}