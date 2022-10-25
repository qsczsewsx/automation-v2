package com.automation.tools;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.automation.complaints.ApiException;
import com.automation.login.JwtBody;
import com.automation.login.JwtDto;
import com.automation.login.JwtHeader;
import com.automation.login.LoginData;
import io.restassured.response.Response;
import net.thucydides.core.annotations.Step;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import static com.automation.config.common.CommonConfig.LOGIN_API;
import static net.serenitybdd.rest.SerenityRest.given;

public class LoginUtil {
  @Step("Get token from login api")
  public static LoginData login(String username, String password) throws Exception {
    Map<String, String> params = new HashMap<>();
    params.put("username", username);
    params.put("password", password);
    Gson gson = new Gson();

    Response response = given()
      .baseUri(LOGIN_API)
      .header("Content-Type", "application/json")
      .relaxedHTTPSValidation()
      .body(gson.toJson(params))
      .post();
    if (response == null) {
      throw new ApiException(null, "api not response");
    } else if (response.getStatusCode() != HttpURLConnection.HTTP_OK) {
      throw new ApiException(response, response.getStatusCode() + " - " + response.getStatusLine());
    } else {
      return gson.fromJson(response.asString(), LoginData.class);
    }
  }

  public static JwtDto decodeJwt(String token) throws IOException {
    ObjectMapper om = new ObjectMapper();
    String[] arrToken = token.split("\\.");
    Base64 base64Url = new Base64(true);
    String header = new String(base64Url.decode(arrToken[0]));
    String body = new String(base64Url.decode(arrToken[1]));
    JwtHeader jwtHeader = om.readValue(header, JwtHeader.class);
    JwtBody jwtBody = om.readValue(body, JwtBody.class);
    return new JwtDto(jwtHeader, jwtBody);
  }
}
