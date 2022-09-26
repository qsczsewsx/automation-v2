package com.tcbs.automation.toolBau;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsUser;
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

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/toolBau/UpdateCustomerTypeBau.csv", separator = '|')
public class UpdateCustomerTypeBauTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String actionKey;
  private String data;
  private String username;
  private String cusType;
  private String note;
  private HashMap<String, Object> body;


  @Before
  public void setup() {
    actionKey = syncData(actionKey);
    data = syncData(data);
    username = syncData(username);
    cusType = syncData(cusType);
    note = syncData(note);

    String str = "{\"username\":\"#username#\", \"cusType\":#cusType#}";

    if (!data.isEmpty()) {
      data = str.replace("#username#", username).replace("#cusType#", cusType);
    }

    body = new HashMap<>();
    body.put("actionKey", actionKey);
    body.put("data", data);
    body.put("note", note);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api update customer type")
  public void performTest() {

    System.out.println("Test case name: " + testCaseName);

    RequestSpecification requestSpecification = given()
      .baseUri(UPDATE_CUSTOMER_TYPE)
      .header("Authorization", "Bearer " +
        (testCaseName.contains("no permission") ? TCBSPROFILE_AUTHORIZATION : BACK_TOOL_BAU_KEY));

    Response response;

    if (testCaseName.contains("missing BODY")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(body).post();
    }

    assertThat("verify status code", response.getStatusCode(), is(statusCode));

    if (testCaseName.contains("case valid")) {
      assertThat("verify status", TcbsUser.getByUserName(username).getCustype().toString(), is("1"));
    } else if (statusCode == 403) {
      assertThat("verify error message", response.jsonPath().get("errorMessage"), is(errorMessage));
    } else {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }

  @After
  public void teardown() {
    if (testCaseName.contains("case valid")) {
      TcbsUser.updateCusType("0", username);
    }
  }
}