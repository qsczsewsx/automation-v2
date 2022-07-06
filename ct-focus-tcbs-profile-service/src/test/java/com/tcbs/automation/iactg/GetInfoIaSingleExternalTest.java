package com.tcbs.automation.iactg;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.login.LoginApi;
import com.tcbs.automation.login.TheUserInfo;
import io.restassured.response.Response;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.screenplay.Actor;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.GET_INFO_IA_SINGLE_EXT;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/iactg/GetIaSingleExternalData.csv")
public class GetInfoIaSingleExternalTest {
  private String testCaseName;
  private String fieldKey;
  private String fieldValue;
  private int statusCode;
  private String iaConnect;
  private String isIa;
  private String jwt;

  @Before
  public void setup() {
    Actor actor = Actor.named("haihv");
    LoginApi.withCredentials("105C738764", "abc123").performAs(actor);
    jwt = TheUserInfo.aboutLoginData().answeredBy(actor).getToken();
  }

  @Test
  @TestCase(name = "#testCaseName")
  public void verifyApiGetIaSingleExternalInfo() {
    fieldValue = syncData(fieldValue);
    Response response = given()
      .baseUri(GET_INFO_IA_SINGLE_EXT + fieldKey + "/" + fieldValue)
      .header("Authorization", "Bearer " + jwt)
      .when()
      .get();
    assertThat("verify status code", response.statusCode(), is(statusCode));
  }
}
