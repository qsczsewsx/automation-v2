package com.tcbs.automation.authen;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsUser;
import com.tcbs.automation.login.LoginApi;
import com.tcbs.automation.login.TheUserInfo;
import common.CommonUtils;
import io.restassured.response.Response;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.screenplay.Actor;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.API_KEY;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.AUTHEN_GET_CLIENT_KEY;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/authen/GenClientKey.csv", separator = '|')
public class GetClientKeyTest {
  private String testCaseName;
  private int statusCode;
  private String tcbsId;
  private String code105C;
  private String errorMessage;
  private String expectedClientKey;

  @Before
  public void setup() {
    tcbsId = syncData(tcbsId);
    code105C = syncData(code105C);
    if (testCaseName.contains("clientKey in database is empty") || testCaseName.contains("Verify format of client key")) {
      TcbsUser.updateClientKeyTcbsId(tcbsId, "");
    }
    if (statusCode == 200) {
      Actor actor = Actor.named("haihv");
      LoginApi.withCredentials(code105C, "abc123").performAs(actor);
      String token = TheUserInfo.aboutLoginData().answeredBy(actor).getToken();
      Map<String, Object> claims = CommonUtils.decodeToken(token);
      expectedClientKey = (String) claims.get("client_key");
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api get client key")
  public void getClientKeyTest() {
    System.out.println("TestcaseName : " + testCaseName);

    Response response = given()
      .baseUri(AUTHEN_GET_CLIENT_KEY.replace("{tcbsId}", tcbsId))
      .header("x-api-key", API_KEY)
      .contentType("application/json")
      .get();

    assertThat("verify status code", response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      Map<String, Object> getResponse = response.jsonPath().getMap("");
      String clientKey = (String) getResponse.get("clientKey");
      assertEquals(expectedClientKey, clientKey);
      if (testCaseName.contains("Verify format of client key")) {
        assertEquals(32, clientKey.length());
        Pattern clientKeyPattern = Pattern.compile("([0-9]{10,11}.[a-zA-Z0-9]{20,21})$");
        Matcher tcbsIdMatcher = clientKeyPattern.matcher(clientKey);
        assertEquals(true, tcbsIdMatcher.find());
      }
    } else {
      assertThat("verify error message", response.jsonPath().get("message").toString().trim(), is(errorMessage));
    }
  }
}
