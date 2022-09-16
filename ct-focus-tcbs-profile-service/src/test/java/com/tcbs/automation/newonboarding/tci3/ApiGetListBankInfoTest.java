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

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.GET_LIST_BANK_INFO;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.GET_LIST_COUNTRY;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/ApiGetListBankInfo.csv", separator = '|')
public class ApiGetListBankInfoTest {

  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMsg;
  private String pageSize;
  private HashMap<String, Object> params;

  @Before
  public void setup() {
    params = new HashMap<>();
    if (StringUtils.isNotEmpty(pageSize)) {
      params.put("pageSize", pageSize);
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api get list bank info")
  public void verifyGetListBankInfoTest() {
    System.out.println("TestCaseName : " + testCaseName);

    Response response = given()
      .baseUri(GET_LIST_BANK_INFO)
      .contentType("application/json")
      .params(params)
      .get();

    assertThat(response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      List<Map> listBankInfo = response.jsonPath().get("data");
      assertThat("verify list country", listBankInfo, is(notNullValue()));
    } else if (statusCode == 400) {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMsg));
    }

  }
}