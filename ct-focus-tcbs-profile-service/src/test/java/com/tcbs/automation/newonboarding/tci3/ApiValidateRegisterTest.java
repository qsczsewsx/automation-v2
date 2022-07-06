package com.tcbs.automation.newonboarding.tci3;

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
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.REGISTER_VERIFICATION;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.junit.Assert.assertEquals;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/ApiValidateRegister.csv", separator = '|')
public class ApiValidateRegisterTest {

  private static String gen_email;
  private static String gen_PhoneNumber;
  private static String gen_idNumber;
  private static String gen_accountNo;
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errMess;
  private String email;
  private String fullName;
  private String phoneNumber;
  private  String phoneCode;
  private String idNumber;
  private String idPlace;
  private String idDate;
  private String gender;
  private String birthday;
  private String contactAddress;
  private String province;
  private String accountNo;
  private String accountName;
  private String bankCode;
  private String bankName;
  private String bankProvince;
  private String branchCode;
  private String bankType;
  private String password;

  @BeforeClass
  public static void beforeClass() {
    TcbsBankAccount user = TcbsBankAccount.getListByBankBranch("NH TMCP KY THUONG VN").get(0);
    gen_accountNo = user.getBankAccountNo();
    gen_email = TcbsUser.getById(user.getUserId()).getEmail();
    gen_PhoneNumber = TcbsUser.getById(user.getUserId()).getPhone();
    gen_idNumber = TcbsIdentification.getByUserId(user.getUserId().toString()).getIdNumber();

  }

  @Before
  public void before() {
    if (email.equalsIgnoreCase("gen")) {
      email = gen_email;
    }
    if (phoneNumber.equalsIgnoreCase("gen")) {
      phoneNumber = gen_PhoneNumber;
    }
    if (accountNo.equalsIgnoreCase("gen")) {
      accountNo = gen_accountNo;
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify Api validate information while register")
  public void validateRegister() {
    System.out.println("Test Case: " + testCaseName);

    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    LinkedHashMap<String, Object> bankAccounts = new LinkedHashMap<>();
    List<LinkedHashMap<String, Object>> bankList = new ArrayList<>();

    bankAccounts.put("accountNo", accountNo);
    bankAccounts.put("accountName", accountName);
    bankAccounts.put("bankCode", bankCode);
    bankAccounts.put("bankName", bankName);
    bankAccounts.put("bankProvince", null);
    bankAccounts.put("branchCode", null);
    bankAccounts.put("bankType", bankType);

    bankList.add(bankAccounts);

    body.put("email", email);
    body.put("fullName", fullName);
    body.put("phoneNumber", phoneNumber);
    body.put("phoneCode", phoneCode);

    body.put("idNumber", idNumber);
    body.put("idPlace", idPlace);
    body.put("idDate", idDate);
    body.put("gender", gender);
    body.put("birthday", birthday);
    body.put("contactAddress", contactAddress);
    body.put("province", province);
    body.put("bankAccounts", bankList);
    body.put("password", password);

    Gson gson = new Gson();

    Response response = given()
      .baseUri(REGISTER_VERIFICATION)
      .contentType("application/json")
      .body(gson.toJson(body))
      .when()
      .post();

    assertEquals(statusCode, response.getStatusCode());
    if (statusCode ==200){
      if (testCaseName.contains("already exist")){
        assertEquals("false", response.jsonPath().get("isValid").toString());
      } else {
        assertEquals("true", response.jsonPath().get("isValid").toString());
      }
    }
    if (statusCode == 400) {
      assertEquals(errMess, response.jsonPath().get("message"));
    }
  }
}
