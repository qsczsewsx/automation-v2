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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/iactg/UpdateIaTci3Data.csv")
public class UpdateIaTci3Test {
  private static String token;
  private static TcbsUser tcbsUser;
  private String testCaseName;
  private String tcbsId;
  private String accountNo;
  private String bankSource;
  private int statusCode;
  private String errorCode;
  private String autoTransfer;
  private String isIAPaid;
  private String body;

  @BeforeClass
  public static void beforeClass() throws Exception {
    LoginData loginData = LoginUtil.login("105C007122", "abc123");
    token = loginData.getToken();

    tcbsUser = TcbsUser.getByTcbsId("10000022726");
    TcbsBankIaaccount.updateStatusById(TcbsBankIaaccount.getListBanks(tcbsUser.getId().toString()).get(0).getId(), "1");
    CallApiUtils.clearCache(DELETE_CACHE, "x-api-key", API_KEY);
  }

  @Before
  public void before() {
    accountNo = syncData(accountNo);
    bankSource = syncData(bankSource);
    autoTransfer = syncData(autoTransfer);
    isIAPaid = syncData(isIAPaid);

    Map<String, Object> request = new HashMap<>();

    Map<String, Object> iaBankAccount = new HashMap<>();
    iaBankAccount.put("accountNo", accountNo);
    iaBankAccount.put("bankSource", bankSource);
    Map<String, Object> iaStatus = new HashMap<>();
    iaStatus.put("autoTransfer", autoTransfer);
    iaStatus.put("isIAPaid", isIAPaid);
    Map<String, Object> accountStatus = new HashMap<>();
    accountStatus.put("iaStatus", iaStatus);

    request.put("accountStatus", accountStatus);
    request.put("iaBankAccount", iaBankAccount);

    Gson gson = new Gson();
    body = gson.toJson(request);
  }

  @Test
  @TestCase(name = "#testCaseName")
  public void verifyApiDisconnectIaTci3() {
    Response response = given()
      .baseUri(UPDATE_IA_TCI3 + "/" + tcbsId)
      .header("Authorization", "Bearer " + token)
      .contentType("application/json")
      .body(body)
      .post();

    assertThat("Verify status code", response.statusCode(), is(statusCode));
    if (statusCode == 200) {
      List<TcbsBankIaaccount> bankIaaccountList = TcbsBankIaaccount.getListBanks(tcbsUser.getId().toString());
      assertThat("verify data in IA account table",
        bankIaaccountList.get(0),
        hasProperty("status", is(BigDecimal.valueOf(5)))
      );
    }
    if (statusCode == 400) {
      assertThat("Verify error code", response.jsonPath().get("code"), is(errorCode));
    }
  }
}
