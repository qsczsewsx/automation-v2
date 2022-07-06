package com.tcbs.automation.derivative;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.GET_MSG_SENT_TO_DERIVATIVE;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.TCBSPROFILE_BACKENDWBLKEY;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static common.ProfileTools.TOKEN;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/derivative/GetMsgSentToDerivative.csv", separator = '|')
public class GetMsgSentToDerivativeTest {
  private final String str = "CUSTODYCD,TXDATE,GENDER,CUST_NAME,CARD_ID,IDTYPE,IDDATE,EXPIRE_DATE,IDPLACE,ADDRESS";
  @Getter
  private String testCaseName;
  private int statusCode;
  private String tcbsId;
  private String errorMessage;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api get msg sent to derivative")
  public void perfomTest() {

    tcbsId = syncData(tcbsId);

    Response response = given()
      .baseUri(GET_MSG_SENT_TO_DERIVATIVE.replaceAll("#tcbsId#", tcbsId))
      .contentType("application/json")
      .header("Authorization", "Bearer " +
        (testCaseName.contains("user has no permission") ? TCBSPROFILE_BACKENDWBLKEY : TOKEN))
      .get();

    assertThat("verify status", response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      HashMap<String, Object> res = response.jsonPath().get("data");
      for (String item : str.split(",", -1)) {
        assertThat(res, hasKey(item));
      }
    } else if (statusCode == 403) {
      assertThat("verify error message", response.jsonPath().get("errorMessage"), is(errorMessage));
    } else {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }

  }
}
