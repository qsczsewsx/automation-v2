package com.tcbs.automation.prod;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsApplicationUser;
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
@UseTestDataFrom(value = "data/prod/ApiCheck105C.csv", separator = '|')
public class ApiCheckcode105CTest {

  @Getter
  private String testCaseName;
  @Getter
  private int statusCode;
  private String code105C;
  private String erroMsg;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api Check code105C")
  public void verifyCheckcode105CTest() {

    System.out.println("TestCaseName : " + testCaseName);

    Response response = callPRODFindCustOpsApi(testCaseName, "missing code105C param", "code105c", code105C);

    assertThat(response.getStatusCode(), is(statusCode));

    if (response.statusCode() == 200) {

      verifyPRODPersonalInfo(response, "profileDto.personalInfo", "code105C", code105C);
      verifyPRODIdentityCard(response, "profileDto.personalInfo.identityCard", "code105C", code105C);
      verifyPRODBankInfo(response, "profileDto.bankAccounts", "code105C", code105C);
      verifyPRODContractTrust(response, "contractTrustDto", "code105C", code105C);
      verifyPRODKycHistory(response, "kycHistoryDtos", "code105C", code105C);
      String derivativeActivationStatus = response.jsonPath().getString("profileDto.accountStatus.derivativeActivationStatus");
      if (TcbsApplicationUser.getByUserAppId(code105C, "7").size() > 0) {
        TcbsApplicationUser tcbsApplicationUser = TcbsApplicationUser.getByUserAppId(code105C, "7").get(0);
        assertEquals(tcbsApplicationUser.getStatus(), getDerivativeActivationStatus(derivativeActivationStatus));
      } else {
        assertEquals("NOT_VSD_ACTIVATE_YET", derivativeActivationStatus);
      }
    } else if (response.statusCode() == 400) {
      String actualMessage = response.jsonPath().get("message");
      assertEquals(erroMsg, actualMessage);
    }
  }

}
