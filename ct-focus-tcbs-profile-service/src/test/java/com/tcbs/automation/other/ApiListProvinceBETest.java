package com.tcbs.automation.other;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import common.CallApiUtils;
import common.CommonUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
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
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/other/ApiListProvinceBE.csv", separator = '|')
public class ApiListProvinceBETest {

  private final HashMap<String, Object> params = new HashMap<>();
  @Getter
  private String testCaseName;
  @Getter
  private int statusCode;
  private String countryCode;
  private String errorMsg;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api list province backend")
  public void verifyListProvinceTest() {

    System.out.println("TestCaseName : " + testCaseName);

    RequestSpecification request = given()
      .baseUri(LIST_PROVINCE_BE)
      .header("Authorization", "Bearer " +
        (testCaseName.contains("user has no permission") ? TCBSPROFILE_AUTHORIZATION : TCBSPROFILE_CUSTODY_MAKER));

    Response response = CallApiUtils.callListProvincesApi(testCaseName, countryCode, request);

    assertThat(response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      List<HashMap<String, Object>> getResponseList = response.jsonPath().getList("provinces");
      List<HashMap<String, Object>> getDataList = CommonUtils.convertProvincesListJsonToHashmapList
        ("src/test/resources/requestBody/ProvincesListBody.json");
      assertEquals(getResponseList.size(), getDataList.size());
      for (int i = 0; i < getResponseList.size(); i++) {
        assertThat(getResponseList.get(i).get("divisionCode"), is(getDataList.get(i).get("divisionCode")));
        assertThat(getResponseList.get(i).get("divisionName"), is(getDataList.get(i).get("divisionName")));
      }
    } else if (statusCode == 403) {
      assertThat(response.jsonPath().get("errorMessage"), is(errorMsg));
    } else {
      assertThat(response.jsonPath().get("message"), is(errorMsg));
    }
  }
}
