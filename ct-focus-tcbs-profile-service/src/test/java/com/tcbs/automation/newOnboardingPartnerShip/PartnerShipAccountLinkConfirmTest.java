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

import java.util.*;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newOnboardingPartnerShip/ApiPartnerShipAccountLinkConfirm.csv", separator = '|')
public class PartnerShipAccountLinkConfirmTest {
  @Getter
  private String testCaseName;
  private int statusCode;

  private String errorMessage;
  private String confirmId;
  private String custodyCode;
  private String authoToken;
  private String dataConfirmId;
  private HashMap<String, Object> body;


  @Before

  public void setup() {

    Actor actor = Actor.named("logintoken");
    LoginApi.withCredentials(custodyCode, "abc123").performAs(actor);
    authoToken = TheUserInfo.aboutLoginData().answeredBy(actor).getToken();

    dataConfirmId = creatConfirmID();

  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify partnership account link confirm")
  public void partnerShipAccountLinkConfirm() {
    System.out.println("Test Case: " + testCaseName);

    if (confirmId.equals("genConfirmId")) {
      confirmId = dataConfirmId;
    } else {
      confirmId = syncData(confirmId);
    }

    body = new HashMap<>();
    body.put("confirmId", confirmId);

    RequestSpecification requestSpecification = given()
      .baseUri(PARTNERSHIP_CONFIRM)
      .header("Authorization", "Bearer " + (testCaseName.contains("invalid Authorization") ? FMB_X_API_KEY : authoToken));

    Response response;

    if (testCaseName.contains("missing body")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(body).post();
    }

    assertEquals(statusCode, response.getStatusCode());
    if (statusCode == 200 && testCaseName.contains("case successful")) {
      assertTrue("true", response.jsonPath().get("data"));
      assertThat("verify confirmId", dataConfirmId, is(nullValue()));
    } else if (testCaseName.contains("invalid Authorization")) {
      assertEquals(statusCode, response.statusCode());
    } else {
      assertEquals(errorMessage, response.jsonPath().get("message"));
    }
  }

  @After
  public void deleteByPartnerAccountId () {
    TcbsPartnerShip.deleteByPartnerAccountId("PH00111111222");
  };

  public static String creatConfirmID() {
    LinkedHashMap<String, Object> bodyLink = new LinkedHashMap<>();
    String s = "ACCOUNT,IA";
    ArrayList<String> listLinkType = new ArrayList<String>(Arrays.asList(s.split(",")));
    Object[] listLinkType1 = listLinkType.toArray();

    bodyLink.put("partnerId", "CTG");
    bodyLink.put("partnerAccountId", "PH00111111222");
    bodyLink.put("code105C", "105C189336");
    bodyLink.put("idNumber", "34567555333");
    bodyLink.put("birthday", "16/08/1996");
    bodyLink.put("linkType", listLinkType1[0]);


    Response response = given()
      .baseUri(PARTNERSHIP_ACCOUNT_LINK)
      .header("x-api-key", PARTNERSHIP_X_API_KEY)
      .body(bodyLink)
      .post();
    return  TcbsPartnerShip.getPartnerShip("PH00111111222").getConfirmId();
  }

}


