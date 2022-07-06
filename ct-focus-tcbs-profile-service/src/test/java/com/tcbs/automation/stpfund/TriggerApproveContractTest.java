package com.tcbs.automation.stpfund;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.cas.TcbsApplicationUser;
import com.tcbs.automation.cas.VsdTransaction;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.ASSIGN_TASK_TO_MAKER_KEY;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.SUPPORT_BY_ACTION;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/stpfund/TriggerApproveContract.csv", separator = '|')
public class TriggerApproveContractTest {
  private String testcaseName;
  private String data;
  private String subType;
  private String stpMode;

  @Before
  public void setup() {
    if ("OFF".equalsIgnoreCase(stpMode)) {
      ApiRetrySTPTransactionTest.setStpMode("OFF");
    } else {
      ApiRetrySTPTransactionTest.setStpMode("ON");
    }

    callApiSupportByAction("UPDATE_NEW_ONBOARDING_STATUS", "{ " + data + ": { ECONTRACT_STATUS: VERIFIED } }");
    List<String> list105C = new ArrayList<>(Arrays.asList(data));
    TcbsApplicationUser.updateStatusAppByList(list105C, "4", "0");
    VsdTransaction.deleteByList(list105C);
  }

  @Test
  @TestCase(name = "#testcaseName#")
  @Title("Verify trigger approve contract")
  public void triggerApproveContract() throws InterruptedException {
    System.out.println("Testcase Name: " + testcaseName);

    callApiSupportByAction("COMPLETE_ONLINE_AUTHENTICATION", data);

    Thread.sleep(120000);

    List<TcbsApplicationUser> listUserApp = TcbsApplicationUser.getByUserAppId(data, "4");
    assertNotEquals(BigDecimal.valueOf(0), listUserApp.get(0).getStatus());
    VsdTransaction vsdTransaction = VsdTransaction.getLastBy105C(data);
    assertEquals(vsdTransaction.getUpdatedBy(), "SYSTEM");
    if ("OFF".equalsIgnoreCase(stpMode)) {
      assertEquals(vsdTransaction.getChannel(), "MANUAL");
      assertEquals(vsdTransaction.getStatus(), BigDecimal.valueOf(0));
      assertEquals(vsdTransaction.getStep(), "PROFILE");
    } else {
      assertEquals(vsdTransaction.getChannel(), "STP");
      ApiRetrySTPTransactionTest.verifyRetryTransactionDependOnVsd(vsdTransaction.getId().intValue(), vsdTransaction.getUserId().toString());
    }
  }

  public static void callApiSupportByAction(String action, String data) {
    LinkedHashMap<String, Object> bodyRetry = new LinkedHashMap<>();
    Gson gson = new Gson();
    bodyRetry.put("action", action);
    bodyRetry.put("data", data);
    given()
      .baseUri(SUPPORT_BY_ACTION)
      .header("Authorization", "Bearer " + ASSIGN_TASK_TO_MAKER_KEY)
      .contentType("application/json")
      .body(gson.toJson(bodyRetry))
      .when()
      .post();
  }
}
