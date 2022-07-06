package com.tcbs.automation.stpfund;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.tcbs.automation.cas.TcbsApplicationUser;
import com.tcbs.automation.cas.VsdTransaction;
import com.tcbs.automation.cas.VsdTransactionLog;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Objects;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.ConvertUtils.fileTxtToString;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.junit.Assert.*;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/stpfund/RetrySTPTransaction.csv", separator = '|')
public class ApiRetrySTPTransactionTest {
  @Getter
  private String testcaseName;
  private int id;
  private int statusCode;
  private String stpStatus;
  private String errorMsg;
  private String stpMode;
  private String userId;
  private String getBody;
  private LinkedHashMap<String, Object> body;

  @Before
  public void setup() {
    setStpMode("ON");
    if (stpMode.equalsIgnoreCase("OFF")) {
      setStpMode("OFF");
    }
    if (testcaseName.contains("mode stp fund is off") || testcaseName.contains("mode stp fund is on")) {
      VsdTransaction.updateStatus(userId, stpStatus);
      if (testcaseName.contains("user has been activated")) {
        TcbsApplicationUser.updateStatusApp(userId, "4", "1");
      } else {
        TcbsApplicationUser.updateStatusApp(userId, "4", "2");
      }
    }
  }

  @Test
  @TestCase(name = "#testcaseName")
  @Title("Verify API retry stp transaction")
  public void RetrySTPTransaction() throws JsonProcessingException {
    System.out.println("Testcase Name: " + testcaseName);
    getBody = getBodyRetry();

    Response response = given()
      .baseUri(RETRY_STP_TRANSACTION.replace("{id}", String.valueOf(id)))
      .header("Authorization", "Bearer " +
        (testcaseName.contains("Authorization key") ? FMB_X_API_KEY : STP_AUTHORIZATION_KEY))
      .contentType("application/json")
      .body(getBody)
      .when()
      .post();

    if (!testcaseName.contains("depend on VSD")) {
      assertEquals(statusCode, response.getStatusCode());
    }
    if (statusCode == 403) {
      assertEquals(errorMsg, response.jsonPath().get("errorMessage"));
    } else if (statusCode == 400) {
      assertEquals(errorMsg, response.jsonPath().get("message"));
    } else if (testcaseName.contains("user has been activated")) {
      VsdTransaction vtpTransaction = VsdTransaction.getById(String.valueOf(id));
      assertEquals(BigDecimal.valueOf(4), vtpTransaction.getStatus());
    } else {
      verifyRetryTransactionDependOnVsd(id, userId);
    }
  }

  public static boolean verifyRetryTransactionDependOnVsd(int id, String userId) {
    boolean isSuccess = false;
    VsdTransaction vsdTransaction = VsdTransaction.getById(String.valueOf(id));
    String transactionCode = vsdTransaction.getTransactionCode();
    if (Objects.nonNull(transactionCode)) {
      VsdTransactionLog vsdTransactionLog = VsdTransactionLog.getByTransactionCode(transactionCode);
      if ("success".equals(vsdTransactionLog.getSendStatus())) {
        assertEquals(BigDecimal.valueOf(2), vsdTransaction.getStatus());
        assertEquals("VSD_TRANS", vsdTransaction.getStep());
        assertNotNull(vsdTransaction.getPayload());
        assertNull(vsdTransaction.getErrType());
        assertNull(vsdTransaction.getErrCode());
        assertNull(vsdTransaction.getErrMsg());
        TcbsApplicationUser tcbsApplicationUser = TcbsApplicationUser.getByTcbsApplicationUserAppId2(userId, "4");
        assertEquals(BigDecimal.valueOf(3), tcbsApplicationUser.getStatus());
        isSuccess = true;
      } else {
        assertEquals(BigDecimal.valueOf(0), vsdTransaction.getStatus());
        assertEquals("MIX", vsdTransaction.getStep());
        assertNotNull(vsdTransaction.getPayload());
        assertEquals(BigDecimal.valueOf(0), vsdTransaction.getErrType());
        assertNotNull(vsdTransaction.getErrCode());
        assertNotNull(vsdTransaction.getErrMsg());
      }
    } else {
      assertEquals(BigDecimal.valueOf(0), vsdTransaction.getStatus());
      assertEquals("PROFILE", vsdTransaction.getStep());
      assertEquals(BigDecimal.valueOf(0), vsdTransaction.getErrType());
      assertNotNull(vsdTransaction.getErrCode());
      assertNotNull(vsdTransaction.getErrMsg());
    }

    return isSuccess;
  }

  public static void setStpMode(String stpMode) {
    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    body.put("product", "FUND");
    body.put("mode", stpMode);
    body.put("note", "SET MODE");
    Gson gson = new Gson();

    Response response = given()
      .baseUri(SET_MODE_STP_FUND)
      .header("Authorization", "Bearer " + STP_AUTHORIZATION_KEY)
      .contentType("application/json")
      .body(gson.toJson(body))
      .when()
      .post();

    System.out.println(body);
  }

  private String getBodyRetry() {
    if (testcaseName.contains("change info success")) {
      getBody = fileTxtToString("src/test/resources/requestBody/RetrySTPTransaction");
    } else {
      getBody = fileTxtToString("src/test/resources/requestBody/RetrySTPTransaction1");
    }
    return getBody;
  }
}


