package com.tcbs.automation.partnership;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.cas.TcbsIdentification;
import com.tcbs.automation.cas.TcbsPartnership;
import com.tcbs.automation.cas.TcbsUser;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.FMB_X_API_KEY;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.PARTNER_CHECK_ACCOUNT_EXIST;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/partnership/CheckPartnerAccountExist.csv", separator = '|')
public class CheckPartnerAccountExistTest {

  @Getter
  private String testCaseName;
  @Getter
  private int statusCode;
  private String partnerId;
  private String idNumber;
  private String birthday;
  private String errorMsg;
  private HashMap<String, Object> body;
  Map<String, String> statusMap;
  Map<String, String> cusTypeMap;

  @Before
  public void setup() {
    body = new HashMap<>();
    if (testCaseName.contains("missing BODY")) {
      body = null;
    } else {
      body.put("partnerId", partnerId);
      body.put("idNumber", idNumber);
      body.put("birthday", birthday);
    }
    statusMap = new HashMap<String, String>() {{
      put("ACTIVE", "1");
      put("INACTIVE", "-1");
      put("LOCK", "0");
      put("BLOCK", "-2");
    }};
    cusTypeMap = new HashMap<String, String>() {{
      put("INDIVIDUAL", "0");
      put("CORPORATE", "1");
    }};
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api Check PartnerShip Account Exist")
  public void verifyCheckPartnerShipAccountExistTest() {
    System.out.println("TestCaseName : " + testCaseName);

    Gson gson = new Gson();

    Response response = given()
      .baseUri(PARTNER_CHECK_ACCOUNT_EXIST)
      .header("x-api-key", FMB_X_API_KEY)
      .contentType("application/json")
      .body(gson.toJson(body))
      .when()
      .post();

    assertThat(response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      Map<String, Object> basicInfo = response.jsonPath().get("basicInfo");
      BigDecimal userId = TcbsIdentification.getByIdNumber(idNumber).getUserId();
      TcbsUser tcbsUser = TcbsUser.getById(userId);
      assertThat("verify code 105C", basicInfo.get("code105C"), is(tcbsUser.getUsername()));
      assertThat("verify account status", statusMap.get(basicInfo.get("status").toString()), is(tcbsUser.getAccountStatus().toString()));
      assertThat("verify code 105C", cusTypeMap.get(basicInfo.get("type").toString()), is(tcbsUser.getCustype().toString()));
      Map<String, Object> partnership = response.jsonPath().get("partnership");
      TcbsPartnership tcbsPartnership = TcbsPartnership.getByUserId(userId);
      assertThat("verify partnerAccountId", partnership.get("linkAccount.partnerAccountId"), is(tcbsPartnership.getPartnerAccountId()));
    } else {
      assertEquals(errorMsg, response.jsonPath().get("message"));
    }
  }

}