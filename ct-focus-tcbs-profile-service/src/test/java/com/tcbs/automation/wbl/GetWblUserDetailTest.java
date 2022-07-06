package com.tcbs.automation.wbl;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/wbl/GetWblUserDetail.csv", separator = '|')
public class GetWblUserDetailTest {
  private final String str = "wblUserId,fullName,address,idNumber,identifications,policies,stakeHolders,reportDocs";
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String wblUserId;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api get policy action list")
  public void perfomTest() {

    Response response = given()
      .baseUri(GET_WBL_USER_DETAIL.replaceAll("#wblUserId#", wblUserId))
      .header("Authorization", "Bearer " +
        (testCaseName.contains("user has no permission") ? TCBSPROFILE_AUTHORIZATION : TCBSPROFILE_SPECIALWBLKEY))
      .when()
      .get();

    assertThat("verify status code", response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      Map<String, Object> data = response.jsonPath().get();
      for (String item : str.split(",", -1)) {
        assertThat(data, hasKey(item));
      }
    } else if (statusCode == 403) {
      assertThat("verify error message", response.jsonPath().get("errorMessage"), is(errorMessage));
    } else {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }
}
