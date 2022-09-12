package com.tcbs.automation.newonboarding2022;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.*;
import common.CallApiUtils;
import common.CommonUtils;
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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.CONFIRM_BOOKING_FANCY_105C;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.OB_REGISTER;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static common.CallApiUtils.callGenRefIdAndConfirmPhoneApi;
import static common.CallApiUtils.callPostApiHasBody;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;


@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding2022/ObRegister.csv", separator = '|')
public class ObRegisterTest {
  private static DateTimeFormatter formatter;
  private static LocalDateTime currentDate;
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String fullName;
  private String phoneNumber;
  private String phoneCode;
  private String phoneConfirm;
  private String gender;
  private String birthday;
  private String contactAddress;
  private String permanentAddress;
  private String email;
  private String idNumber;
  private String idDate;
  private String idPlace;
  private String accountNo;
  private String bankCode;
  private String bankName;
  private String iwealthPartnerCode;
  private String iwealthPartnerChannel;
  private String password;
  private String province;
  private int cusTransparent;
  private int isComfirmiWPCare;
  private String referralCode;
  private String code105C;
  private HashMap<String, Object> body;
  private String phoneNumPre;

  @BeforeClass
  public static void beforeClass() {
    formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'+07:00'");
    currentDate = LocalDateTime.now();
  }

  @Before
  public void before() {
    // prepare birthday
    if (birthday.equalsIgnoreCase("notyet18")) {
      birthday = formatter.format(currentDate.plusYears(-18).plusDays(1));
    } else if (birthday.equalsIgnoreCase("just18")) {
      birthday = formatter.format(currentDate.plusYears(-18));
    } else {
      birthday = syncData(birthday);
    }

    // prepare phone
    if (phoneNumber.equalsIgnoreCase("autoGen")) {
      phoneNumber = CommonUtils.genPhoneNumberByDateTime();
    } else {
      phoneNumber = syncData(phoneNumber);
    }
    if (testCaseName.contains("case valid phoneNumber start with")) {
      phoneNumPre = phoneNumber;
      phoneNumber = "0" + phoneNumPre;
    }
    phoneCode = syncData(phoneCode);
    phoneConfirm = phoneCode + phoneNumber;

    String prepareValue = String.valueOf(new Date().getTime() / 10);

    // prepare email
    if (email.equalsIgnoreCase("autoGen")) {
      email = "tcbs.customer" + prepareValue + "@gmail.com";
    } else if (email.equalsIgnoreCase("tcbMail")) {
      email = "tcb_user_" + prepareValue + "@techcombank.com.vn";
    } else {
      email = syncData(email);
    }

    // prepare idNumber
    if (idNumber.equalsIgnoreCase("autoGen")) {
      idNumber = prepareValue;
      if (testCaseName.contains("ID card expired")) {
        idNumber = prepareValue.substring(3);
      }
    } else {
      idNumber = syncData(idNumber);
    }

    // prepare accountNo
    if (accountNo.equalsIgnoreCase("autoGen")) {
      accountNo = "10" + prepareValue;
    } else {
      accountNo = syncData(accountNo);
    }

    //prepare code105C
    code105C = "105C" + prepareValue.substring(6, 11) + "Z";

    // Call API confirm phone and gen referenceId, authenKey
    // Call API confirm booking fancy 105C
    Response resRef;
    if (!testCaseName.contains("missing BODY") && !testCaseName.contains("invalid phoneCode") && !testCaseName.contains("invalid phoneNumber")) {
      resRef = CallApiUtils.callGenRefIdAndConfirmPhoneApi(phoneCode, phoneNumber);
      HashMap<String, Object> bodyFancy = CommonUtils.getConfirmBookingFancy105CBody(testCaseName, phoneConfirm, code105C);
      Response resFancy = callPostApiHasBody(CONFIRM_BOOKING_FANCY_105C, "", "", bodyFancy);
      assertThat(resFancy.getStatusCode(), is(200));
    } else {
      // call for exception case
      resRef = CallApiUtils.callGenAuthenKeyApi();
    }

    String authenKey;
    if (testCaseName.contains("invalid authenKey")) {
      authenKey = "e399d12a30dfae9e4d1bc7f8c85bc7fde5aa4a5c6c6ceddb1f50fd2ed7e73669";
    } else {
      authenKey = resRef.jsonPath().get("authenKey");
    }
    String referenceId;
    if (testCaseName.contains("invalid referenceId") || testCaseName.contains("exception")) {
      referenceId = "+84775493526F20072022160630";
    } else if (testCaseName.contains("invalid phoneCode") || testCaseName.contains("invalid phoneNumber")) {
      referenceId = callGenRefIdAndConfirmPhoneApi(phoneCode, phoneNumber).jsonPath().get("referenceId");
    } else {
      referenceId = resRef.jsonPath().get("referenceId");
    }


    fullName = syncData(fullName);
    gender = syncData(gender);
    contactAddress = syncData(contactAddress);
    permanentAddress = syncData(permanentAddress);
    idDate = syncData(idDate);
    idPlace = syncData(idPlace);
    bankCode = syncData(bankCode);
    bankName = syncData(bankName);
    province = syncData(province);
    referralCode = syncData(referralCode);
    password = syncData(password);
    iwealthPartnerCode = syncData(iwealthPartnerCode);
    iwealthPartnerChannel = syncData(iwealthPartnerChannel);

    body = new HashMap<>();

    HashMap<String, Object> identityCard = new HashMap<>();
    identityCard.put("idNumber", idNumber);
    identityCard.put("idPlace", idPlace);
    identityCard.put("idDate", idDate);

    HashMap<String, Object> personalInfo = new HashMap<>();
    personalInfo.put("fullName", fullName);
    personalInfo.put("email", email);
    if (testCaseName.contains("phone do not match")) {
      phoneNumber = "+84978021412";
    }
    personalInfo.put("phoneNumber", phoneNumber);
    personalInfo.put("phoneCode", phoneCode);
    personalInfo.put("gender", gender);
    personalInfo.put("birthday", birthday);
    personalInfo.put("contactAddress", contactAddress);
    personalInfo.put("permanentAddress", permanentAddress);
    personalInfo.put("nationality", "VN");
    personalInfo.put("province", province);
    personalInfo.put("password", password);
    personalInfo.put("identityCard", identityCard);

    List<HashMap<String, Object>> bankAccounts = new ArrayList<>();
    HashMap<String, Object> bankAccount1 = new HashMap<>();
    bankAccount1.put("accountNo", accountNo);
    bankAccount1.put("accountName", fullName);
    bankAccount1.put("bankCode", bankCode);
    bankAccount1.put("bankName", bankName);
    bankAccount1.put("bankProvince", null);
    bankAccount1.put("branchCode", null);
    bankAccount1.put("bankType", "CENTRALIZED_PAYMENT");
    bankAccounts.add(bankAccount1);

    HashMap<String, Object> campaignInfo = new HashMap<>();
    campaignInfo.put("referralCode", referralCode);

    HashMap<String, Object> wealthPartnerInfo = new HashMap<>();
    wealthPartnerInfo.put("iwealthPartnerCode", iwealthPartnerCode);
    wealthPartnerInfo.put("iwealthPartnerChannel", iwealthPartnerChannel);
    wealthPartnerInfo.put("cusTransparent", cusTransparent);
    wealthPartnerInfo.put("isComfirmiWPCare", isComfirmiWPCare);

    HashMap<String, Object> basicInfo = new HashMap<>();
    if (testCaseName.contains("invalid code105C")) {
      code105C = "105C08H329";
    }
    basicInfo.put("code105C", code105C);

    body.put("personalInfo", personalInfo);
    body.put("bankAccounts", bankAccounts);
    body.put("campaignInfo", campaignInfo);
    body.put("wealthPartnerInfo", wealthPartnerInfo);
    body.put("basicInfo", basicInfo);
    body.put("receiveAdvertise", 1);
    body.put("pkVid", "");
    body.put("referenceId", referenceId);
    body.put("authenKey", authenKey);

    // prepare for test case email of tcb user existed in system
    if (testCaseName.contains("email of tcb user existed in system")) {
      // Insert data in TCBS_USER (Rm user login through LDAP)
      TcbsUser tcbsUser = new TcbsUser();
      tcbsUser.setLastname("Hoàng Thu");
      tcbsUser.setFirstname("Trang");
      tcbsUser.setTcbsid(prepareValue.substring(1));
      tcbsUser.setEmail(email);
      tcbsUser.setGender(BigDecimal.valueOf(1));
      tcbsUser.setCustype(BigDecimal.valueOf(0));
      tcbsUser.setBirthday(Timestamp.valueOf(
        LocalDateTime.parse(birthday,
          DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS+07:00"))));
      tcbsUser.insert();
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api ob register")
  public void performTest() {
    System.out.println(testCaseName);
    RequestSpecification requestSpecification = SerenityRest.given()
      .baseUri(OB_REGISTER)
      .contentType("application/json");

    Response response;
    if (testCaseName.contains("missing BODY")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(body).post();
    }

    assertThat("verify status code", response.getStatusCode(), is(statusCode));
    if (statusCode == 400) {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }

    if (statusCode == 200) {
      assertThat(response.jsonPath().get("loginKey"), is(notNullValue()));
      assertThat(response.jsonPath().get("custodyCode"), is(code105C));
      if (testCaseName.contains("case valid phoneNumber start with VN")) {
        phoneConfirm = phoneCode + phoneNumPre;
      }
      String userId = TcbsUser.getByPhoneNumber(phoneConfirm).getId().toString();
      assertThat(userId, is(notNullValue()));
      assertThat(TcbsUserOpenAccountQueue.getByPhone(phoneConfirm).getUserId().toString(), is(userId));
      assertThat(TcbsIdentification.getByUserId(userId).getIdNumber(), is(idNumber));
      assertThat(TcbsAddress.getByTcbsAddress(userId).getAddress(), is(contactAddress));
      assertThat(TcbsBankAccount.getBank(userId).getBankAccountNo(), is(accountNo));
      assertThat(TcbsApplicationUser.getByTcbsApplicationUserAppId2(userId, "2"), is(notNullValue()));
      assertThat(TcbsNewOnboardingStatus.getByUserIdAndStatusKey(userId, "ID_STATUS").getStatusValue(), is("WAIT_FOR_VERIFY"));
    }

  }
}