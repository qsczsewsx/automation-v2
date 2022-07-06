package com.tcbs.automation.newonboarding.tci3;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.shazam.shazamcrest.MatcherAssert;
import com.tcbs.automation.cas.ObTask;
import com.tcbs.automation.cas.TcbsBankAccount;
import com.tcbs.automation.cas.TcbsNewOnboardingStatus;
import com.tcbs.automation.cas.TcbsUser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/UpdateBankInfo.csv", separator = '|')
public class UpdateBankInfoTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String tcbsId;
  private String errorMessage;
  private String bankAccountId;
  private String accountNo;
  private String accountName;
  private String bankCode;
  private String bankName;
  private String bankProvince;
  private String branchCode;
  private String bankType;
  private HashMap<String, Object> body;
  private String userId;

  @Before
  public void setup() {
    bankAccountId = syncData(bankAccountId);
    accountNo = syncData(accountNo);
    accountName = syncData(accountName);
    bankCode = syncData(bankCode);
    bankName = syncData(bankName);
    bankProvince = syncData(bankProvince);
    branchCode = syncData(branchCode);
    bankType = syncData(bankType);

    body = new HashMap<>();

    List<LinkedHashMap<String, Object>> bankAccounts = new ArrayList<>();

    LinkedHashMap<String, Object> bankAccount1 = new LinkedHashMap<>();
    bankAccount1.put("bankAccountId", StringUtils.isNumeric(bankAccountId) ? Long.parseLong(bankAccountId) : bankAccountId);
    bankAccount1.put("accountNo", accountNo);
    bankAccount1.put("accountName", accountName);
    bankAccount1.put("bankCode", bankCode);
    bankAccount1.put("bankName", bankName);
    bankAccount1.put("bankProvince", bankProvince);
    bankAccount1.put("branchCode", branchCode);
    bankAccount1.put("bankType", bankType);
    bankAccounts.add(bankAccount1);

    body.put("bankAccounts", bankAccounts);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api update bank info")
  public void perfomTest() {
    RequestSpecification requestSpecification = given()
      .baseUri(TCBSPROFILE_UPDATE_BANK_ACCOUNT.replace("#tcbsId#", tcbsId))
      .header("x-api-key", TCBSPROFILE_INQUIRYGROUPINFOKEY)
      .contentType("application/json");
    Response response;

    if (testCaseName.contains("missing BODY")) {
      response = requestSpecification.put();
    } else {
      response = requestSpecification.body(body).put();
    }

    assertThat("verify status", response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      // Query from DB
      userId = TcbsUser.getByTcbsId(tcbsId).getId().toString();
      List<TcbsBankAccount> tcbsBankAccount = TcbsBankAccount.getListBanks(userId);

      // Verify input data and data in DB
      assertThat(accountNo, is(tcbsBankAccount.get(0).getBankAccountNo()));
      assertThat(accountName, is(tcbsBankAccount.get(0).getBankAccountName()));
      assertThat(bankCode, is(tcbsBankAccount.get(0).getBankCode()));
      assertThat(bankName, is(tcbsBankAccount.get(0).getBankName()));
      assertThat(bankProvince, is(tcbsBankAccount.get(0).getBankprovince()));
      assertThat(branchCode, is(tcbsBankAccount.get(0).getBankBranch()));

      // Verify update BANK_INFO_STATUS to WAIT_FOR_VERIFY
      String tcbsBankInfoStatus = TcbsNewOnboardingStatus.getByUserIdAndStatusKey(userId, "BANK_INFO_STATUS").getStatusValue();
      assertThat(tcbsBankInfoStatus, is("WAIT_FOR_VERIFY"));

      // Verify generate task for OPS
      assertThat(ObTask.getByUserIdAndActionId(userId, "12").getId(), is(notNullValue()));

    } else {
      MatcherAssert.assertThat("verify error message", response.jsonPath().get("message"),
        is(errorMessage));
    }
  }

  @After
  public void clearData() {
    // Clear data
    if (statusCode == 200) {
      ObTask.deleteByUserId(userId, "12");
      TcbsNewOnboardingStatus.updateStatusValueByUserId(userId, "BANK_INFO_STATUS", "WAIT_FOR_UPDATE");
      Response responseCache = given()
        .baseUri(DELETE_CACHE)
        .header("x-api-key", API_KEY)
        .delete();
      assertThat(responseCache.getStatusCode(), is(204));
    }
  }
}
