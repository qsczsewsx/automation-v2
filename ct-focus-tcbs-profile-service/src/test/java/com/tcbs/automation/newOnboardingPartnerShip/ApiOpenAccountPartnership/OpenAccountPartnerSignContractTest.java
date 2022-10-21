package com.tcbs.automation.newOnboardingPartnerShip.ApiOpenAccountPartnership;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsBankIaaccount;
import com.tcbs.automation.cas.TcbsNewOnboardingStatus;
import com.tcbs.automation.cas.TcbsPartnerShip;
import com.tcbs.automation.cas.TcbsUser;
import common.CallApiUtils;
import common.CommonUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.*;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newOnboardingPartnerShip/ApiOpenAccountPartnership/ApiOpenAccountPartnerSignContract.csv", separator = '|')
public class OpenAccountPartnerSignContractTest {
  @Getter
  private String testCaseName;
  private int statusCode;

  private String errorMessage;
  private String partnerId;
  private String code105C;
  private String otp;
  private String otpId;
  private String linkTypes;
  private String autoTransfer;
  private String isIAPaid;
  private String iaBankAccount;
  private String partnerAccountId;
  private HashMap<String, Object> body;

  @Before

  public void setup() {
    if (statusCode == 200) {
      String prepareValue = String.valueOf(new Date().getTime() / 10);

      linkTypes = syncData(linkTypes);
      autoTransfer = syncData(autoTransfer);
      isIAPaid = syncData(isIAPaid);
      iaBankAccount = syncData(iaBankAccount);
      partnerAccountId = syncData(partnerAccountId);
      List<String> linkType = new ArrayList<>(Arrays.asList(linkTypes.split(",")));

     if (iaBankAccount.equalsIgnoreCase("autoGen")) {
       iaBankAccount = "10" + prepareValue;
     }
     if (partnerAccountId.equalsIgnoreCase("autoGen")) {
       partnerAccountId = "PHUONGMBB" + prepareValue;
     }

      Response resRef;
      resRef = CallApiUtils.callRegisterPartnership(linkType, autoTransfer, isIAPaid, iaBankAccount, partnerAccountId);
      if (code105C.equals("autogen")) {
        code105C = resRef.jsonPath().get("basicInfo.code105C");
      }
    }

    code105C = syncData(code105C);
    partnerId = syncData(partnerId);
    otp = syncData(otp);
    otpId = syncData(otpId);

    body = new HashMap<>();
    body.put("partnerId", partnerId);
    body.put("code105C", code105C);
    body.put("otp", otp);
    body.put("otpId", otpId);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify open account partnership sign contract")
  public void partnerShipSignContract() {
    System.out.println("Test Case: " + testCaseName);
    RequestSpecification requestSpecification = given()
      .baseUri(OPEN_ACCOUNT_PARTNER_SIGN_CONTRACT)
      .header("x-api-key", (testCaseName.contains("invalid x-api-key") ? FMB_X_API_KEY : PARTNERSHIP_X_API_KEY));

    Response response;

    if (testCaseName.contains("missing body")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(body).post();
    }

    assertEquals(statusCode, response.getStatusCode());
    if (statusCode == 200) {
      BigDecimal userId = TcbsUser.getByUserName(code105C).getId();
      assertEquals("WAIT_FOR_VERIFY", TcbsNewOnboardingStatus.getByUserIdAndStatusKey(userId.toString(), "ECONTRACT_STATUS").getStatusValue());
      if (testCaseName.contains("open account have account link and IA link") || !testCaseName.contains("not IA link")) {
        assertEquals("1",  TcbsPartnerShip.getByUserId(userId).getLinkIaStatus());
        assertEquals("1", TcbsBankIaaccount.getIaiSaveBank(userId.toString(), partnerId).toString());
      }
      if (testCaseName.contains("open account have account link and IA link") || !testCaseName.contains("not account link")) {
        assertEquals("1",  TcbsPartnerShip.getByUserId(userId).getLinkAccountStatus());
      }
    } else {
      assertTrue(response.jsonPath().get("message").toString().contains(errorMessage));
    }
  }
}


