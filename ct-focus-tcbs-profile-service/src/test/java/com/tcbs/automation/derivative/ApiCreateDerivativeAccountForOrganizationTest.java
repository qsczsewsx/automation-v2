package com.tcbs.automation.derivative;

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
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/ApiOpenDerivativeAccountForOrganization.csv", separator = '|')
public class ApiCreateDerivativeAccountForOrganizationTest {

  @Getter
  private String testCaseName;
  private String tcbsId;
  private String validTcbsId;
  private String unactivatedTcbsId;
  private String personTcbsId;
  private String actor;
  private int statusCode;
  private String errMessage;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify open derivative account for organization")
  public void apiOpenDerivativeAccountForOrganization() {
    System.out.println("Testcase Name: " + testCaseName);

    Response response = given()
      .baseUri(OPEN_DERIVATIVE_ACCOUNT_FOR_ORGANIZATION.replace("#tcbsId#", tcbsId))
      .header("Authorization", "Bearer " +
        (actor.equalsIgnoreCase("amops") ? ASSIGN_TASK_TO_AMOPS_MAKER : TCBS_USER_TOKEN))
      .when()
      .post();

    assertThat(response.getStatusCode(), is(statusCode));
    System.out.println("Actual Response Code: " + response.getStatusCode());

    if (response.getStatusCode() == 400) {
      assertEquals(errMessage, response.jsonPath().get("message"));
    } else if (response.getStatusCode() == 403) {
      assertEquals(errMessage, response.jsonPath().get("errorMessage"));
    } else if (response.getStatusCode() == 200) {
      assertEquals(errMessage, response.jsonPath().get("msg"));
    }
  }
}
