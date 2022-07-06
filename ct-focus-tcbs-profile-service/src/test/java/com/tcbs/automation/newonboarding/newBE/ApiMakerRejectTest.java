package com.tcbs.automation.newonboarding.newBE;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.ObTask;
import com.tcbs.automation.cas.TcbsUser;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.jsoup.internal.StringUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.*;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static common.CommonUtils.createTaskNewOBAndAssignToMaker;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/ApiMakerReject.csv", separator = '|')
public class ApiMakerRejectTest {
  private String makerTaskId;
  @Getter
  private String testCaseName;
  private String authorization;
  private String taskId;
  private String reasonId;
  private String reasonNode;
  private int statusCode;
  private String errorMsg;

  @Before
  public void creatAndAssignTask() {
    makerTaskId = createTaskNewOBAndAssignToMaker("10000000744");
    reasonNode = syncData(reasonNode);
    reasonId = syncData(reasonId);
    taskId = syncData(taskId);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api Maker reject")
  public void verifyApiMakerRejectTest() {
    System.out.println("Test Case : " + testCaseName);

    List<String> reason;
    if (reasonId.contains(",")) {
      reason = new ArrayList<>(Arrays.asList(reasonId.split(",")));
    } else {
      reason = new ArrayList<>(Collections.singletonList(reasonId));
    }

    if (taskId.equalsIgnoreCase("gen")) {
      taskId = makerTaskId;
    }

    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    body.put("taskId", StringUtil.isNumeric(taskId) ? Integer.valueOf(taskId) : taskId);
    body.put("reasonNode", reasonId.isEmpty() ? null : reasonNode);
    body.put("reasonIds", reasonId.isEmpty() ? null : reason);

    Response response = given()
      .baseUri(MAKER_REJECT)
      .header("Authorization", "Bearer " + (authorization.equalsIgnoreCase("maker") ? ASSIGN_TASK_TO_MAKER_KEY : ASSIGN_TASK_TO_AMOPS_MAKER))
      .contentType("application/json")
      .body(body)
      .when()
      .post();

    assertThat(response.getStatusCode(), is(statusCode));

    if (response.statusCode() == 200) {

      TcbsUser tcbsUser = TcbsUser.getByTcbsId("10000000744");
      ObTask obTask = ObTask.getByTaskId(makerTaskId);
      assertEquals("MAKER", obTask.getType());
      assertEquals(tcbsUser.getId(), obTask.getUserId());
      assertEquals("DONE", obTask.getStatus());
      assertEquals("REJECT", obTask.getKycStatus());
      assertEquals("AMOPS.MAKER", obTask.getActor());


    } else if (response.statusCode() == 400) {
      assertEquals(errorMsg, response.jsonPath().get("message"));
    }
  }
}
