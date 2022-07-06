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
import java.util.LinkedHashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.CLOSE_ACCOUNT_UPLOAD_DOC;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.UPLOAD_DOC_CLOSE;
import static com.tcbs.automation.tools.ConvertUtils.fileTxtToString;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/accountClosing/CloseAccountUploadDocTest.csv", separator = '|')
public class CloseAccountUploadDocTest {

  private String getTaskId;
  @Getter
  private String testCaseName;
  @Getter
  private String taskId;
  private String fileName;
  private String fileContents;
  private int statusCode;
  private String errorMessage;
  private LinkedHashMap<String, Object> body;


  @Before
  public void setup() {
    getTaskId = String.valueOf(CommonUtils.createTaskCloseAccount());
    if (taskId.equalsIgnoreCase("taskId")) {
      taskId = getTaskId;
    } else {
      taskId = syncData(taskId);
    }
  }


  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api Upload Doc")
  public void verifyApiUploadDocTest() {

    System.out.println("TestCaseName : " + testCaseName);

    fileContents = getFileContents(fileContents);
    body = new LinkedHashMap<>();
    body.put("taskId", taskId);
    body.put("fileName", fileName);
    body.put("fileContents", fileContents);

    Response response = given()
      .baseUri(UPLOAD_DOC_CLOSE)
      .header("Authorization", "Bearer " + CLOSE_ACCOUNT_UPLOAD_DOC)
      .contentType("application/json")
      .body(body)
      .when()
      .post();

    assertThat(response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      assertThat("verify Upload Doc", response.jsonPath().get("data"), is(true));
      assertThat(ObUserCloseDocs.getCloseDocs(taskId).size(), is(1));
    } else if (statusCode == 400) {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }

  }

  @After
  public void teardown() {
    ObUserCloseDocs.deleteByObTaskCloseId(new BigDecimal(getTaskId));
    CommonUtils.deleteTaskCloseAccount(new BigDecimal(getTaskId));
  }

  private String getFileContents(String fileContents) {
    if (fileContents.equalsIgnoreCase("file")) {
      fileContents = fileTxtToString("src/test/resources/requestBody/CloseAccountContent");
    } else if (fileContents.equalsIgnoreCase("file invalid")) {
      fileContents = fileTxtToString("src/test/resources/requestBody/CloseAccountContent2");
    } else if (fileContents.equalsIgnoreCase("file lon hon 20mb")) {
      fileContents = fileTxtToString("src/test/resources/requestBody/CloseAccountContent1");
    } else {
      fileContents = syncData(fileContents);
    }
    return fileContents;
  }

}
