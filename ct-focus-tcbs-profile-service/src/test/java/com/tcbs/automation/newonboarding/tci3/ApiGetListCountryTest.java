package com.tcbs.automation.newonboarding.tci3;

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

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.GET_LIST_COUNTRY;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/ApiGetListCountry.csv", separator = '|')
public class ApiGetListCountryTest {

  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMsg;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api get list country test")
  public void verifyGetListCountryTest() {
    System.out.println("TestCaseName : " + testCaseName);

    Response response = given()
      .baseUri(GET_LIST_COUNTRY)
      .contentType("application/json")
      .get();

    assertThat(response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      List<Map> listCountry = response.jsonPath().get("countries");
      assertThat("verify list country", listCountry, is(notNullValue()));
    } else if (statusCode == 400) {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMsg));
    }

  }
}