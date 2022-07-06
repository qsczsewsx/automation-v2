package com.tcbs.automation.other;

import com.adaptavist.tm4j.junit.annotation.TestCase;
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
@UseTestDataFrom(value = "data/other/SynSubaccountAndConnectIA.csv", separator = '|')
public class SynSubaccountAndConnectIATest {

  @Getter
  private String testCaseName;
  private int statusCode;
  private String errMess;
  private String xApiKey;

  @Test
  @TestCase(name = "#testCaseName#")
  @Title("Verify API sync subaccounts and connect IA")
  public void syncSubaccountAndConnectIa() {
    System.out.println("Testcase Name: " + testCaseName);

    if (xApiKey.equalsIgnoreCase("invalid")) {
      xApiKey = FMB_X_API_KEY;
    } else if (xApiKey.equalsIgnoreCase("empty")) {
      xApiKey = "";
    } else {
      xApiKey = API_KEY;
    }

    Response response = given()
      .baseUri(SYNC_SUBACCOUNT_AND_CONNECT_IA)
      .header("x-api-key", xApiKey)
      .when()
      .get();


    assertEquals(statusCode, response.getStatusCode());

    if (response.getStatusCode() == 401) {
      assertEquals(errMess, response.jsonPath().get("message"));
    }
  }
}
