package com.tcbs.automation.prod;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import static common.CallApiUtils.callPRODFindCustOpsApi;
import static common.CommonUtils.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/prod/ApiCheckPhoneNumber.csv", separator = '|')
public class ApiCheckPhoneNumberTest {

  @Getter
  private String testCaseName;
  @Getter
  private int statusCode;
  private String phone;
  private String erroMsg;
  private String userId;
  private String tcbsId;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api Check Phone Number")
  public void verifyCheckPhoneNumberTest() {

    System.out.println("TestCaseName : " + testCaseName);

    Response response = callPRODFindCustOpsApi(testCaseName, "missing phoneNumber param", "phone", phone);

    assertThat(response.getStatusCode(), is(statusCode));

    if (response.statusCode() == 200) {

      verifyPRODPersonalInfo(response, "profileDto.personalInfo", "phone", phone);
      verifyPRODIdentityCard(response, "profileDto.personalInfo.identityCard", "phone", phone);
      verifyPRODBankInfo(response, "profileDto.bankAccounts", "phone", phone);
      verifyPRODContractTrust(response, "contractTrustDto", "phone", phone);
      verifyPRODKycHistory(response, "kycHistoryDtos", "phone", phone);

    } else if (response.statusCode() == 400) {
      String actualMessage = response.jsonPath().get("message");
      assertEquals(erroMsg, actualMessage);
    }
  }

}
