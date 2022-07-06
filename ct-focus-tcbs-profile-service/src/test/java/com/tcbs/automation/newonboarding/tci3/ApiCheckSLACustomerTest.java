package com.tcbs.automation.newonboarding.tci3;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.ObConfig;
import com.tcbs.automation.cas.ObTask;
import com.tcbs.automation.cas.TcbsNewOnboardingStatus;
import com.tcbs.automation.cas.TcbsUser;
import com.tcbs.automation.login.LoginApi;
import com.tcbs.automation.login.TheUserInfo;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.screenplay.Actor;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.CALCULATE_SLA_FOR_OB;
import static common.CallApiUtils.*;
import static common.CommonUtils.*;
import static common.DatesUtils.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/ApiCheckSLACustomer.csv", separator = '|')
public class ApiCheckSLACustomerTest {

  private static String userId;
  private static String taskId;
  private final LinkedHashMap<String, Object> body = new LinkedHashMap<>();
  private final TcbsNewOnboardingStatus tcbsNewOnboardingStatus = new TcbsNewOnboardingStatus();
  private final TcbsUser tcbsUser = new TcbsUser();
  @Getter
  private String testCaseName;
  @Getter
  private String tcbsId;
  private int statusCode;
  private String erroMsg;
  private String token;

  @BeforeClass
  public static void setDataFromDB() {
    checkTaskExist("5260177");
    HashMap<String, Object> data = new HashMap<>();
    data.put("ID_STATUS", "WAIT_FOR_VERIFY");
    data.put("BANK_INFO_STATUS", "WAIT_FOR_VERIFY");
    data.put("EKYC_STATUS", "WAIT_FOR_VERIFY");
    data.put("ECONTRACT_STATUS", "WAIT_FOR_VERIFY");
    setDataToDB("5260177", data);
  }

  public static void setDataToDB(String userId, HashMap<String, Object> setData) {
    for (HashMap.Entry<String, Object> entry : setData.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue().toString();
      TcbsNewOnboardingStatus.updateStatusValueByUserId(userId, key, value);
    }
  }

  @Before
  public void getUserLoginToken() {
    Actor actor = Actor.named("trungnd10");
    String userName = get105CFromTcbsId(tcbsId);
    LoginApi.withCredentials(userName, "abc123").performAs(actor);
    token = TheUserInfo.aboutLoginData().answeredBy(actor).getToken();
  }

  public String updateDataDependOnTestCase(String testCaseName) {
    String taskId = "";
    if (testCaseName.contains("both idNumber and video")) {
      taskId = createTaskNewOBAndAssignToMaker("10000000744");
    }
    return taskId;
  }

  public Timestamp getWorkingTime(Timestamp currentTime, int plusDay, String value) {

    String currentDate = addPlusDay(currentTime.toString().split(" ")[0], plusDay);
    return convertStringToTimestamp(currentDate + " " + value);
  }

  public boolean checkWorkingTime(Timestamp currentTime, Timestamp starTime, Timestamp endTime) {
    return currentTime.compareTo(starTime) > 0 && currentTime.compareTo(endTime) < 0;
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api Check SL ACustomer")
  public void verifyApiCheckSLACustomer() {

    System.out.println("TestCaseName : " + testCaseName);

    taskId = updateDataDependOnTestCase(testCaseName);
    System.out.println("Task ID : " + taskId);

    Response response = callGetApiHasNoParams(CALCULATE_SLA_FOR_OB.replace("{tcbsId}", tcbsId), "Authorization", "Bearer " + token);

    assertThat(response.getStatusCode(), is(statusCode));

    if (response.statusCode() == 200) {
      if (testCaseName.contains("both idNumber and video")) {
        String actualData = response.jsonPath().getString("data");
        long slaDueDateChecker = Long.parseLong(ObConfig.getByCode("SLA_TASK_DUEDATE_CHECKER").getValue());
        long slaDueDateVSD = Long.parseLong(ObConfig.getByCode("SLA_TASK_DUEDATE_VSD").getValue());

        Timestamp currentSlaEndTime = ObTask.getByTaskId(taskId).getSlaEndDatetime();
        System.out.println("Maker SLA End Time : " + currentSlaEndTime);

        Timestamp checker = getDatePlusMinutes(currentSlaEndTime, slaDueDateChecker * 60 * 1000);
        Timestamp plusChecker = checkSLATime(currentSlaEndTime, checker);
        System.out.println("Plus Checker Atual : " + checker);
        System.out.println("Plus Checker Calc : " + plusChecker);

        Timestamp vsd = getDatePlusMinutes(plusChecker, slaDueDateVSD * 60 * 1000);
        Timestamp plusVsd = checkSLATime(plusChecker, vsd);
        System.out.println("Plus vsd Atual : " + vsd);
        System.out.println("Plus vsd Calc : " + covertTimeStampToStringDate(plusVsd));
        System.out.println("Actual Time : " + actualData);
        assertEquals(covertTimeStampToStringDate(plusVsd), actualData);

      }


    } else if (response.statusCode() == 400) {
      String actualData = response.jsonPath().getString("data");
      assertEquals(checkDataIsNullOrNot(erroMsg), actualData);
    }
  }

  public String get105CFromTcbsId(String tcbsId) {
    String expected;
    if (tcbsId.equalsIgnoreCase("10000000744")) {
      expected = "105C493897";
    } else {
      TcbsUser tcbsUser = TcbsUser.getByTcbsId(tcbsId);
      expected = tcbsUser.getUsername();
    }
    return expected;
  }

  public Timestamp checkSLATime(Timestamp currentTime, Timestamp plusTime) {

    int plusDay = getDayOfWeek(plusTime);
    String startWorking = "09:00:00";
    String startBeak = "12:00:00";
    String endBreak = "13:30:00";
    String endWorking = "18:00:00";
    Timestamp ts = null;
    while (true) {
      if (plusDay == 7) {
        Timestamp beforeEndWorkingTime = getWorkingTime(plusTime, -1, endWorking);
        Timestamp afterStartWorkingTime = getWorkingTime(plusTime, 0, startWorking);
        Timestamp afterStartBreakTime = getWorkingTime(plusTime, 0, startBeak);
        if (checkWorkingTime(plusTime, beforeEndWorkingTime, afterStartWorkingTime)) {
          ts = getDatePlusMinutes(afterStartWorkingTime, getSubtractionMinutes(plusTime, beforeEndWorkingTime));
          if (checkWorkingTime(ts, afterStartWorkingTime, beforeEndWorkingTime)) {
            ts = new Timestamp(ts.getTime());
            break;
          }
        } else if (checkWorkingTime(plusTime, afterStartWorkingTime, afterStartBreakTime)) {
          assert ts != null;
          ts = new Timestamp(ts.getTime() + getSubtractionMinutes(afterStartWorkingTime, currentTime));
          if (checkWorkingTime(ts, afterStartWorkingTime, afterStartBreakTime)) {
            ts = new Timestamp(ts.getTime());
            break;
          }
        } else {
          Timestamp startWorkingTime01 = getWorkingTime(plusTime, 2, startWorking);
          Timestamp startBreakTime01 = getWorkingTime(plusTime, 2, startBeak);
          Timestamp endBreakTime01 = getWorkingTime(plusTime, 2, endBreak);
          Timestamp endWorkingTime01 = getWorkingTime(plusTime, 2, endWorking);
          ts = getDatePlusMinutes(startWorkingTime01, getSubtractionMinutes(plusTime, afterStartBreakTime));
          if (checkWorkingTime(ts, startWorkingTime01, startBreakTime01)) {
            ts = new Timestamp(ts.getTime());
            break;
          } else if (checkWorkingTime(ts, startBreakTime01, endBreakTime01)) {
            ts = getDatePlusMinutes(endBreakTime01, getSubtractionMinutes(ts, startBreakTime01));
            break;
          } else if (checkWorkingTime(ts, endBreakTime01, endWorkingTime01)) {
            ts = getDatePlusMinutes(endBreakTime01, getSubtractionMinutes(endBreakTime01, startBreakTime01));
            break;
          }
        }
      } else {
        Timestamp startWorkingTime = getWorkingTime(plusTime, 0, startWorking);
        Timestamp startBreakTime = getWorkingTime(plusTime, 0, startBeak);
        Timestamp endBreakTime = getWorkingTime(plusTime, 0, endBreak);
        Timestamp endWorkingTime = getWorkingTime(plusTime, 0, endWorking);
        if (checkWorkingTime(currentTime, startWorkingTime, startBreakTime)) {
          if (checkWorkingTime(plusTime, startWorkingTime, startBreakTime)) {
            ts = plusTime;
            break;
          } else if (checkWorkingTime(plusTime, startBreakTime, endBreakTime)) {
            ts = new Timestamp(endBreakTime.getTime() + getSubtractionMinutes(plusTime, startBreakTime));
            if (ts.compareTo(endWorkingTime) < 0) {
              ts = new Timestamp(ts.getTime());
            } else {
              ts = getSLAWorkingTime(ts, startWorking, startBeak, endBreak, endWorking, endWorkingTime);
            }
            break;
          } else if (checkWorkingTime(plusTime, endBreakTime, endWorkingTime)) {
            ts = new Timestamp(plusTime.getTime() + getSubtractionMinutes(endBreakTime, startBreakTime));
            if (ts.compareTo(endWorkingTime) < 0) {
              ts = new Timestamp(ts.getTime());
            } else {
              ts = getSLAWorkingTime(ts, startWorking, startBeak, endBreak, endWorking, endWorkingTime);
            }
            break;
          } else {
            ts = getSLAWorkingTime(plusTime, startWorking, startBeak, endBreak, endWorking, endWorkingTime);
            break;
          }
        } else if (checkWorkingTime(currentTime, startBreakTime, endBreakTime)) {
          if (checkWorkingTime(plusTime, startWorkingTime, startBreakTime)) {
            ts = plusTime;
            break;
          } else if (checkWorkingTime(plusTime, startBreakTime, endBreakTime)) {
            ts = new Timestamp(plusTime.getTime() + getSubtractionMinutes(plusTime, startBreakTime));
            if (ts.compareTo(endWorkingTime) < 0) {
              ts = new Timestamp(ts.getTime());
            } else {
              ts = getSLAWorkingTime(ts, startWorking, startBeak, endBreak, endWorking, endWorkingTime);
            }
            break;
          } else if (checkWorkingTime(plusTime, endBreakTime, endWorkingTime)) {
            ts = new Timestamp(plusTime.getTime() + getSubtractionMinutes(endBreakTime, startBreakTime));
            if (ts.compareTo(endWorkingTime) < 0) {
              ts = new Timestamp(ts.getTime());
            } else {
              ts = getSLAWorkingTime(ts, startWorking, startBeak, endBreak, endWorking, endWorkingTime);
            }
            break;
          } else {
            ts = getSLAWorkingTime(plusTime, startWorking, startBeak, endBreak, endWorking, endWorkingTime);
            break;
          }

        } else {
          if (checkWorkingTime(currentTime, endBreakTime, endWorkingTime)) {
            if (checkWorkingTime(plusTime, endBreakTime, endWorkingTime)) {
              ts = plusTime;
            } else {
              ts = getSLAWorkingTime(plusTime, startWorking, startBeak, endBreak, endWorking, endWorkingTime);
            }
            break;
          }
        }
      }
    }
    return ts;
  }

  public Timestamp getSLAWorkingTime(Timestamp plusTime, String startWorking, String startBeak, String endBreak, String endWorking, Timestamp endWorkingTime) {

    Timestamp expectTime = null;
    Timestamp startWorkingTime01 = getWorkingTime(plusTime, 1, startWorking);
    Timestamp startBreakTime01 = getWorkingTime(plusTime, 1, startBeak);
    Timestamp endBreakTime01 = getWorkingTime(plusTime, 1, endBreak);
    Timestamp endWorkingTime01 = getWorkingTime(plusTime, 1, endWorking);
    Timestamp ts = getDatePlusMinutes(startWorkingTime01, getSubtractionMinutes(plusTime, endWorkingTime));
    if (getDayOfWeek(ts) == 7) {
      if (checkWorkingTime(ts, startWorkingTime01, startBreakTime01)) {
        expectTime = new Timestamp(ts.getTime());
      } else {
        Timestamp startWorkingTime02 = getWorkingTime(plusTime, 3, startWorking);
        Timestamp startBreakTime02 = getWorkingTime(plusTime, 3, startBeak);
        Timestamp endBreakTime02 = getWorkingTime(plusTime, 3, endBreak);
        Timestamp endWorkingTime02 = getWorkingTime(plusTime, 3, endWorking);
        ts = getDatePlusMinutes(startWorkingTime02, getSubtractionMinutes(ts, startBreakTime01));
        if (checkWorkingTime(ts, startWorkingTime02, startBreakTime02)) {
          expectTime = new Timestamp(ts.getTime());
        } else if (checkWorkingTime(ts, startBreakTime02, endBreakTime02)) {
          expectTime = getDatePlusMinutes(endBreakTime01, getSubtractionMinutes(ts, startBreakTime01));
        } else if (checkWorkingTime(ts, endBreakTime02, endWorkingTime02)) {
          expectTime = getDatePlusMinutes(endBreakTime01, getSubtractionMinutes(endBreakTime01, startBreakTime01));
        }
      }
    } else {
      if (checkWorkingTime(ts, startWorkingTime01, startBreakTime01)) {
        expectTime = new Timestamp(ts.getTime());
      } else if (checkWorkingTime(ts, startBreakTime01, endBreakTime01)) {
        expectTime = getDatePlusMinutes(endBreakTime01, getSubtractionMinutes(ts, startBreakTime01));
      } else if (checkWorkingTime(ts, endBreakTime01, endWorkingTime01)) {
        expectTime = getDatePlusMinutes(endBreakTime01, getSubtractionMinutes(endBreakTime01, startBreakTime01));
      }
    }

    return expectTime;
  }

}
