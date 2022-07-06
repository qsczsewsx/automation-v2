package com.tcbs.automation.newonboarding.newBE;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.shazam.shazamcrest.MatcherAssert;
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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/GetTaskHistory.csv", separator = '|')
public class GetTaskHistoryTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String pageSize;
  private String pageNumber;
  private String errorMessage;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api get task history")
  public void verifyGetTaskHistoryTest() {
    pageNumber = syncData(pageNumber);
    pageSize = syncData(pageSize);

    System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,SSLv3");
    RequestSpecification requestSpecification = given()
      .baseUri(TCBSPROFILE_GET_TASK_HISTORY)
      .relaxedHTTPSValidation()
      .header("Authorization", "Bearer " +
        (testCaseName.contains("has no permission") ? TCBSPROFILE_INQUIRYGROUPINFOKEY : ASSIGN_TASK_TO_MAKER_KEY));

    Response response;

    if (testCaseName.contains("missing page_size param")) {
      response = requestSpecification.param("pageNumber", pageNumber).get();
    } else if (testCaseName.contains("missing page_number param")) {
      response = requestSpecification.param("pageSize", pageSize).get();
    } else {
      response = requestSpecification.param("pageSize", pageSize).param("pageNumber", pageNumber).get();
    }

    assertThat("verify status", response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      List<HashMap<String, Object>> listData = response.jsonPath().getList("data.content");
      MatcherAssert.assertThat(listData.size(), is((greaterThan(0))));
      if (testCaseName.contains("SLA_START_DATETIME")) {
        for (int i = 0; i < listData.size() - 1; i++) {
          assertThat("verify order by KYC_GROUP_DATETIME Descend at index " + i + " and " + (i + 1),
            listData.get(i).get("createdDatetime").toString(),
            greaterThan((listData.get(i + 1).get("createdDatetime").toString()))
          );
        }
      }
    } else if (statusCode == 403) {
      assertThat("verify error message", response.jsonPath().get("errorMessage"),
        is(errorMessage));
    } else {
      assertThat("verify error message", response.jsonPath().get("message"),
        is(errorMessage));
    }
  }
}