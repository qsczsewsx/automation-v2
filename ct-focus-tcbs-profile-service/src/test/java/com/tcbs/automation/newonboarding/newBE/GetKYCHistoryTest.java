package com.tcbs.automation.newonboarding.newBE;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.ObReason;
import com.tcbs.automation.cas.ObTask;
import com.tcbs.automation.cas.TcbsUser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.TCBSPROFILE_AUTHORIZATION;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.TCBSPROFILE_KYC_HISTORY;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static common.CommonUtils.*;
import static common.DatesUtils.convertTimestampToStringWithFormat;
import static common.ProfileTools.TOKEN;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/GetKYCHistory.csv", separator = '|')
public class GetKYCHistoryTest {
  @Getter
  private String testCaseName;
  @Getter
  private String tcbsId;
  private int statusCode;
  private String errorMessage;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api get KYC history")
  public void verifyGetKYCHistoryTest() {
    System.out.println(testCaseName);

    tcbsId = syncData(tcbsId);
    RequestSpecification requestSpecification = given()
      .baseUri(TCBSPROFILE_KYC_HISTORY)
      .header("Authorization", "Bearer " +
        (testCaseName.contains("has no permission") ? TCBSPROFILE_AUTHORIZATION : TOKEN));

    Response response;

    if (testCaseName.contains("missing param")) {
      response = requestSpecification.get();
    } else {
      response = requestSpecification.param("tcbsId", tcbsId).get();
    }

    assertThat("verify status code", response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      ArrayList<Object> arrayList = response.jsonPath().get("taskId");
      if (testCaseName.contains("for valid request")) {
        assertThat(arrayList.size(), is((greaterThan(0))));
        verifyTaskListField(response, tcbsId);
      } else {
        assertThat(arrayList.size(), is((0)));
      }
    } else {
      assertThat("verify error message", response.jsonPath().get("errorMessage"), is(errorMessage));
    }
  }

  public void verifyTaskListField(Response response, String tcbsId) {

    List<HashMap<String, Object>> responsesList = response.jsonPath().getList("");
    for (HashMap<String, Object> responses : responsesList) {
      ObTask obTask = ObTask.getByTaskId(responses.get("taskId").toString());
      TcbsUser tcbsUser = TcbsUser.getByTcbsId(tcbsId);
      assertEquals(responses.get("userId").toString(), tcbsUser.getId().toString());
      assertEquals(responses.get("reason"), obTask.getReason());
      assertEquals(responses.get("actor"), obTask.getActor());
      assertEquals(responses.get("channelCode"), getChannel(obTask.getChannelId().toString()));
      assertEquals(responses.get("kycStatus"), obTask.getKycStatus());
      if (responses.get("reasons") != null) {
        List<HashMap<String, Object>> reasons = (List<HashMap<String, Object>>) convertObjectToList(responses.get("reasons"));
        for (HashMap<String, Object> reason : reasons) {
          ObReason obReason = ObReason.getById(reason.get("id").toString());
          verifyRejectReasonField(reason, obReason);
        }
      }
      assertEquals(convertTimestampToStringWithFormat(responses.get("createdDatetime").toString(), "yyyy-MM-dd HH:mm:ss.S"), obTask.getCreatedDatetime().toString());
      assertEquals(convertTimestampToStringWithFormat(responses.get("updatedDatetime").toString(), "yyyy-MM-dd HH:mm:ss.S"), obTask.getUpdatedDatetime().toString());
      assertEquals(convertTimestampToStringWithFormat(responses.get("startDatetime").toString(), "yyyy-MM-dd HH:mm:ss.S"), obTask.getStartDatetime().toString());
      assertEquals(convertTimestampToStringWithFormat(responses.get("endDatetime").toString(), "yyyy-MM-dd HH:mm:ss.S"), obTask.getEndDatetime().toString());
      assertEquals(convertTimestampToStringWithFormat(responses.get("slaStartDatetime").toString(), "yyyy-MM-dd HH:mm:ss.S"), obTask.getSlaStartDatetime().toString());
      assertEquals(convertTimestampToStringWithFormat(responses.get("slaEndDatetime").toString(), "yyyy-MM-dd HH:mm:ss.S"), obTask.getSlaEndDatetime().toString());
    }
  }

}