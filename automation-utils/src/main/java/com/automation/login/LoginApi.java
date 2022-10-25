package com.automation.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;

import static com.automation.config.common.CommonConfig.LOGIN_API;
import static com.automation.config.common.CommonKey.JWT_INFO;
import static com.automation.config.common.CommonKey.LOGIN_INFO;
import static net.serenitybdd.rest.SerenityRest.given;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class LoginApi implements Task {

  @Steps
  private LoginData loginData;
  private String usr;
  private String pwd;

  public LoginApi(String usr, String pwd) {
    this.usr = usr;
    this.pwd = pwd;
  }

  public static LoginApi withCredentials(String usr, String pwd) {
    return instrumented(LoginApi.class, usr, pwd);
  }

  @Override
  @Step("[API] {0} logs in to TCI3")
  public <T extends Actor> void performAs(T actor) {
    Response response = given()
      .baseUri(LOGIN_API)
      .relaxedHTTPSValidation()
      .contentType(ContentType.JSON)
      .body(getBody(usr, pwd))
      .when()
      .post();
    try {
      response.prettyPeek();
      Gson gson = new Gson();
      loginData = gson.fromJson(response.asString(), LoginData.class);
      // loginData = actor.asksFor(LastResponse.received()).as(LoginData.class); // only work if object field names are exactly matched
      String token = loginData.getToken();
      if (token != null && !token.equals("")) {
        actor.remember(LOGIN_INFO, loginData);
        actor.remember(JWT_INFO, decodeJwt(token));
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private String getBody(String usr, String pwd) {
    return String.format("{\"username\":\"%s\", \"password\":\"%s\"}", usr, pwd);
  }

  private JwtDto decodeJwt(String token) throws IOException {
    String[] arrToken = token.split("\\.");
    ObjectMapper om = new ObjectMapper();
    Base64 base64Url = new Base64(true);
    String header = new String(base64Url.decode(arrToken[0]));
    String body = new String(base64Url.decode(arrToken[1]));
    JwtHeader jwtHeader = om.readValue(header, JwtHeader.class);
    JwtBody jwtBody = om.readValue(body, JwtBody.class);
    return new JwtDto(jwtHeader, jwtBody);
  }
}
