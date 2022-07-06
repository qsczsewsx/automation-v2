package com.tcbs.automation.wbl;


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
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/wbl/GetBusinessTitleList.csv", separator = '|')
public class GetBusinessTitleListTest {
  private final String str = "id,type,code,value,description";
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String authorized;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api get business title list")
  public void perfomTest() {
    Response response = given()
      .baseUri(GET_BUSINESS_TITLE_LIST)
      .header("Authorization", "Bearer " +
        (authorized.contains("no") ? TCBSPROFILE_AUTHORIZATION : TCBSPROFILE_SPECIALWBLKEY))
      .when()
      .get();

    assertThat("verify status code", response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      List<Map<String, Object>> maps = response.jsonPath().get();
      for (String item : str.split(",", -1)) {
        assertThat(maps.get(0).keySet(), hasItem(item));
      }
    } else if (statusCode == 403) {
      assertThat("verify error message", response.jsonPath().get("errorMessage"), is(errorMessage));
    }
  }
}
