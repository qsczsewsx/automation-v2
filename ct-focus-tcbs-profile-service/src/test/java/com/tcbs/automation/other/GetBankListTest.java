package com.tcbs.automation.other;


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
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/other/GetBankList.csv", separator = '|')
public class GetBankListTest {
  private final String str = "id,bankCode,bankName,branchCode,branchName,bankType,city,status";
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api get bank list")
  public void perfomTest() {

    Response response = given()
      .baseUri(GET_BANK_LIST)
      .header("x-api-key",
        (testCaseName.contains("invalid x-api-key") ? FMB_X_API_KEY : BANKLIST_X_API_KEY))
      .when()
      .get();

    assertThat("verify status code", response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      List<Map<String, Object>> data = response.jsonPath().get("data");
      for (String item : str.split(",", -1)) {
        assertThat(data.get(0), hasKey(item));
      }
    } else {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }
}
