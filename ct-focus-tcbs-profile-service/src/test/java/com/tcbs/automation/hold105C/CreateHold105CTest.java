package com.tcbs.automation.hold105C;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsFancy105C;
import com.tcbs.automation.cas.TcbsUser;
import com.tcbs.automation.cas.TcbsUserCampaignHold105C;
import common.CommonUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/hold105C/CreateHold105C.csv", separator = '|')
public class CreateHold105CTest {

  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String username;
  private String strCode105C;
  private String token;
  List<HashMap<String, Object>> body;
  private static final String CODE105C = "code105C";

  @Before
  public void before() {
    token = CommonUtils.getToken(username);
    strCode105C = syncData(strCode105C);
    body = new ArrayList<>();
    HashMap<String, Object> code105CFirst = new HashMap<>();
    HashMap<String, Object> code105CSecond = new HashMap<>();
    if (strCode105C.equalsIgnoreCase("")) {
      code105CFirst.put(CODE105C, "");
      code105CSecond.put(CODE105C, "");
    } else {
      List<String> listCode105C = new ArrayList<>(Arrays.asList(strCode105C.split(",")));
      code105CFirst.put(CODE105C, listCode105C.get(0));
      code105CSecond.put(CODE105C, listCode105C.get(1));
    }
    body.add(code105CFirst);
    body.add(code105CSecond);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api create hold 105C")
  public void verifyCreateHold105CTest() {

    System.out.println("TestCaseName : " + testCaseName);

    RequestSpecification requestSpecification = given()
      .baseUri(CREATE_HOLD_105C)
      .header("Authorization", "Bearer " + token)
      .contentType("application/json");

    Response response;

    if (testCaseName.contains("missing BODY")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(body).post();
    }

    assertThat(response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      assertThat("verify hold105C", response.jsonPath().get("hold105C"), is(true));
      assertThat("verify status", response.jsonPath().get("status"), is(1));
      assertThat("verify total", response.jsonPath().get("total"), is(2));
    } else if (statusCode == 400) {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }

  @After
  public void tearDown() {
    // Clear data
    if (statusCode == 200) {
      String phoneNumber = TcbsUser.getByUserName(username).getPhone();
      TcbsUserCampaignHold105C.updateStatus(phoneNumber, "0");
      TcbsFancy105C.deleteByOwnerBy(phoneNumber);
      Response responseCache = given()
        .baseUri(DELETE_CACHE)
        .header("x-api-key", API_KEY)
        .delete();
      MatcherAssert.assertThat(responseCache.getStatusCode(), is(204));
    }
  }

}