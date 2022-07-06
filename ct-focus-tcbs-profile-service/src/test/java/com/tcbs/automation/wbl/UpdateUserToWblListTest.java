package com.tcbs.automation.wbl;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.*;
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
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.ConvertUtils.fileTxtToString;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/wbl/UpdateUserToWblList.csv", separator = '|')
public class UpdateUserToWblListTest {
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
  private String wblUserId;

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

    if (testCaseName.contains("case idNumber") || testCaseName.contains("param idNumber")) {
      idNumber = syncData(identifications);
    } else {
      idNumber = prepareValue.substring(1);
    }

    if (testCaseName.contains("case for valid request")) {
      // Add user to wbl list
      CallApiUtils.callPrepareUserToWblListApi();
      BigDecimal wblUserIdDecimal = WblUser.getByIdNumber("010195857978").getId();
      wblUserId = wblUserIdDecimal.toString();
      List<WblUserIdentification> wblUserIdentifications = WblUserIdentification.getByWblUserId(wblUserIdDecimal);
      BigDecimal identifiedId1 = wblUserIdentifications.get(0).getId();
      BigDecimal identifiedId2 = wblUserIdentifications.get(1).getId();
      List<WblPolicyUser> wblPolicyUsers = WblPolicyUser.getByWblUserIdAndPolicyId(wblUserIdDecimal, WblPolicy.getByPolicyCode(policyCode).getId());
      BigDecimal wblPolicyUserId1 = wblPolicyUsers.get(0).getId();
      BigDecimal wblPolicyUserId2 = wblPolicyUsers.get(1).getId();
      List<WblStakeHolder> wblStakeHolders = WblStakeHolder.getByWblUserId(wblUserIdDecimal);
      BigDecimal wblStakeHolder1 = wblStakeHolders.get(0).getId();
      BigDecimal wblStakeHolder2 = wblStakeHolders.get(1).getId();

      validBody = fileTxtToString("src/test/resources/requestBody/UpdateUserToWblList.json")
        .replaceAll("#identifiedId1#", String.valueOf(identifiedId1))
        .replaceAll("#identifiedId2#", String.valueOf(identifiedId2))
        .replaceAll("#idNumber1#", "2" + idNumber.substring(1))
        .replaceAll("#idNumber2#", "3" + idNumber.substring(1))
        .replaceAll("#policyUserId1#", String.valueOf(wblPolicyUserId1))
        .replaceAll("#policyUserId2#", String.valueOf(wblPolicyUserId2))
        .replaceAll("#idStakeHolder1#", String.valueOf(wblStakeHolder1))
        .replaceAll("#idStakeHolder2#", String.valueOf(wblStakeHolder2));
    } else {
      hashMapBody = CommonUtils.prepareDataAddUserToWbl(testCaseName, prepareValue, idNumber, startDatetime, endDatetime, type,
        policyCode, refIdNumber, fullName, address);
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api update user to wbl list")
  public void perfomTest() {

    System.out.println(testCaseName);
    RequestSpecification requestSpecification = given()
      .baseUri(UPDATE_USER_TO_WBL_LIST.replaceAll("#wblUserId#", wblUserId))
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
        assertThat(wblUser.getFullName(), is(fullName));
        assertThat(wblUser.getAddress(), is(address));
        assertThat(WblUserIdentification.getByWblUserId(wblUser.getId()).get(0).getIdNumber(), is(idNumber));
        if (testCaseName.contains("type as NLQ")) {
          assertThat(wblPolicyUser.getRefUserId(), is(refIdNumber));
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
    if (testCaseName.contains("case for valid request")) {
      CallApiUtils.callDeleteUserFromWblListApi(wblUserId);
    }
  }
}
