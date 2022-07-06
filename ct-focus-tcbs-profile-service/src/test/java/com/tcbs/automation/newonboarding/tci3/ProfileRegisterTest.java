package com.tcbs.automation.newonboarding.tci3;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import common.CallApiUtils;
import common.CommonUtils;
import common.VinidUtils;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static common.CallApiUtils.callPostApiHasBody;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/ProfileRegister.csv", separator = '|')
public class ProfileRegisterTest {
  private static DateTimeFormatter formatter;
  private static LocalDateTime currentDate;
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String fullName;
  private String email;
  private String phoneNumber;
  private String phoneCode;
  private String gender;
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
  private String custodyCode;
  private String iwealthPartnerCode;
  private String iwealthPartnerChannel;
  private int cusTransparent;
  private int isComfirmiWPCare;
  private String referralCode;
  private HashMap<String, Object> body;

  @BeforeClass
  public static void beforeClass() {
    formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'+07:00'");
    currentDate = LocalDateTime.now();
  }

  @Before
  public void before() {
    if (birthday.equalsIgnoreCase("notyet18")) {
      birthday = formatter.format(currentDate.plusYears(-18).plusDays(1));
    } else if (birthday.equalsIgnoreCase("just18")) {
      birthday = formatter.format(currentDate.plusYears(-18));
    }
    String prepareValue = String.valueOf(new Date().getTime() / 10);
    phoneCode = syncData(phoneCode);
    if (phoneNumber.equalsIgnoreCase("autoGen")) {
      phoneNumber = "3" + prepareValue.substring(4);
    } else {
      phoneNumber = syncData(phoneNumber);
    }
    String phoneConfirm = phoneCode + phoneNumber;
    email = "long.trinh" + prepareValue + "@gmail.com";
    idNumber = prepareValue;
    String code105C = "105C" + prepareValue.substring(6, 11) + "V";

    // Call API confirm booking fancy 105C
    if (!testCaseName.contains("missing BODY") && !testCaseName.contains("invalid phoneCode") && !testCaseName.contains("invalid phoneNumber")) {
      HashMap<String, Object> bodyFancy = CommonUtils.getConfirmBookingFancy105CBody(testCaseName, phoneConfirm, code105C);
      Response resFancy = callPostApiHasBody(CONFIRM_BOOKING_FANCY_105C, "", "", bodyFancy);
      assertThat(resFancy.getStatusCode(), is(200));
    }

    //Verify fullname, email and phoneNumber
    if (testCaseName.contains("invalid phoneNumber") || testCaseName.contains("invalid phoneCode")) {
      Response verifyResponse = CallApiUtils.registerValidation(fullName, email, phoneNumber, phoneCode);
      assertThat(verifyResponse.jsonPath().get("message"), is(errorMessage));
      assertThat(verifyResponse.getStatusCode(), is(400));
    }


    fullName = syncData(fullName);
    gender = syncData(gender);
    birthday = syncData(birthday);
    contactAddress = syncData(contactAddress);
    province = syncData(province);
    idPlace = syncData(idPlace);
    idDate = syncData(idDate);
    accountNo = syncData(accountNo);
    accountName = syncData(accountName);
    bankCode = syncData(bankCode);
    bankName = syncData(bankName);
    referralCode = syncData(referralCode);
    custodyCode = syncData(custodyCode);
    iwealthPartnerCode = syncData(iwealthPartnerCode);
    iwealthPartnerChannel = syncData(iwealthPartnerChannel);

    if (referralCode.equalsIgnoreCase("autoGen")) {
      VinidUtils vinidUtils = new VinidUtils();
      List<String> vouchers = vinidUtils.getListActiveCode("VINID_REFERRAL", "VINID", 1);
      if (vouchers.size() > 0) {
        referralCode = vouchers.get(0);
      }
    }
    if (accountNo.equalsIgnoreCase("autoGen")) {
      accountNo = "19" + prepareValue;
    }
    body = new HashMap<>();

    HashMap<String, Object> identityCard = new HashMap<>();
    identityCard.put("idNumber", idNumber);
    identityCard.put("idPlace", idPlace);
    identityCard.put("idDate", idDate);

    HashMap<String, Object> personalInfo = new HashMap<>();
    personalInfo.put("fullName", fullName);
    personalInfo.put("email", email);
    personalInfo.put("phoneNumber", phoneNumber);
    personalInfo.put("phoneCode", phoneCode);
    personalInfo.put("gender", gender);
    personalInfo.put("birthday", birthday);
    personalInfo.put("contactAddress", contactAddress);
    personalInfo.put("nationality", "VN");
    personalInfo.put("province", province);
    personalInfo.put("password", "Abc@1234");
    personalInfo.put("identityCard", identityCard);

    List<HashMap<String, Object>> bankAccounts = new ArrayList<>();
    HashMap<String, Object> bankAccount1 = new HashMap<>();
    bankAccount1.put("accountNo", accountNo);
    bankAccount1.put("accountName", accountName);
    bankAccount1.put("bankCode", bankCode);
    bankAccount1.put("bankName", bankName);
    bankAccount1.put("bankProvince", null);
    bankAccount1.put("branchCode", null);
    bankAccount1.put("bankType", "CENTRALIZED_PAYMENT");
    bankAccounts.add(bankAccount1);

    HashMap<String, Object> campaignInfo = new HashMap<>();
    campaignInfo.put("referralCode", referralCode);

    HashMap<String, Object> wealthPartnerInfo = new HashMap<>();
    wealthPartnerInfo.put("custodyCode", custodyCode);
    wealthPartnerInfo.put("iwealthPartnerCode", iwealthPartnerCode);
    wealthPartnerInfo.put("iwealthPartnerChannel", iwealthPartnerChannel);
    wealthPartnerInfo.put("cusTransparent", cusTransparent);
    wealthPartnerInfo.put("isComfirmiWPCare", isComfirmiWPCare);

    HashMap<String, Object> basicInfo = new HashMap<>();
    basicInfo.put("code105C", code105C);

    body.put("personalInfo", personalInfo);
    body.put("bankAccounts", bankAccounts);
    body.put("campaignInfo", campaignInfo);
    body.put("wealthPartnerInfo", wealthPartnerInfo);
    body.put("basicInfo", basicInfo);
    body.put("receiveAdvertise", 1);
    body.put("pkVid", "");
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api profile register")
  public void performTest() {
    System.out.println(testCaseName);
    RequestSpecification requestSpecification = SerenityRest.given()
      .baseUri(TCBSPROFILE_PUB_API + TCBSPROFILE_REGISTER)
      .contentType("application/json");

    Response response;
    //In case phone and birthday are valid
    if (testCaseName.contains("missing BODY")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(body).post();
    }

    assertThat("verify status code", response.getStatusCode(), is(statusCode));
    assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
  }

}
