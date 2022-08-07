package com.tcbs.automation.authen.oauth2;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.Oauth2RefreshToken;
import com.tcbs.automation.cas.Oauth2RegisteredClient;
import com.tcbs.automation.cas.TcbsAuthenInfo;
import com.tcbs.automation.cas.TcbsUser;
import com.tcbs.automation.tools.ConvertUtils;
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

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.OAUTH2_GET_ACCESS_TOKEN_FROM_AUTHORIZATION_CODE;
import static com.tcbs.automation.tools.CompareUtils.equalLists;
import static com.tcbs.automation.tools.FormatUtils.syncData;
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
  public void setup() {
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
      Integer expiresIn = response.jsonPath().get("expires_in");
      Map<String, Object> accessTokenClaims = CommonUtils.decodeToken(accessToken);
      Map<String, Object> refreshTokenClaims = CommonUtils.decodeToken(refreshToken);
      TcbsUser tcbsUser = TcbsUser.getByUserName(USERNAME_DEFAULT);
      String tcbsId = tcbsUser.getTcbsid();
      String sessionId = (String) accessTokenClaims.get("sessionID");
      TcbsAuthenInfo tcbsAuthenInfo = null;
      Oauth2RegisteredClient oauth2RegisteredClient = null;
      Oauth2RefreshToken oauth2RefreshToken = null;
      try {
        tcbsAuthenInfo = TcbsAuthenInfo.getBySessionId(sessionId);
      } catch (Exception ex) {
        ex.printStackTrace();
        System.out.println("TcbsAuthenInfo.getBySessionId error");
      }
      try {
        oauth2RegisteredClient = Oauth2RegisteredClient.getByClientId(CLIENT_ID_DEFAULT);
      } catch (Exception ex) {
        System.out.println("Oauth2RegisteredClient.getByClientId error");
      }
      try {
        oauth2RefreshToken = Oauth2RefreshToken.getBySessionId(sessionId);
      } catch (Exception ex) {
        ex.printStackTrace();
        System.out.println("Oauth2RefreshToken.getBySessionId error");
      }

      assertEquals(USERNAME_DEFAULT, accessTokenClaims.get("custodyID"));
      assertEquals("customer", accessTokenClaims.get("subType"));
      assertEquals(CLIENT_ID_DEFAULT, accessTokenClaims.get("clientID"));
      assertEquals(tcbsId, accessTokenClaims.get("sub"));

      assertEquals(sessionId, refreshTokenClaims.get("sessionID"));
      assertEquals(tcbsId, refreshTokenClaims.get("sub"));

      assertNotNull(tcbsAuthenInfo);
      assertEquals(tcbsAuthenInfo.getUserId(), tcbsUser.getId().toString());
      assertNotNull(oauth2RegisteredClient);
      assertEquals(oauth2RegisteredClient.getId(), new BigDecimal(tcbsAuthenInfo.getRegisteredClientId()));
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
      .baseUri(OAUTH2_GET_ACCESS_TOKEN_FROM_AUTHORIZATION_CODE);
    return requestSpecification.formParams(body).post();
  }
}
