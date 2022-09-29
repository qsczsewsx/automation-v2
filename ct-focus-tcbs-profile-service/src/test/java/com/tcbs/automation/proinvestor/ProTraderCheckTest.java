package com.tcbs.automation.proinvestor;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsUser;
import common.CommonUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.PRO_TRADER_CHECK;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/proinvestor/ProTraderCheck.csv", separator = '|')
public class ProTraderCheckTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String username;
  private String date;
  private String type;
  private String identityNo;
  private String token;
  private HashMap<String, Object> body;

  @Before
  public void before() {
    token = CommonUtils.getToken(username);
    date = syncData(date);
    type = syncData(type);
    identityNo = syncData(identityNo);

    body = new HashMap<>();
    if (identityNo.equalsIgnoreCase("")) {
      String tcbsId;
      if (testCaseName.contains("tcbsId do not match token")) {
        tcbsId = "10000025749";
      } else {
        tcbsId = TcbsUser.getByUserName(username).getTcbsid();
      }
      body.put("tcbsId", tcbsId);
    } else {
      body.put("identityNo", identityNo);
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api pro Trader check")
  public void performTest() {
    RequestSpecification requestSpecification = given()
      .baseUri(PRO_TRADER_CHECK)
      .contentType("application/json")
      .header("Authorization", "Bearer " + token);

    if (!testCaseName.contains("param date and type")) {
      Map<String, Object> params = new HashMap<>();
      params.put("date", date);
      params.put("type", type);
      requestSpecification.queryParams(params);
    }

    Response response;

    if (testCaseName.contains("missing BODY")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(body).post();
    }

    assertThat("verify status code", response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      String str = "custType,isProTrader,hnxAccount,threshold,totalAssets,deficitAmount,expireDate";
      Map<String, Object> result = response.jsonPath().get();
      for (String item : str.split(",", -1)) {
        assertThat(result.keySet(), hasItem(item));
      }
      assertThat(result.get("isProTrader"), anyOf(is("0"), is("1")));
      if (result.get("isProTrader").equals("1")) {
        List<String> proTraderType = response.jsonPath().get("proTraderType");
        assertThat(proTraderType.size(), greaterThan(0));
      }
    } else if (statusCode == 400) {
      assertThat("verify error message", response.jsonPath().get("message"), containsString(errorMessage));
    }
  }
}