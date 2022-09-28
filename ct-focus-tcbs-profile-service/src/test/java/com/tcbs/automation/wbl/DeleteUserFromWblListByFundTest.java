package com.tcbs.automation.wbl;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.WblPolicyUser;
import com.tcbs.automation.cas.WblUser;
import common.CallApiUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.*;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/wbl/DeleteUserFromWblListByFund.csv", separator = '|')
public class DeleteUserFromWblListByFundTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String wblUserId;
  private String prepareValue;
  private String idNumber;
  private String actor;
  private HashMap<String, Object> body;
  private static final String DTWBLUSERID = "wblUserId";

  @Before
  public void setup() {
    prepareValue = String.valueOf(new Date().getTime());
    idNumber = prepareValue.substring(1);
    if (testCaseName.contains("fund case valid request")) {
      CallApiUtils.callAddUserToWblListFundApi("Hoài Linh","192 Lê Trọng Tấn, Hà Nội", idNumber, "TCBF", "test",
        "Test", "2021-09-01", "2022-09-30");
      wblUserId = WblUser.getByIdNumber(idNumber).getId().toString();
    } else {
      wblUserId = syncData(wblUserId);
    }
    body = deleteUserBody();
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api delete user from wbl list by fund")
  public void deleteUserFromWblFund() {
    RequestSpecification requestSpecification = given()
      .baseUri(DELETE_USER_FROM_WBL_LIST_FUND)
      .contentType("application/json")
      .header("x-api-key", (testCaseName.contains("invalid x-api-key") ? FMB_X_API_KEY : STP_X_API_KEY));
    Response response;

    if (testCaseName.contains("missing body")) {
      response = requestSpecification.post();
    }else {
      response = requestSpecification.body(body).post();
    }

    assertThat("verify status code", response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      WblPolicyUser wblPolicyUsers = WblPolicyUser.getByWblUserId(new BigDecimal(wblUserId));
      boolean result = false;
      {
        if (wblPolicyUsers == null) {
          result = true;
          assertTrue("verify result delete", result);
        }
      }
    }else {
      assertThat("verify error message", response.jsonPath().get("message"), is(containsString(errorMessage)));
    }
  }

  public HashMap<String, Object> deleteUserBody() {
    List<HashMap<String, Object>> wblUsers = new ArrayList<>();

    HashMap<String, Object> wblUserIdFirst = new HashMap<>();
    HashMap<String, Object> wblUserIdSecond = new HashMap<>();

    if (testCaseName.contains("multi")) {
      List<String> listWblUserId = new ArrayList<>(Arrays.asList(wblUserId.split(",")));
      wblUserIdFirst.put(DTWBLUSERID, listWblUserId.get(0));
      wblUserIdSecond.put(DTWBLUSERID, listWblUserId.get(1));
      wblUsers.add(wblUserIdSecond);
    } else {
      wblUserIdFirst.put(DTWBLUSERID, wblUserId);
    }
    wblUsers.add(wblUserIdFirst);

    HashMap<String, Object> body = new HashMap<>();
    body.put("actor",actor);
    body.put("wblUsers", wblUsers);

    return body;
  }
}
