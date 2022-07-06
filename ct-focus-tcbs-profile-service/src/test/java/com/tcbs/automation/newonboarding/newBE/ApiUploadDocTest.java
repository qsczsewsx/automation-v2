package com.tcbs.automation.newonboarding.newBE;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.ObDocs;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.ASSIGN_TASK_TO_MAKER_KEY;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.UPLOAD_DOC;
import static com.tcbs.automation.tools.ConvertUtils.fileTxtToString;
import static common.CallApiUtils.*;
import static common.CommonUtils.createTaskNewOBAndAssignToMaker;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/ApiUploadDoc.csv", separator = '|')
public class ApiUploadDocTest {

  private static String getTaskId;
  private final LinkedHashMap<String, Object> body = new LinkedHashMap<>();
  @Getter
  private String testCaseName;
  @Getter
  private String taskId;
  private String documentGroup;
  private String fileName;
  private String mimeType;
  private String fileContents;
  private int statusCode;
  private String erroMsg;

  @BeforeClass
  public static void creatAndAssignTask() {
    getTaskId = createTaskNewOBAndAssignToMaker("10000000744");
  }

  @AfterClass
  public static void rejectTask() {
    callNewOBMakerRejectApi(getTaskId);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api Upload Doc")
  public void verifyApiUploadDocTest() {

    System.out.println("TestCaseName : " + testCaseName);

    fileContents = getFileContents(fileContents);
    taskId = getTaskId(taskId);
    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    body.put("taskId", taskId);
    body.put("documentGroup", documentGroup);
    body.put("fileName", fileName + mimeType);
    body.put("fileContents", fileContents);

    Response response = given()
      .baseUri(UPLOAD_DOC)
      .header("Authorization", "Bearer " + ASSIGN_TASK_TO_MAKER_KEY)
      .contentType("application/json")
      .body(body)
      .when()
      .post();

    assertThat(response.getStatusCode(), is(statusCode));

    if (response.statusCode() == 200) {

      String objectId = response.jsonPath().getString("objectId");
      String fileContentsRes = response.jsonPath().getString("fileContents");

      ObDocs obDocs = ObDocs.getByEcmID(objectId);
      assertEquals(fileContents, fileContentsRes);
      assertEquals("Doctype", obDocs.getFileType());
      assertEquals(new BigDecimal(taskId), obDocs.getTaskId());
      assertEquals("AMOPS.MAKER", obDocs.getActor());
      documentGroup = getDocumentGroup(documentGroup);
      assertEquals(documentGroup, obDocs.getDocumentGroup());
      assertEquals(mimeType, obDocs.getFileSuffix());

    } else if (response.statusCode() == 400) {
      String actualMessage = response.jsonPath().get("message");
      assertEquals(erroMsg, actualMessage);
    }
  }

  private String getFileContents(String fileContents) {
    if (fileContents.equalsIgnoreCase("file")) {
      fileContents = fileTxtToString("src/test/resources/requestBody/DocFileContent");
    } else {
      fileContents = null;
    }
    return fileContents;
  }

  private String getDocumentGroup(String documentGroup) {
    List<String> documentGroupList = Arrays.asList("REGISTER_INFORMATION", "BANK_INFORMATION", "OTHER");
    String documentText = null;
    for (String document : documentGroupList) {
      if (documentGroup.equalsIgnoreCase(document)) {
        documentText = documentGroup;
      }
    }
    return documentText;
  }

  private String getTaskId(String taskId) {
    if (taskId.contains("#taskId#")) {
      taskId = getTaskId;
    }
    return taskId;
  }

}
