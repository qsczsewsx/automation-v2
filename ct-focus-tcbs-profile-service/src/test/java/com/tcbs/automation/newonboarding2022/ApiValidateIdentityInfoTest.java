package com.tcbs.automation.newonboarding2022;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import common.CommonUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.rest.RestRequests;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.ConvertUtils.fileTxtToString;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static org.junit.Assert.assertEquals;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding2022/RegisterValidateIdentityInfo.csv", separator = '|')
public class ApiValidateIdentityInfoTest {

  private static DateTimeFormatter formatter;
  private static LocalDateTime currentDate;
  @Getter
  private String testCaseName;
  private int statusCode;

  private String errorMessage;
  private String fullName;
  private String birthday;
  private String gender;
  private String idNumber;
  private String idDate;
  private String idPlace;
  private String contactAddress;
  private String province;
  private String permanentAddress;
  private HashMap<String, Object> body;


  @BeforeClass
  public static void beforeClass() {
    formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'00:00:00.000'Z'");
    currentDate = LocalDateTime.now();
  }

  @Before
  public void setup() {

    if (gender.equalsIgnoreCase("genGender")) {
      List<String> givenList = Arrays.asList("MALE", "FEMALE");
      String randomGender = givenList.get(new Random().nextInt(givenList.size()));
      gender = randomGender;
    } else {
      gender =syncData(gender);
    }

    if (idDate.equalsIgnoreCase("genDateCurrent")) {
      idDate = formatter.format(currentDate);
    } if (idDate.equalsIgnoreCase("genDateFuture")) {
      idDate = formatter.format(currentDate.plusDays(+1));
    } else {
      idDate = syncData(idDate);
    }


    fullName = syncData(fullName);
    idPlace = syncData(idPlace);
    contactAddress = syncData(contactAddress);
    permanentAddress = syncData(permanentAddress);
    birthday = syncData(birthday);
    idNumber = syncData(idNumber);
    province = syncData(province);

    body = new HashMap<>();
    body.put("fullName", fullName);
    body.put("birthday", birthday);
    body.put("gender", gender);
    body.put("idNumber", idNumber);
    body.put("idDate", idDate);
    body.put("idPlace", idPlace);
    body.put("contactAddress", contactAddress);
    body.put("province", province);
    body.put("permanentAddress", permanentAddress);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify validate identity info")
  public void validateIdentityInfo() {
    System.out.println("Test Case: " + testCaseName);
    RequestSpecification requestSpecification = RestRequests.given()
      .baseUri(REGISTER_VALIDATE_IDENTITY_INFO)
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
