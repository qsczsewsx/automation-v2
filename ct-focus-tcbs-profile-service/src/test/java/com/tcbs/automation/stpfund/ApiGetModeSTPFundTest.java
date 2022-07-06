package com.tcbs.automation.stpfund;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.VsdModeHistory;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.junit.Assert.assertEquals;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/stpfund/GetModeSTPFund.csv", separator = '|')

public class ApiGetModeSTPFundTest {
  @Getter
  private String testcaseName;
  private String product;
  private int statusCode;
  private String errorMsg;

  @Test
  @TestCase(name = "#testcaseName")
  @Title("Verify API get mode STP Fund")
  public void GetModeSTPFundTest() {
    System.out.println("Testcase Name: " + testcaseName);

    Response response = given()
      .baseUri(GET_MODE_STP_FUND)
      .param("product", product)
      .contentType("application/json")
      .header("Authorization", "Bearer " +
        (testcaseName.contains("Authorization key") ? FMB_X_API_KEY : STP_AUTHORIZATION_KEY))
      .when()
      .get();

    assertEquals(statusCode, response.getStatusCode());
    if (statusCode == 200) {
      VsdModeHistory vsdModeHistory = VsdModeHistory.getLatestModeByProduct(product);
      assertEquals(vsdModeHistory.getStpMode(), response.jsonPath().get("data"));
    } else if (testcaseName.contains("product")) {
      assertEquals(errorMsg, response.jsonPath().get("message"));
    }
  }
}

