package com.tcbs.automation.other;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import io.restassured.response.Response;
import lombok.Getter;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;

import java.util.HashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static common.CallApiUtils.callGetApiHasParams;
import static common.CallApiUtils.clearCache;
import static common.CommonUtils.*;
import static common.ProfileTools.TOKEN;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

//@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/ApiGetBankSubAccountByUsername.csv", separator = '|')
public class ApiGetBankSubAccountByUserNameTest {

  @Getter
  private String testCaseName;
  @Getter
  private String code105C;
  private String fields;
  private String fieldsVal;
  private int statusCode;
  private String erroMsg;


  @TestCase(name = "#testCaseName")
  @Title("Verify api Get Bank SubAccount By UserName")
  public void verifyApiGetBankSubAccountByUserNameTest() {

    System.out.println("TestCaseName : " + testCaseName);

    clearCache(CLEAR_CACHE, "x-api-key", API_KEY);

    HashMap<String, Object> params = getParams(testCaseName, fields, fieldsVal);

    Response response = callGetApiHasParams(GET_BANK_SUBACCOUNT_BY_USERNAME.replace("{code105C}", code105C), "x-api-key", TOKEN, params);

    assertThat(response.getStatusCode(), is(statusCode));

    if (response.statusCode() == 200) {

      if (testCaseName.contains("valid code105C")) {
        verifyBankSubAccountsInfo(response, "bankSubAccounts", "code105c", code105C);
      } else {
        verifyOnboardingBasicInfo(response, "basicInfo", "code105C", code105C);
        verifyOnboaringPersonalInfo(response, "personalInfo", "code105C", code105C);
      }

    } else if (response.statusCode() == 400) {
      String actualMessage = response.jsonPath().get("message");
      assertEquals(erroMsg, actualMessage);
    }
  }

  public HashMap<String, Object> getParams(String testCaseName, String fields, String fieldsVal) {
    HashMap<String, Object> getParams = new HashMap<>();
    if (testCaseName.contains("missing params")) {
      getParams = new HashMap<>();
    } else {
      getParams.put(fields, fieldsVal);
    }
    return getParams;
  }

}
