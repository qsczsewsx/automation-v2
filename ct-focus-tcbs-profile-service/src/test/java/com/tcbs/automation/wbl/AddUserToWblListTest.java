package com.tcbs.automation.wbl;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.WblPolicy;
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
@UseTestDataFrom(value = "data/wbl/AddUserToWblList.csv", separator = '|')
public class AddUserToWblListTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String type;
  private String policyCode;
  private String identifications;
  private String refIdNumber;
  private String fullName;
  private String address;
  private String startDatetime;
  private String endDatetime;
  private HashMap<String, Object> hashMapBody;
  private String validBody;
  private String idNumber;
  private String prepareValue;

  @Before
  public void setup() {

    type = syncData(type);
    policyCode = syncData(policyCode);
    refIdNumber = syncData(refIdNumber);
    fullName = syncData(fullName);
    address = syncData(address);
    startDatetime = syncData(startDatetime);
    endDatetime = syncData(endDatetime);
    prepareValue = String.valueOf(new Date().getTime());

    if (testCaseName.contains("case idNumber") || testCaseName.contains("param idNumber") || testCaseName.contains("existed in same time")) {
      idNumber = syncData(identifications);
    } else {
      idNumber = prepareValue.substring(1);
    }

    if (testCaseName.contains("case for valid request")) {
      validBody = fileTxtToString("src/test/resources/requestBody/AddUserToWblList.json")
        .replaceAll("#idNumber1#", idNumber)
        .replaceAll("#idNumber2#", "2" + idNumber.substring(1));
    } else {
      hashMapBody = CommonUtils.prepareDataAddUserToWbl(testCaseName, prepareValue, idNumber, startDatetime, endDatetime, type,
        policyCode, refIdNumber, fullName, address);
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api add user to wbl list")
  public void perfomTest() {

    System.out.println(testCaseName);
    RequestSpecification requestSpecification = given()
      .baseUri(ADD_USER_TO_WBL_LIST)
      .contentType("application/json")
      .header("Authorization", "Bearer " +
        (testCaseName.contains("user has no permission") ? TCBSPROFILE_AUTHORIZATION : TCBSPROFILE_SPECIALWBLKEY));

    Response response;

    if (testCaseName.contains("missing BODY")) {
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
        WblPolicyUser wblPolicyUser = WblPolicyUser.getByWblUserIdAndPolicyId(wblUser.getId(), WblPolicy.getByPolicyCode(policyCode).getId()).get(0);
        assertThat(wblPolicyUser.getType(), is(type));
        assertThat(wblPolicyUser.getStartDatetime(), is(Timestamp.valueOf(
          LocalDateTime.parse(startDatetime,
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")))));
        assertThat(wblPolicyUser.getEndDatetime(), is(Timestamp.valueOf(
          LocalDateTime.parse(endDatetime,
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")))));
        assertThat(wblUser.getFullName(), is(fullName));
        assertThat(wblUser.getAddress(), is(address));
        assertThat(WblUserIdentification.getByWblUserId(wblUser.getId()).get(0).getIdNumber(), is(idNumber));
        if (testCaseName.contains("type as NLQ")) {
          assertThat(WblUserIdentification.getByWblUserId(wblPolicyUser.getRefWbluserId()).get(0).getIdNumber(), is(refIdNumber));
        }
      }
    } else if (statusCode == 403) {
      assertThat("verify error message", response.jsonPath().get("errorMessage"), is(errorMessage));
    } else {
      assertThat("verify error message", response.jsonPath().get("message"), is(containsString(errorMessage)));
    }
  }

  @After
  public void clearData() {
    if (statusCode == 200) {
      CallApiUtils.callDeleteUserFromWblListApi(WblUserIdentification.getByIdNumber(idNumber).getWbluserId().toString());
    }
  }
}
