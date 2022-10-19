package com.tcbs.automation.newOnboardingPartnerShip;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsBankIaaccount;
import com.tcbs.automation.cas.TcbsPartnerShip;
import com.tcbs.automation.cas.TcbsUser;
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

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.TCBS_ACCOUNT_UNLINK;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newOnboardingPartnerShip/TcbsAccountUnLink.csv", separator = '|')
public class TcbsAccountUnLinkTest {
  @Getter
  private String testCaseName;
  private int statusCode;

  private String errorMessage;
  private String partnerId;
  private String partnerAccountId;
  private String code105C;
  private String linkType;
  private String accountNo;
  private String token;
  private HashMap<String, Object> body;


  @Before
  public void setup() {
    Actor actor = Actor.named("linhtth");
    LoginApi.withCredentials(code105C, "abc123").performAs(actor);
    token = TheUserInfo.aboutLoginData().answeredBy(actor).getToken();

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

    if (linkType.contains("IA")) {
      body.put("iaBankAccount", iaBankAccount);
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify partnership account unlink")
  public void tcbsAccountUnLink() {
    System.out.println("Test Case: " + testCaseName);
    RequestSpecification requestSpecification = given()
      .baseUri(TCBS_ACCOUNT_UNLINK)
      .header("Authorization", "Bearer " + token);

    Response response;
    if (testCaseName.contains("missing body")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(body).post();
    }

    assertEquals(statusCode, response.getStatusCode());
    if (statusCode == 200) {
      assertEquals("true", response.jsonPath().get("data").toString());
      assertEquals("0", TcbsPartnerShip.getPartnerShip(partnerAccountId).getLinkAccountStatus());
      assertEquals("4", TcbsBankIaaccount.getpartnershipIALink(accountNo).getStatus().toString());
    } else {
      assertTrue(response.jsonPath().get("message").toString().contains(errorMessage));
    }
  }

  @After
  public void resetData() {
    if (statusCode == 200) {
      TcbsPartnerShip.updatePartnerStatusLinkAcc(partnerAccountId, "1", "1");
      TcbsBankIaaccount.updateStatusByUserId(TcbsUser.getByUserName(code105C).getId().toString(), "1");
    }
  }
}


