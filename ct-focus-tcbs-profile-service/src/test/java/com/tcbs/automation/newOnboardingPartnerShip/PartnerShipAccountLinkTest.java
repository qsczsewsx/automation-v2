package com.tcbs.automation.newOnboardingPartnerShip;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsPartnerShip;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.*;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.junit.Assert.assertEquals;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newOnboardingPartnerShip/ApiPartnerShipAccountLink.csv", separator = '|')
public class PartnerShipAccountLinkTest {
  @Getter
  private String testCaseName;
  private int statusCode;

  private String errorMessage;
  private String partnerId;
  private String partnerAccountId;
  private String code105C;
  private String idNumber;
  private String birthday;
  private String linkType;
  private String accountNo;
  private String accountName;
  private HashMap<String, Object> body;


  @Before
  public void setup() {

    linkType = syncData(linkType);
    List<String> listLinkType = new ArrayList<>(Arrays.asList(linkType.split(",")));

    HashMap<String, Object> iaBankAccount = new HashMap<>();
    iaBankAccount.put("accountNo", accountNo);
    iaBankAccount.put("accountName", accountName);

    partnerId = syncData(partnerId);
    partnerAccountId = syncData(partnerAccountId);
    code105C = syncData(code105C);
    idNumber = syncData(idNumber);
    birthday = syncData(birthday);
    accountNo = syncData(accountNo);
    accountName = syncData(accountName);
    body = new HashMap<>();

    body.put("partnerId", partnerId);
    body.put("partnerAccountId", partnerAccountId);
    body.put("code105C", code105C);
    body.put("idNumber", idNumber);
    body.put("birthday", birthday);
    body.put("linkType", listLinkType);

    if (!testCaseName.contains("case linkType is ACCOUNT")) {
      body.put("iaBankAccount", iaBankAccount);
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify partnership account link")
  public void partnerShipAccountLink() {
    System.out.println("Test Case: " + testCaseName);
    RequestSpecification requestSpecification = given()
      .baseUri(PARTNERSHIP_ACCOUNT_LINK)
      .header("x-api-key", testCaseName.contains("invalid x-api-key") ? FMB_X_API_KEY : PARTNERSHIP_X_API_KEY);

    Response response;

    if (testCaseName.contains("missing body")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(body).post();
    }

    assertEquals(statusCode, response.getStatusCode());
    if (statusCode == 200) {
      assertEquals("true", response.jsonPath().get("data").toString());
      assertEquals(partnerId, TcbsPartnerShip.getPartnerShip(partnerAccountId).getPartnerId());
      if (testCaseName.contains("linkType is ACCOUNT") || testCaseName.contains("linkType ACCOUNT and IA")) {
        assertEquals("P", TcbsPartnerShip.getPartnerShip(partnerAccountId).getLinkAccountStatus());
      } else if (testCaseName.contains("linkType is IA") || testCaseName.contains("linkType ACCOUNT and IA")) {
        assertEquals("P", TcbsPartnerShip.getPartnerShip(partnerAccountId).getLinkIaStatus());
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
