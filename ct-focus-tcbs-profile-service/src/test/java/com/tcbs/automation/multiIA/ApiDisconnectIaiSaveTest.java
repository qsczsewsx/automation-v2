package com.tcbs.automation.multiIA;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsBankIaaccount;
import com.tcbs.automation.cas.TcbsBankSubaccount;
import com.tcbs.automation.login.LoginApi;
import com.tcbs.automation.login.TheUserInfo;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.screenplay.Actor;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.MULTI_CONNECT_IA_ISAVE;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.MULTI_DISCONNECT_IA_ISAVE;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/multiIA/ApiDisconnectIaiSaveTest.csv", separator = '|')
public class ApiDisconnectIaiSaveTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String username;
  private String tcbsId;
  private String userId;
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
  @Title("Verify api disconnect IA iSave")
  public void verifyDisconnectIaiSaveTest() {
    System.out.println("TestCaseName : " + testCaseName);
    Response response = given()
      .baseUri(MULTI_DISCONNECT_IA_ISAVE.replace("{tcbsId}", tcbsId))
      .header("Authorization", "Bearer " + token)
      .contentType("application/json")
      .post();

    assertThat("verify status code", response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      assertThat("verify connect ia isave success", TcbsBankIaaccount.getIaiSaveBank(userId, "ISAVE"), is(new BigDecimal(0)));
      assertThat("verify connect ia isave success", TcbsBankSubaccount.getSubAccountByUserIdAndAccountType(userId, "ISAVE"), is(notNullValue()));
    } else if (statusCode == 400) {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }

  @After
  public void connectiSave() {
    given()
      .baseUri(MULTI_CONNECT_IA_ISAVE.replace("{tcbsId}", tcbsId))
      .header("Authorization", "Bearer " + token)
      .contentType("application/json")
      .post();
  }
}
