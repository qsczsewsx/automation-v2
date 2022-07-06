package com.tcbs.automation.newonboarding.tci3;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsUser;
import com.tcbs.automation.login.LoginApi;
import com.tcbs.automation.login.TheUserInfo;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.screenplay.Actor;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.UPDATE_PROFILE_TCI3;
import static com.tcbs.automation.tools.ConvertUtils.fileTxtToString;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/UpdateProfile.csv", separator = '|')
public class UpdateProfileTest {

  @Getter
  private String testCaseName;
  private int statusCode;
  private String tcbsId;
  private String errorMsg;
  private HashMap<String, Object> body;
  private String avatarData;
  private String jwt;

  @Before
  public void setup() throws UnsupportedEncodingException {

    tcbsId = syncData(tcbsId);
    avatarData = syncData(avatarData);

    Actor actor = Actor.named("haihv");
    String userName = TcbsUser.getByTcbsId(tcbsId).getUsername();
    if (testCaseName.contains("tcbsId does not match token")) {
      LoginApi.withCredentials("105C313993", "abc123").performAs(actor);
    } else {
      LoginApi.withCredentials(userName, "abc123").performAs(actor);
    }
    jwt = TheUserInfo.aboutLoginData().answeredBy(actor).getToken();

    if (avatarData.equalsIgnoreCase("avatarData")) {
      avatarData = fileTxtToString("src/test/resources/requestBody/ImgBase64.txt");
    }

    HashMap<String, Object> personalInfo = new HashMap<>();
    personalInfo.put("avatarData", avatarData);

    body = new HashMap<>();
    body.put("personalInfo", personalInfo);

  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api update profile test")
  public void verifyUpdateProfileTest() {

    System.out.println("TestCaseName : " + testCaseName);

    RequestSpecification requestSpecification = given()
      .baseUri(UPDATE_PROFILE_TCI3.replace("#tcbsId#", tcbsId))
      .header("Authorization", "Bearer " + jwt)
      .contentType("application/json");

    Response response;

    if (testCaseName.contains("missing BODY")) {
      response = requestSpecification.put();
    } else {
      response = requestSpecification.body(body).put();
    }

    assertThat(response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      assertThat("verify avatar url", TcbsUser.getByTcbsId(tcbsId).getAvatarUrl(), is(containsString("avatar/")));
    } else if (statusCode == 400) {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMsg));
    }

  }
}