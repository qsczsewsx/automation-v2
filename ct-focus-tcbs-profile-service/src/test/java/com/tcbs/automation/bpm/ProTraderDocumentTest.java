package com.tcbs.automation.bpm;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.API_KEY;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.PRO_TRADER_DOCUMENT;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/bpm/ProTraderDocument.csv", separator = '|')
public class ProTraderDocumentTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String tcbsId;
  private String errorMessage;
  private String documentType;
  private List<HashMap<String, Object>> bodyList;
  private HashMap<String, Object> body;

  @Before
  public void setup() {

    tcbsId = syncData(tcbsId);
    documentType = syncData(documentType);

    HashMap<String, Object> body = new HashMap<>();
    body.put("documentType", documentType);
    body.put("paperType", null);
    body.put("startDate", "2021-11-09");
    body.put("endDate", "2022-11-09");

    bodyList = new ArrayList<>();
    bodyList.add(body);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api pro trader document")
  public void performTest() {

    RequestSpecification requestSpecification = given()
      .baseUri(PRO_TRADER_DOCUMENT.replace("#tcbsId#", tcbsId))
      .contentType("application/json")
      .header("x-api-key", API_KEY);
    Response response;

    Gson gson = new Gson();

    if (testCaseName.contains("missing BODY")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(gson.toJson(bodyList)).post();
    }

    assertThat("verify status", response.getStatusCode(), is(statusCode));
    assertThat("verify error message", response.jsonPath().get("message"), is(containsString(errorMessage)));

  }
}