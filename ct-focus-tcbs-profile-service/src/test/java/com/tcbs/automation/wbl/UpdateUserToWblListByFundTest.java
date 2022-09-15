package com.tcbs.automation.wbl;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.WblPolicyUser;
import com.tcbs.automation.cas.WblUser;
import com.tcbs.automation.cas.WblUserIdentification;
import common.CallApiUtils;
import common.CommonUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.ConvertUtils.fileTxtToString;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/wbl/UpdateUserToWblListByFund.csv", separator = '|')
public class UpdateUserToWblListByFundTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String idNumber;
  private String actor;
  private String fundCode;
  private String note;
  private String fullName;
  private String address;
  private String startDatetime;
  private String endDatetime;
  private HashMap<String, Object> hashMapBody;
  private String prepareValue;
  private String wblUserId;
  private String validBody;

  @Before
  public void setup() {
    actor = syncData(actor);
    fundCode = syncData(fundCode);
    note = syncData(note);
    fullName = syncData(fullName);
    address = syncData(address);
    startDatetime = syncData(startDatetime);
    endDatetime = syncData(endDatetime);
    prepareValue = String.valueOf(new Date().getTime());

    if (idNumber.equalsIgnoreCase("idNumber")) {
      idNumber = prepareValue.substring(1);
    } else {
      idNumber = syncData(idNumber);
    }

    if (testCaseName.contains("case for valid request")) {
      // Add user to wbl list
      CallApiUtils.callPrepareUserToWblListFundApi();
      BigDecimal wblUserIdDecimal = WblUser.getByIdNumber("1505101997").getId();
      wblUserId = wblUserIdDecimal.toString();

      validBody = fileTxtToString("src/test/resources/requestBody/UpdateUserToWblListFund.json")
        .replaceAll("#wblUserId1#", String.valueOf(wblUserIdDecimal))
        .replaceAll("#idNumber1#", "2" + idNumber.substring(1));

    } else {
      hashMapBody = CommonUtils.prepareDataUpdateUserToWblByFund(fullName, address, idNumber, fundCode,
        note, actor, startDatetime, endDatetime, wblUserId);
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api update user to wbl list")
  public void updateUserWblFund() {

    System.out.println(testCaseName);
    RequestSpecification requestSpecification = given()
      .baseUri(UPDATE_USER_TO_WBL_LIST_FUND)
      .contentType("application/json")
      .header("x-api-key", (testCaseName.contains("invalid x-api-key") ? FMB_X_API_KEY : STP_X_API_KEY));

    Response response;

    if (testCaseName.contains("missing body")) {
      response = requestSpecification.post();
    } else if (testCaseName.contains("case for valid request")) {
      response = requestSpecification.body(validBody).post();
    } else {
      response = requestSpecification.body(hashMapBody).post();
    }

    assertThat("verify status code", response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      if (!testCaseName.contains("case for valid request")) {
        WblUser wblUser = WblUser.getByIdNumber(idNumber);
        assertThat(wblUser.getFullName(), is(fullName));
        assertThat(wblUser.getAddress(), is(address));
        assertThat(WblUserIdentification.getByWblUserId(wblUser.getId()).get(0).getIdNumber(), is(idNumber));
      }
    } else {
      assertThat("verify error message", response.jsonPath().get("message"), is(containsString(errorMessage)));
    }
  }

  @After
  public void clearData() {
    if (testCaseName.contains("case for valid request")) {
      CallApiUtils.callDeleteUserFromWblByFundApi(wblUserId);
    }
  }
}
