package com.automation.login;

import io.restassured.response.Response;

import java.util.HashMap;

import static com.automation.config.common.CommonConfig.*;
import static net.serenitybdd.rest.SerenityRest.given;

public class Wso2LoginInfo {

  public static HashMap<String, Object> withCredentials(String usr, String pwd) {
    Response response = given()
      .baseUri(WSO2_LOGIN_URL)
      .header("Connection", "keep-alive")
      .contentType("application/x-www-form-urlencoded")
      .param("grant_type", "password")
      .param("username", usr)
      .param("password", pwd)
      .param("scope", "openid")
      .param("client_id", WSO2_LOGIN_CLIENT_ID)
      .param("client_secret", WSO2_LOGIN_CLIENT_SECRET)
      .post();
    return response.jsonPath().get();
  }

  public static String getToken(String username, String password) {
    HashMap<String, Object> info = withCredentials(username, password);
    return String.valueOf(info.get("access_token"));
  }
}
