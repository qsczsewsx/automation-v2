package com.tcbs.automation.hold105C;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import common.CommonUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.VALIDATE_HOLD_105C;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/hold105C/ValidateHold105C.csv", separator = '|')
public class ValidateHold105CTest {

  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String code105C;
  private String token;
  private HashMap<String, Object> body;

  @Before
  public void before() {
    token = CommonUtils.getToken("105C043442");
    code105C = syncData(code105C);
    body = new HashMap<>();
    body.put("code105C", code105C);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api validate hold 105C")
  public void verifyValidateHold105CTest() {

    System.out.println("TestCaseName : " + testCaseName);

    RequestSpecification requestSpecification = given()
      .baseUri(VALIDATE_HOLD_105C)
      .header("Authorization", "Bearer " + token)
      .contentType("application/json");

    Response response;

    if (testCaseName.contains("missing BODY")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(body).post();
    }

    assertThat(response.getStatusCode(), is(statusCode));
    assertThat("verify code", response.jsonPath().get("code"), is(errorMessage));

  }
}