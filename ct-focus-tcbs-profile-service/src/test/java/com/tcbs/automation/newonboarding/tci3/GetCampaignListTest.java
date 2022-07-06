package com.tcbs.automation.newonboarding.tci3;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Map;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/GetCampaignList.csv", separator = '|')
public class GetCampaignListTest {
  private final String str = "id,userName,fullName,campaignCode,referralCode";
  @Getter
  private String testCaseName;
  private String campaignCode;
  private int statusCode;
  private String errorMessage;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api get campaign list")
  public void verifyGetCampaignListTest() {
    System.out.println(testCaseName);
    campaignCode = syncData(campaignCode);

    Response response = given()
      .baseUri(GET_CAMPAIGN_LIST.replaceAll("#compaignCode#", campaignCode))
      .header("Authorization", "Bearer " +
        (testCaseName.contains("has no permission") ? TCBSPROFILE_AUTHORIZATION : TCBSPROFILE_BACKENDCAMPAIGN))
      .get();

    assertThat("verify status code", response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      List<Map<String, Object>> maps = response.jsonPath().get();
      if (testCaseName.contains("data is returned as objects correctly")) {
        for (String item : str.split(",", -1)) {
          assertThat(maps.get(0).keySet(), hasItem(item));
        }
      } else {
        assertThat(maps.size(), is(0));
      }
    } else if (statusCode == 404) {
      assertThat("verify error message", response.jsonPath().get("error"), is(errorMessage));
    } else {
      assertThat("verify error message", response.jsonPath().get("errorMessage"), is(errorMessage));
    }
  }
}