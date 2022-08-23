package com.tcbs.automation.newOnboardingPartnerShip;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newOnboardingPartnerShip/ApiReturnInfoForSocias.csv", separator = '|')
public class ApiReturnInfoForSociasTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String partnerId;
  private String partnerAccountId;
  private String linkType;
  private String autoTransfer;
  private String isIAPaid;
  private String iaBankAccount;
  private String kycLevel;
  private String bankType;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api return info for Socias")
  public void apiReturnInfoForSocias() {
    System.out.println("Test Case: " + testCaseName);
    LinkedHashMap<String, Object> body = returnInfoForSociasBody();
    RequestSpecification requestSpecification = given()
      .baseUri(RETURN_INFO_SOCIAS)
      .header("x-api-key", testCaseName.contains("invalid x-api-key") ? FMB_X_API_KEY : BANKLIST_X_API_KEY);

    Response response;

    if (testCaseName.contains("missing body")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(body).post();
    }

    assertEquals(statusCode, response.getStatusCode());
    if (statusCode == 200) {
      if (!testCaseName.contains("kycLevel <> 1")) {
        assertThat("verify info", response.jsonPath().get("data.personalInfo.phoneNumber.editable"), is(true));
      } else {
        assertThat("verify info", response.jsonPath().get("data.personalInfo.phoneNumber.editable"), is(false));
      }
      assertThat("verify info", response.jsonPath().get("data.partnerInfo.partnerId"), is(partnerId));
    } else {
      assertEquals(errorMessage, response.jsonPath().get("message"));
    }
  }

  public LinkedHashMap<String, Object> returnInfoForSociasBody() {
    LinkedHashMap<String, Object> fullbody = new LinkedHashMap<>();
    List<String> listLinkType = new ArrayList<>(Arrays.asList(linkType.split(",")));
    LinkedHashMap<String, Object> value = new LinkedHashMap<>();
    value.put("value", "1");
    LinkedHashMap<String, Object> value1 = new LinkedHashMap<>();
    value1.put("value", "2022-05-05");

    LinkedHashMap<String, Object> partnerInfo = new LinkedHashMap<>();
    LinkedHashMap<String, Object> connect = new LinkedHashMap<>();
    connect.put("linkType", listLinkType);
    connect.put("autoTransfer", autoTransfer);
    connect.put("isIAPaid", isIAPaid);
    connect.put("iaBankAccount", iaBankAccount.isEmpty() ? null : iaBankAccount);

    partnerInfo.put("partnerId", partnerId);
    partnerInfo.put("kycLevel", kycLevel);
    partnerInfo.put("connect", connect);
    partnerInfo.put("partnerAccountId", partnerAccountId);

    LinkedHashMap<String, Object> personalInfo = new LinkedHashMap<>();
    LinkedHashMap<String, Object> identityCard = new LinkedHashMap<>();

    identityCard.put("idNumber", value);
    identityCard.put("idPlace", value);
    identityCard.put("idDate", value1);

    personalInfo.put("fullName", value);
    personalInfo.put("email", value);
    personalInfo.put("phoneNumber", value);
    personalInfo.put("phoneCode", value);
    personalInfo.put("gender", value);
    personalInfo.put("birthday", value1);
    personalInfo.put("contactAddress", value);
    personalInfo.put("permanentAddress", value);
    personalInfo.put("nationality", value);
    personalInfo.put("identityCard", identityCard);
    personalInfo.put("province", value);

    List<LinkedHashMap<String, Object>> bankAccountsList = new ArrayList<>();
    LinkedHashMap<String, Object> bankAccounts = new LinkedHashMap<>();

    bankAccounts.put("accountNo", value);
    bankAccounts.put("accountName", value);
    bankAccounts.put("bankCode", value);
    bankAccounts.put("bankName", value);
    bankAccounts.put("bankProvince", value);
    bankAccounts.put("branchCode", value);
    bankAccounts.put("bankType", bankType);
    bankAccountsList.add(bankAccounts);

    if (testCaseName.contains("missing partnerInfo")) {
      fullbody.put("personalInfo", personalInfo);
      fullbody.put("bankAccounts", bankAccountsList);
    } else {
      fullbody.put("personalInfo", personalInfo);
      fullbody.put("bankAccounts", bankAccountsList);
      fullbody.put("partnerInfo", partnerInfo);
    }
    return fullbody;
  }

}


