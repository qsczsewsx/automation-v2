package com.tcbs.automation.newOnboardingPartnerShip;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsPartnerShip;
import com.tcbs.automation.cas.TcbsPartnerShipConfirm;
import com.tcbs.automation.login.LoginApi;
import com.tcbs.automation.login.TheUserInfo;
import common.CommonUtils;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.FMB_X_API_KEY;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.TCBS_ACCOUNT_CONFIRM;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newOnboardingPartnerShip/TcbsAccountLinkConfirm.csv", separator = '|')
public class TcbsAccountLinkConfirmTest {
  @Getter
  private String testCaseName;
  private int statusCode;

  private String errorMessage;
  private String partnerId;
  private String confirmId;
  private String token;
  private String linkTypes;
  private HashMap<String, Object> body;

  @Before
  public void setup() {
    Actor actor = Actor.named("logintoken");
    LoginApi.withCredentials("105C066114", "abc123").performAs(actor);
    token = TheUserInfo.aboutLoginData().answeredBy(actor).getToken();

    if (confirmId.equalsIgnoreCase("genConfirmId")) {
      confirmId = CommonUtils.tcbsCreateConfirmID(partnerId, "PNACID01");
    } else {
      confirmId = syncData(confirmId);
    }

    linkTypes = syncData(linkTypes);
    List<String> listLinkType = new ArrayList<>(Arrays.asList(linkTypes.split(",")));
    body = new HashMap<>();
    body.put("partnerId", partnerId);
    body.put("confirmId", confirmId);
    body.put("linkTypes", listLinkType);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify tcbs account link confirm")
  public void tcbsAccountLinkConfirm() {
    System.out.println("Test Case: " + testCaseName);

    RequestSpecification requestSpecification = given()
      .baseUri(TCBS_ACCOUNT_CONFIRM)
      .header("Authorization", "Bearer " + (testCaseName.contains("invalid Authorization") ? FMB_X_API_KEY : token));

    Response response;

    if (testCaseName.contains("missing body")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(body).post();
    }

    assertEquals(statusCode, response.getStatusCode());
    if (statusCode == 200) {
      assertThat("verify data", response.jsonPath().get("data.confirmId"), is(confirmId));
      assertThat("verify confirmId", TcbsPartnerShipConfirm.getConfirmIdByPartnerAndType(TcbsPartnerShip.getPartnerShip("PNACID01").getId(), "ACCOUNT").getStatus(), is(new BigDecimal(1)));
    } else if (statusCode == 400) {
      assertEquals(errorMessage, response.jsonPath().get("message"));
    }
  }

  @After
  public void deleteByPartnerAccountId() {
    TcbsPartnerShip.deleteByPartnerAccountId("PNACID01");
  }
}


