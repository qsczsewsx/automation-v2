package com.tcbs.automation.otpGlobalPhone;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsRegion;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/otpGlobalPhone/ApiGetListPhoneCode.csv", separator = '|')
public class ApiGetListPhoneCodeTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api get list phone code")
  public void verifyGetListPhoneCodeTest() {
    System.out.println(testCaseName);

    Response response = given()
      .baseUri(OTP_GET_LIST_PHONE_CODE)
      .contentType("application/json")
      .get();

    assertThat("verify status", response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      List<HashMap<String, Object>> listData = response.jsonPath().getList("");
      List<TcbsRegion> tcbsRegionList  = TcbsRegion.getAllRegion();

      for (int i=0; i< listData.size(); i++) {
        assertThat(listData.get(i).get("countryCode"), is(tcbsRegionList.get(i).getCode()));
        assertThat(listData.get(i).get("countryName"), is(tcbsRegionList.get(i).getName()));
        assertThat(listData.get(i).get("phoneCode"), is(tcbsRegionList.get(i).getPhoneCode()));
        assertThat(listData.get(i).get("nationalFlagPath"), is(tcbsRegionList.get(i).getNationalFlagPath()));
      }
    } else {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }
}
