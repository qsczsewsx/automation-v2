package com.tcbs.automation.newonboarding.tci3;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.*;
import com.tcbs.automation.login.LoginApi;
import com.tcbs.automation.login.TheUserInfo;
import com.tcbs.automation.tools.StringUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.screenplay.Actor;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/CompleteInfoIA.csv", separator = '|')
public class CompleteInfoIATest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String tcbsId;
  private String tcbsIdBody;
  private String errorMessage;
  private String fullName;
  private String contactAddress;
  private String province;
  private String gender;
  private String idPlace;
  private HashMap<String, Object> body;
  private String jwt;
  private String userId;

  @Before
  public void setup() throws UnsupportedEncodingException {

    Actor actor = Actor.named("haihv");
    String userName = TcbsUser.getByTcbsId(tcbsId).getUsername();
    if (testCaseName.contains("tcbsId does not match token")) {
      LoginApi.withCredentials("105C313993", "abc123").performAs(actor);
    } else {
      LoginApi.withCredentials(userName, "abc123").performAs(actor);
    }
    jwt = TheUserInfo.aboutLoginData().answeredBy(actor).getToken();

    fullName = syncData(fullName);
    contactAddress = syncData(contactAddress);
    province = syncData(province);
    gender = syncData(gender);
    idPlace = syncData(idPlace);
    tcbsIdBody = syncData(tcbsIdBody);


    body = new HashMap<>();

    body.put("contactAddress", contactAddress);
    body.put("province", province);
    body.put("fullName", fullName);
    body.put("gender", StringUtils.isNumber(gender) ? Integer.valueOf(gender) : gender);
    body.put("tcbsId", tcbsIdBody);
    body.put("idPlace", idPlace);

  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api complete info ia")
  public void verifyCompleteInfoIATest() {
    System.out.println("Test case name: " + testCaseName);

    RequestSpecification requestSpecification = given()
      .baseUri(TCBSPROFILE_COMPLETE_INFO_IA.replace("#tcbsId#", tcbsId))
      .header("Authorization", "Bearer " + jwt)
      .contentType("application/json");
    Response response;

    if (testCaseName.contains("missing BODY")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(body).post();
    }

    assertThat("verify status code", response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      assertThat("verify code", response.jsonPath().get("code"), is("200"));

      TcbsUser resultUser = TcbsUser.getByTcbsId(tcbsId);
      userId = resultUser.getId().toString();
      TcbsAddress resultAddress = TcbsAddress.getByTcbsAddress(userId);
      TcbsAddressDivision resultDivision = TcbsAddressDivision.getById(resultAddress.getDivision2());

      assertThat(resultUser.getDocusignStatus(), is(new BigDecimal(3)));
      assertThat(resultUser.getLastname() + " " + resultUser.getFirstname(), is(fullName));
      assertThat(resultUser.getGender(), is(new BigDecimal(gender)));
      assertThat(TcbsIdentification.getByTcbsIdentification(userId).getIdPlace(), is(idPlace));
      assertThat(resultAddress.getAddress(), is(contactAddress));
      assertThat(resultDivision.getDivisionCode(), is(province));
    } else if (statusCode == 400) {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }

  @After
  public void clearData() {
    // Clear data
    if (statusCode == 200) {
      TcbsUser.updateDocCusSign(tcbsId, "0");
      TcbsUser.updateNameGender(tcbsId, "Chau", "Hoang Minh", "1");
      TcbsAddress.updateAddress(userId, "số 191 Bà Triệu, Lê Đại Hành, Hai Bà Trưng", null);
      TcbsIdentification.updateIdPlace(userId, "Cuc CS QLHC TTXH");
      TcbsNewOnboardingStatus.updateStatusValueByUserId(userId, "ID_STATUS", "WAIT_FOR_UPLOAD");
      Response responseCache = given()
        .baseUri(DELETE_CACHE)
        .header("x-api-key", API_KEY)
        .delete();
      assertThat(responseCache.getStatusCode(), is(204));
    }
  }
}
