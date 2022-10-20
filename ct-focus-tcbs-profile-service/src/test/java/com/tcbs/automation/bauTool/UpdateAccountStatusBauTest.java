package com.tcbs.automation.bauTool;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsUser;
import common.CallApiUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/bauTool/UpdateAccountStatusBau.csv", separator = '|')
public class UpdateAccountStatusBauTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String actionKey;
  private String data;
  private String username;
  private String accountStatus;
  private String note;
  private HashMap<String, Object> body;
  Map<String, String> statusMap;
  private TcbsUser tcbsUser;
  private final String VALUE_UPDATE = "1";

  @Before
  public void setup() {
    actionKey = syncData(actionKey);
    data = syncData(data);
    username = syncData(username);
    accountStatus = syncData(accountStatus);
    note = syncData(note);
    errorMessage = syncData(errorMessage);

    String str = "{\"username\":\"#username#\", \"status\":\"#accountStatus#\"}";

    if (!data.isEmpty()) {
      data = str.replace("#username#", username).replace("#accountStatus#", accountStatus);
    }

    body = new HashMap<>();
    body.put("actionKey", actionKey);
    body.put("data", data);
    body.put("note", note);

    statusMap = new HashMap<String, String>() {{
      put("ACTIVE", VALUE_UPDATE);
      put("INACTIVE", "-1");
      put("LOCK", "0");
      put("BLOCK", "-2");
      put("EXCEPTION", "2");
      put("LOCK_WAITING_TO_CLOSE", "3");
    }};
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api update customer type")
  public void performTest() {

    System.out.println("Test case name: " + testCaseName);

    RequestSpecification requestSpecification = given()
      .baseUri(ACTION_TOOL_BAU)
      .header("Authorization", "Bearer " +
        (testCaseName.contains("no permission") ? TCBSPROFILE_AUTHORIZATION : BAU_AUTHORIZATION_TOKEN));

    Response response;

    if (testCaseName.contains("missing BODY")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(body).post();
    }

    assertThat("verify status code", response.getStatusCode(), is(statusCode));

    if (testCaseName.contains("case valid")) {
      tcbsUser = TcbsUser.getByUserName(username);
      assertThat("verify status", tcbsUser.getAccountStatus().toString(), is(statusMap.get(accountStatus)));
      if (testCaseName.contains("to LOCK_WAITING_TO_CLOSE")) {
        assertThat(tcbsUser.getTransferStatus().toString(), is("0"));
      } else if (testCaseName.contains("to ACTIVE")) {
        assertThat(tcbsUser.getSignCloseContract(), is(nullValue()));
      }
    } else if (statusCode == 403) {
      assertThat("verify error message", response.jsonPath().get("errorMessage"), is(errorMessage));
    } else {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }

  @After
  public void teardown() {
    if (testCaseName.contains("to LOCK_WAITING_TO_CLOSE")) {
      TcbsUser.updateTransferStatusById(tcbsUser.getId().toString(), VALUE_UPDATE);
    } else if (testCaseName.contains("to ACTIVE")) {
      TcbsUser.updateSignCloseContract(username, VALUE_UPDATE);
    } else if (testCaseName.contains("to BLOCK")) {
      TcbsUser.updateAccountStatus(username, VALUE_UPDATE);
    }
    CallApiUtils.clearCache(DELETE_CACHE, "x-api-key", API_KEY);
  }
}