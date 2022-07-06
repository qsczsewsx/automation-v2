package com.tcbs.automation.stpfund;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.cas.TcbsApplicationUser;
import com.tcbs.automation.cas.VsdTransaction;
import com.tcbs.automation.tools.StringUtils;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.junit.Assert.assertEquals;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/stpfund/CancelSTPTransaction.csv", separator = '|')
public class ApiCancelSTPTransactionTest {

  @Getter
  private String testcaseName;
  private String id;
  private Integer status;
  private String note;
  private int statusCode;
  private String errorMsg;
  private String userId;

  @Before
  public void setup() {
    if (Objects.nonNull(status)) {
      List<Integer> listId = Collections.singletonList(Integer.valueOf(id));
      VsdTransaction.updateStatusByListIds(listId, status);
    }
  }

  @Test
  @TestCase(name = "#testcaseName")
  @Title("Verify API cancel STP Transaction")
  public void CancelTransactionTest() {
    int oldFundStatus = 0;
    if (StringUtils.isInteger(id)) {
      VsdTransaction vsdTransaction = VsdTransaction.getById(id);
      TcbsApplicationUser appUser = TcbsApplicationUser.getByTcbsApplicationUserAppId2(vsdTransaction.getUserId().toString(), "4");
      oldFundStatus = appUser.getStatus().intValue();
    }

    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    body.put("id", id);
    body.put("note", note);

    System.out.println("Testcase Name: " + testcaseName);
    Gson gson = new Gson();
    Response response = given()
      .baseUri(CANCEL_STP_TRANSACTION)
      .header("Authorization", "Bearer " +
        (testcaseName.contains("Authorization key") ? FMB_X_API_KEY : STP_AUTHORIZATION_KEY))
      .contentType("application/json")
      .body(gson.toJson(body))
      .when()
      .post();

    assertEquals(statusCode, response.getStatusCode());
    if (statusCode == 403) {
      assertEquals(errorMsg, response.jsonPath().get("errorMessage"));
    } else if (statusCode == 400) {
      assertEquals(errorMsg, response.jsonPath().get("message"));
    } else {
      verifySuccessCase(oldFundStatus);
    }
  }

  private void verifySuccessCase(int oldFundStatus) {
    VsdTransaction vsdTransaction = VsdTransaction.getById(id);
    assertEquals(BigDecimal.valueOf(4), vsdTransaction.getStatus());
    if (!testcaseName.contains("note is empty")) {
      assertEquals(note, vsdTransaction.getNote());
    } else {
      assertEquals(null, vsdTransaction.getNote());
    }
    TcbsApplicationUser appUser = TcbsApplicationUser.getByTcbsApplicationUserAppId2(vsdTransaction.getUserId().toString(), "4");
    if (oldFundStatus == 1) {
      assertEquals(BigDecimal.valueOf(1), appUser.getStatus());
    } else {
      assertEquals(BigDecimal.valueOf(0), appUser.getStatus());
    }
  }
}
