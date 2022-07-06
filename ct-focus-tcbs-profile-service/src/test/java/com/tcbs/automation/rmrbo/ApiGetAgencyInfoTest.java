package com.tcbs.automation.rmrbo;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.R3RdAgency;
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

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.RMRBO_CHECKER_AUTHORIZATION_KEY;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.RMRBO_GET_AGENCY_INFO;
import static net.serenitybdd.rest.RestRequests.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/rmrbo/ApiGetAgencyInfo.csv", separator = '|')
public class ApiGetAgencyInfoTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String code;
  private String errorMessage;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify API get agency info")
  public void apiGetAgencyInfo() {
    System.out.println("Testcase Name : " + testCaseName);

    RequestSpecification requestSpecification = given()
      .baseUri(RMRBO_GET_AGENCY_INFO)
      .param("code", code)
      .header("Authorization", "Bearer " + (testCaseName.contains("Authorization is incorrect") ? "" : RMRBO_CHECKER_AUTHORIZATION_KEY))
      .contentType("application/json");

    Response response = requestSpecification.get();

    assertThat(response.getStatusCode(), is(statusCode));

    if (response.getStatusCode() == 200) {
      List<String> agencyDb = R3RdAgency.getByAgencyCode(code);
      List<Map> agencyRes = response.jsonPath().getList("data");
      assertEquals(agencyDb.size(), agencyRes.size());

      for (Map map : agencyRes) {
        String code = map.get("agencyCode").toString();
        assertTrue(agencyDb.stream().anyMatch(it -> it.equalsIgnoreCase(code)));
      }
    } else {
      assertEquals(errorMessage, response.jsonPath().get("message"));
    }
  }
}



