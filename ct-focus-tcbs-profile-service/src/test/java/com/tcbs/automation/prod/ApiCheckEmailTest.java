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
@UseTestDataFrom(value = "data/prod/ApiCheckEmail.csv", separator = '|')
public class ApiCheckEmailTest {

  @Getter
  private String testCaseName;
  @Getter
  private int statusCode;
  private String email;
  private String erroMsg;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api Check Email")
  public void verifyCheckEmailTest() {

    System.out.println("TestCaseName : " + testCaseName);

    Response response = callPRODFindCustOpsApi(testCaseName, "missing email param", "email", email);

    assertThat(response.getStatusCode(), is(statusCode));

    if (response.statusCode() == 200) {

      verifyPRODPersonalInfo(response, "profileDto.personalInfo", "email", email);
      verifyPRODIdentityCard(response, "profileDto.personalInfo.identityCard", "email", email);
      verifyPRODBankInfo(response, "profileDto.bankAccounts", "email", email);
      verifyPRODContractTrust(response, "contractTrustDto", "email", email);
      verifyPRODKycHistory(response, "kycHistoryDtos", "email", email);

    } else if (response.statusCode() == 400) {
      String actualMessage = response.jsonPath().get("message");
      assertEquals(erroMsg, actualMessage);
    }
  }

}
