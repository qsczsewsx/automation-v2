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

import static com.automation.tools.CompareUtils.equalLists;
import static com.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/authen/oauth2/ApiGetAccessTokenFromAuthorizationCode.csv", separator = '|')
public class ApiGetAccessTokenFromAuthorizationCodeTest {
  public static String CLIENT_ID_DEFAULT = "343d5164-1638-11ed-861d-0242ac120002";
  public static String REDIRECT_URI_DEFAULT = "signintcinvest://callback";
  public static String USERNAME_DEFAULT = "105C065421";
  public static String PASSWORD_DEFAULT = "abc123";
  public static String CODE_CHALLENGE_DEFAULT = "T2gzU-SiAM0hzU94QacQQRMFReCtE-OI-_h_1Uu4504";

  private String testCaseName;
  private int statusCode;
  private String errorCode;
  private String errorMessage;
  private String clientId;
  private String grantType;
  private String codeVerifier;
  private String authorizationCode;

  @Before
  public void setup() throws Exception {
    clientId = syncData(clientId);
    codeVerifier = syncData(codeVerifier);
    authorizationCode = syncData(authorizationCode);

    if ("gen".equalsIgnoreCase(authorizationCode)) {
      Response rs = ApiGetAuthorizationCodeTest.callApiGetAuthorizationCode(CLIENT_ID_DEFAULT, USERNAME_DEFAULT, PASSWORD_DEFAULT, CODE_CHALLENGE_DEFAULT, REDIRECT_URI_DEFAULT);
      if (rs.getStatusCode() == 200) {
        String redirectUri = rs.jsonPath().get("redirect_uri");
        authorizationCode = redirectUri.replaceAll(REDIRECT_URI_DEFAULT + "\\?authorization_code=", "");
      }
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api get access token from authorization code")
  public void verifyApiGetAccessTokenFromAuthorizationCode() {
    System.out.println("TestCaseName : " + testCaseName);

    Response response = callApiGetAccessTokenFromAuthorizationCode(clientId, codeVerifier, grantType, authorizationCode);
    assertThat(response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      assertEquals("Bearer", response.jsonPath().get("token_type"));
      String accessToken = response.jsonPath().get("access_token");
      String refreshToken = response.jsonPath().get("refresh_token");
      Map<String, Object> accessTokenClaims = CommonUtils.decodeToken(accessToken);
      Map<String, Object> refreshTokenClaims = CommonUtils.decodeToken(refreshToken);
      xxxxUser xxxxUser = xxxxUser.getByUserName(USERNAME_DEFAULT);
      String xxxxId = xxxxUser.getxxxxid();
      String sessionId = (String) accessTokenClaims.get("sessionID");
      xxxxAuthenInfo xxxxAuthenInfo = null;
      Oauth2RegisteredClient oauth2RegisteredClient = null;
      Oauth2RefreshToken oauth2RefreshToken = null;
      try {
        xxxxAuthenInfo = xxxxAuthenInfo.getBySessionId(sessionId);
      } catch (Exception ex) {
        System.out.println("xxxxAuthenInfo.getBySessionId error");
      }
      try {
        oauth2RegisteredClient = Oauth2RegisteredClient.getByClientId(CLIENT_ID_DEFAULT);
      } catch (Exception ex) {
        System.out.println("Oauth2RegisteredClient.getByClientId error");
      }
      try {
        oauth2RefreshToken = Oauth2RefreshToken.getBySessionId(sessionId);
      } catch (Exception ex) {
        System.out.println("Oauth2RefreshToken.getBySessionId error");
      }

      assertEquals(USERNAME_DEFAULT, accessTokenClaims.get("custodyID"));
      assertEquals("customer", accessTokenClaims.get("subType"));
      assertEquals(CLIENT_ID_DEFAULT, accessTokenClaims.get("clientID"));
      assertEquals(xxxxId, accessTokenClaims.get("sub"));

      assertEquals(sessionId, refreshTokenClaims.get("sessionID"));
      assertEquals(xxxxId, refreshTokenClaims.get("sub"));

      assertNotNull(xxxxAuthenInfo);
      assertEquals(xxxxAuthenInfo.getUserId(), xxxxUser.getId().toString());
      assertNotNull(oauth2RegisteredClient);
      assertEquals(oauth2RegisteredClient.getId(), new BigDecimal(xxxxAuthenInfo.getRegisteredClientId()));
      List<String> scopes = ConvertUtils.convertJsonToArray(oauth2RegisteredClient.getScopes(), String.class);
      assertThat("verify scopes", equalLists(scopes, (List<String>) accessTokenClaims.get("scope")));

      assertNotNull(oauth2RefreshToken);
      assertEquals(new BigDecimal(1), oauth2RefreshToken.getStatus());
    } else if (statusCode == 400) {
      assertThat("verify error code", response.jsonPath().get("code"), is(errorCode));
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }

  public static Response callApiGetAccessTokenFromAuthorizationCode(String clientId, String codeVerifier, String grantType, String authorizationCode) {
    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    body.put("client_id", clientId);
    body.put("code_verifier", codeVerifier);
    body.put("grant_type", grantType);
    body.put("authorization_code", authorizationCode);

    RequestSpecification requestSpecification = given()
      .baseUri(xxxxProfileServiceConfig.OAUTH2_GET_ACCESS_TOKEN_FROM_AUTHORIZATION_CODE);
    return requestSpecification.formParams(body).post();
  }
}
