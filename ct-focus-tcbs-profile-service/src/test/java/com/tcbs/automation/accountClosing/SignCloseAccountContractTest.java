package com.tcbs.automation.accountClosing;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsUser;
import com.tcbs.automation.login.LoginApi;
import com.tcbs.automation.login.TheUserInfo;
import common.CommonUtils;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.screenplay.Actor;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.SIGN_CLOSE_ACCOUNT_CONTRACT;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/accountClosing/SignCloseAccountContractTest.csv", separator = '|')
public class SignCloseAccountContractTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String username;
  private String token;
  private String errorMessage;


  @Before
  public void before() {
    if (token.equalsIgnoreCase("token")) {
      Actor actor = Actor.named("vietdb");
      LoginApi.withCredentials(username, "abc123").performAs(actor);
      token = TheUserInfo.aboutLoginData().answeredBy(actor).getToken();
      String tcbsId = TcbsUser.getByUserName(username).getTcbsid();
      token = CommonUtils.authenOTP(tcbsId, token);
    } else {
      token = syncData(token);
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api view close account contract")
  public void verifySignCloseAccountContractTest() {
    Response response = given()
      .baseUri(SIGN_CLOSE_ACCOUNT_CONTRACT)
      .header("Authorization", "Bearer " + token)
      .post();

    assertThat("verify status code", response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      if (testCaseName.contains("view and sign contract")) {
        assertThat("verify signaccountClose", response.jsonPath().get("result"), is(true));
        assertThat("verify account status", TcbsUser.getByUserName(username).getAccountStatus().toString(), is("0"));
      } else {
        assertThat("verify signaccountClose", response.jsonPath().get("result"), is(false));
      }
    } else if (statusCode == 400) {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }

}
