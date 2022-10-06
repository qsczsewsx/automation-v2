package com.tcbs.automation.newOnboardingPartnerShip;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newOnboardingPartnerShip/PartnerShipApiGetInfoByUsername.csv", separator = '|')
public class PartnerShipApiGetInfoByUsernameTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String linkAccountStatus;
  private String username;
  private String tcbsId;
  private HashMap<String, Object> params;

  @Before
  public void setup() {
    params = new HashMap<>();
    if (StringUtils.isNotEmpty(linkAccountStatus)) {
      params.put("linkAccountStatus", linkAccountStatus);
    }
    if (StringUtils.isNotEmpty(tcbsId)) {
      params.put("tcbsId", tcbsId);
    }
    if (StringUtils.isNotEmpty(username)) {
      params.put("username", username);
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify API get info partnership by username")
  public void getInfoPartnerShipByUsername() {
    System.out.println("Test Case: " + testCaseName);
    Response response = given()
      .baseUri(GET_INFO_BY_USERNAME)
      .header("x-api-key", testCaseName.contains("invalid x-api-key") ? FMB_X_API_KEY : PARTNERSHIP_X_API_KEY)
      .contentType("application/json")
      .params(params)
      .get();

    assertEquals(statusCode, response.getStatusCode());
    if (statusCode == 200) {
      assertThat("verify", response.jsonPath().get("basicInfo.code105C"), is(username));
      assertThat("verify", response.jsonPath().get("accountStatus"), is(notNullValue()));
      if (testCaseName.contains("linkAccountStatus <> 0")) {
        List<Map> listPartnerShip = response.jsonPath().get("partnerships");
        assertTrue("verify", listPartnerShip.isEmpty());
      }
    } else {
      assertEquals(errorMessage, response.jsonPath().get("message"));
    }
  }
}


