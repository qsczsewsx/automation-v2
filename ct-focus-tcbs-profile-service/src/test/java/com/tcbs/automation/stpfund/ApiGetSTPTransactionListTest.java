package com.tcbs.automation.stpfund;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.tools.StringUtils;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/stpfund/GetSTPTransactionList.csv", separator = '|')

public class ApiGetSTPTransactionListTest {
  @Getter
  private String testcaseName;
  private String status;
  private String step;
  private String type;
  private String product;
  private String channel;
  private String idNumber;
  private String code105C;
  private String pageSize;
  private String pageNumber;
  private String errorMsg;
  private int statusCode;
  private String errType;
  private int totalRow;
  private int totalpage;
  private String gen_status;
  private String isContent;

  @Before
  public void before() {
    pageSize = syncData(pageSize);
    pageNumber = syncData(pageNumber);
  }

  @Test
  @TestCase(name = "#testcaseName")
  @Title("Verify API get STP Transaction List")
  public void GetSTPTransactionListTest() {
    System.out.println("Testcase Name: " + testcaseName);
    Response response = given()
      .baseUri(GET_STP_TRANSACTION_LIST)
      .param("status", status)
      .param("step", step)
      .param("type", type)
      .param("product", product)
      .param("channel", channel)
      .param("idNumber", idNumber)
      .param("code105C", code105C)
      .param("pageSize", StringUtils.isNumber(pageSize) ? new BigDecimal(pageSize) : pageSize)
      .param("pageNumber", StringUtils.isNumber(pageNumber) ? new BigDecimal(pageNumber) : pageNumber)
      .param("errType", errType)
      .contentType("application/json")
      .header("Authorization", "Bearer " +
        (testcaseName.contains("Authorization key") ? FMB_X_API_KEY : STP_AUTHORIZATION_KEY))
      .when()
      .get();

    assertEquals(statusCode, response.getStatusCode());

    if (statusCode == 403) {
      assertEquals(errorMsg, response.jsonPath().get("errorMessage"));
    } else if (statusCode == 400) {
      assertEquals(errorMsg, response.jsonPath().get("message"));
    } else if (testcaseName.contains("pageSize is more than")) {
      totalRow = response.jsonPath().get("totalRow");
      totalpage = ((totalRow - 1) / Integer.valueOf(pageSize)) + 1;
      assertEquals(Integer.valueOf(totalpage).toString(), response.jsonPath().get("totalpage").toString());
    } else if (statusCode == 200) {
      if (!(isContent.equals("0"))) {
        assertThat("verify get transaction totalpage", response.jsonPath().get("totalpage"), is(notNullValue()));
        assertThat("verify get transaction content", response.jsonPath().get("content"), is(notNullValue()));
        assertThat("verify get transaction totalRow", response.jsonPath().get("totalRow"), is(notNullValue()));
      }
    }
  }
}
