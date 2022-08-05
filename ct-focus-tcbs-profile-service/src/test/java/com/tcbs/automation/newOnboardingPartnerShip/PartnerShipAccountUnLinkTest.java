package com.tcbs.automation.newOnboardingPartnerShip;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsBankIaaccount;
import com.tcbs.automation.cas.TcbsPartnerShip;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.After;
import org.junit.AfterClass;
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
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newOnboardingPartnerShip/ApiPartnerShipAccountUnLink.csv", separator = '|')
public class PartnerShipAccountUnLinkTest {
  @Getter
  private String testCaseName;
  private int statusCode;

  private String errorMessage;
  private String partnerId;
  private String partnerAccountId;
  private String linkType;
  private String accountNo;
  private HashMap<String, Object> body;


  @Before
  public void setup() {

    linkType = syncData(linkType);
    List<String> listLinkType = new ArrayList<>(Arrays.asList(linkType.split(",")));

    accountNo = syncData(accountNo);
    HashMap<String, Object> iaBankAccount = new HashMap<>();
    iaBankAccount.put("accountNo", accountNo);

    partnerId = syncData(partnerId);
    partnerAccountId = syncData(partnerAccountId);
    body = new HashMap<>();

    body.put("partnerId", partnerId);
    body.put("partnerAccountId", partnerAccountId);
    body.put("linkType", listLinkType);

    if (!testCaseName.contains("case linkType is ACCOUNT")) {
      body.put("iaBankAccount", iaBankAccount);
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify partnership account unlink")
  public void partnerShipAccountUnLink() {
    System.out.println("Test Case: " + testCaseName);
    RequestSpecification requestSpecification = given()
      .baseUri(PARTNERSHIP_ACCOUNT_UNLINK)
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
      if (testCaseName.contains("case linkType is ACCOUNT")) {
        assertEquals("N", TcbsPartnerShip.getPartnerShip(partnerAccountId).getLinkAccountStatus());
      } else if (testCaseName.contains("case linkType is IA")) {
        assertEquals("4", TcbsBankIaaccount.getpartnershipIALink(accountNo).getStatus().toString());
      }
    } else {
      assertEquals(errorMessage, response.jsonPath().get("message"));
    }
  }
  @After
  public void resetData() {
    if (testCaseName.contains("case linkType is ACCOUNT with partnerAccountId has status not in (linked)")) {
      TcbsPartnerShip.updatePartnerStatusLinkAcc(partnerAccountId, "Y");
    }
  }
}


