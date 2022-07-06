package com.tcbs.automation.reminder;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsReminder;
import com.tcbs.automation.cas.TcbsReminderSchedule;
import com.tcbs.automation.cas.TcbsReminderSource;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/reminder/UpdateReminderSource.csv", separator = '|')
public class UpdateReminderSourceTest {
  HashMap<String, Object> body;
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String tcbsId;
  private int reminderId;
  private String action;
  private String value;
  private String key;
  private int reminderStatus;
  private String expectedContent;

  @Before
  public void setup() {

    tcbsId = syncData(tcbsId);
    action = syncData(action);
    value = syncData(value);
    ArrayList<Integer> reminderIds = new ArrayList<>();
    reminderIds.add(reminderId);
    if (testCaseName.contains("reminderIds has size not equal than 1")) {
      reminderIds.add(2);
    }

    HashMap<String, Object> params = new HashMap<>();
    params.put(key, value);

    body = new HashMap<>();
    body.put("tcbsId", tcbsId);
    body.put("reminderIds", reminderIds);
    body.put("action", action);
    body.put("params", params);

    if (testCaseName.contains("case valid action")) {
      TcbsReminder tcbsReminder = TcbsReminder.getById(BigDecimal.valueOf(reminderId));
      if (!"".equals(value)) {
        expectedContent = tcbsReminder.getTemplate().replace(key, value);
      }
      Date now = new Date();
      Calendar cal = Calendar.getInstance();
      cal.setTime(now);
      cal.add(Calendar.MONTH, 1);
      Date endTime = cal.getTime();
      TcbsReminder.updateStatusAndEndTimeById(reminderId, reminderStatus, endTime);
      TcbsReminderSchedule.deleteByTcbsIdAndReminderId(tcbsId, reminderId);
      TcbsReminderSource.deleteByTcbsIdAndReminderId(tcbsId, reminderId);
      if (!testCaseName.contains("create no source")) {
        TcbsReminderSource source = TcbsReminderSource.builder()
          .content(tcbsReminder.getTemplate())
          .createdDate(now)
          .updatedDate(now)
          .status(testCaseName.contains("inactive source") ? BigDecimal.valueOf(0) : BigDecimal.valueOf(1))
          .reminderId(BigDecimal.valueOf(reminderId))
          .expireTime(endTime)
          .tcbsId(tcbsId)
          .build();
        source.insert();
      }
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api update reminder source")
  public void performTest() {
    System.out.println(testCaseName);
    RequestSpecification requestSpecification = given()
      .baseUri(UPDATE_REMINDER_SOURCE)
      .contentType("application/json")
      .header("Authorization", "Bearer " +
        (testCaseName.contains("user has no permission") ? INTERNAL_PROINVESTOR : TOKEN_REMINDER));

    Response response;

    if (testCaseName.contains("missing BODY")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(body).post();
    }
    assertThat("verify status code", response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      verifyCase200();
    } else if (statusCode == 403) {
      assertThat("verify error message", response.jsonPath().get("errorMessage"), is(errorMessage));
    } else {
      assertThat("verify error message", response.jsonPath().get("message"), is(containsString(errorMessage)));
    }
  }

  private void verifyCase200() {
    TcbsReminderSource source = TcbsReminderSource.getByTcbsIdAndReminderId(tcbsId, reminderId);
    if (testCaseName.contains("create no source")) {
      assertNull(source);
    } else {
      if ("REMOVE".equals(action)) {
        assertEquals(BigDecimal.valueOf(0), source.getStatus());
      } else {
        if (expectedContent != null) {
          assertEquals(expectedContent, source.getContent());
        }
        if ("ADD".equals(action)) {
          assertEquals(BigDecimal.valueOf(1), source.getStatus());
          TcbsReminderSchedule schedule = TcbsReminderSchedule.getByTcbsIdAndReminderIdAndStatus(tcbsId, reminderId, 0);
          if (testCaseName.contains("create schedule")) {
            assertEquals(BigDecimal.valueOf(0), schedule.getStatus());
            assertEquals(expectedContent, schedule.getContent());
          } else {
            assertNull(schedule);
          }
        }
      }
    }
  }
}
