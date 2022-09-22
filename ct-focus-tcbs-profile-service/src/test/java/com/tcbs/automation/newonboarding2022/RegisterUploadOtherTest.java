package com.tcbs.automation.newonboarding2022;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.cas.TcbsUserOpenAccountQueue;
import com.tcbs.automation.cas.TcbsUserOpenAccountQueueUpload;
import common.CallApiUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.*;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.REGISTER_UPLOAD_OTHER;
import static com.tcbs.automation.tools.ConvertUtils.fileTxtToString;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hibernate.internal.CoreLogging.logger;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding2022/RegisterUploadOther.csv", separator = '|')
public class RegisterUploadOtherTest {

  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String referenceId;
  private String authenKey;
  private String file;
  private List<String> fileList;
  private HashMap<String, Object> body;
  private String tuoqId;

  @Before
  public void before() {
    referenceId = syncData(referenceId);
    if (authenKey.equalsIgnoreCase("gen")) {
      authenKey = CallApiUtils.callRegisterConfirmPhoneApi(referenceId);
    } else {
      authenKey = syncData(authenKey);
    }
    file = syncData(file);
    String frontFile = fileTxtToString("src/test/resources/requestBody/FrontFileContent");
    String backFile = fileTxtToString("src/test/resources/requestBody/BackFileContent");

    switch (file) {
      case "1":
        fileList = new ArrayList<>(Collections.singletonList(frontFile));
        break;
      case "2":
        fileList = new ArrayList<>(Arrays.asList(frontFile, backFile));
        break;
      case "4":
        fileList = new ArrayList<>(Arrays.asList(frontFile, backFile, frontFile, backFile));
        break;
      case "6":
        fileList = new ArrayList<>(Arrays.asList(frontFile, backFile, frontFile, backFile, frontFile, backFile));
        break;
      case "abc":
        fileList = new ArrayList<>(Collections.singletonList("abc"));
        break;
      default:
        logger("empty list");
    }

    body = new HashMap<>();
    body.put("referenceId", referenceId);
    body.put("files", fileList);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify register upload other")
  public void verifyRegisterUploadOther() {

    System.out.println("Test Case: " + testCaseName);

    RequestSpecification requestSpecification = given()
      .baseUri(REGISTER_UPLOAD_OTHER)
      .contentType("application/json")
      .header("Authorization", "Bearer " + authenKey);

    Response response;
    Gson gson = new Gson();
    if (testCaseName.contains("missing body")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(gson.toJson(body)).post();
    }

    assertEquals(statusCode, response.getStatusCode());
    if (statusCode != 403) {
      assertEquals(errorMessage, response.jsonPath().get("message"));
      if (statusCode == 200) {
        tuoqId = TcbsUserOpenAccountQueue.getByPhone(referenceId.substring(0, 12)).getId().toString();
        for (int i = 0; i < fileList.size(); i++) {
          assertThat(TcbsUserOpenAccountQueueUpload.getByTuoqIdAndFileType(tuoqId, "SCAN_OTHER_ID_IMAGE_FRONT").get(i).getId(), is(notNullValue()));
        }
      }
    }
  }

  @After
  public void clearData() {
    // Clear data
    if (statusCode == 200) {
      TcbsUserOpenAccountQueueUpload.deleteByTuoqIdAndFileType(tuoqId, "SCAN_OTHER_ID_IMAGE_FRONT");
    }
  }
}