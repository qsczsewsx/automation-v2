package com.tcbs.automation.wbl;


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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/wbl/GetWblByPolicyCodeInt.csv", separator = '|')
public class GetWblByPolicyCodeIntTest {
  private final String str = "policyId,policyCode,policyName,policyActions,users";
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String policyCode;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api get wbl by policyCode internal")
  public void perfomTest() {
    Response response = given()
      .baseUri(GET_WBL_BY_POLICYCODE_INT.replace("#policyCode#", policyCode))
      .header("X-Api-Key", (testCaseName.contains("invalid x-api-key") ? TCBSPROFILE_AUTHORIZATION : TCBSPROFILE_INQUIRYGROUPINFOKEY))
      .when()
      .get();

    assertThat("verify status code", response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      for (String item : str.split(",", -1)) {
        assertThat(response.jsonPath().getMap(""), hasKey(item));
      }
      assertThat("verify policyCode", response.jsonPath().get("policyCode"), is(policyCode));
    } else {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }
}
