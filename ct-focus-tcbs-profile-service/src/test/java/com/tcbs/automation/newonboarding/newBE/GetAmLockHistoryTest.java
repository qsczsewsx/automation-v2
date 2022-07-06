package com.tcbs.automation.newonboarding.newBE;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.AMLOCK_KEY;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.GET_AMLOCK_HISTORY;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/GetAmLockHistory.csv", separator = '|')
public class GetAmLockHistoryTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String userName;
  private String errorMsg;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api get AMLOCK history")
  public void performTest() {
    userName = syncData(userName);
    String keys = "id,cusName,cusOtherName,note,birthPlace,amLockDate,source,addressOne,addressTow,idNumber,birthDate,updatedDate,scanAmLockDate,status";

    RequestSpecification requestSpecification = given()
      .baseUri(GET_AMLOCK_HISTORY)
      .header("Authorization", "Bearer " + AMLOCK_KEY);

    Response response;

    if (testCaseName.contains("missing param username")) {
      response = requestSpecification.get();
    } else {
      response = requestSpecification.param("username", userName).get();
    }

    assertThat("verify status code", response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      List<HashMap<String, Object>> data = response.jsonPath().get("");
      if (testCaseName.contains("case valid request")) {
        for (String item : keys.split(",", -1)) {
          assertThat(data.get(0), hasKey(item));
        }
      } else {
        assertThat(data.size(), is(0));
      }
    } else {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMsg));
    }

  }
}