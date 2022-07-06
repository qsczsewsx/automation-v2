package com.tcbs.automation.wbl;


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
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/wbl/GetActionOrgList.csv", separator = '|')
public class GetActionOrgListTest {
  private final String str = "id,code,name,isRptDoc";
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String policyCode;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api get action org list")
  public void perfomTest() {
    RequestSpecification requestSpecification = given()
      .baseUri(GET_ACTION_ORG_LIST)
      .header("Authorization", "Bearer " +
        (testCaseName.contains("user has no permission") ? TCBSPROFILE_AUTHORIZATION : TCBSPROFILE_SPECIALWBLKEY));

    Response response;

    if (testCaseName.contains("missing param policyCode")) {
      response = requestSpecification.get();
    } else {
      response = requestSpecification.param("policyCode", policyCode).get();
    }

    assertThat("verify status code", response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      List<Map<String, Object>> maps = response.jsonPath().get();
      for (String item : str.split(",", -1)) {
        assertThat(maps.get(0).keySet(), hasItem(item));
      }
    } else if (statusCode == 403) {
      assertThat("verify error message", response.jsonPath().get("errorMessage"), is(errorMessage));
    } else {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }
}
