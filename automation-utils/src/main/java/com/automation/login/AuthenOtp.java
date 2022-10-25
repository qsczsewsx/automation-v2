package com.automation.login;

import io.restassured.response.Response;

import static com.automation.config.common.CommonConfig.AUTHEN_OTP;
import static net.serenitybdd.rest.SerenityRest.given;

public class AuthenOtp {
  public static AuthenDTO authenWithFullData(String token, String xxxxId, String otpTypeName, String otp, int duration) {
    Response response = given()
      .baseUri(AUTHEN_OTP)
      .contentType("application/json")
      .header("Authorization", "Bearer " + token)
      .body("{\n" +
        "    \"xxxxId\": \"" + xxxxId + "\",\n" +
        "    \"otpTypeName\": \"" + otpTypeName + "\",\n" +
        "    \"otp\": \"" + otp + "\",\n" +
        "    \"duration\": " + duration + "\n" +
        "}").post();
    return response.as(AuthenDTO.class);
  }

  public static AuthenDTO authenWithIOtp(String token, String xxxxId) {
    return authenWithFullData(token, xxxxId, "TOTP", "111111", 28800);
  }
}
