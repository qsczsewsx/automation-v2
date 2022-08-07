package com.tcbs.automation.authen.oauth2;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.LinkedHashMap;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.OAUTH2_GET_AUTHORIZATION_CODE;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/authen/oauth2/ApiGetAuthorizationCode.csv", separator = '|')
public class ApiGetAuthorizationCodeTest {
  private String testCaseName;
  private int statusCode;
  private String errorCode;
  private String errorMessage;
  private String clientId;
  private String username;
  private String password;
  private String codeChallenge;
  private String redirectUri;

  @Before
  public void setup() {
    clientId = syncData(clientId);
    username = syncData(username);
    password = syncData(password);
    codeChallenge = syncData(codeChallenge);
    redirectUri = syncData(redirectUri);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api get authorization code")
  public void verifyApiGetAuthorizationCode() {
    System.out.println("TestCaseName : " + testCaseName);

    Response response = callApiGetAuthorizationCode(clientId, username, password, codeChallenge, redirectUri);
    assertThat(response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      String rs = response.jsonPath().get("redirect_uri");
      assertThat("verify redirect uri 1", rs.length() > redirectUri.length());
      assertThat("verify redirect uri 2", rs.startsWith(redirectUri));
    } else if (statusCode == 400) {
      assertThat("verify error code", response.jsonPath().get("code"), is(errorCode));
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }

  public static Response callApiGetAuthorizationCode(String clientId, String username, String password, String codeChallenge, String redirectUri) {
    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    body.put("client_id", clientId);
    body.put("username", username);
    body.put("password", password);
    body.put("code_challenge", codeChallenge);
    body.put("redirect_uri", redirectUri);
    body.put("allow_redirect", false);

    RequestSpecification requestSpecification = given()
      .baseUri(OAUTH2_GET_AUTHORIZATION_CODE);
    return requestSpecification.formParams(body).post();
  }
}