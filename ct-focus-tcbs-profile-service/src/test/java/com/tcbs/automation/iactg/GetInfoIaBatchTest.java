package com.tcbs.automation.iactg;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import io.restassured.response.Response;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.API_KEY;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.GET_INFO_IA_BATCH;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/iactg/GetIaBatchData.csv")
public class GetInfoIaBatchTest {
  private String testCaseName;
  private String fieldKey;
  private String fieldValue;
  private int statusCode;
  private String iaConnect;
  private String isIa;

  @Test
  @TestCase(name = "#testCaseName")
  public void verifyApiGetIaBatchInfo() {
    fieldValue = syncData(fieldValue);
    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("accounts", StringUtils.isNotEmpty(fieldValue) ? fieldValue.split("#") : fieldValue);
    Gson gson = new Gson();
    Response response = given()
      .baseUri(GET_INFO_IA_BATCH + fieldKey)
      .header("Content-Type", "application/json")
      .header("x-api-key", API_KEY)
      .body(gson.toJson(requestBody))
      .post();
    assertThat("verify status code", response.statusCode(), is(statusCode));
  }
}
