package com.tcbs.automation.resignContract;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.cas.TcbsNewOnboardingStatus;
import com.tcbs.automation.cas.TcbsUser;
import com.tcbs.automation.tools.ConvertUtils;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.junit.Assert.assertEquals;

/*
API used to remove customer from list containing customers need to resign contract
User can enter multiple 105C codes
 */

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/resignContract/ApiRemoveCustomerFromList.csv", separator = '|')
public class ApiRemoveCustomerFromListTest {

  private static String list_105C;
  @Getter
  private String testCaseName;
  private int statusCode;
  private String list105C;
  private String note;
  private String errMess;
  private String dataSource;
  private String gen_105code;
  private List<String> list105C_asList = new ArrayList<>();

  /*
  Prepare data test:
  105C needs to already in list:
  - Status: RESIGN_STATUS 1= CANCELED
   */
  @Before
  public void before() {
    if (dataSource.equalsIgnoreCase("fromFileMoreThan100")) {
      list_105C = ConvertUtils.fileTxtToString("src/test/resources/data/resignContract/MoreThan100_105Ccode.txt");
      list105C_asList = new ArrayList<>(Arrays.asList(list_105C.split(",")));

    } else if (dataSource.equalsIgnoreCase("fromFile100")) {
      list_105C = ConvertUtils.fileTxtToString("src/test/resources/data/resignContract/List100_105Ccode.txt");
      list105C_asList = new ArrayList<>(Arrays.asList(list_105C.split(",")));
      TcbsNewOnboardingStatus.updateByStatusKeyAndList105C(list105C_asList, "RESIGN_STATUS", "WAIT_CUSTOMER");

    } else if (dataSource.equalsIgnoreCase("gen")) {
      BigDecimal gen_userId = TcbsNewOnboardingStatus.getByStatusKeyAndStatusValue("RESIGN_STATUS", "CANCELED").get(0).getUserId();
      list105C = TcbsUser.getById(gen_userId).getUsername();
      list105C_asList.add(list105C);

    } else if (dataSource.equalsIgnoreCase("csv")) {
      list105C_asList = new ArrayList<>(Arrays.asList(list105C.split(",")));
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify API remove customer from list that needs to resign contract")
  public void apiRemoveCustomerFromList() {
    System.out.println("Test case: " + testCaseName);

    Gson gson = new Gson();
    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    body.put("list105C", (!dataSource.isEmpty()) ? list105C_asList : list105C);
    body.put("note", note);

    Response response = given()
      .baseUri(REMOVE_CUSTOMER_FROM_LIST)
      .contentType("application/json")
      .header("Authorization", "Bearer " + (statusCode == 403 ? FMB_X_API_KEY : TCBSPROFILE_CUSTODY_MAKER))
      .body(gson.toJson(body))
      .when()
      .post();

    assertEquals(statusCode, response.getStatusCode());
  }

}
