package com.automation.authen;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.automation.config.xxxxprofileservice.xxxxProfileServiceConfig;
import com.automation.cas.xxxxUser;
import com.automation.login.LoginApi;
import com.automation.login.TheUserInfo;
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

import static com.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/authen/GenClientKey.csv", separator = '|')
public class GetClientKeyTest {
  private String testCaseName;
  private int statusCode;
  private String xxxxId;
  private String code105C;
  private String errorMessage;
  private String expectedClientKey;

  @Before
  public void setup() {
    xxxxId = syncData(xxxxId);
    code105C = syncData(code105C);
    if (testCaseName.contains("clientKey in database is empty") || testCaseName.contains("Verify format of client key")) {
      xxxxUser.updateClientKeyxxxxId(xxxxId, "");
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
      .baseUri(xxxxProfileServiceConfig.AUTHEN_GET_CLIENT_KEY.replace("{xxxxId}", xxxxId))
      .header("x-api-key", xxxxProfileServiceConfig.API_KEY)
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
        Matcher xxxxIdMatcher = clientKeyPattern.matcher(clientKey);
        assertEquals(true, xxxxIdMatcher.find());
      }
    } else {
      assertThat("verify error message", response.jsonPath().get("message").toString().trim(), is(errorMessage));
    }
  }
}
