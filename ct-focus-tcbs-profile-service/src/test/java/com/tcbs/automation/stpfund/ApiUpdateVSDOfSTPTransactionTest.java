package com.tcbs.automation.stpfund;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.cas.TcbsApplicationUser;
import com.tcbs.automation.cas.TcbsUser;
import com.tcbs.automation.cas.VsdTransaction;
import com.tcbs.automation.tools.ConvertUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/stpfund/UpdateVSDOfSTPTransaction.csv", separator = '|')

public class ApiUpdateVSDOfSTPTransactionTest {
  private static final Logger logger = LoggerFactory.getLogger(ConvertUtils.class);
  private static final String LESSTHAN_500 = ConvertUtils.fileTxtToString("src/test/resources/data/stpfund/Lessthan500.txt");
  private static final String CONTAIN_500 = ConvertUtils.fileTxtToString("src/test/resources/data/stpfund/Contain500.txt");
  private static final String MORETHAN_500 = ConvertUtils.fileTxtToString("src/test/resources/data/stpfund/Morethan500.txt");
  private static List<String> listCode105C;
  @Getter
  private String testcaseName;
  private String code105CList;
  private String note;
  private int statusCode;
  private String errorMsg;
  private int size;
  private int appUserStatus;
  private int stpStatus;
  private int appId;
  private BigDecimal userId;

  @Before
  public void setup() {
    List<TcbsApplicationUser> userList = new ArrayList<>();

    List<String> listCodeMoreThan500 = new ArrayList<>(Arrays.asList(MORETHAN_500.split(",")));
    List<String> listCode500 = new ArrayList<>(Arrays.asList(CONTAIN_500.split(",")));

    if (testcaseName.contains("code105CList is empty")) {
      listCode105C = new ArrayList<>();
    } else if (testcaseName.contains("less than 500")) {
      listCode105C = new ArrayList<>(Arrays.asList(LESSTHAN_500.split(",")));
    } else if (testcaseName.contains("contains 500 values")) {
      listCode105C = new ArrayList<>(Arrays.asList(CONTAIN_500.split(",")));
    } else if (testcaseName.contains("more than 500 values")) {
      listCode105C = new ArrayList<>(Arrays.asList(MORETHAN_500.split(",")));
    } else {
      listCode105C = new ArrayList<>(Arrays.asList(code105CList));
    }

    if (listCode105C.size() > 1) {
      TcbsApplicationUser.updateStatusAppByList(listCode105C, "4", "2");
      VsdTransaction.updateStatusByList(listCode105C, "2");
    } else if (!(code105CList.isEmpty())) {
      TcbsApplicationUser.updateStatusAppByList(listCode105C, Integer.toString(appId), Integer.toString(appUserStatus));
      VsdTransaction.updateStatusByList(listCode105C, Integer.toString(stpStatus));
    }

  }

  @Test
  @TestCase(name = "#testcaseName")
  @Title("Verify API update VSD Of STP Transaction")
  public void UpdateVSDOfSTPTransaction() {
    System.out.println("Testcase Name: " + testcaseName);
    Gson gson = new Gson();

    LinkedHashMap<String, Object> body1 = new LinkedHashMap<>();
    body1.put("code105CList", listCode105C);
    body1.put("note", note);

    RequestSpecification responseSpecification = given()
      .baseUri(UPDATE_VSD_OF_STP_TRANSACTION)
      .header("Authorization", "Bearer " +
        (testcaseName.contains("Maker role") ? ASSIGN_TASK_TO_AMOPS_MAKER : STP_AUTHORIZATION_KEY))
      .contentType("application/json");

    Response response;

    if (testcaseName.contains("body is missing")) {
      response = responseSpecification.post();
    } else {
      response = responseSpecification.body(body1).post();
    }

    assertThat("verify status code", response.getStatusCode(), is(statusCode));


    if (statusCode == 200) {
      if (testcaseName.contains("successList")) {
        List<String> list105c = response.jsonPath().get("data.successList");
        assertThat(list105c.size(), is(size));

      } else if (testcaseName.contains("failedList")) {
        List<String> list105c = response.jsonPath().get("data.failedList");
        assertThat(list105c.size(), is(size));
      }
    } else if (statusCode == 403) {
      assertEquals(errorMsg, response.jsonPath().get("errorMessage"));
    } else {
      assertEquals(errorMsg, response.jsonPath().get("message"));

    }

    if (!(code105CList.isEmpty()) && !(testcaseName.contains("code105CList doesn't exist"))) {
      String userId = TcbsUser.getByUserName(code105CList).getId().toString();
      VsdTransaction.updateStatus(userId, "2");
      TcbsApplicationUser.updateStatusApp(userId, "4", "2");
    }

  }
}




