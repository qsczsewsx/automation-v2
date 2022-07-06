package com.tcbs.automation.proinvestor;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsProInvestorDocument;
import com.tcbs.automation.cas.TcbsUser;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.RestRequests.given;
import static org.junit.Assert.assertEquals;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/ApiGetEndDateOfProInvestor.csv", separator = '|')
public class ApiGetEndDateOfProInvestorTest {

  private static LocalDateTime currentDate;
  private static DateTimeFormatter formatter;
  @Getter
  private String testCaseName;
  private int statusCode;
  private String tcbsId;
  private String status;
  private String errMess;
  private String dbDate;
  private String gen_tcbsId;

  @BeforeClass
  public static void beforeClass() {
    formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    currentDate = LocalDateTime.now();
  }

  @Before
  public void before() {
    tcbsId = syncData(tcbsId);

    String userId = TcbsProInvestorDocument.getListOfProInvestorByStatus("ACTIVE").get(0).getUserId().toString();
    gen_tcbsId = TcbsUser.getById(new BigDecimal(userId)).getTcbsid();
    if (tcbsId.equalsIgnoreCase("gen")) {
      tcbsId = gen_tcbsId;
    }
    if (status != null) {
      if (dbDate.equalsIgnoreCase("today")) {
        TcbsProInvestorDocument.updateEndDateAndStatusByUserId(formatter.format(currentDate), status, userId);
      } else if (dbDate.equalsIgnoreCase("past")) {
        TcbsProInvestorDocument.updateEndDateAndStatusByUserId(formatter.format(currentDate.plusYears(-1)), status, userId);
      } else if (dbDate.equalsIgnoreCase("future")) {
        TcbsProInvestorDocument.updateEndDateAndStatusByUserId(formatter.format(currentDate.plusYears(1)), status, userId);
      }
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api get pro investor end date")
  public void apiGetProinvestorEnddate() {
    System.out.println("Test Case: " + testCaseName);

    Response response = given()
      .baseUri(GET_PROINVESTOR_END_DATE.replaceAll("#tcbsId#", (tcbsId == null) ? null : tcbsId))
      .header("x-api-key", statusCode == 401 ? FMB_X_API_KEY : API_KEY)
      .when()
      .get();

    assertEquals(statusCode, response.getStatusCode());

    if (statusCode == 401 || statusCode == 400) {
      assertEquals(errMess, response.jsonPath().get("message"));
    } else {
      if (status.equalsIgnoreCase("active") && !dbDate.equalsIgnoreCase("past")) {
        assertEquals("1", response.jsonPath().get("isProInvestor"));
      } else if (status.equalsIgnoreCase("inactive")) {
        assertEquals("0", response.jsonPath().get("isProInvestor"));
      }
    }
  }

}
