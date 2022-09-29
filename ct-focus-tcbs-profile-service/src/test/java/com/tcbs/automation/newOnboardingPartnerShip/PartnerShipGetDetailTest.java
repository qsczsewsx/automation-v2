package com.tcbs.automation.newOnboardingPartnerShip;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Map;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newOnboardingPartnerShip/PartnerShipGetDetail.csv", separator = '|')
public class PartnerShipGetDetailTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMsg;
  private String username;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api partnership get detail")
  public void verifyPartnerShipGetDetailTest() {
    System.out.println("TestcaseName: " + testCaseName);
    String keys = "partnerId,partnerAccountId,channel,updatedDate,bankName,bankCode,bankAccountNo,bankAccountName,autoTransfer,isIaPaid,linkType,status";
    username = syncData(username);

    RequestSpecification requestSpecification = given()
      .baseUri(PARTNERSHIP_GET_DETAIL)
      .header("Authorization", "Bearer " +
        (testCaseName.contains("has no permission") ? TCBSPROFILE_AUTHORIZATION : ASSIGN_TASK_TO_MAKER_KEY));

    if (!testCaseName.contains("missing param username")) {
      requestSpecification.param("username", username);
    }
    Response response = requestSpecification.get();

    assertThat("verify status code", response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      List<Map<String, Object>> listPartners = response.jsonPath().get("partners");
      if (testCaseName.contains("is invalid")) {
        assertThat("verify count elements", listPartners.size(), is(0));
      } else {
        assertThat("verify count elements", listPartners.size(), is(greaterThan(0)));
        for (String item : keys.split(",", -1)) {
          assertThat(listPartners.get(0), hasKey(item));
        }
      }
    } else if (statusCode == 400) {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMsg));
    } else {
      assertThat("verify error message", response.jsonPath().get("errorMessage"), is(errorMsg));
    }
  }
}