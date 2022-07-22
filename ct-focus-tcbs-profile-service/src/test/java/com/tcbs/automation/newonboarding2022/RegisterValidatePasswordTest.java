package com.tcbs.automation.newonboarding2022;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.rest.RestRequests;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.*;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.REGISTER_VALIDATE_PASSWORD;
import static com.tcbs.automation.tools.ConvertUtils.fileTxtToString;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static org.junit.Assert.assertEquals;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding2022/RegisterValidatePassword.csv", separator = '|')
public class RegisterValidatePasswordTest {

  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String password;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify validate password")
  public void validatePassWord() {
    System.out.println("Test Case: " + testCaseName);

    if (password.equalsIgnoreCase("blacklist")) {
      String strPassword = fileTxtToString("src/test/resources/requestBody/PasswordBlackList.txt");
      List<String> passwordBlackList = new ArrayList<>(Arrays.asList(strPassword.split(",")));
      for (String s : passwordBlackList) {
        password = s;
        verifyRegisterValidatePassword(password, testCaseName);
      }
    } else {
      password = syncData(password);
      verifyRegisterValidatePassword(password, testCaseName);
    }
  }

  public void verifyRegisterValidatePassword(String password, String testCaseName) {
    HashMap<String, Object> body = new HashMap<>();
    body.put("password", password);
    RequestSpecification requestSpecification = RestRequests.given()
      .baseUri(REGISTER_VALIDATE_PASSWORD)
      .contentType("application/json")
      .when();

    Response response;
    Gson gson = new Gson();
    if (testCaseName.contains("missing body")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(gson.toJson(body)).post();
    }
    assertEquals(statusCode, response.getStatusCode());
    if (statusCode == 200) {
      if (testCaseName.contains("success")) {
        assertEquals("true", response.jsonPath().get("isValid").toString());
      } else {
        assertEquals("false", response.jsonPath().get("isValid").toString());
      }
    }
    assertEquals(errorMessage, response.jsonPath().get("message"));
  }
}
