package com.tcbs.automation.newonboarding.newBE;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.ObDocs;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.DELETE_DOC;
import static common.CallApiUtils.*;
import static common.CommonUtils.createTaskNewOBAndAssignToMaker;
import static common.ProfileTools.TOKEN;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/ApiDeleteDoc.csv", separator = '|')
public class ApiDeleteDocTest {

  @Getter
  private static String fileContents;
  private static String ecmId;
  private static String getTaskId;
  @Getter
  private String testCaseName;
  private String docId;
  private int statusCode;
  private String errorMsg;

  @BeforeClass
  public static void creatAndAssignTask() {
    getTaskId = createTaskNewOBAndAssignToMaker("10000000744");
    ecmId = callNewOBUploadDocAndGetEcmId(getTaskId);
  }

  @AfterClass
  public static void rejectTask() {
    callNewOBMakerRejectApi(getTaskId);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api Delete Doc")
  public void verifyApiDeleteDocTest() {

    System.out.println("TestCaseName : " + testCaseName);

    docId = getDocId(docId);

    Response response = callDeleteDocApi(testCaseName, docId);

    assertThat(response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      List<ObDocs> obDocs = ObDocs.getListByEcmID(docId);
      assertTrue(obDocs.isEmpty());
    } else if (statusCode == 400) {
      assertEquals(errorMsg, response.jsonPath().get("message"));
    }
  }

  private Response callDeleteDocApi(String testCaseName, String docId) {
    Response response;
    RequestSpecification requestSpecification = given()
      .baseUri(DELETE_DOC)
      .header("Authorization", "Bearer " + TOKEN)
      .contentType("application/json");
    if (testCaseName.contains("missing param")) {
      response = requestSpecification
        .when()
        .delete();
    } else {
      response = requestSpecification
        .param("docId", docId)
        .when()
        .delete();
    }
    return response;
  }

  private String getDocId(String docId) {
    if (docId.equalsIgnoreCase("#docId#")) {
      ObDocs obDocs = ObDocs.getByEcmID(ecmId);
      docId = obDocs.getId().toString();
    }
    return docId;
  }

}
