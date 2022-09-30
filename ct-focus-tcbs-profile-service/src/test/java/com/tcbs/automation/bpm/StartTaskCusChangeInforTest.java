package com.tcbs.automation.bpm;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.cas.UserChangeInforRecordEntity;
import common.CallApiUtils;
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

import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/bpm/StartTaskCusChangeInfor.csv", separator = '|')
public class StartTaskCusChangeInforTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String tcbsId;
  private String errorMessage;
  private int status;
  private String processInstanceId;
  private String type;
  private String newPhone;
  private String newEmail;
  private String newIdentityNo;
  private HashMap<String, Object> body;

  @Before
  public void setup() {

    tcbsId = syncData(tcbsId);
    processInstanceId = syncData(processInstanceId);
    type = syncData(type);
    newPhone = syncData(newPhone);
    newEmail = syncData(newEmail);
    newIdentityNo = syncData(newIdentityNo);

    body = new HashMap<>();
    body.put("tcbsId", tcbsId);
    body.put("status", status);
    body.put("processInstanceId", processInstanceId);
    body.put("type", type);
    body.put("newPhone", newPhone);
    body.put("newEmail", newEmail);
    body.put("newIdentityNo", newIdentityNo);

  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api start task cus change infor")
  public void performTest() {

    int oldSize = UserChangeInforRecordEntity.getListByTcbsIdAndType(tcbsId, type).size();

    RequestSpecification requestSpecification = given()
      .baseUri(START_TASK_CUS_CHANGE_INFOR)
      .contentType("application/json")
      .header("x-api-key", API_KEY);
    Response response;

    Gson gson = new Gson();

    if (testCaseName.contains("missing BODY")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(gson.toJson(body)).post();
    }

    assertThat("verify status", response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      List<UserChangeInforRecordEntity> listResult = UserChangeInforRecordEntity.getListByTcbsIdAndType(tcbsId, type);
      assertThat(listResult.size(), greaterThan(oldSize));
      if (testCaseName.contains("identityCard_change")) {
        assertThat(listResult.get(0).getNewIdentityNo(), is(newIdentityNo));
      } else if (testCaseName.contains("contactInfo_change")) {
        assertThat(listResult.get(0).getNewEmail(), is(newEmail));
      } else if (testCaseName.contains("status as 1")) {
        assertThat(listResult.get(0).getNewPhone(), is(newPhone));
      }
    } else {
      assertThat("verify error message", response.jsonPath().get("message"), is(containsString(errorMessage)));
    }
  }

  @After
  public void clearData() {
    if (statusCode == 200) {
      UserChangeInforRecordEntity.deleteByTcbsIdAndType(tcbsId, type);
      CallApiUtils.clearCache(DELETE_CACHE, "x-api-key", API_KEY);
    }
  }
}