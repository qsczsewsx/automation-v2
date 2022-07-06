package com.tcbs.automation.newonboarding.tci3;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.login.LoginApi;
import com.tcbs.automation.login.TheUserInfo;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.rest.RestRequests;
import net.serenitybdd.screenplay.Actor;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.GET_ACCOUNT_STATUS_API;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/GetAccountStatus.csv", separator = '|')
public class GetAccountStatusTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String accountLogin;
  private String tcbsid;
  private String derivativeAccountStatus;
  private String token;

  @Before
  public void before() {
    Actor actor = Actor.named("tuanna2");
    LoginApi.withCredentials(accountLogin, "abc123").performAs(actor);
    token = TheUserInfo.aboutLoginData().answeredBy(actor).getToken();
  }


  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api get account status")
  public void verifySignContractTest() {

    System.out.println("TescaseName : " + testCaseName);

    Response response = RestRequests.given()
      .headers(new Headers(
        new Header("Authorization", "Bearer " + token)
      ))
      .when()
      .get(GET_ACCOUNT_STATUS_API + tcbsid);

    assertThat("verify status code", response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      assertThat("verify derivative status", response.jsonPath().get("derivativeAccountStatus").toString().trim(), is(derivativeAccountStatus));
    }
  }
}
