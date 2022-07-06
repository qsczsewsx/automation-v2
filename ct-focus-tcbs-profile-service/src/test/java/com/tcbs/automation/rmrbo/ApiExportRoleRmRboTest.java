package com.tcbs.automation.rmrbo;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import io.restassured.response.Response;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static net.serenitybdd.rest.RestRequests.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/rmrbo/ApiExportRoleRmRbo.csv", separator = '|')
public class ApiExportRoleRmRboTest {
  private String testCaseName;
  private int statusCode;
  private String custodyCode;
  private String bankCode;
  private String role;
  private String branchCode;
  private String status;
  private String errorMessage;
  private HashMap<String, Object> params;

  @Before
  public void before() {
    params = new HashMap<>();
    if (StringUtils.isNotEmpty(custodyCode)) {
      params.put("username", custodyCode);
    }
    if (StringUtils.isNotEmpty(bankCode)) {
      params.put("bankCode", bankCode);
    }
    if (StringUtils.isNotEmpty(role)) {
      params.put("role", role);
    }
    if (StringUtils.isNotEmpty(branchCode)) {
      params.put("branchCode", branchCode);
    }
    if (StringUtils.isNotEmpty(status)) {
      params.put("status", status);
    }
    System.out.println(params);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify API Export role RM RBO")
  public void apiExportRoleRMRBOTest() {
    System.out.println("Testcase Name : " + testCaseName);

    Response response = given()
      .baseUri(EXPORT_ROLE_RM_RBO)
      .params(params)
      .header("Authorization", "Bearer " + (testCaseName.contains("authorization is invalid") ? FMB_X_API_KEY : EXPORT_ROLE_RM_RBO_TOKEN))
      .contentType("application/json")
      .get();

    assertThat(response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      Date date = new Date();
      SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
      String strDate= formatter.format(date);
      assertThat(response.header("Content-Disposition"), containsString("export_role_" + strDate + ".xlsx"));


    } else if (statusCode == 403) {
      assertEquals(errorMessage, response.jsonPath().get("errorMessage"));
    }
  }
}
