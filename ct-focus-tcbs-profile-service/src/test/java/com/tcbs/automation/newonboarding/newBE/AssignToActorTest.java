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

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.ASSIGN_TASK_TO_MAKER_OR_CHECKER;
import static common.CallApiUtils.callNewOBMakerRejectApi;
import static common.CommonUtils.*;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/AssignToActor.csv", separator = '|')
public class AssignToActorTest {
  private static String taskId;
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String actor;
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
  @Title("Verify api assign to actor")
  public void verifyAssignToActorTest() {

    System.out.println(testCaseName);

    String getTask = getTaskId(task, taskId);
    String getAuthor = getAuthorization(actor);

    Response response = given()
      .baseUri(ASSIGN_TASK_TO_MAKER_OR_CHECKER.replace("#taskID#", getTask))
      .header("Authorization", "Bearer " + getAuthor)
      .when()
      .post();

    assertThat("verify status code", response.getStatusCode(), is(statusCode));
    if (statusCode == 400) {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }
}