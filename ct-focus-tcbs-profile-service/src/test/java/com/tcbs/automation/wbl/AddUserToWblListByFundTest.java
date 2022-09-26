package com.tcbs.automation.wbl;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcbs.automation.cas.WblPolicyUser;
import com.tcbs.automation.cas.WblUser;
import com.tcbs.automation.cas.WblUserIdentification;
import common.CallApiUtils;
import common.CommonUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.*;
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
import java.util.*;

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
  private List<WblUserData> users;

  @Before
  public void setup() throws JsonProcessingException {
    users = new ArrayList<>();
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
      ObjectMapper mapper = new ObjectMapper();
      users = mapper.readValue(validBody, WblUserInput.class).getWblUsers();
    } else {
      hashMapBody = CommonUtils.prepareDataAddUserToWblByFund(fullName, address, idNumber, fundCode,
        note, actor, startDatetime, endDatetime);
      users.add(WblUserData.builder()
        .idNumber(idNumber)
        .address(address)
        .fullName(fullName)
        .fundCode(fundCode)
        .note(note)
        .startDatetime(startDatetime)
        .endDatetime(endDatetime)
        .build());
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
      if (errorMessage.equalsIgnoreCase("null")) {
        for (WblUserData user : users) {
          WblUser wblUser = WblUser.getByIdNumber(user.getIdNumber());
          WblPolicyUser wblPolicyUser = WblPolicyUser.getByWblUserId(wblUser.getId());
          assertThat(wblPolicyUser.getStartDatetime(), is(Timestamp.valueOf(
            LocalDateTime.parse(user.getStartDatetime() + " 00:00",
              DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))));
          assertThat(wblPolicyUser.getEndDatetime(), is(Timestamp.valueOf(
            LocalDateTime.parse(user.getEndDatetime() + " 00:00",
              DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))));
          assertThat(wblUser.getFullName(), is(user.getFullName()));
          assertThat(WblUserIdentification.getByWblUserId(wblUser.getId()).get(0).getIdNumber(), is(user.getIdNumber()));
        }
      } else {
        List<Map<String, Object>> result = response.jsonPath().get("");
        assertThat("verify error message", result.get(0).get("errorMessage").toString(), is(containsString(errorMessage)));
      }
    } else {
      assertThat("verify error message", response.jsonPath().get("message"), is(containsString(errorMessage)));
    }
  }

  @After
  public void clearData() {
    if (statusCode == 200 && errorMessage.equalsIgnoreCase("null")) {
      CallApiUtils.callDeleteUserFromWblByFundApi(WblUserIdentification.getByIdNumber(idNumber).getWbluserId().toString());
    }
  }
}

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class WblUserInput {
  private String actor;
  private List<WblUserData> wblUsers;
}

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class WblUserData {
  private String idNumber;
  private String fullName;
  private String address;
  private String fundCode;
  private String startDatetime;
  private String endDatetime;
  private String note;
}