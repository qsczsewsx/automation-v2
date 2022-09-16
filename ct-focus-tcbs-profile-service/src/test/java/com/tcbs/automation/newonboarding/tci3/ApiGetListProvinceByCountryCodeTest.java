package com.tcbs.automation.newonboarding.tci3;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.GET_LIST_PROVINCE_BY_COUNTRY;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/ApiGetListProvinceByCountryCode.csv", separator = '|')
public class ApiGetListProvinceByCountryCodeTest {

  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMsg;
  private String countryCode;
  private HashMap<String, Object> params;

  @Before
  public void setup() {
    params = new HashMap<>();
    if (StringUtils.isNotEmpty(countryCode)) {
      params.put("countryCode", countryCode);
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api get list province by country code test")
  public void verifyGetListProvinceTest() {
    System.out.println("TestCaseName : " + testCaseName);

    Response response = given()
      .baseUri(GET_LIST_PROVINCE_BY_COUNTRY)
      .contentType("application/json")
      .params(params)
      .get();

    assertThat(response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      List<Map> listProvince = response.jsonPath().get("provinces");
      assertThat("verify list provinces", listProvince, is(notNullValue()));
    } else if (statusCode == 400) {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMsg));
    }

  }
}