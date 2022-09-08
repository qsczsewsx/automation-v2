package com.tcbs.automation.newonboarding.tci3;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/UpdateInformation.csv", separator = '|')
public class UpdateInformationTest {
  private static DateTimeFormatter formatter;
  private static LocalDateTime currentDate;
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String fullName;
  private String email;
  private String phoneNumber;
  private String birthday;
  private String contactAddress;
  private String province;
  private String idNumber;
  private String idPlace;
  private String idDate;
  private String accountNo;
  private String accountName;
  private String bankCode;
  private String bankName;
  private String firstName;
  private String lastName;
  private String code105C;
  private String tcbsId;
  private HashMap<String, Object> body;

  @BeforeClass
  public static void beforeClass() {
    formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'+07:00'");
    currentDate = LocalDateTime.now();
  }

  @Before
  public void before() {

    phoneNumber = syncData(phoneNumber);
    fullName = syncData(fullName);
    birthday = syncData(birthday);
    contactAddress = syncData(contactAddress);
    province = syncData(province);
    idPlace = syncData(idPlace);
    idDate = syncData(idDate);
    accountNo = syncData(accountNo);
    accountName = syncData(accountName);
    bankCode = syncData(bankCode);
    bankName = syncData(bankName);
    firstName = syncData(firstName);
    lastName = syncData(lastName);
    code105C = syncData(code105C);
    tcbsId = syncData(tcbsId);
    idNumber = syncData(idNumber);
    email = syncData(email);
    body = new HashMap<>();

    HashMap<String, Object> identityCard = new HashMap<>();
    identityCard.put("idNumber", idNumber);
    identityCard.put("idPlace", idPlace);
    identityCard.put("idDate", idDate);
    identityCard.put("idUrl", null);
    identityCard.put("expireDate", "2023-08-26");

    HashMap<String, Object> personalInfo = new HashMap<>();
    personalInfo.put("fullName", fullName);
    personalInfo.put("firstName", firstName);
    personalInfo.put("lastName", lastName);
    personalInfo.put("email", email);
    personalInfo.put("phoneNumber", phoneNumber);

    personalInfo.put("birthday", birthday);
    personalInfo.put("contactAddress", contactAddress);
    personalInfo.put("identityCard", identityCard);

    List<HashMap<String, Object>> bankAccounts = new ArrayList<>();
    HashMap<String, Object> bankAccount1 = new HashMap<>();
    bankAccount1.put("bankAccountId", "1878");
    bankAccount1.put("accountNo", accountNo);
    bankAccount1.put("accountName", accountName);
    bankAccount1.put("bankCode", bankCode);
    bankAccount1.put("bankName", bankName);

    bankAccounts.add(bankAccount1);

    HashMap<String, Object> enterpriseInfo = new HashMap<>();
    enterpriseInfo.put("chiefAccountantInfo", null);

    HashMap<String, Object> basicInfo = new HashMap<>();
    basicInfo.put("code105C", code105C);
    basicInfo.put("tcbsId", tcbsId);
    basicInfo.put("status", "ACTIVE");
    basicInfo.put("type", "INDIVIDUAL");
    basicInfo.put("accountType", "ADVANCED");

    body.put("personalInfo", personalInfo);
    body.put("bankAccounts", bankAccounts);
    body.put("enterpriseInfo", enterpriseInfo);
    body.put("basicInfo", basicInfo);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api update information of customer")
  public void updateInfoCustomer() {
    System.out.println(testCaseName);
    RequestSpecification requestSpecification = SerenityRest.given()
      .baseUri(UPDATE_INFO_CUSTOMER.replace("{tcbsId}", tcbsId))
      .header("x-api-key", (testCaseName.contains("invalid x-api-key") ? FMB_X_API_KEY : TCBSPROFILE_BACKENDWBLKEY))
      .contentType("application/json");

    Response response;
    if (testCaseName.contains("missing body")) {
      response = requestSpecification.put();
    } else {
      response = requestSpecification.body(body).put();
    }

    assertThat(response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      assertThat("verify error message", response.jsonPath().get("message"), is("Updated profile success"));
    } else {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }
}
