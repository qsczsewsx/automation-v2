package com.tcbs.automation.authen.oauth2;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.Oauth2RegisteredClient;
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

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.API_KEY;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.OAUTH2_GET_SERVICE_TOKEN;
import static com.tcbs.automation.tools.CompareUtils.equalLists;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/authen/oauth2/ApiGetServiceToken.csv", separator = '|')
public class ApiGetServiceTokenTest {
  private String testCaseName;
  private int statusCode;
  private String errorCode;
  private String errorMessage;
  private String clientId;
  private String grantType;
  private String clientSecret;
  private String scope;

  private String tmpScope;

  @Before
  public void setup() {
    clientId = syncData(clientId);
    grantType = syncData(grantType);
    clientSecret = syncData(clientSecret);
    tmpScope = syncData(scope);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api get service token")
  public void verifyApiGetServiceToken() {
    System.out.println("TestCaseName : " + testCaseName);

    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    body.put("client_id", clientId);
    body.put("client_secret", clientSecret);
    body.put("grant_type", grantType);
    body.put("scope", tmpScope);

    RequestSpecification requestSpecification = given()
      .header("x-api-key", API_KEY)
      .baseUri(OAUTH2_GET_SERVICE_TOKEN);
    Response response = requestSpecification.formParams(body).post();
    assertThat(response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      assertEquals("Bearer", response.jsonPath().get("token_type"));
      String accessToken = response.jsonPath().get("access_token");
      Map<String, Object> accessTokenClaims = CommonUtils.decodeToken(accessToken);
      assertEquals(clientId, accessTokenClaims.get("sub"));
      Oauth2RegisteredClient oauth2RegisteredClient = null;
      try {
        oauth2RegisteredClient = Oauth2RegisteredClient.getByClientId(clientId);
      } catch (Exception ex) {
        System.out.println("Oauth2RegisteredClient.getByClientId error");
      }
      if ("empty".equalsIgnoreCase(scope)) {
        List<String> scopes = ConvertUtils.convertJsonToArray(oauth2RegisteredClient.getScopes(), String.class);
        assertThat("verify scopes", equalLists(scopes, (List<String>) accessTokenClaims.get("scope")));
      } else {
        List<String> scopesInp = Arrays.asList(scope.split(","));
        assertThat("verify scopes", equalLists(scopesInp, (List<String>) accessTokenClaims.get("scope")));
      }
    } else if (statusCode == 400) {
      assertThat("verify error code", response.jsonPath().get("code"), is(errorCode));
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }
}
