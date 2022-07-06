package com.tcbs.automation.prod;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsUser;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

import static common.CallApiUtils.callPRODFindCustOpsApi;
import static common.CommonUtils.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/prod/ApiCheckTcbsId.csv", separator = '|')
public class ApiCheckTcbsIdTest {

  @Getter
  private String testCaseName;
  @Getter
  private int statusCode;
  private String tcbsId;
  private String erroMsg;
  private String userId;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api Check TcbsId")
  public void verifyCheckTcbsIdTest() {

    System.out.println("TestCaseName : " + testCaseName);

    Response response = callPRODFindCustOpsApi(testCaseName, "missing tcbsId param", "tcbsId", tcbsId);

    assertThat(response.getStatusCode(), is(statusCode));

    if (response.statusCode() == 200) {

      verifyPRODPersonalInfo(response, "profileDto.personalInfo", "tcbsid", tcbsId);
      verifyPRODIdentityCard(response, "profileDto.personalInfo.identityCard", "tcbsid", tcbsId);
      verifyPRODScanIdFiles(response, "profileDto.personalInfo.identityCard.scanIdFiles", "tcbsid", tcbsId);
      verifyAccountStatus(response, tcbsId);
      verifyFMBOnboardingStatus(response, "profileDto.accountStatus.onboardingStatus.", "tcbsid", tcbsId);
      verifyFMBBasicInfo(response, "profileDto.basicInfo", "tcbsId", tcbsId);
      verifyPRODBankInfo(response, "profileDto.bankAccounts", "tcbsid", tcbsId);
      verifyPRODContractTrust(response, "contractTrustDto", "tcbsid", tcbsId);
      verifyPRODKycHistory(response, "kycHistoryDtos", "tcbsid", tcbsId);

    } else if (response.statusCode() == 400) {
      String actualMessage = response.jsonPath().get("message");
      assertEquals(erroMsg, actualMessage);
    }
  }

  public void verifyAccountStatus(Response response, String tcbsId) {
    Map<String, Object> getResponse = response.jsonPath().getMap("profileDto.accountStatus");
    if (getResponse != null) {
      TcbsUser tcbsUser = TcbsUser.getByTcbsId(tcbsId);
      assertEquals(tcbsUser.getUsername(), getResponse.get("fundAccount"));
      assertEquals(tcbsUser.getUsername(), getResponse.get("flexAccount"));
      assertEquals(tcbsId, getResponse.get("tcBondAccount"));
    }
  }

}
