package com.tcbs.automation.authen.oauth2;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import lombok.Setter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.Security;
import java.util.Base64;
import java.util.HashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.OAUTH2_GET_AUTHORIZATION_CODE;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

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
  public void verifyApiGetAuthorizationCode() throws Exception {
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

  public static Response callApiGetAuthorizationCode(String clientId, String username, String password, String codeChallenge, String redirectUri) throws Exception {
    HashMap<String, Object> body = new HashMap<>();
    body.put("client_id", clientId);
    body.put("username", username);
    body.put("password", password);
    body.put("allow_redirect", false);

    ClientData clientData = new ClientData();
    clientData.setId_number("22");
    clientData.setRedirect_uri(redirectUri);
    clientData.setCode_challenge(codeChallenge);
    clientData.setTimestamp("20/10/2030 11:15:30");
    Gson gson = new Gson();
    String jsonified = gson.toJson(clientData);
    String encryptedText = encrypt(jsonified);
    body.put("data", encryptedText);

    RequestSpecification requestSpecification = given()
      .contentType("application/x-www-form-urlencoded")
      .baseUri(OAUTH2_GET_AUTHORIZATION_CODE);
    return requestSpecification.formParams(body).post();
  }

  public static String encrypt(String plainText) throws Exception {
    Security.addProvider(new BouncyCastleProvider());
    Key secretKey = new SecretKeySpec("c352f391654d57a9d7d25a6b7e85e735".getBytes(), "AES");
    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
    byte[] plainTextByte = plainText.getBytes();

    byte[] iv = new byte[12];
    IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);

    byte[] encryptedByte = cipher.doFinal(plainTextByte);
    Base64.Encoder encoder = Base64.getEncoder();
    String encryptedText = encoder.encodeToString(encryptedByte);
    return encryptedText;
  }
}

@Getter
@Setter
class ClientData {
  private String redirect_uri;
  private String id_number;
  private String code_challenge;
  private String timestamp;
}