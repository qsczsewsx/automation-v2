package com.tcbs.automation.accountClosing;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.ObUserCloseDocs;
import common.CommonUtils;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.CLOSE_ACCOUNT_DELETE_DOC;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.DELETE_DOC_CLOSE;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/accountClosing/CloseAccountDeleteDocTest.csv", separator = '|')
public class CloseAccountDeleteDocTest {

  @Getter
  private String testCaseName;
  private String docId;
  private int statusCode;
  private String errorMessage;
  private String taskId;

  @Before
  public void creatAndAssignTask() {
    taskId = String.valueOf(CommonUtils.createTaskCloseAccount());
    if (docId.equalsIgnoreCase("#docId#")) {
      docId = CommonUtils.uploadCloseDocs(taskId).toString();
    } else {
      docId = syncData(docId);
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api Delete Doc")
  public void verifyApiDeleteDocTest() {

    System.out.println("TestCaseName : " + testCaseName);

    Response response = given()
      .baseUri(DELETE_DOC_CLOSE)
      .header("Authorization", "Bearer " + CLOSE_ACCOUNT_DELETE_DOC)
      .contentType("application/json")
      .param("docId", docId)
      .when()
      .delete();
    assertThat(response.getStatusCode(), is(statusCode));

    if (response.statusCode() == 200) {
      assertThat("verify Delete Doc", response.jsonPath().get("data"), is(true));
      assertThat(ObUserCloseDocs.getCloseDocs(taskId).size(), is(0));
    } else if (response.statusCode() == 400) {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }

  @After
  public void teardown() {
    CommonUtils.deleteTaskCloseAccount(new BigDecimal(taskId));
  }

}

