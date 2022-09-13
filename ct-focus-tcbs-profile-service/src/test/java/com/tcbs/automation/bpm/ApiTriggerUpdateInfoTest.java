package com.tcbs.automation.bpm;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.common.KafkaTool;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/bpm/ApiTriggerUpdateInfo.csv", separator = '|')
public class ApiTriggerUpdateInfoTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String tcbsId;
  private HashMap<String, Object> body;
  private String changeType;
  private String errorMessage;
  private static KafkaTool toolWarning;

  @BeforeClass
  public static void before() {
    toolWarning = new KafkaTool("notify.focus.profile.change");
    System.out.println(toolWarning.getMessages(5000));
  }

  @Before
  public void setup() {
    tcbsId = syncData(tcbsId);
    changeType = syncData(changeType);
    List<String> listChangeType = new ArrayList<>(Arrays.asList(changeType.split(",")));

    body = new HashMap<>();
    body.put("tcbsId", tcbsId);
    body.put("changeType", listChangeType);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api trigger update info")
  public void triggerUpdateInfo() {

    RequestSpecification requestSpecification = given()
      .baseUri(TRIGGER_UPDATE_INFO)
      .contentType("application/json")
      .header("x-api-key", testCaseName.contains("invalid x-api-key") ? FMB_X_API_KEY : TCBSPROFILE_BACKENDWBLKEY);

    Response response;
    Gson gson = new Gson();
    if (testCaseName.contains("missing body")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(gson.toJson(body)).post();
    }

    assertThat("verify status", response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      if (!testCaseName.contains("missing tcbsId")) {
        List<String> listQueue = toolWarning.getMessages(5000);
        boolean result = false;
        for (String s : listQueue) {
          if (s.contains(tcbsId)) {
            result = true;
            assertTrue("verify return info", result);
          }
        }
      }
    } else {
      assertThat("verify error message", response.jsonPath().get("message"), is(containsString(errorMessage)));
    }
  }
}
