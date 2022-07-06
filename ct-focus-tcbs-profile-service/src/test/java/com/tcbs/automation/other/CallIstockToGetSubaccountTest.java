package com.tcbs.automation.other;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/other/CallIstockToGetSubaccount.csv", separator = '|')
public class CallIstockToGetSubaccountTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String trackingid;
  private String lastUpdateFrom;
  private String lastUpdateTo;
  private String function;
  private String pageIndex;
  private String pageSize;
  private String custodycd;
  private String errMess;

  @Test
  @TestCase(name = "#testCaseName#")
  @Title("Verify API call Istock to get Subaccount")
  public void callIstockToGetSubaccount() {
    System.out.println("Testcase Name: " + testCaseName);

    Response response = given()
      .urlEncodingEnabled(false)
      .baseUri(CALL_ISTOCK_GET_SUBACCOUNT)
      .params("trackingId", trackingid)
      .param("function", function)
      .param("params", lastUpdateFrom, lastUpdateTo, custodycd, pageIndex, pageSize)
      .header("x-api-key", testCaseName.contains("user has no permission") ? FMB_X_API_KEY : API_KEY)
      .when()
      .get();

    assertEquals(statusCode, response.getStatusCode());

    if (response.getStatusCode() == 401) {
      assertEquals(errMess, response.jsonPath().get("message"));
    } else if (response.getStatusCode() == 400) {
      assertEquals(errMess, response.jsonPath().get("errorMessage"));
    } else {
      assertThat("Validate errorMessage null with status code 200", response.jsonPath().get("errorMessage"), is(nullValue()));
    }
  }


}
