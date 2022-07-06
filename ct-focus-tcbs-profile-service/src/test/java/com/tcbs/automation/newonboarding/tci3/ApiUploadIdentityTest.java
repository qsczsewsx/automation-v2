package com.tcbs.automation.newonboarding.tci3;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsNewOnboardingStatus;
import com.tcbs.automation.cas.TcbsUser;
import com.tcbs.automation.login.LoginApi;
import com.tcbs.automation.login.TheUserInfo;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.screenplay.Actor;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.LinkedHashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.UPLOAD_IDENTITY;
import static com.tcbs.automation.tools.ConvertUtils.fileTxtToString;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/ApiUploadIdentity.csv", separator = '|')
public class ApiUploadIdentityTest {

  private static String userId;
  @Getter
  private String testCaseName;
  @Getter
  private String tcbsId;
  private String frontIdentity;
  private String backIdentity;
  private int statusCode;
  private String erroMsg;
  private String token;

  @AfterClass
  public static void resetStatusValue() {

    TcbsNewOnboardingStatus.updateStatusValueByUserId(userId, "ID_STATUS", "WAIT_FOR_UPLOAD");

  }

  @Before
  public void getUserLoginToken() {
    Actor actor = Actor.named("trungnd10");
    LoginApi.withCredentials("105C596401", "abc123").performAs(actor);
    token = TheUserInfo.aboutLoginData().answeredBy(actor).getToken();
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api Upload Identity")
  public void verifyApiUploadIdentityTest() {

    System.out.println("TestCaseName : " + testCaseName);

    frontIdentity = getFileContents(frontIdentity);
    backIdentity = getFileContents(backIdentity);
    userId = getUserId(testCaseName, tcbsId);

    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    body.put("frontIdentity", frontIdentity);
    body.put("backIdentity", backIdentity);

    Response response = given()
      .baseUri(UPLOAD_IDENTITY.replace("#tcbsId#", tcbsId))
      .header("Authorization", "Bearer " + token)
      .contentType("application/json")
      .body(body)
      .when()
      .post();

    assertThat(response.getStatusCode(), is(statusCode));

    if (response.statusCode() == 200) {
      String actualMessage = response.jsonPath().get("message");
      assertEquals(erroMsg, actualMessage);
      TcbsNewOnboardingStatus tcbsNewOnboardingStatus = TcbsNewOnboardingStatus.getByUserIdAndStatusKey(userId, "ID_STATUS");
      assertEquals("WAIT_FOR_VERIFY", tcbsNewOnboardingStatus.getStatusValue());

    } else if (response.statusCode() == 400) {
      String actualMessage = response.jsonPath().get("message");
      assertEquals(erroMsg, actualMessage);
    }
  }

  public String getFileContents(String fileContents) {
    if (fileContents.equalsIgnoreCase("front")) {
      fileContents = fileTxtToString("src/test/resources/requestBody/FrontFileContent");
    } else if (fileContents.equalsIgnoreCase("com/tcbs/automation/back")) {
      fileContents = fileTxtToString("src/test/resources/requestBody/BackFileContent");
    }
    return fileContents;
  }

  public String getUserId(String testCaseName, String tcbsId) {
    String userId = "";
    if (!testCaseName.contains("invalid value")) {
      TcbsUser tcbsUser = TcbsUser.getByTcbsId(tcbsId);
      userId = tcbsUser.getId().toString();
    }
    return userId;
  }
}
