package com.tcbs.automation.resignContract;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.cas.TcbsApplicationUser;
import com.tcbs.automation.cas.TcbsNewOnboardingStatus;
import com.tcbs.automation.tools.ConvertUtils;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.junit.Assert.assertEquals;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/resignContract/ApiAddCustomerToList.csv", separator = '|')
public class ApiAddCustomerToListTest {

  private static String list105C_from_file;
  @Getter
  private String testCaseName;
  private int statusCode;
  private String list105C;
  private String errMess;
  private String dataSource;
  private String activatedStock;
  private int numberOf105C;
  private String fund;
  private String securities;
  private List<String> list105C_asList;

  /*
  Prepare values for test:
   - Case more than 100 105C: Do nothing cause the quantity is already invalid
   - Case 100 105C: Activate Quy and CK (with status key = 4/2 has status value=1)
   - Case 2 105C: Depend on testcase name to activate/unactivate Quy/CK
   - All cases: delete row with status RESIGN_STATUS
   */
  @Before
  public void before() {
    if (list105C.contains("src/test")) {
      list105C_from_file = ConvertUtils.fileTxtToString(list105C);
      list105C_asList = new ArrayList<>(Arrays.asList(list105C_from_file.split(",")));
      if (!fund.isEmpty() && !securities.isEmpty()) {
        TcbsApplicationUser.updateStatusAppByList(list105C_asList, "2", securities);
        TcbsApplicationUser.updateStatusAppByList(list105C_asList, "4", fund);
        TcbsNewOnboardingStatus.deleteByListAndStatusKey("RESIGN_STATUS", list105C_asList, null);
      }
    }

  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify API add customer to list that need to resign contract")
  public void apiAddCustomerToList() {
    System.out.println("Test Case: " + testCaseName);

    Gson gson = new Gson();
    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    body.put("list105C", list105C.contains("src/test") ? list105C_from_file : list105C);

    Response response = given()
      .baseUri(ADD_CUSTOMER_TO_LIST)
      .header("Authorization", "Bearer " + (statusCode == 403 ? API_KEY : TCBSPROFILE_CUSTODY_MAKER))
      .contentType("application/json")
      .body(gson.toJson(body))
      .when()
      .post();

    assertEquals(statusCode, response.getStatusCode());

    if (response.getStatusCode() == 400) {
      assertEquals(errMess, response.jsonPath().get("message"));
    } else if (response.getStatusCode() == 403) {
      assertEquals(errMess, response.jsonPath().get("errorMessage"));
    } else if (response.getStatusCode() == 200) {

      List<String> resultList;

      if (testCaseName.contains("both fund and securities unactivated")) {
        resultList = new ArrayList<>(Arrays.asList(response.jsonPath().get("data.failedList").toString().split(",")));
        assertEquals(numberOf105C, resultList.size());
      } else {
        resultList = new ArrayList<>(Arrays.asList(response.jsonPath().get("data.successList").toString().split(",")));
        assertEquals(numberOf105C, resultList.size());
      }
    }
  }

}
