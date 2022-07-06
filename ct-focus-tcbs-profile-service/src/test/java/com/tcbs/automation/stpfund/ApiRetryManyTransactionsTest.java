package com.tcbs.automation.stpfund;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.cas.TcbsApplicationUser;
import com.tcbs.automation.cas.VsdTransaction;
import com.tcbs.automation.tools.ConvertUtils;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/stpfund/RetryManyTransactions.csv", separator = '|')

public class ApiRetryManyTransactionsTest {
  @Getter
  private String testcaseName;
  private List<Integer> listOfID;
  private String idList;
  private int statusCode;
  private String errorMsg;
  private int stpStatus;
  private String stpMode;
  private int size;
  private static final String LESSTHAN_500_IDS = ConvertUtils.fileTxtToString("src/test/resources/data/stpfund/LessThan500Ids.txt");
  private static final String CONTAIN_500_IDS = ConvertUtils.fileTxtToString("src/test/resources/data/stpfund/Contain500Ids.txt");
  private static final String MORETHAN_500_IDS = ConvertUtils.fileTxtToString("src/test/resources/data/stpfund/MoreThan500Ids.txt");
  private static final Logger logger = LoggerFactory.getLogger(ConvertUtils.class);
  private String userId;
  private static final String FAILED_LIST = "failedList";

  @Before
  public void setup() {
    idList = syncData(idList);
    listOfID = new ArrayList<>();
    if (testcaseName.contains("mode is off")) {
      ApiRetrySTPTransactionTest.setStpMode("OFF");
    } else {
      ApiRetrySTPTransactionTest.setStpMode("ON");
    }

    List<String> listIdMoreThan500 = new ArrayList<>(Arrays.asList(MORETHAN_500_IDS.split(",")));
    List<String> listID500 = new ArrayList<>(Arrays.asList(CONTAIN_500_IDS.split(",")));
    List<String> listIdLessThan500 = new ArrayList<>(Arrays.asList(LESSTHAN_500_IDS.split(",")));
    System.out.println(idList);
    if (!(StringUtils.isBlank(idList))) {
      listOfID.add(Integer.valueOf(idList));
      if (testcaseName.contains("successList")) {
        VsdTransaction vsdTransaction = VsdTransaction.getById(idList);
        userId = String.valueOf(vsdTransaction.getUserId());
        VsdTransaction.updateStatus(userId, String.valueOf(stpStatus));
        if (testcaseName.contains("user has been activated")) {
          TcbsApplicationUser.updateStatusApp(userId, "4", "1");
        } else {
          TcbsApplicationUser.updateStatusApp(userId, "4", "2");
        }
      }
    } else {
      if (testcaseName.contains("less than 500 ids")) {

        listOfID = ConvertUtils.parseStringArrayToInt(listIdLessThan500);
        VsdTransaction.updateStatusByListIds(listOfID, stpStatus);

      } else if (testcaseName.contains("has 500 ids")) {

        listOfID = ConvertUtils.parseStringArrayToInt(listID500);
        VsdTransaction.updateStatusByListIds(listOfID, stpStatus);

      } else if (testcaseName.contains("more than 500 ids")) {

        listOfID = ConvertUtils.parseStringArrayToInt(listIdMoreThan500);
        VsdTransaction.updateStatusByListIds(listOfID, stpStatus);
      }
    }

  }

  @Test
  @TestCase(name = "#testcaseName")
  @Title("Verify API retry may transactions")
  public void RetryManyTransactions() {
    System.out.println("Testcase Name: " + testcaseName);

    LinkedHashMap<String, Object> bodyRetry = new LinkedHashMap<>();
    Gson gson = new Gson();

    bodyRetry.put("idList", listOfID);

    Response response = given()
      .baseUri(RETRY_MANY_TRANSACTIONS)
      .header("Authorization", "Bearer " +
        (testcaseName.contains("Authorization key") ? FMB_X_API_KEY : STP_AUTHORIZATION_KEY))
      .contentType("application/json")
      .body(gson.toJson(bodyRetry))
      .when()
      .post();

    assertEquals(statusCode, response.getStatusCode());
    if (statusCode == 200) {
      if (testcaseName.contains(FAILED_LIST)) {
        listOfID = response.jsonPath().get(FAILED_LIST);
        assertThat(listOfID.size(), is(size));
      } else {
        int successList = ((List<Integer>) response.jsonPath().get("successList")).size();
        int failedList = ((List<Integer>) response.jsonPath().get(FAILED_LIST)).size();
        assertEquals(failedList + successList, size);
        if (testcaseName.contains("depend on VSD")) {
          boolean isSuccess = ApiRetrySTPTransactionTest.verifyRetryTransactionDependOnVsd(Integer.parseInt(idList), userId);
          if (isSuccess) {
            assertEquals(successList, size);
          } else {
            assertEquals(failedList, size);
          }
        }
      }
    } else if (statusCode == 403) {
      assertEquals(errorMsg, response.jsonPath().get("errorMessage"));
    } else if (statusCode == 400) {
      assertEquals(errorMsg, response.jsonPath().get("message"));
    }
  }
}


