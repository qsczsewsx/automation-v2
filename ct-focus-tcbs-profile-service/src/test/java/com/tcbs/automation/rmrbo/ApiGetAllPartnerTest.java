package com.tcbs.automation.rmrbo;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.R3RdRole;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static net.serenitybdd.rest.RestRequests.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/rmrbo/ApiGetAllPartner.csv", separator = '|')
public class ApiGetAllPartnerTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify API get all partner")
  public void apiGetAllPartner() {
    System.out.println("Testcase Name : " + testCaseName);

    Response response = given()
      .baseUri(GET_ALL_PARTNER)
      .header("Authorization", "Bearer " + (testCaseName.contains("incorrect Authorization") ? FMB_X_API_KEY : RMRBO_CHECKER_AUTHORIZATION_KEY))
      .contentType("application/json")
      .get();
    assertThat(response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      List<String> partnersDB = R3RdRole.getAllPartner();
      List<String> partnersRes = response.jsonPath().getList("");
      assertEquals(partnersDB.size(), partnersRes.size());
      for (String p : partnersRes) {
        assertTrue(partnersDB.contains(p));
      }
    } else {
      assertTrue(response.jsonPath().get("errorMessage").toString().contains(errorMessage));
    }
  }
}
