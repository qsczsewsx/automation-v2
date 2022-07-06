package com.tcbs.automation.bpm;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsBankAccount;
import com.tcbs.automation.cas.TcbsUser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/bpm/CheckDuplicateBankInfo.csv", separator = '|')
public class CheckDuplicateBankInfoTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String accountNo;
  private String bankCode;
  private String tcbsId;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api check duplicate bank info")
  public void performTest() {
    accountNo = syncData(accountNo);
    bankCode = syncData(bankCode);
    tcbsId = syncData(tcbsId);

    RequestSpecification requestSpecification = given()
      .baseUri(CHECK_DUPLICATE_BANK_INFO)
      .header("x-api-key",
        testCaseName.contains("invalid x-api-key") ? TCBSPROFILE_AUTHORIZATION : TCBSPROFILE_INQUIRYGROUPINFOKEY);

    Response response;

    if (testCaseName.contains("missing param accountNo")) {
      response = requestSpecification.param("bankCode", bankCode).get();
    } else if (testCaseName.contains("missing param bankCode")) {
      response = requestSpecification.param("accountNo", accountNo).get();
    } else if (testCaseName.contains("missing param tcbsId")) {
      response = requestSpecification.param("accountNo", accountNo).get();
    } else {
      response = requestSpecification.param("accountNo", accountNo).param("bankCode", bankCode).param("tcbsid", tcbsId).get();
    }

    assertThat("verify status code", response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      assertThat("verify code in response", response.jsonPath().get("code"), is(errorMessage));
      if (testCaseName.contains("as ACTIVE")) {
        assertThat("verify isValid", response.jsonPath().get("isValid"), is(false));
        String code105C = TcbsUser.getById(TcbsBankAccount.getUserIdByAccountNo(accountNo).getUserId()).getUsername();
        assertThat("verify data user existed", response.jsonPath().get("data"), is(code105C));
      } else {
        assertThat("verify isValid", response.jsonPath().get("isValid"), is(true));
      }
    } else {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }
}