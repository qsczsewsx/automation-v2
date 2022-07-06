package com.tcbs.automation.other;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsBankSubaccount;
import com.tcbs.automation.cas.TcbsUser;
import io.restassured.response.Response;
import lombok.Getter;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;

import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static common.CallApiUtils.callDeleteApiHasNoParams;
import static common.CallApiUtils.clearCache;
import static common.ProfileTools.TOKEN;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

//@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/ApiClearBankSubAccountByUsername.csv", separator = '|')
public class ApiClearBankSubAccountByUserNameTest {

  @Getter
  private String testCaseName;
  @Getter
  private String code105C;
  private int statusCode;
  private String erroMsg;


  @TestCase(name = "#testCaseName")
  @Title("Verify api Get Bank SubAccount By UserName")
  public void verifyApiClearBankSubAccountByUserNameTest() {

    System.out.println("TestCaseName : " + testCaseName);

    clearCache(CLEAR_CACHE, "x-api-key", API_KEY);

    Response response = callDeleteApiHasNoParams(CLEAR_BANK_SUBACCOUNT_STATUS_BY_USERNAME.replace("{code105C}", code105C), "x-api-key", TOKEN);

    assertThat(response.getStatusCode(), is(statusCode));

    if (response.statusCode() == 200) {
      assertEquals(response.asString(), erroMsg);
      if (TcbsUser.getListByUserName(code105C).size() > 0) {
        TcbsUser tcbsUser = TcbsUser.getByUserName(code105C);
        List<TcbsBankSubaccount> tcbsBankSubaccountList = TcbsBankSubaccount.getListBank(tcbsUser.getId().toString());
        assertEquals(tcbsBankSubaccountList.size(), 0);
      }


    } else if (response.statusCode() == 400) {
      assertEquals(response.asString(), erroMsg);
    }
  }

}
