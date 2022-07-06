package com.tcbs.automation.reminder;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsReminder;
import com.tcbs.automation.cas.TcbsReminderSchedule;
import com.tcbs.automation.cas.TcbsReminderSource;
import io.restassured.response.Response;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/reminder/ExecuteReminder.csv", separator = '|')
public class ExecuteReminderTest {
  private static final String CONTENT_TEST = "test";
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private int reminderId;
  private int reminderStatus;
  private int reminderEndTime;
  private String tcbsId;
  private int sourceStatus;
  private TcbsReminderSchedule oldSchedule;

  @Before
  public void before() throws Exception {
    if (testCaseName.contains("case valid request")) {
      Date now = new Date();
      Calendar cal = Calendar.getInstance();
      cal.setTime(now);
      cal.add(Calendar.MONTH, reminderEndTime);
      Date endTime = cal.getTime();
      TcbsReminder.updateStatusAndEndTimeById(reminderId, reminderStatus, endTime);
      if (statusCode == 200) {
        TcbsReminderSchedule.deleteByTcbsIdAndReminderId(tcbsId, reminderId);
        TcbsReminderSource.deleteByTcbsIdAndReminderId(tcbsId, reminderId);
        TcbsReminderSource source = TcbsReminderSource.builder()
          .content(CONTENT_TEST)
          .createdDate(now)
          .updatedDate(now)
          .status(BigDecimal.valueOf(sourceStatus))
          .reminderId(BigDecimal.valueOf(reminderId))
          .expireTime(endTime)
          .tcbsId(tcbsId)
          .build();
        source.insert();
        if (testCaseName.contains("create schedule")) {
          oldSchedule = TcbsReminderSchedule.builder()
            .intendTime(now)
            .status(BigDecimal.valueOf(0))
            .content(CONTENT_TEST)
            .createdDate(now)
            .sourceId(source.getId())
            .build();
          oldSchedule.insert();
        }
      }
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api execute reminder")
  public void performTest() {
    System.out.println(testCaseName);
    Response response = given()
      .baseUri(EXECUTE_REMINDER.replace("#reminderId#", String.valueOf(reminderId)))
      .header("x-api-key", testCaseName.contains("case invalid x-api-key") ? ASSIGN_TASK_TO_AMOPS_MAKER : PROFILE_X_API_KEY)
      .get();

    assertThat("verify status code", response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      TcbsReminderSchedule schedule = TcbsReminderSchedule.getByTcbsIdAndReminderIdAndStatus(tcbsId, reminderId, 0);
      if (testCaseName.contains("create no schedule")) {
        assertNull(schedule);
      } else {
        assertNotNull(schedule);
        assertEquals(BigDecimal.valueOf(0), schedule.getStatus());
        assertEquals(CONTENT_TEST, schedule.getContent());
        oldSchedule = TcbsReminderSchedule.getById(oldSchedule.getId());
        assertNotNull(oldSchedule);
        assertEquals(BigDecimal.valueOf(1), oldSchedule.getStatus());
      }
    } else {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }
}