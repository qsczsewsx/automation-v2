package com.tcbs.automation.derivative;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsApplicationUser;
import com.tcbs.automation.cas.TcbsUser;
import common.CallApiUtils;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static common.ProfileTools.TOKEN;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/derivative/StopSendMsgToDerivative.csv", separator = '|')
public class StopSendMsgToDerivativeTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String tcbsId;
  private String errorMessage;
  private String body;
  private String userId;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api stop send msg to derivative")
  public void perfomTest() {

    tcbsId = syncData(tcbsId);

    Response response = given()
      .baseUri(STOP_SEND_MSG_TO_DERIVATIVE.replaceAll("#tcbsId#", tcbsId))
      .contentType("application/json")
      .header("Authorization", "Bearer " +
        (testCaseName.contains("user has no permission") ? TCBSPROFILE_BACKENDWBLKEY : TOKEN))
      .post();

    assertThat("verify status", response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      userId = TcbsUser.getByTcbsId(tcbsId).getId().toString();
      String status = TcbsApplicationUser.getByTcbsApplicationUserAppId2(userId, "7")
        .getStatus().toString();
      assertThat(status, is("8"));
    } else if (statusCode == 403) {
      assertThat("verify error message", response.jsonPath().get("errorMessage"), is(errorMessage));
    } else {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }

  @After
  public void clearData() {
    // Clear data
    if (statusCode == 200) {
      TcbsApplicationUser.updateStatusApp(userId, "7", "9");
      CallApiUtils.clearCache(DELETE_CACHE, "x-api-key", API_KEY);
    }
  }
}
