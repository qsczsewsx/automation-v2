package com.tcbs.automation.proinvestor;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.ObRetry;
import com.tcbs.automation.cas.TcbsProInvestorDocument;
import com.tcbs.automation.cas.TcbsUser;
import common.CallApiUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/proinvestor/JobSyncProInvestorFromAni.csv", separator = '|')
public class JobSyncProInvestorFromAniTest {

  @Getter
  private String testCaseName;
  private String date;
  private int statusCode;
  private String errorMsg;
  private final String userId = TcbsUser.getByUserName("105C123456").getId().toString();
  private final String status = "ACTIVE";
  private final String retryAction = "SYNC_PROTRADER_FROM_ANI";

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api sync pro investor from ani")
  public void performTest() {
    System.out.println("Testcase Name: " + testCaseName);
    date = syncData(date);

    RequestSpecification requestSpecification = given()
      .baseUri(SYNC_PROINVESTOR_FROM_ANI)
      .contentType("application/json")
      .header("x-api-key",
        (testCaseName.contains("having no permission") ? ASSIGN_TASK_TO_MAKER_KEY : INTERNAL_PROINVESTOR));

    Response response;
    if (testCaseName.contains("missing param")) {
      response = requestSpecification.get();
    } else {
      response = requestSpecification.param("date", date).get();
    }

    assertThat(response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      TcbsProInvestorDocument tcbsProInvestorDocument = TcbsProInvestorDocument.getProInvestorByUserIdAndStatus(userId, status);
      assertThat(tcbsProInvestorDocument.getDocumentType(), is("TOTAL_ASSETS"));
      assertThat(tcbsProInvestorDocument.getReason(), is(notNullValue()));
      LocalDate startDateExpected = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")).plusDays(1);
      LocalDate endDateExpected = startDateExpected.plusMonths(3).minusDays(1);
      LocalDate startDateActual = tcbsProInvestorDocument.getStartDate().toLocalDateTime().toLocalDate();
      LocalDate endDateActual = tcbsProInvestorDocument.getEndDate().toLocalDateTime().toLocalDate();
      assertEquals(startDateExpected, startDateActual);
      assertEquals(endDateExpected, endDateActual);
    } else if (statusCode == 500) {
      if (date.isEmpty()) {
        date = LocalDate.now().minusDays(1).toString();
      }
      assertNotNull(ObRetry.getByActionAndErrorCode(retryAction, date).getId());
    } else {
      assertEquals(errorMsg, response.jsonPath().get("message"));
    }
  }

  @After
  public void clearData() {
    if (statusCode == 200) {
      TcbsProInvestorDocument.deleteProInvestorByUserIdAndStatus(userId, status);
      CallApiUtils.clearCache(DELETE_CACHE, "x-api-key", API_KEY);
    } else if (statusCode == 500) {
      ObRetry.deleteByActionAndErrorCode(retryAction, date);
    }
  }
}