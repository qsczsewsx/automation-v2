package com.tcbs.automation.newonboarding.newBE;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.ASSIGN_TASK_TO_MAKER_KEY;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.GET_STATUS_CHECK_BANK_ACCOUNT_OWNER;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/GetStatusCheckBankAccountOwner.csv", separator = '|')
public class GetStatusCheckBankAccountOwnerTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String tcbsId;
  private String expectedStatus;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api get status check bank account owner")
  public void performTest() {
    tcbsId = syncData(tcbsId);
    expectedStatus = syncData(expectedStatus);
    String keys = "result,h2hName,history,checkDate,inputName";

    Response response = given()
      .baseUri(GET_STATUS_CHECK_BANK_ACCOUNT_OWNER.replace("#tcbsId#", tcbsId))
      .header("Authorization", "Bearer " + ASSIGN_TASK_TO_MAKER_KEY)
      .get();

    assertThat("verify status code", response.getStatusCode(), is(statusCode));

    if (testCaseName.contains("case valid request")) {
      for (String item : keys.split(",", -1)) {
        assertThat(response.jsonPath().getMap("data"), hasKey(item));
      }
      assertThat(response.jsonPath().get("data.checkDate").toString(), is(notNullValue()));
    } else {
      assertThat(response.jsonPath().get("data.result"), is(0));
    }
  }
}