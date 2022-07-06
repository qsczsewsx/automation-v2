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
@UseTestDataFrom(value = "data/prod/ApiCheckIdNumber.csv", separator = '|')
public class ApiCheckIdNumberTest {

  @Getter
  private String testCaseName;
  @Getter
  private int statusCode;
  private String idNumber;
  private String erroMsg;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api Check IdNumber")
  public void verifyCheckIdNumberTest() {

    System.out.println("TestCaseName : " + testCaseName);

    Response response = callPRODFindCustOpsApi(testCaseName, "missing idNumber param", "idNumber", idNumber);

    assertThat(response.getStatusCode(), is(statusCode));

    if (response.statusCode() == 200) {

      verifyPRODPersonalInfo(response, "profileDto.personalInfo", "idNumber", idNumber);
      verifyPRODIdentityCard(response, "profileDto.personalInfo.identityCard", "idNumber", idNumber);
      verifyPRODBankInfo(response, "profileDto.bankAccounts", "idNumber", idNumber);
      verifyPRODContractTrust(response, "contractTrustDto", "idNumber", idNumber);
      verifyPRODKycHistory(response, "kycHistoryDtos", "idNumber", idNumber);

    } else if (response.statusCode() == 400) {
      String actualMessage = response.jsonPath().get("message");
      assertEquals(erroMsg, actualMessage);
    }
  }

}
