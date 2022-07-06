package com.tcbs.automation.wbl;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Map;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/wbl/GetActionDocInt.csv", separator = '|')
public class GetActionDocIntTest {
  private final String str = "policyName,policyCode,idNumber,userName,policyUserType,actionCode,actionName,startDatetime,endDatetime";
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String orgCode;
  private String userName;
  private String startDate;
  private String endDate;
  private String key;
  private String value;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api get action doc int")
  public void perfomTest() {

    String orgCodeText = "orgCode";

    orgCode = syncData(orgCode);
    userName = syncData(userName);
    startDate = syncData(startDate);
    endDate = syncData(endDate);
    key = syncData(key);
    value = syncData(value);

    RequestSpecification requestSpecification = given()
      .baseUri(GET_ACTION_DOC_INT)
      .header("X-Api-Key", (testCaseName.contains("invalid x-api-key") ? TCBSPROFILE_AUTHORIZATION :
        TCBSPROFILE_BACKENDWBLKEY));

    Response response;
    if (key == null && value == null) {
      if (testCaseName.contains("missing param orgCode")) {
        response = requestSpecification.param("userName", userName).get();
      } else if (testCaseName.contains("missing param userName")) {
        response = requestSpecification.param(orgCodeText, orgCode).get();
      } else {
        response = requestSpecification.param(orgCodeText, orgCode)
          .param("userName", userName)
          .param("startDate", startDate)
          .param("endDate", endDate)
          .get();
      }
    } else {
      response = requestSpecification.param(orgCodeText, orgCode)
        .param("key", key)
        .param("value", value)
        .param("startDate", startDate)
        .param("endDate", endDate)
        .get();
    }

    assertThat("verify status code", response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      List<Map<String, Object>> listData = response.jsonPath().get();
      if (!listData.isEmpty()) {
        if (testCaseName.contains("return no any data")) {
          assertThat(listData.size(), is(0));
        } else {
          for (String item : str.split(",", -1)) {
            assertThat(listData.get(0).keySet(), hasItem(item));
          }
        }
      }
    } else {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }
}
