package com.tcbs.automation.resignContract;


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
@UseTestDataFrom(value = "data/resignContract/GetBankListKycTask.csv", separator = '|')
public class GetBankListKycTaskTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api get bank list kyc task")
  public void performTest() {

    Response response = given()
      .baseUri(GET_BANK_LIST_KYC_TASK)
      .header("Authorization", "Bearer " +
        (testCaseName.contains("invalid token") ? FMB_X_API_KEY : ASSIGN_TASK_TO_MAKER_KEY))
      .when()
      .get();

    assertThat("verify status code", response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      List<Map<String, Object>> data = response.jsonPath().get("");
      String str = "bankCode,bankName";
      for (String item : str.split(",", -1)) {
        assertThat(data.get(0), hasKey(item));
      }
    } else {
      assertThat("verify error message", response.jsonPath().get("errorMessage"), is(errorMessage));
    }
  }
}