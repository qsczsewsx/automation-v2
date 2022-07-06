package com.tcbs.automation.accountClosing;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.login.LoginApi;
import com.tcbs.automation.login.TheUserInfo;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.screenplay.Actor;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Base64;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.VIEW_CLOSE_ACCOUNT_CONTRACT;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/accountClosing/ViewCloseAccountContractTest.csv", separator = '|')
public class ViewCloseAccountContractTest {
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
    } else {
      token = syncData(token);
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api view close account contract")
  public void verifyViewCloseAccountContractTest() {
    Response response = given().baseUri(VIEW_CLOSE_ACCOUNT_CONTRACT).header("Authorization", "Bearer " + token).get();

    assertThat("verify status code", response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      assertThat("verify accountClose", verifyStringIsBase64(response.jsonPath().get("accountClose")), is(true));
    } else if (statusCode == 400) {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }

  public boolean verifyStringIsBase64(String path) {
    try {
      Base64.getDecoder().decode(path);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }
}
