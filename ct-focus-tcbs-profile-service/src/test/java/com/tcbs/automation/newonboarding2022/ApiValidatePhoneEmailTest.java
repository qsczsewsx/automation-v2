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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.*;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.API_VALIDATE_PHONE_EMAIL;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static org.junit.Assert.assertEquals;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding2022/ApiValidatePhoneEmail.csv", separator = ',')
public class ApiValidatePhoneEmailTest {

  @Getter
  private String testCaseName;
  private int statusCode;

  private String errorMessage;
  private String phoneCode;
  private String phoneNumber;
  private String email;


  @Before
  public void before() {
    List<String> givenList = Arrays.asList("3", "5", "7", "8", "9");
    String randomElement = givenList.get(new Random().nextInt(givenList.size()));

    String prepareValue = String.valueOf(new Date().getTime());
    System.out.println(prepareValue);
    String suffixPhone = randomElement + prepareValue.substring(5);

    if (phoneNumber.equals("genrandom")) {
      phoneNumber = suffixPhone;
    } else {
      phoneNumber = syncData(phoneNumber);
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify validate phone email")
  public void validatePhoneEmail() {

    System.out.println("Test Case: " + testCaseName);
    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    body.put("phoneCode", phoneCode);
    body.put("phoneNumber", phoneNumber);
    body.put("email", email);

    RequestSpecification requestSpecification = RestRequests.given()
      .baseUri(API_VALIDATE_PHONE_EMAIL)
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
