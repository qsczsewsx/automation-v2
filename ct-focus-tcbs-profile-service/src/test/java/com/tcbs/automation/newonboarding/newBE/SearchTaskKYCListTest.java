package com.tcbs.automation.newonboarding.newBE;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.ObAction;
import com.tcbs.automation.cas.ObTask;
import com.tcbs.automation.cas.TcbsIdentification;
import com.tcbs.automation.cas.TcbsUser;
import common.CallApiUtils;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.ASSIGN_TASK_TO_MAKER_KEY;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.TCBSPROFILE_GET_TASK_KYC;
import static common.CommonUtils.checkDataIsNullOrNot;
import static common.CommonUtils.getChannel;
import static common.DatesUtils.convertTimestampToStringWithFormat;
import static common.DatesUtils.covertDateToStringWithFormat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/SearchTaskKYCList.csv", separator = '|')
public class SearchTaskKYCListTest {
  private static HashMap<String, Object> taskOnlineP = new HashMap<>();
  private static HashMap<String, Object> taskOnlineT = new HashMap<>();
  private static HashMap<String, Object> taskOnlineCP = new HashMap<>();
  private static HashMap<String, Object> taskOnlineCT = new HashMap<>();
  private static HashMap<String, Object> taskRmP = new HashMap<>();
  private static HashMap<String, Object> taskIaP = new HashMap<>();
  private static HashMap<String, Object> taskRmT = new HashMap<>();
  private static HashMap<String, Object> taskIaT = new HashMap<>();
  @Getter
  private String testCaseName;
  private int statusCode;
  private String pageNumber;
  private String pageSize;
  private String result;
  private String userName;
  private String fullName;
  private String idNumber;
  private String channel;
  private String startTime;
  private String slaEndTime;
  private String asignedBy;
  private String type;
  private String errorMessage;

  @BeforeClass
  public static void getTaskKYCData() {

    taskOnlineP = getTaskKYC("MAKER", "0", "PROCESSING");
    taskOnlineT = getTaskKYC("MAKER", "0", "TODO");
    taskOnlineCP = getTaskKYC("CHECKER", "0", "PROCESSING");
    taskOnlineCT = getTaskKYC("CHECKER", "0", "TODO");
    taskRmP = getTaskKYC("MAKER", "1", "PROCESSING");
    taskIaP = getTaskKYC("MAKER", "2", "PROCESSING");
    taskRmT = getTaskKYC("MAKER", "1", "TODO");
    taskIaT = getTaskKYC("MAKER", "2", "TODO");

  }

  public static HashMap<String, Object> getTaskKYC(String typeValue, String channelValue, String statusValue) {

    HashMap<String, Object> taskHM = new HashMap<>();
    if (!ObTask.getListByStatus(typeValue, channelValue, statusValue).isEmpty()) {
      ObTask obTask = ObTask.getListByStatus(typeValue, channelValue, statusValue).get(0);
      TcbsUser tcbsUser = TcbsUser.getById(obTask.getUserId());
      TcbsIdentification tcbsIdentification = TcbsIdentification.getByUserId(tcbsUser.getId().toString());
      ObAction obAction = ObAction.getByActionId(obTask.getActionId().toString());

      taskHM.put("taskId", obTask.getId());
      taskHM.put("taskReferId", obTask.getTaskRefId());
      taskHM.put("userId", tcbsUser.getId());
      taskHM.put("tcbsId", tcbsUser.getTcbsid());
      taskHM.put("fullName", tcbsUser.getLastname() + " " + tcbsUser.getFirstname());
      taskHM.put("userName", tcbsUser.getUsername());
      taskHM.put("channel", getChannel(channelValue));
      taskHM.put("idNumber", tcbsIdentification.getIdNumber());
      taskHM.put("startTime", covertDateToStringWithFormat(obTask.getSlaStartDatetime(), "yyyy-MM-dd"));
      taskHM.put("slaEndTime", covertDateToStringWithFormat(obTask.getSlaEndDatetime(), "yyyy-MM-dd"));
      taskHM.put("asignedBy", checkDataIsNullOrNot(obTask.getActor()));
      taskHM.put("actionName", obAction.getName());
      taskHM.put("type", obTask.getType());
    } else {
      taskHM = new HashMap<>();
    }


    return taskHM;
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api Search Task KYC List")
  public void verifySearchTaskKYCListTest() {

    System.out.println("TestcaseName : " + testCaseName);
    userName = getKeywordData("userName", userName);
    fullName = getKeywordData("fullName", fullName);
    idNumber = getKeywordData("idNumber", idNumber);
    channel = getKeywordData("channel", channel);
    startTime = getKeywordData("startTime", startTime);
    slaEndTime = getKeywordData("slaEndTime", slaEndTime);
    asignedBy = getKeywordData("asignedBy", asignedBy);
    type = getKeywordData("type", type);

    HashMap<String, Object> params = getSearchTaskKYCListParams();
    Response response = CallApiUtils.callGetApiHasParams(TCBSPROFILE_GET_TASK_KYC, "Authorization", "Bearer " + ASSIGN_TASK_TO_MAKER_KEY, params);

    assertThat(response.getStatusCode(), is(statusCode));
    if (response.statusCode() == 400) {
      String actualMessage = response.jsonPath().get("message");
      assertEquals(errorMessage, actualMessage);
    } else {
      if (response.jsonPath().get("message") != null) {
        String actualMessage = response.jsonPath().get("message");
        assertEquals(errorMessage, actualMessage);
      } else {
        switch (result) {
          case "validT":
          case "makerT":
            verifySearchTaskResult(response, taskOnlineT);
            break;
          case "validP":
          case "makerP":
            verifySearchTaskResult(response, taskOnlineP);
            break;
          case "RmP":
            verifySearchTaskResult(response, taskRmP);
            break;
          case "RmT":
            verifySearchTaskResult(response, taskRmT);
            break;
          case "IaP":
            verifySearchTaskResult(response, taskIaP);
            break;
          case "IaT":
            verifySearchTaskResult(response, taskIaT);
            break;
          case "checkerP":
            verifySearchTaskResult(response, taskOnlineCP);
            break;
          case "checkerT":
            verifySearchTaskResult(response, taskOnlineCT);
            break;
          default:
            System.out.println("Invalid Result : " + result);
            break;
        }

      }

    }


  }

  public HashMap<String, Object> getSearchTaskKYCListParams() {

    HashMap<String, Object> params = new HashMap<>();
    params.put("pageNumber", pageNumber);
    params.put("pageSize", pageSize);
    params.put("userName", userName);
    params.put("fullName", fullName);
    params.put("idNumber", idNumber);
    params.put("channel", channel);
    params.put("startTime", startTime);
    params.put("slaEndTime", slaEndTime);
    params.put("asignedBy", asignedBy);
    params.put("type", type);

    return params;
  }

  public String getKeywordData(String keyword, String keywordVal) {

    String data;
    if (keyword != null) {
      switch (keywordVal) {
        case "validT":
        case "makerT":
          data = checkDataIsNullOrNot(taskOnlineT.get(keyword));
          break;
        case "validP":
        case "makerP":
          data = checkDataIsNullOrNot(taskOnlineP.get(keyword));
          break;
        case "RmP":
          data = checkDataIsNullOrNot(taskRmP.get(keyword));
          break;
        case "RmT":
          data = checkDataIsNullOrNot(taskRmT.get(keyword));
          break;
        case "IaP":
          data = checkDataIsNullOrNot(taskIaP.get(keyword));
          break;
        case "IaT":
          data = checkDataIsNullOrNot(taskIaT.get(keyword));
          break;
        case "checkerP":
          data = checkDataIsNullOrNot(taskOnlineCP.get(keyword));
          break;
        case "checkerT":
          data = checkDataIsNullOrNot(taskOnlineCT.get(keyword));
          break;
        default:
          data = keywordVal;
          break;
      }
    } else {
      data = "";
    }
    return data;
  }

  public void verifySearchTaskResult(Response response, HashMap<String, Object> expectedResult) {

    List<HashMap<String, Object>> actualHM = response.jsonPath().getList("data.content");
    for (HashMap<String, Object> actual : actualHM) {
      if (actual.get("taskId").equals(expectedResult.get("taskId"))) {
        assertEquals(checkDataIsNullOrNot(actual.get("taskReferId")), checkDataIsNullOrNot(expectedResult.get("taskReferId")));
        assertEquals(checkDataIsNullOrNot(actual.get("userId")), checkDataIsNullOrNot(expectedResult.get("userId")));
        assertEquals(checkDataIsNullOrNot(actual.get("fullName")), checkDataIsNullOrNot(expectedResult.get("fullName")));
        assertEquals(checkDataIsNullOrNot(actual.get("userName")), checkDataIsNullOrNot(expectedResult.get("userName")));
        assertEquals(checkDataIsNullOrNot(convertTimestampToStringWithFormat(checkDataIsNullOrNot(actual.get("slaStartTime")), "yyyy-MM-dd")), checkDataIsNullOrNot(expectedResult.get("startTime")));
        assertEquals(checkDataIsNullOrNot(convertTimestampToStringWithFormat(checkDataIsNullOrNot(actual.get("slaEndTime")), "yyyy-MM-dd")), checkDataIsNullOrNot(expectedResult.get("slaEndTime")));
        assertEquals(checkDataIsNullOrNot(actual.get("actor")), checkDataIsNullOrNot(expectedResult.get("asignedBy")));
        assertEquals(checkDataIsNullOrNot(actual.get("actionName")), checkDataIsNullOrNot(expectedResult.get("actionName")));
        assertEquals(checkDataIsNullOrNot(actual.get("type")), checkDataIsNullOrNot(expectedResult.get("type")));
      }
    }
  }
}
