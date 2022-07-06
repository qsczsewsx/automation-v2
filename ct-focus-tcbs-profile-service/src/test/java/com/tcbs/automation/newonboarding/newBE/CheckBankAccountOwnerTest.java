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
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.CHECK_BANK_ACCOUNT_OWNER;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/CheckBankAccountOwner.csv", separator = '|')
public class CheckBankAccountOwnerTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String bankAccountName;
  private String bankAccountNumber;
  private String expectedStatus;
  private String tcbsId;
  private String errorMessage;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api check bank account owner")
  public void performTest() {
    bankAccountName = syncData(bankAccountName);
    bankAccountNumber = syncData(bankAccountNumber);

    Response response = given()
      .baseUri(CHECK_BANK_ACCOUNT_OWNER.replace("#tcbsId#", tcbsId))
      .header("Authorization", "Bearer " + ASSIGN_TASK_TO_MAKER_KEY)
      .param("bankAccountName", bankAccountName)
      .param("bankAccountNumber", bankAccountNumber)
      .get();

    assertThat("verify status code", response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      assertThat(response.jsonPath().get("data.result").toString(), is(expectedStatus));
    } else {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }
}