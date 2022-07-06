package com.tcbs.automation.resignContract;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.login.LoginApi;
import com.tcbs.automation.login.TheUserInfo;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.screenplay.Actor;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.UnsupportedEncodingException;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.GET_CUSTOMER_PROFILE;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.junit.Assert.assertEquals;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/resignContract/ApiGetCustomerProfile.csv", separator = '|')
public class ApiGetCustomerProfileTest {
  private static String gen_valid_tcbsid;
  @Getter
  private String testCaseName;
  private int statusCode;
  private String tcbsId;
  private String errMess;
  private String jwt;

  @Before
  public void setup() throws UnsupportedEncodingException {
    tcbsId = syncData(tcbsId);

    Actor actor = Actor.named("trangnt88");
    if (testCaseName.contains("user having no permission")) {
      LoginApi.withCredentials("105C234304", "abc123").performAs(actor);
    } else {
      LoginApi.withCredentials("105C277900", "abc123").performAs(actor);
    }
    jwt = TheUserInfo.aboutLoginData().answeredBy(actor).getToken();

  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify Api get customer profile")
  public void apiGetCustomerProfile() {
    System.out.println("Test case: " + testCaseName);

    if (tcbsId.equalsIgnoreCase("valid")) {
      tcbsId = gen_valid_tcbsid;
    }

    Response response = given()
      .baseUri(GET_CUSTOMER_PROFILE.replaceAll("#tcbsId#", tcbsId.equalsIgnoreCase("null") ? null : tcbsId))
      .header("Authorization", "Bearer " + jwt)
      .contentType("application/json")
      .param("fields", "accountStatus,personalInfo,basicInfo")
      .when()
      .get();

    assertEquals(statusCode, response.getStatusCode());

    if (response.getStatusCode() == 404) {
      assertEquals(errMess, response.jsonPath().get("message"));
    } else if (response.getStatusCode() == 200) {
      assertEquals("WAIT_CUSTOMER", response.jsonPath().get("accountStatus.onboardingStatus.resignContract.value"));
    }
  }
}
