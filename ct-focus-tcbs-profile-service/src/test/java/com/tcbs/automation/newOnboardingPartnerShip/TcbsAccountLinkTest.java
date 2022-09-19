package com.tcbs.automation.newOnboardingPartnerShip;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsPartnerShip;
import com.tcbs.automation.login.LoginApi;
import com.tcbs.automation.login.TheUserInfo;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.screenplay.Actor;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.junit.Assert.assertEquals;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newOnboardingPartnerShip/TcbsAccountLink.csv", separator = '|')
public class TcbsAccountLinkTest {
  @Getter
  private String testCaseName;
  private int statusCode;

  private String errorMessage;
  private String partnerId;
  private String partnerAccountId;
  private String code105C;
  private String token;
  private String autoTransfer;
  private String isIAPaid;
  private String linkType;
  private String accountNo;
  private String accountName;
  private HashMap<String, Object> body;


  @Before
  public void setup() {
    Actor actor = Actor.named("linhtth");
    LoginApi.withCredentials(code105C, "abc123").performAs(actor);
    token = TheUserInfo.aboutLoginData().answeredBy(actor).getToken();

    linkType = syncData(linkType);
    List<String> listLinkType = new ArrayList<>(Arrays.asList(linkType.split(",")));

    HashMap<String, Object> iaBankAccount = new HashMap<>();
    iaBankAccount.put("accountNo", accountNo);
    iaBankAccount.put("accountName", accountName);
    iaBankAccount.put("autoTransfer", autoTransfer);
    iaBankAccount.put("isIAPaid", isIAPaid);

    partnerId = syncData(partnerId);
    partnerAccountId = syncData(partnerAccountId);
    code105C = syncData(code105C);
    autoTransfer = syncData(autoTransfer);
    isIAPaid = syncData(isIAPaid);
    accountNo = syncData(accountNo);
    accountName = syncData(accountName);
    body = new HashMap<>();

    body.put("partnerId", partnerId);
    body.put("partnerAccountId", partnerAccountId);
    body.put("linkType", listLinkType);

    if (!testCaseName.contains("ACCOUNT only")) {
      body.put("iaBankAccount", iaBankAccount);
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify tcbs partnership account link")
  public void tcbsAccountLink() {
    System.out.println("Test Case: " + testCaseName);
    RequestSpecification requestSpecification = given()
      .baseUri(TCBS_ACCOUNT_LINK)
      .header("Authorization", "Bearer " + token)
      .contentType("application/json");

    Response response;

    if (testCaseName.contains("missing body")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(body).post();
    }

    assertEquals(statusCode, response.getStatusCode());
    if (statusCode == 200) {
      if (testCaseName.contains("linkType is ACCOUNT") || testCaseName.contains("linkType ACCOUNT and IA")) {
        assertEquals("P", TcbsPartnerShip.getByPartnerAccountIdAndPartnerId(partnerId,partnerAccountId).getLinkAccountStatus());
      } else if (testCaseName.contains("linkType is IA") || testCaseName.contains("linkType ACCOUNT and IA")) {
        assertEquals("N", TcbsPartnerShip.getByPartnerAccountIdAndPartnerId(partnerId,partnerAccountId).getLinkIaStatus());
      }
    } else {
      assertEquals(errorMessage, response.jsonPath().get("message"));
    }
  }

  @After
  public void clearData() {
    if (statusCode == 200) {
      TcbsPartnerShip.deleteByPartnerAccountId(partnerAccountId);
    }
  }
}

