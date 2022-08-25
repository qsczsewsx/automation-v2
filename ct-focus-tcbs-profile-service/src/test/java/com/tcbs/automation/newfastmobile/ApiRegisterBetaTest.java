package com.tcbs.automation.newfastmobile;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.cas.TcbsBankAccount;
import com.tcbs.automation.cas.TcbsIdentification;
import com.tcbs.automation.cas.TcbsUser;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.*;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static common.CallApiUtils.clearCache;
import static common.CommonUtils.*;
import static common.DatesUtils.convertTimestampToString;
import static common.DatesUtils.covertDateToString;
import static common.ProfileTools.TOKEN;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newfastmobile/ApiRegisterBeta.csv", separator = '|')
public class ApiRegisterBetaTest {

  private String idNumberVal;
  private String getPhoneNumber;
  private String getEmail;
  @Getter
  private String testCaseName;
  @Getter
  private int statusCode;
  private String contactAddress;
  private String nationality;
  private String province;
  private String fullName;
  private String gender;
  private String email;
  private String phoneNumber;
  private String birthday;
  private String idNumber;
  private String idPlace;
  private String idDate;
  private String accountNo;
  private String accountName;
  private String bankCode;
  private String bankName;
  private String bankProvince;
  private String branchCode;
  private String bankType;
  private String message;
  private String userId;
  private String valid_bankAccount;

  @Before
  public void beforeTest() {
    String prepareValue = String.valueOf(new Date().getTime());
    idNumberVal = prepareValue.substring(0, 12);
    getPhoneNumber = "05" + prepareValue.substring(4, 12);
    getEmail = "anhbui1" + prepareValue.substring(6, 12) + "@gmail.com";
    valid_bankAccount = "99" + idNumberVal;
    clearCache(CLEAR_CACHE_REDIS.replace("{phoneNumber}", getPhoneNumber), "x-api-key", TOKEN);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api Register Basic")
  public void verifyRegisterBasicTest() {

    System.out.println("TestCaseName : " + testCaseName);

    LinkedHashMap<String, Object> body = getRegisterBasicBody(testCaseName);
    Gson gson = new Gson();

    Response response = given()
      .baseUri(FMB_REGISTER_BASIC)
      .header("x-api-key", FMB_X_API_KEY)
      .contentType("application/json")
      .body(gson.toJson(body))
      .when()
      .post();

    assertThat(response.getStatusCode(), is(statusCode));

    if (response.statusCode() == 200) {
      verifyOnboardingStatus(response, idNumberVal);
      verifyFMBBasicInfo(response, "basicInfo", "userId", userId);
      verifyInputAndOutput(userId);
      verifyRegIAStatus(response);

    } else if (response.statusCode() == 400) {

      String actualMessage = response.jsonPath().get("message");
      assertEquals(message, actualMessage);
    }
  }

  public LinkedHashMap<String, Object> getRegisterBasicBody(String testCaseName) {

    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    LinkedHashMap<String, Object> personalInfo = new LinkedHashMap<>();
    LinkedHashMap<String, Object> identityCard = new LinkedHashMap<>();
    List<LinkedHashMap<String, Object>> bankAccounts = new ArrayList<>();
    if (testCaseName.contains("missing Body")) {
      body = new LinkedHashMap<>();
    } else {

      identityCard.put("idNumber", getDesiredData(testCaseName, "idnumber", idNumber, idNumberVal));
      identityCard.put("idPlace", idPlace);
      identityCard.put("idDate", idDate);

      personalInfo.put("nationality", nationality);
      personalInfo.put("fullName", fullName);
      personalInfo.put("gender", gender);
      personalInfo.put("email", getDesiredData(testCaseName, "email", email, getEmail));
      personalInfo.put("phoneNumber", getDesiredData(testCaseName, "phone", phoneNumber, getPhoneNumber));
      personalInfo.put("birthday", birthday);
      personalInfo.put("identityCard", identityCard);

      LinkedHashMap<String, Object> bankAccount = new LinkedHashMap<>();
      bankAccount.put("accountNo", (testCaseName.contains("400") && testCaseName.contains("accountNo")) ? accountNo : valid_bankAccount);
      bankAccount.put("accountName", accountName);
      bankAccount.put("bankCode", bankCode);
      bankAccount.put("bankName", bankName);
      bankAccount.put("bankProvince", null);
      bankAccount.put("branchCode", null);
      bankAccount.put("bankType", bankType);
      bankAccounts.add(bankAccount);

      body.put("personalInfo", personalInfo);
      body.put("bankAccounts", bankAccounts);
    }
    return body;
  }

  public void verifyOnboardingStatus(Response response, String idNumber) {
    userId = TcbsIdentification.getByIdNumber(idNumber).getUserId().toString();
    verifyFMBOnboardingStatus(response, "accountStatus.onboardingStatus.", "userId", userId);
  }

  public void verifyRegIAStatus(Response response) {
    Map<String, Object> getResponse = response.jsonPath().getMap("accountStatus.iaStatus");
    assertNull(getResponse);
  }

  public void verifyInputAndOutput(String userId) {

    TcbsIdentification tcbsIdentification = TcbsIdentification.getByUserId(userId);
    assertEquals(idNumberVal, tcbsIdentification.getIdNumber());
    assertEquals(idPlace, tcbsIdentification.getIdPlace());
    String expected = convertTimestampToString((idDate));
    String actual = covertDateToString(tcbsIdentification.getIdDate());
    assertEquals(expected, actual);

    TcbsUser tcbsUser = TcbsUser.getById(new BigDecimal(userId));
    if (testCaseName.contains("missing param email")) {
      getEmail = "noemail_" + getPhoneNumber.substring(1) + "@tcbs.com.vn";
    }
    assertEquals(getEmail, tcbsUser.getEmail());
    assertEquals(fullName, tcbsUser.getLastname() + " " + tcbsUser.getFirstname());
    expected = convertTimestampToString((birthday));
    actual = covertDateToString(tcbsUser.getBirthday());
    assertEquals(expected, actual);

    TcbsBankAccount tcbsBankAccount = TcbsBankAccount.getBank(userId);
    if (testCaseName.contains("400") && testCaseName.contains("accountNo")) {
      assertEquals(accountNo, tcbsBankAccount.getBankAccountNo());
    } else {
      assertEquals(valid_bankAccount, tcbsBankAccount.getBankAccountNo());
    }
    assertEquals(accountName, tcbsBankAccount.getBankAccountName());
    assertEquals(bankCode, tcbsBankAccount.getBankCode());
    assertEquals(bankName, tcbsBankAccount.getBankName());
  }

  @After
  public void afterTest() {
    deleteFMBRegisterBetaData(getPhoneNumber, idNumberVal, getEmail);
    clearCache(CLEAR_CACHE_REDIS.replace("{phoneNumber}", getPhoneNumber), "x-api-key", TOKEN);
  }
}
