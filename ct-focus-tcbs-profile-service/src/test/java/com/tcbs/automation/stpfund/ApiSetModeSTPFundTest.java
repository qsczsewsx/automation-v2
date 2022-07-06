package com.tcbs.automation.stpfund;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.cas.VsdModeHistory;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.LinkedHashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.junit.Assert.assertEquals;


@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/stpfund/SetModeSTPFund.csv", separator = '|')
public class ApiSetModeSTPFundTest {
  @Getter
  private String testcaseName;
  private String product;
  private int statusCode;
  private String errorMsg;
  private String mode;
  private String note;

  @Test
  @TestCase(name = "#testcaseName")
  @Title("Verify API set mode STP Fund")
  public void SetModeSTPFundTest() {
    System.out.println("Testcase Name: " + testcaseName);

    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    body.put("product", product);
    body.put("mode", mode);
    body.put("note", note);

    RequestSpecification requestSpecification = given()
      .baseUri(SET_MODE_STP_FUND)
      .header("Authorization", "Bearer " +
        (testcaseName.contains("Maker role") ? ASSIGN_TASK_TO_AMOPS_MAKER : STP_AUTHORIZATION_KEY))
      .contentType("application/json");

    Response response;
    Gson gson = new Gson();
    if (testcaseName.contains("missing body")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(gson.toJson(body)).post();
    }

    assertEquals(statusCode, response.getStatusCode());

    if (statusCode == 200) {
      VsdModeHistory vsdModeHistory = VsdModeHistory.getLatestModeByProduct(product);
      assertEquals(vsdModeHistory.getStpMode(), mode);
      assertEquals(vsdModeHistory.getNote(), note);
    } else if (statusCode == 400) {
      assertEquals(errorMsg, response.jsonPath().get("message"));
    } else if (statusCode == 403) {
      assertEquals(errorMsg, response.jsonPath().get("errorMessage"));
    }
  }
}


