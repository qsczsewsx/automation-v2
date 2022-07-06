package com.tcbs.automation.iactg;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import io.restassured.response.Response;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.API_KEY;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.GET_INFO_IA_BATCH;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/iactg/GetIaSingleData.csv")
public class GetInfoIaSingleTest {
  private String testCaseName;
  private String fieldKey;
  private String fieldValue;
  private int statusCode;
  private String iaConnect;
  private String isIa;

  @Test
  @TestCase(name = "#testCaseName")
  public void verifyApiGetIaInfo() {
    fieldValue = syncData(fieldValue);
    Response response = given()
      .baseUri(GET_INFO_IA_BATCH + fieldKey + "/" + fieldValue)
      .header("x-api-key", API_KEY)
      .when()
      .get();
    assertThat("verify status code", response.statusCode(), is(statusCode));
  }
}