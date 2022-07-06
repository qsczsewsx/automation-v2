package com.tcbs.automation.iactg;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.cas.TcbsBankIaaccount;
import com.tcbs.automation.cas.TcbsUser;
import com.tcbs.automation.login.LoginData;
import com.tcbs.automation.tools.LoginUtil;
import common.CallApiUtils;
import io.restassured.response.Response;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.publicmatcher.HashMapMatcher.has;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/iactg/DisconnectIaTci3Data.csv", separator = '|')
public class DisconnectIaTci3Test {
  private static String token;
  private String testCaseName;
  private String tcbsId;
  private String accountNo;
  private String bankSource;
  private int statusCode;
  private String errorCode;
  private String body;

  @BeforeClass
  public static void beforeClass() throws Exception {
    LoginData loginData = LoginUtil.login("105C200341", "abc123");
    token = loginData.getToken();

    TcbsUser tcbsUser = TcbsUser.getByTcbsId("10000021932");
    TcbsBankIaaccount.updateStatusById(TcbsBankIaaccount.getListBanks(tcbsUser.getId().toString()).get(0).getId(), "1");
    CallApiUtils.clearCache(DELETE_CACHE, "x-api-key", API_KEY);
  }

  @Before
  public void before() {
    accountNo = syncData(accountNo);
    bankSource = syncData(bankSource);

    Map<String, Object> request = new HashMap<>();
    Map<String, Object> iaBankAccount = new HashMap<>();
    iaBankAccount.put("accountNo", accountNo);
    iaBankAccount.put("bankSource", bankSource);
    request.put("iaBankAccount", iaBankAccount);

    Gson gson = new Gson();
    body = gson.toJson(request);
  }

  @Test
  @TestCase(name = "#testCaseName")
  public void verifyApiDisconnectIaTci3() {
    Response response = given()
      .baseUri(DISCONNECT_IA_TCI3 + "/" + tcbsId)
      .header("Authorization", "Bearer " + token)
      .contentType("application/json")
      .body(body)
      .post();

    assertThat("Verify status code", response.statusCode(), is(statusCode));
    if (statusCode == 400) {
      assertThat("Verify error code", response.jsonPath().get(),
        has("code", is(errorCode)));
    }
  }
}
