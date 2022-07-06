package com.tcbs.automation.hold105C;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import common.CommonUtils;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.GET_INFO_HOLD_105C;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/hold105C/GetInfoHold105C.csv", separator = '|')
public class GetInfoHold105CTest {

  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String username;
  private int status;
  private String token;
  private int expectHold105C;
  private boolean hold105C;

  @Before
  public void before() {
    token = CommonUtils.getToken(username);
    hold105C = expectHold105C != 0;
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api get info hold 105C")
  public void verifyGetInfoHold105CTest() {

    System.out.println("TestCaseName : " + testCaseName);

    Response response = SerenityRest.given()
      .baseUri(GET_INFO_HOLD_105C)
      .header("Authorization", "Bearer " + token)
      .get();

    assertThat(response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      assertThat("verify hold105C", response.jsonPath().get("hold105C"), is(hold105C));
      assertThat("verify status", response.jsonPath().get("status"), is(status));
      if (testCaseName.contains("booked fancy 105C successfully")) {
        List<HashMap<String, Object>> data = response.jsonPath().getList("data");
        String condition = "code105C,referCodeHold105C,fullName,status";
        for (String item : condition.split(",", -1)) {
          MatcherAssert.assertThat(data.get(0), hasKey(item));
        }
      }
    } else if (statusCode == 400) {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }
}