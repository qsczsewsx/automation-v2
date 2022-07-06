package com.tcbs.automation.newonboarding.newBE;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.ObTask;
import com.tcbs.automation.cas.TcbsUser;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.ASSIGN_TASK_TO_MAKER_KEY;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.MAKER_SEND_APPROVE;
import static common.CommonUtils.createTaskNewOBAndAssignToMaker;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/ApiMakerApprove.csv", separator = '|')
public class ApiMakerApproveTest {

  private String makerTaskID;
  @Getter
  private String testCaseName;
  @Getter
  private String authorization;
  private String taskId;
  private int statusCode;
  private String errorMsg;

  @Before
  public void creatAndAssignTask() {
    String tcbsId;
    if (testCaseName.contains("account is not belong to owner")) {
      tcbsId = "10000024788";
    } else {
      tcbsId = "10000000744";
    }
    makerTaskID = createTaskNewOBAndAssignToMaker(tcbsId);
  }


  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api Maker send approve")
  public void verifyApiMakerApproveTest() {

    System.out.println("TestCaseName : " + testCaseName);

    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    body.put("taskId", taskId.replace("#taskId#", makerTaskID));

    Response response = given()
      .baseUri(MAKER_SEND_APPROVE)
      .header("Authorization", authorization.replaceAll("maker", "Bearer " + ASSIGN_TASK_TO_MAKER_KEY))
      .contentType("application/json")
      .body(body)
      .when()
      .post();

    assertThat(response.getStatusCode(), is(statusCode));

    if (response.statusCode() == 200) {

      TcbsUser tcbsUser = TcbsUser.getByTcbsId("10000000744");
      ObTask obTask = ObTask.getByTaskId(makerTaskID);
      assertEquals("MAKER", obTask.getType());
      assertEquals(tcbsUser.getId(), obTask.getUserId());
      assertEquals("DONE", obTask.getStatus());
      assertEquals("APPROVED", obTask.getKycStatus());
      assertEquals("AMOPS.MAKER", obTask.getActor());

      obTask = ObTask.getByTaskRefId(new BigDecimal(makerTaskID));
      assertEquals("CHECKER", obTask.getType());
      assertEquals(new BigDecimal(makerTaskID), obTask.getTaskRefId());
      assertEquals(tcbsUser.getId(), obTask.getUserId());
      assertEquals("TODO", obTask.getStatus());

    } else if (response.statusCode() == 400) {
      assertEquals(errorMsg, response.jsonPath().get("message"));
    }
  }
}