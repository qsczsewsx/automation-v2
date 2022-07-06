package com.tcbs.automation.rmrbo;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.R3RdUser;
import io.restassured.response.Response;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static net.serenitybdd.rest.RestRequests.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/rmrbo/ApiSearchRoleRmRbo.csv", separator = '|')
public class ApiSearchRoleRmRboTest {
  private String testCaseName;
  private int statusCode;
  private String custodyCode;
  private String bankCode;
  private String role;
  private String branchCode;
  private String status;

  private String pageSize;

  private String pageNumber;
  private String errorMessage;
  private HashMap<String, Object> params;

  @Before
  public void before() {
    params = new HashMap<>();
    if (StringUtils.isNotEmpty(custodyCode)) {
      params.put("custodyCode", custodyCode);
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
    if (StringUtils.isNotEmpty(pageSize)) {
      params.put("pageSize", pageSize);
    }
    if (StringUtils.isNotEmpty(pageNumber)) {
      params.put("pageNumber", pageNumber);
    }

    System.out.println(params);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify API search role RM RBO")
  public void apiSearchRoleRMRBOTest() {
    System.out.println("Testcase Name : " + testCaseName);

    Response response = given()
      .baseUri(SEARCH_ROLE_RM_RBO)
      .params(params)
      .header("Authorization", "Bearer " + (testCaseName.contains("authorization is invalid") ? FMB_X_API_KEY : RMRBO_CHECKER_AUTHORIZATION_KEY))
      .contentType("application/json").get();

    assertThat(response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      List<R3RdUser> dataDB = R3RdUser.searchByCondition(custodyCode, bankCode, branchCode, role, status);
      List<HashMap> dataRes = response.jsonPath().getList("data.content");
//      assertEquals(dataDB.size(), dataRes.size());
      for (R3RdUser user : dataDB) {
        boolean hasUser = false;
        for (HashMap data : dataRes) {
          if (user.getId().toString().equals(data.get("id").toString())) {
            hasUser = true;
            assertEquals(user.getTcbsId(), data.get("tcbsId"));
            assertEquals(user.getUsername(), data.get("username"));
            assertEquals(user.getCustodyCode(), data.get("custodyCode"));
            assertEquals(user.getFullName(), data.get("fullName"));
            assertEquals(user.getEmail(), data.get("email"));
            assertEquals(user.getBankCode(), data.get("bankCode"));
            assertEquals(user.getBranchCode(), data.get("branchCode"));
            assertEquals(user.getMasterRole(), data.get("masterRole"));
            assertEquals(user.getStatus().toString(), data.get("status").toString());
            break;
          }
        }
        assertTrue(hasUser);
      }
    } else if (statusCode == 403) {
      assertEquals(errorMessage, response.jsonPath().get("errorMessage"));
    } else {
      assertEquals(errorMessage, response.jsonPath().get("message"));
    }
  }
}
