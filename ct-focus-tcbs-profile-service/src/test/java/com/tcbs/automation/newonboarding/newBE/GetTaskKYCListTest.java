package com.tcbs.automation.newonboarding.newBE;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/GetTaskKYCList.csv", separator = '|')
public class GetTaskKYCListTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String pageSize;
  private String pageNumber;
  private String kycFlow;
  private String userName;
  private String fullName;
  private String idNumber;
  private String channel;
  private String startTime;
  private String slaEndTime;
  private String asignedBy;
  private String bankCode;
  private String errorMessage;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api task KYC List")
  public void verifyGetTaskKYCListTest() {
    pageNumber = syncData(pageNumber);
    pageSize = syncData(pageSize);
    userName = syncData(userName);
    fullName = syncData(fullName);
    idNumber = syncData(idNumber);
    channel = syncData(channel);
    startTime = syncData(startTime);
    slaEndTime = syncData(slaEndTime);
    asignedBy = syncData(asignedBy);
    bankCode = syncData(bankCode);
    kycFlow = syncData(kycFlow);

    RequestSpecification requestSpecification = given()
      .baseUri(TCBSPROFILE_GET_TASK_KYC)
      .header("Authorization", "Bearer " +
        (testCaseName.contains("has no permission") ? TCBSPROFILE_AUTHORIZATION : AMLOCK_KEY))
      .contentType("application/json");

    Response response;

    if (testCaseName.contains("missing page_size param")) {
      response = requestSpecification.param("pageNumber", pageNumber).get();
    } else if (testCaseName.contains("missing page_number param")) {
      response = requestSpecification.param("pageSize", pageSize).get();
    } else {
      response = requestSpecification
        .param("pageNumber", pageNumber)
        .param("pageSize", pageSize)
        .param("userName", userName)
        .param("fullName", fullName)
        .param("idNumber", idNumber)
        .param("channel", channel)
        .param("startTime", startTime)
        .param("slaEndTime", slaEndTime)
        .param("asignedBy", asignedBy)
        .param("kycFlow", kycFlow)
        .param("bankCode", bankCode)
        .get();
    }

    assertThat("verify status", response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      if (!errorMessage.equalsIgnoreCase("No record found")) {
        List<HashMap<String, Object>> listData = response.jsonPath().getList("data.content");
        assertThat(listData.size(), is((greaterThan(0))));
        if (testCaseName.contains("SLA_START_DATETIME")) {
          for (int i = 0; i < listData.size() - 1; i++) {
            if (listData.get(i).get("isAssigned").toString().equals(
              listData.get(i + 1).get("isAssigned").toString()
            )) {
              assertThat("verify order by SLA_START_DATETIME Ascend at index " + i + " and " + (i + 1),
                listData.get(i).get("slaStartTime").toString(),
                lessThanOrEqualTo(listData.get(i + 1).get("slaStartTime").toString())
              );
            }
          }
        }
      }

      List<String> kycFlowValueList = response.jsonPath().getList("data.content.kycFlow");
      if (kycFlow.equalsIgnoreCase("ONBOARDING")) {
        for (String i : kycFlowValueList) {
          assertEquals("ONBOARDING", i);
        }
      } else if (kycFlow.equalsIgnoreCase("RESIGN_CONTRACT")) {
        for (String i : kycFlowValueList) {
          assertEquals("RESIGN_CONTRACT", i);
        }
      }
    } else if (statusCode == 400) {
      assertThat("verify error message", response.jsonPath().get("message"),
        is(errorMessage));
    }
  }
}
