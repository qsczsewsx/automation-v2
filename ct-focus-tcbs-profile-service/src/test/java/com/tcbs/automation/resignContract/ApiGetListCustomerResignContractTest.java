package com.tcbs.automation.resignContract;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/resignContract/ApiGetListCustomerResignContract.csv", separator = '|')
public class ApiGetListCustomerResignContractTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String pageSize;
  private String pageNumber;
  private String status;
  private String createdDate;
  private String flowOpenAccount;
  private String code105C;
  private String idNumber;
  private String errMess;

  @Before
  public void before() {
    pageSize = syncData(pageSize);
    pageNumber = syncData(pageNumber);
    status = syncData(status);
    flowOpenAccount = syncData(flowOpenAccount);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api get list of customers who need to resign contract")
  public void apiGetListCustomerResignContract() {
    System.out.println("Test Case: " + testCaseName);

    Response response = given()
      .baseUri(GET_CUSTOMER_LIST)
      .header("Authorization", "Bearer " + (statusCode == 403 ? API_KEY : ASSIGN_TASK_TO_AMOPS_MAKER))
      .param("pageSize", pageSize.isEmpty() ? null : pageSize)
      .param("pageNumber", pageNumber.isEmpty() ? null : pageNumber)
      .param("status", status.isEmpty() ? null : status)
      .param("createdDate", createdDate.isEmpty() ? null : createdDate)
      .param("flowOpenAccount", flowOpenAccount.isEmpty() ? null : flowOpenAccount)
      .param("code105C", code105C.isEmpty() ? null : code105C)
      .param("idNumber", idNumber.isEmpty() ? null : idNumber)
      .when()
      .get();

    assertEquals(statusCode, response.getStatusCode());

    if (response.getStatusCode() == 400) {
      assertEquals(errMess, response.jsonPath().get("message"));
    } else if (response.getStatusCode() == 403) {
      assertEquals(errMess, response.jsonPath().get("errorMessage"));
    } else if (response.getStatusCode() == 200) {
      assertThat("Total rows in response body", response.jsonPath().get("totalRow"), not(0));
    }

  }
}
