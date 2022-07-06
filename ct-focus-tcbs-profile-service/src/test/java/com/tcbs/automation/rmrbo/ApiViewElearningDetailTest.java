package com.tcbs.automation.rmrbo;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.*;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.RestRequests.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/rmrbo/ApiViewElearningDetail.csv", separator = '|')
public class ApiViewElearningDetailTest {

  @Getter
  private String testCaseName;
  private String tcbsId;
  private int statusCode;
  private String errorMessage;

  @Before
  public void before() {
    //get data dynamic from database
    if (tcbsId.equalsIgnoreCase("gen")) {
      tcbsId = R3RdUser.getAllList().get(0).getTcbsId();;
    } else {
      tcbsId = syncData(tcbsId);
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api get view elearning detail")
  public void apiViewElearningDetail() {
    System.out.println("Testcase Name: " + testCaseName);

    Response response = given()
      .baseUri(VIEW_ELEARNING_DETAIL.replace("{tcbsId}", tcbsId))
      .header("Authorization", "Bearer " + (testCaseName.contains("authorization is incorrect") ? TCBS_USER_TOKEN : RMRBO_VIEW_TOKEN))
      .contentType("application/json")
      .get();

    assertThat(response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      List<HashMap<String, Object>> eLearningDetail = response.jsonPath().get("");
      if (testCaseName.contains("and return data")) {
        List<R3RdElearning> r3RdElearnings = R3RdElearning.getByTcbsId(tcbsId);
        for (int i = 0; i < r3RdElearnings.size(); i++) {
          assertThat(eLearningDetail.get(i).get("role"), is(r3RdElearnings.get(i).getRole()));
          assertThat(eLearningDetail.get(i).get("userName"), is(r3RdElearnings.get(i).getUsername()));
          assertThat(eLearningDetail.get(i).get("courseCode"), is(r3RdElearnings.get(i).getCourseCode()));
          assertThat(eLearningDetail.get(i).get("courseStatus").toString(), is(r3RdElearnings.get(i).getCourseStatus().toString()));
          assertThat(eLearningDetail.get(i).get("partner"), is(r3RdElearnings.get(i).getBankCode()));
          assertThat(eLearningDetail.get(i).get("fullName"), is(r3RdElearnings.get(i).getFullName()));
        }
      } else {
        assertThat(eLearningDetail.size(), is(0));
      }
    } else {
      assertThat("verify authorization", response.jsonPath().get("errorMessage"), is(errorMessage));
    }
  }
}