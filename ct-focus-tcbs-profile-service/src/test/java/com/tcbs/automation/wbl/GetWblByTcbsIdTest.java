package com.tcbs.automation.wbl;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import io.restassured.response.Response;
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
@UseTestDataFrom(value = "data/wbl/GetWblByTcbsId.csv", separator = '|')
public class GetWblByTcbsIdTest {
  private final String str = "policyId,policyCode,policyName,policyActions,users";
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String tcbsId;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api get wbl by tcbsId")
  public void perfomTest() {
    Response response = given()
      .baseUri(GET_WBL_BY_TCBSID.replace("#tcbsId#", tcbsId))
      .header("X-Api-Key", (testCaseName.contains("invalid x-api-key") ? TCBSPROFILE_AUTHORIZATION : TCBSPROFILE_INQUIRYGROUPINFOKEY))
      .when()
      .get();

    assertThat("verify status code", response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      List<Map<String, Object>> maps = response.jsonPath().get();
      //if response is not empty
      if (!maps.isEmpty()) {
        for (String item : str.split(",", -1)) {
          assertThat(maps.get(0).keySet(), hasItem(item));
        }
        assertThat("verify tcbsId", response.jsonPath().get("users[0].tcbsId[0]"), is(tcbsId));
      } else {

      }

    } else {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }
}
