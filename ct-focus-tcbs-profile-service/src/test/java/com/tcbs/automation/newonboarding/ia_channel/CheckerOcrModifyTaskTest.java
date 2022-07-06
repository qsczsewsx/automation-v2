package com.tcbs.automation.newonboarding.ia_channel;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.ObTask;
import com.tcbs.automation.cas.ObUserOpenaccountQueue;
import com.tcbs.automation.cas.OcrData;
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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/CheckerOcrModifyTask.csv", separator = '|')
public class CheckerOcrModifyTaskTest {

  private static String taskIdActual;
  @Getter
  private String testCaseName;
  private String taskId;
  private String reasonNode;
  private String fullName;
  private String gender;
  private String birthday;
  private String contactAddress;
  private String idNumber;
  private String idPlace;
  private String idDate;
  private String expireDate;
  private int statusCode;
  private String errorMsg;
  private ObUserOpenaccountQueue obUserOpenaccountQueue = new ObUserOpenaccountQueue();

  @Before
  public void genTaskByOcrWebHook() {
    CallApiUtils.callOcrWebhookApi("ab71bf35-68c3-49b9-bc60-fd188e29c2c6", "Hoàng Minh Châu", "19/05/1990", "625799374074", "10/04/2021", "Cuc Canh Sat QLHC ve TTXH", "19/05/2030");
    taskIdActual = String.valueOf(ObTask.getByUserIdAndStatus("5280842", "TODO").getId());
    CallApiUtils.callNewOBAssignTaskToCheckerApi(taskIdActual);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api checker ocr modify task")
  public void verifyOcrModifyTaskTest() {

    System.out.println("TestCaseName : " + testCaseName);

    fullName = syncData(fullName);
    gender = syncData(gender);
    birthday = syncData(birthday);
    contactAddress = syncData(contactAddress);
    idNumber = syncData(idNumber);
    idPlace = syncData(idPlace);
    expireDate = syncData(expireDate);
    reasonNode = syncData(reasonNode);

    LinkedHashMap<String, Object> identityCard = new LinkedHashMap<>();
    identityCard.put("idNumber", idNumber);
    identityCard.put("idPlace", idPlace);
    identityCard.put("idDate", idDate);
    identityCard.put("expireDate", expireDate);
    LinkedHashMap<String, Object> personalInfo = new LinkedHashMap<>();
    personalInfo.put("fullName", fullName);
    personalInfo.put("gender", gender);
    personalInfo.put("birthday", birthday);
    personalInfo.put("contactAddress", contactAddress);
    personalInfo.put("identityCard", identityCard);
    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    body.put("reasonNode", reasonNode);
    body.put("personalInfo", personalInfo);
    body.put("modifyType", "SMALL");


    RequestSpecification requestSpecification = given()
      .baseUri(CHECKER_OCR_MODIFY_TASK)
      .header("Authorization", "Bearer " +
        (testCaseName.contains("has no permission") ? TCBSPROFILE_AUTHORIZATION : ASSIGN_TASK_TO_CHECKER_KEY))
      .contentType("application/json");

    Response response;

    if (testCaseName.contains("missing BODY")) {
      response = requestSpecification.post();
    } else if (testCaseName.contains("missing param taskId")) {
      body.put("taskId", null);
      response = requestSpecification.body(body).post();
    } else {
      body.put("taskId", taskId.contains("#") ? taskId.replace("#taskId#", taskIdActual) : taskId);
      response = requestSpecification.body(body).post();
    }

    assertThat(response.getStatusCode(), is(statusCode));

    if (response.statusCode() == 200) {

      obUserOpenaccountQueue = ObUserOpenaccountQueue.getObUserOpenaccountQueueByTaskId(new BigDecimal(taskIdActual));

      Timestamp birth = Timestamp.valueOf(LocalDateTime.parse(birthday, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")));
      Timestamp idDateT = Timestamp.valueOf(LocalDateTime.parse(idDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")));

      assertThat(obUserOpenaccountQueue.getFirstname(), is(fullName.split(" ")[2]));
      assertThat(obUserOpenaccountQueue.getBirthday(), is(birth));
      assertThat(obUserOpenaccountQueue.getAddress(), is(contactAddress));
      assertThat(obUserOpenaccountQueue.getIdnumber(), is(idNumber));
      assertThat(obUserOpenaccountQueue.getIdplace(), is(idPlace));
      assertThat(obUserOpenaccountQueue.getIddate(), is(idDateT));

    } else if (response.statusCode() == 403) {
      assertThat(response.jsonPath().get("errorMessage"), is(errorMsg));

    } else {
      assertThat(response.jsonPath().get("message"), is(errorMsg));
    }
  }

  @After
  public void clearData() {
    ObTask.deleteByUserIdAndStatus("5280842", "PROCESSING");
    OcrData.updateStatus("ab71bf35-68c3-49b9-bc60-fd188e29c2c6", null);
  }
}
