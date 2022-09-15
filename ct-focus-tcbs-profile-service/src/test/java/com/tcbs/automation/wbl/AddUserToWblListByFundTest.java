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

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
@UseTestDataFrom(value = "data/wbl/AddUserToWblListByFund.csv", separator = '|')
public class AddUserToWblListByFundTest {
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
  private String validBody;
  private String prepareValue;

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

    if (testCaseName.contains("by fund with valid request")) {
      validBody = fileTxtToString("src/test/resources/requestBody/AddUserToWblListByFund.json")
        .replaceAll("#idNumber1#", idNumber)
        .replaceAll("#idNumber2#", "2" + idNumber.substring(1));
    } else {
      hashMapBody = CommonUtils.prepareDataAddUserToWblByFund(fullName, address, idNumber, fundCode,
        note, actor, startDatetime, endDatetime);
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api add user to wbl list by Fund")
  public void addWblUserByFund() {

    System.out.println(testCaseName);
    RequestSpecification requestSpecification = given()
      .baseUri(ADD_USER_TO_WBL_LIST_FUND)
      .contentType("application/json")
      .header("x-api-key", (testCaseName.contains("invalid x-api-key") ? FMB_X_API_KEY : STP_X_API_KEY));

    Response response;

    if (testCaseName.contains("missing body")) {
      response = requestSpecification.post();
    } else if (testCaseName.contains("by fund with valid request")) {
      response = requestSpecification.body(validBody).post();
    } else {
      response = requestSpecification.body(hashMapBody).post();
    }
    assertThat("verify status code", response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      if (!testCaseName.contains("by fund with valid request")) {
        WblUser wblUser = WblUser.getByIdNumber(idNumber);
        WblPolicyUser wblPolicyUser = WblPolicyUser.getByWblUserId(wblUser.getId());
        assertThat(wblPolicyUser.getStartDatetime(), is(Timestamp.valueOf(
          LocalDateTime.parse(startDatetime + " 00:00",
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))));
        assertThat(wblPolicyUser.getEndDatetime(), is(Timestamp.valueOf(
          LocalDateTime.parse(endDatetime + " 00:00",
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))));
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
    if (statusCode == 200) {
      CallApiUtils.callDeleteUserFromWblByFundApi(WblUserIdentification.getByIdNumber(idNumber).getWbluserId().toString());
    }
  }
}
