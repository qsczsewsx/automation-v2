package com.tcbs.automation.wbl;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Map;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/wbl/GetWblUserList.csv", separator = '|')
public class GetWblUserListTest {
  private final String str = "policyUserType,tcbsId,policyCode,userName,idNumber,fullName,address,actions";
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String keySearch;
  private String valueSearch;
  private String policyCode;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api get wbl user list")
  public void perfomTest() {

    keySearch = syncData(keySearch);
    valueSearch = syncData(valueSearch);
    policyCode = syncData(policyCode);

    RequestSpecification requestSpecification = given().baseUri(GET_WBL_USER_LIST);
    Response response;

    if (testCaseName.contains("user has special role")) {
      requestSpecification.header("Authorization", "Bearer " + TCBSPROFILE_SPECIALWBLKEY);
      if (testCaseName.contains("missing key param")) {
        response = requestSpecification.get();
      } else {
        response = requestSpecification.param(keySearch, valueSearch).param("policyCode", policyCode).get();
      }
    } else if (testCaseName.contains("user has no permission")) {
      response = requestSpecification.header("Authorization", "Bearer " + TCBSPROFILE_AUTHORIZATION).get();
    } else {
      requestSpecification.header("Authorization", "Bearer " + TCBSPROFILE_NORMALWBLKEY);
      if (testCaseName.contains("missing key param")) {
        response = requestSpecification.param("policyCode", policyCode).get();
      } else if (testCaseName.contains("missing policyCode param ")) {
        response = requestSpecification.param(keySearch, valueSearch).get();
      } else {
        response = requestSpecification.param(keySearch, valueSearch).param("policyCode", policyCode).get();
      }
    }

    assertThat("verify status code", response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      List<Map<String, Object>> listData = response.jsonPath().get();
      if (testCaseName.contains("not exist")) {
        assertThat(listData.size(), is(0));
      } else {
        for (String item : str.split(",", -1)) {
          assertThat(listData.get(0).keySet(), hasItem(item));
        }
      }
    } else if (statusCode == 403) {
      assertThat("verify error message", response.jsonPath().get("errorMessage"), is(errorMessage));
    } else {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }
}
