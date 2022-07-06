package com.tcbs.automation.other;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.LinkedHashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.junit.Assert.assertEquals;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/other/SendCustomerSettingToIangel.csv", separator = '|')
public class ApiCreateNewRelationIAngelTest {

  @Getter
  private String testCaseName;
  private int statusCode;
  private String errMess;
  private String wealthPartnerTcbsId;
  private String subscriberTcbsId;
  private String wealthPartnerCode;
  private String channel;
  private String subscriberAgreeTransparent;
  private String isSubAgreeFee;

  private boolean isSub;
  private boolean agreeTransparent;

  @Before
  public void before() {
    isSub = Boolean.parseBoolean(isSubAgreeFee);
    agreeTransparent = Boolean.parseBoolean(subscriberAgreeTransparent);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api call iAngel to inform customer's seting")
  public void apiCreateNewRelationIangel() {
    System.out.println("Test case: " + testCaseName);

    LinkedHashMap<String, Object> body = new LinkedHashMap<>();

    body.put("wealthPartnerTcbsId", wealthPartnerTcbsId.isEmpty() ? null : wealthPartnerTcbsId);
    body.put("subscriberTcbsId", subscriberTcbsId.isEmpty() ? null : subscriberTcbsId);
    body.put("wealthPartnerCode", wealthPartnerCode.isEmpty() ? null : wealthPartnerCode);
    body.put("channel", channel.isEmpty() ? null : channel);
    body.put("subscriberAgreeTransparent", subscriberAgreeTransparent.isEmpty() ? null : agreeTransparent);
    body.put("isSubAgreeFee", isSubAgreeFee.isEmpty() ? null : isSub);

    Response response = given()
      .baseUri(CREATE_NEW_RELATION)
      .header("x-api-key", statusCode == 401 ? FMB_X_API_KEY : API_KEY)
      .contentType("application/json")
      .body(body)
      .when()
      .post();

    assertEquals(statusCode, response.getStatusCode());

    if (response.getStatusCode() == 401 || response.getStatusCode() == 400) {
      assertEquals(errMess, response.jsonPath().get("message"));
    }


  }
}
