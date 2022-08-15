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
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static common.CallApiUtils.*;
import static common.CommonUtils.createTaskNewOBAndAssignToMaker;
import static common.DatesUtils.convertTimestampToString;
import static common.DatesUtils.covertDateToString;
import static common.ProfileTools.TOKEN;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/ApiGetDocList.csv", separator = '|')
public class ApiGetDocListTest {

  private static String getTaskId;
  @Getter
  private String testCaseName;
  @Getter
  private String taskId;
  private int statusCode;
  private String errorMsg;

  @BeforeClass
  public static void createAndAssignTask() {
    ObDocs.deleteNullDocumentGroupByUserId("5260177");
    getTaskId = createTaskNewOBAndAssignToMaker("10000000744");
    callNewOBUploadDocAndGetEcmId(getTaskId);
  }

  @AfterClass
  public static void rejectTask() {
    callNewOBMakerRejectApi(getTaskId);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api Get Doc List")
  public void verifyApiGetDocListTest() {

    System.out.println("TestCaseName : " + testCaseName);

    taskId = getTaskId(taskId);

    clearCache(DELETE_CACHE, "x-api-key", API_KEY);

    Response response = given()
      .baseUri(GET_FILE_LIST)
      .header("Authorization", "Bearer " + TOKEN)
      .contentType("application/json")
      .param("taskId", taskId)
      .when()
      .get();

    assertThat(response.getStatusCode(), is(statusCode));

    if (response.statusCode() == 200) {

      List<HashMap<String, Object>> taskDocument = response.jsonPath().getList("data");
      ObDocs obDocs = ObDocs.getByTaskID(new BigDecimal(taskId)).get(0);
      for (HashMap<String, Object> document : taskDocument) {
        if (document.get("id").toString().equalsIgnoreCase(obDocs.getId().toString())) {
          assertEquals(document.get("objectId"), obDocs.getEcmId());
          assertTrue(document.get("fileName").toString().contains(obDocs.getFileName()));
          assertEquals(document.get("fileType"), obDocs.getFileType());
          assertEquals(document.get("documentGroup"), obDocs.getDocumentGroup());
          assertEquals(document.get("actor"), obDocs.getActor());
          assertEquals(document.get("fileSuffix"), obDocs.getFileSuffix());
          assertEquals(convertTimestampToString(document.get("createDate").toString()), covertDateToString((Timestamp) obDocs.getCreateDate()));
        }
      }

    } else if (response.statusCode() == 400) {
      String actualMessage = response.jsonPath().get("message");
      assertEquals(errorMsg, actualMessage);
    }
  }

  private String getTaskId(String taskId) {
    if (taskId.contains("541")) {
      taskId = getTaskId;
    }
    return taskId;
  }

}
