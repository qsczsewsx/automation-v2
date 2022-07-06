package com.tcbs.automation.newonboarding.newBE;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.ObTask;
import com.tcbs.automation.cas.TcbsUser;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.ASSIGN_TASK_TO_CHECKER_KEY;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.CHECKER_APPROVE;
import static common.CallApiUtils.callNewOBAssignTaskToCheckerApi;
import static common.CallApiUtils.callNewOBMakerSendApproveToCheckerApiAndGetTaskId;
import static common.CommonUtils.createTaskNewOBAndAssignToMaker;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/ApiCheckerApprove.csv", separator = '|')
public class ApiCheckerApproveTest {

  private static String makerTaskID;
  private static String checkerTaskID;
  @Getter
  private String testCaseName;
  @Getter
  private String authorization;
  private String taskId;
  private int statusCode;
  private String errorMsg;

  @BeforeClass
  public static void createAndAssignTask() {
    makerTaskID = createTaskNewOBAndAssignToMaker("10000000744");
    checkerTaskID = callNewOBMakerSendApproveToCheckerApiAndGetTaskId(makerTaskID);
    callNewOBAssignTaskToCheckerApi(checkerTaskID);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api Checker Approve")
  public void verifyApiCheckerApproveTest() {

    System.out.println("TestCaseName : " + testCaseName);

    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    body.put("taskId", taskId.replace("#taskId#", checkerTaskID));

    Response response = given()
      .baseUri(CHECKER_APPROVE)
      .header("Authorization", "Bearer " + authorization.replaceAll("checker", ASSIGN_TASK_TO_CHECKER_KEY))
      .contentType("application/json")
      .body(body)
      .when()
      .put();

    assertThat(response.getStatusCode(), is(statusCode));

    if (response.statusCode() == 200) {
      TcbsUser tcbsUser = TcbsUser.getByTcbsId("10000000744");
      ObTask obTask = ObTask.getByTaskId(checkerTaskID);
      assertEquals("CHECKER", obTask.getType());
      assertEquals(new BigDecimal(makerTaskID), obTask.getTaskRefId());
      assertEquals(tcbsUser.getId(), obTask.getUserId());
      assertEquals("DONE", obTask.getStatus());
      assertEquals("APPROVED", obTask.getKycStatus());
      assertEquals("AMOPS.CHECKER", obTask.getActor());

    } else if (response.statusCode() == 400) {
      assertEquals(errorMsg, response.jsonPath().get("message"));
    }
  }

}
