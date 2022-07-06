package com.tcbs.automation.stpfund;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.cas.VsdTransaction;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.LinkedHashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/stpfund/ApiSendCloseAccountSTPFund.csv", separator = '|')
public class ApiSendCloseAccountSTPFundTest {
  @Getter
  private String testcaseName;
  private String tcbsId;
  private int statusCode;
  private String errorMessage;
  private String product;
  private String type;

  @Test
  @TestCase(name = "#testcaseName")
  @Title("Verify API send vsd close account stpfund")
  public void SendCloseAccountSTPFundTest() {
    System.out.println("Testcase Name: " + testcaseName);

    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    body.put("product", product);
    body.put("tcbsId", tcbsId);
    body.put("type", type);

    RequestSpecification requestSpecification = given()
      .baseUri(STP_FUND_CLOSE_ACCOUNT)
      .header("x-api-key", testcaseName.contains("invalid x-api-key") ? STP_AUTHORIZATION_KEY : STP_X_API_KEY)
      .contentType("application/json");

    Response response;
    Gson gson = new Gson();
    if (testcaseName.contains("missing body")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(gson.toJson(body)).post();
    }

    assertEquals(statusCode, response.getStatusCode());
    if (statusCode == 200) {
      assertThat("verify type account", VsdTransaction.getByTcbsId(tcbsId), is(notNullValue()));
    } else {
      assertEquals(errorMessage, response.jsonPath().get("message"));
    }
  }

  @After
  public void teardown() {
    VsdTransaction.deleteByUserId(Collections.singletonList(tcbsId));
  }
}


