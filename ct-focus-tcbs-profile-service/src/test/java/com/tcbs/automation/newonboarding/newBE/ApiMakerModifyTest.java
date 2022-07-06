package com.tcbs.automation.newonboarding.newBE;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.ObTask;
import com.tcbs.automation.cas.ObUserOpenaccountQueue;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.ASSIGN_TASK_TO_MAKER_KEY;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.MAKER_MODIFY;
import static common.CommonUtils.createTaskNewOBAndAssignToMaker;
import static common.CommonUtils.createTaskResignContract;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/ApiMakerModify.csv", separator = '|')
public class ApiMakerModifyTest {

  private static String makerTaskID;
  private static String resignTaskID;
  @Getter
  private String testCaseName;
  @Getter
  private String taskId;
  private String fullName;
  private String gender;
  private String birthday;
  private String contactAddress;
  private String idNumber;
  private String idPlace;
  private String idDate;
  private String expireDate;
  private String removeParam;
  private int expectedHttpCode;
  private String expectedHttpMessage;

  private static final String ID_PLACE = "idPlace";
  private static final String EXPIRE_DATE = "expireDate";
  private static final String FULL_NAME = "fullName";
  private static final String DATA_GENDER = "gender";
  private static final String IDENTITY_CARD = "identityCard";
  private static final String TASK_ID = "taskId";
  private static final String PERSONAL_INFO = "personalInfo";

  @BeforeClass
  public static void createTask() {
    makerTaskID = createTaskNewOBAndAssignToMaker("10000000744");
    resignTaskID = createTaskResignContract().toString();
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api Maker Modify")
  public void verifyMakerModifyTest() {

    System.out.println("TestCaseName : " + testCaseName);
    LinkedHashMap<String, Object> body;

    if (testCaseName.contains("resignContract flow")) {
      body = getSmallMakerModifyBody();
    } else {
      body = getMakerModifyBody(removeParam);
    }

    Response response = given()
      .baseUri(MAKER_MODIFY)
      .header("Authorization", "Bearer " + ASSIGN_TASK_TO_MAKER_KEY)
      .contentType("application/json")
      .body(body)
      .when()
      .post();

    assertThat(response.getStatusCode(), is(expectedHttpCode));

    if (expectedHttpCode == 200) {
      ObUserOpenaccountQueue obUserOpenaccountQueue = ObUserOpenaccountQueue.getObUserOpenaccountQueueByTaskId(new BigDecimal(makerTaskID));
      verifySuccessCase(obUserOpenaccountQueue);
    } else {
      String actualMessage = response.jsonPath().get("message");
      assertEquals(expectedHttpMessage, actualMessage);
    }
  }

  private void verifySuccessCase(ObUserOpenaccountQueue obUserOpenaccountQueue) {
    if (gender.equalsIgnoreCase("MALE")) {
      gender = null;
    } else {
      gender = String.valueOf(0);
    }
    if (!birthday.isEmpty()) {
      Timestamp birth = Timestamp.valueOf(LocalDateTime.parse(birthday, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")));
      assertEquals(birth, obUserOpenaccountQueue.getBirthday());
    }

    if (!idDate.isEmpty()) {
      Timestamp idDateT = Timestamp.valueOf(LocalDateTime.parse(idDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")));
      assertEquals(idDateT, obUserOpenaccountQueue.getIddate());
    }

    if (!fullName.isEmpty()) {
      assertEquals(fullName.split(" ")[1], obUserOpenaccountQueue.getFirstname());
      assertEquals(fullName.split(" ")[0], obUserOpenaccountQueue.getLastname());
    }

    if (obUserOpenaccountQueue.getIdnumber() != null) {
      assertEquals(idNumber, obUserOpenaccountQueue.getIdnumber());
    }
    if (obUserOpenaccountQueue.getIdplace() != null) {
      assertEquals(idPlace, obUserOpenaccountQueue.getIdplace());
    }

  }

  @AfterClass
  public static void deleteTask() {
    ObTask.deleteById(makerTaskID);
    ObTask.deleteById(resignTaskID);
  }

  public LinkedHashMap<String, Object> getMakerModifyBody(String removeParam) {

    LinkedHashMap<String, Object> identityCard = new LinkedHashMap<>();
    identityCard.put("idNumber", idNumber);
    identityCard.put(ID_PLACE, idPlace);
    identityCard.put("idDate", idDate);
    identityCard.put(EXPIRE_DATE, expireDate);
    LinkedHashMap<String, Object> personalInfo = new LinkedHashMap<>();
    personalInfo.put(FULL_NAME, fullName);
    personalInfo.put(DATA_GENDER, gender);
    personalInfo.put("birthday", birthday);
    personalInfo.put("permanentAddress", contactAddress);
    personalInfo.put(IDENTITY_CARD, identityCard);
    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    body.put(TASK_ID, taskId.replace("#taskId#", makerTaskID));
    body.put(PERSONAL_INFO, personalInfo);

    if (removeParam != null) {
      if (removeParam.equalsIgnoreCase("body")) {
        body.clear();
      } else if (removeParam.equalsIgnoreCase(TASK_ID) || removeParam.equalsIgnoreCase(PERSONAL_INFO)) {
        body.remove(removeParam);
      } else if (removeParam.equalsIgnoreCase(FULL_NAME) || removeParam.equalsIgnoreCase(DATA_GENDER) || removeParam.equalsIgnoreCase("birthday") || removeParam.equalsIgnoreCase(
        "permanentAddress") || removeParam.equalsIgnoreCase(IDENTITY_CARD)) {
        personalInfo.remove(removeParam);
      } else if (removeParam.equalsIgnoreCase("idNumber") || removeParam.equalsIgnoreCase(ID_PLACE) || removeParam.equalsIgnoreCase("idDate") || removeParam.equalsIgnoreCase(EXPIRE_DATE)) {
        identityCard.remove(removeParam);
      }
    }
    return body;
  }

  public LinkedHashMap<String, Object> getSmallMakerModifyBody() {

    LinkedHashMap<String, Object> identityCard = new LinkedHashMap<>();
    identityCard.put(ID_PLACE, idPlace);
    identityCard.put(EXPIRE_DATE, expireDate);
    LinkedHashMap<String, Object> personalInfo = new LinkedHashMap<>();
    personalInfo.put(FULL_NAME, fullName);
    personalInfo.put(DATA_GENDER, gender);
    personalInfo.put("contactAddress", contactAddress);
    personalInfo.put(IDENTITY_CARD, identityCard);
    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    body.put(TASK_ID, taskId.replace("#taskId#", resignTaskID));
    body.put(PERSONAL_INFO, personalInfo);
    body.put("modifyType", "SMALL");
    body.put("reasonNode", "");

    return body;
  }
}
