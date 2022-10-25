package com.automation.authen.oauth2;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.automation.config.xxxxprofileservice.xxxxProfileServiceConfig;
import com.automation.cas.Oauth2RefreshToken;
import com.automation.cas.Oauth2RegisteredClient;
import com.automation.cas.xxxxAuthenInfo;
import com.automation.cas.xxxxUser;
import com.automation.tools.ConvertUtils;
import common.CommonUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.automation.authen.oauth2.ApiGetAccessTokenFromAuthorizationCodeTest.*;
import static com.automation.tools.CompareUtils.equalLists;
import static com.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/authen/oauth2/ApiGetAccessTokenFromRefreshToken.csv", separator = '|')
public class ApiGetAccessTokenFromRefreshTokenTest {
  public static String SESSION_ID = "sessionID";
  private String testCaseName;
  private int statusCode;
  private String errorCode;
  private String errorMessage;
  private String clientId;
  private String grantType;
  private String refreshToken;

  @Before
  public void setup() throws Exception {
    clientId = syncData(clientId);
    refreshToken = syncData(refreshToken);

    if ("gen".equalsIgnoreCase(refreshToken)) {
      Response rs = ApiGetAuthorizationCodeTest.callApiGetAuthorizationCode(CLIENT_ID_DEFAULT, USERNAME_DEFAULT, PASSWORD_DEFAULT, CODE_CHALLENGE_DEFAULT, REDIRECT_URI_DEFAULT);
      if (rs.getStatusCode() == 200) {
        String redirectUri = rs.jsonPath().get("redirect_uri");
        String authorizationCode = redirectUri.replaceAll(REDIRECT_URI_DEFAULT + "\\?authorization_code=", "");
        rs = ApiGetAccessTokenFromAuthorizationCodeTest.callApiGetAccessTokenFromAuthorizationCode(CLIENT_ID_DEFAULT, "e3e1b1790d7d3ad451853b506756446cb017d07ef446517a8ddec057", "authorization_code", authorizationCode);
        if (rs.getStatusCode() == 200) {
          refreshToken = rs.jsonPath().get("refresh_token");
        }
      }
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api get access token from refresh token")
  public void verifyApiGetAccessTokenFromRefreshToken() {
    System.out.println("TestCaseName : " + testCaseName);

    Response response = callApiGetAccessTokenFromRefreshToken(clientId, refreshToken, grantType);
    assertThat(response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      String accessToken = response.jsonPath().get("access_token");
      String newRefreshToken = response.jsonPath().get("refresh_token");
      Map<String, Object> accessTokenClaims = CommonUtils.decodeToken(accessToken);
      Map<String, Object> oldRefreshTokenClaims = CommonUtils.decodeToken(refreshToken);
      Map<String, Object> newRefreshTokenClaims = CommonUtils.decodeToken(newRefreshToken);
      xxxxUser xxxxUser = xxxxUser.getByUserName(USERNAME_DEFAULT);
      String xxxxId = xxxxUser.getxxxxid();
      String sessionId = (String) accessTokenClaims.get(SESSION_ID);
      xxxxAuthenInfo xxxxAuthenInfo = null;
      Oauth2RegisteredClient oauth2RegisteredClient = null;
      Oauth2RefreshToken newOauth2RefreshToken = null;
      Oauth2RefreshToken oldOauth2RefreshToken = null;
      try {
        xxxxAuthenInfo = xxxxAuthenInfo.getBySessionId(sessionId);
      } catch (Exception ex) {
        ex.printStackTrace();
        System.out.println("xxxxAuthenInfo.getBySessionId error");
      }
      try {
        oauth2RegisteredClient = Oauth2RegisteredClient.getByClientId(CLIENT_ID_DEFAULT);
      } catch (Exception ex) {
        System.out.println("Oauth2RegisteredClient.getByClientId error");
      }
      try {
        newOauth2RefreshToken = Oauth2RefreshToken.getBySessionId(sessionId);
      } catch (Exception ex) {
        System.out.println("Oauth2RefreshToken.getBySessionId error");
      }
      try {
        oldOauth2RefreshToken = Oauth2RefreshToken.getBySessionId((String) oldRefreshTokenClaims.get(SESSION_ID));
      } catch (Exception ex) {
        System.out.println("Oauth2RefreshToken.getBySessionId error");
      }

      assertEquals(USERNAME_DEFAULT, accessTokenClaims.get("custodyID"));
      assertEquals("customer", accessTokenClaims.get("subType"));
      assertEquals(CLIENT_ID_DEFAULT, accessTokenClaims.get("clientID"));
      assertEquals(xxxxId, accessTokenClaims.get("sub"));

      assertEquals(sessionId, newRefreshTokenClaims.get(SESSION_ID));
      assertEquals(xxxxId, newRefreshTokenClaims.get("sub"));

      assertNotNull(xxxxAuthenInfo);
      assertEquals(xxxxAuthenInfo.getUserId(), xxxxUser.getId().toString());
      assertNotNull(oauth2RegisteredClient);
      assertEquals(oauth2RegisteredClient.getId(), new BigDecimal(xxxxAuthenInfo.getRegisteredClientId()));
      List<String> scopes = ConvertUtils.convertJsonToArray(oauth2RegisteredClient.getScopes(), String.class);
      assertThat("verify scopes", equalLists(scopes, (List<String>) accessTokenClaims.get("scope")));

      assertNotNull(newOauth2RefreshToken);
      assertEquals(new BigDecimal(1), newOauth2RefreshToken.getStatus());
      assertNotNull(oldOauth2RefreshToken);
      assertEquals(new BigDecimal(0), oldOauth2RefreshToken.getStatus());
    } else if (statusCode == 400) {
      assertThat("verify error code", response.jsonPath().get("code"), is(errorCode));
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }

  public static Response callApiGetAccessTokenFromRefreshToken(String clientId, String refreshToken, String grantType) {
    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    body.put("client_id", clientId);
    body.put("refresh_token", refreshToken);
    body.put("grant_type", grantType);

    RequestSpecification requestSpecification = given()
      .baseUri(xxxxProfileServiceConfig.OAUTH2_GET_ACCESS_TOKEN_FROM_REFRESH_TOKEN);
    return requestSpecification.formParams(body).post();
  }
}
