package com.tcbs.automation.iactg;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import io.restassured.response.Response;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.API_KEY;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.INQUIRY_DISCONNECT_IA_CTG;
import static com.tcbs.automation.publicmatcher.CombinableMatcher.all;
import static com.tcbs.automation.publicmatcher.HashMapMatcher.has;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/iactg/UnregisterIAFromCtgData.csv", separator = '|')
public class UnregisterIAFromCtgTest {
  private String testCaseName;
  private String account;
  private String accountName;
  private String bankSource;
  private int statusCode;
  private String messageCode;

  private String body;

  @Before
  public void before() {
    account = syncData(account);
    accountName = syncData(accountName);
    bankSource = syncData(bankSource);

    Map<String, Object> data = new HashMap<>();
    data.put("code105C", account);
    data.put("accountName", accountName);
    List<Map<String, Object>> listData = new ArrayList<>();
    listData.add(data);

    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("bankSource", bankSource);
    requestBody.put("request", listData);

    Gson gson = new Gson();
    body = gson.toJson(requestBody);
  }

  @Test
  @TestCase(name = "#testCaseName")
  public void verifyApiInquiryIAStatusBeforeUnregister() {
    Response response = given()
      .baseUri(INQUIRY_DISCONNECT_IA_CTG)
      .header("Content-Type", "application/json")
      .header("x-api-key", API_KEY)
      .body(body)
      .when()
      .post();

    assertThat("verify status code", response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      assertThat("verify error code",
        response.jsonPath().getList("").get(0),
        all(has("code", is(messageCode)))
          .and(has("message", is(notNullValue())))
      );
    }
  }
}
