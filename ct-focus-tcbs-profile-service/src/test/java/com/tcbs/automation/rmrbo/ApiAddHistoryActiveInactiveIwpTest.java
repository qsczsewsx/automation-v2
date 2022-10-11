package com.tcbs.automation.rmrbo;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.cas.R3RdAuditLog;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/rmrbo/ApiAddHistoryActiveInactiveIwp.csv", separator = '|')
public class ApiAddHistoryActiveInactiveIwpTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String code105c;
  private String status;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify API add history active inactive iwp")
  public void addHistoryActiveInactiveIwp() {

    code105c = syncData(code105c);
    List<String> listCode105c = new ArrayList<>(Arrays.asList(code105c.split(",")));
    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    body.put("code105c", listCode105c);
    body.put("status", status);

    System.out.println("Test Case: " + testCaseName);
    RequestSpecification requestSpecification = given()
      .baseUri(ADD_HISTORY_ACTIVE_INACTIVE_IWP)
      .header("x-api-key", testCaseName.contains("invalid x-api-key") ? FMB_X_API_KEY : PARTNERSHIP_X_API_KEY)
      .contentType("application/json");

    Response response;
    Gson gson = new Gson();
    if (testCaseName.contains("missing body")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(gson.toJson(body)).post();
    }

    assertEquals(statusCode, response.getStatusCode());
    if (statusCode == 200) {
      if (testCaseName.contains("valid information")) {
        assertThat("verify", R3RdAuditLog.getHistoryByTcbsId("0001000003"), is(notNullValue()));
      }
    } else {
      assertEquals(errorMessage, response.jsonPath().get("message"));
    }
  }
}


