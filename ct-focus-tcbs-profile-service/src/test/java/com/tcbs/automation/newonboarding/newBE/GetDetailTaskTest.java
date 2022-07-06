package com.tcbs.automation.newonboarding.newBE;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static common.CallApiUtils.*;
import static common.CommonUtils.createTaskNewOBAndAssignToMaker;
import static common.CommonUtils.getTaskId;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/GetDetailTask.csv", separator = '|')
public class GetDetailTaskTest {
  private static String taskId;
  @Getter
  private String testCaseName;
  @Getter
  private int statusCode;
  private String errorMessage;
  private String task;

  @BeforeClass
  public static void createTask() {
    taskId = createTaskNewOBAndAssignToMaker("10000000744");
  }

  @AfterClass
  public static void rejectTask() {
    callNewOBMakerRejectApi(taskId);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api get detail task")
  public void verifyGetDetailTaskTest() {
    String keys = "id,type,channel,action,userId,status,kycStatus,actor,slaStartDatetime,slaEndDatetime";
    String getTask = getTaskId(task, taskId);

    Response response = given()
      .baseUri(TCBSPROFILE_GET_DETAIL_TASK.replace("#taskID#", getTask))
      .header("Authorization", "Bearer " +
        (testCaseName.contains("has no permission") ? TCBSPROFILE_AUTHORIZATION : ASSIGN_TASK_TO_MAKER_KEY))
      .get();

    assertThat("verify status code", response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      assertThat(response.jsonPath().get("id").toString(), is(getTask));
      for (String item : keys.split(",", -1)) {
        assertThat(response.jsonPath().getMap(""), hasKey(item));
      }
    } else if (statusCode == 400) {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    } else {
      assertThat("verify error message", response.jsonPath().get("errorMessage"), is(errorMessage));
    }
  }
}