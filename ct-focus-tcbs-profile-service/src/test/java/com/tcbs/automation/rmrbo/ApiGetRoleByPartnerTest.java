package com.tcbs.automation.rmrbo;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.R3RdRole;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static net.serenitybdd.rest.RestRequests.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/rmrbo/ApiGetRoleByPartner.csv", separator = '|')
public class ApiGetRoleByPartnerTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;

  private String bankCode;

  private String uri;

  @Before
  public void before() {
    uri = GET_ROLE_BY_PARTNER;
    if (!testCaseName.contains("bankCode is missing")) {
      uri += "?bankCode=" + bankCode;
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify API get all role by partner")
  public void apiGetRoleByPartner() {
    System.out.println("Testcase Name : " + testCaseName);

    Response response = given()
      .header("Authorization", "Bearer " + (testCaseName.contains("incorrect Authorization") ? FMB_X_API_KEY : RMRBO_CHECKER_AUTHORIZATION_KEY))
      .contentType("application/json")
      .get(uri);
    assertThat(response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      List<R3RdRole> rolesDB = R3RdRole.getRoleByPartner(bankCode);
      List<HashMap> rolesDBRes = response.jsonPath().getList("");
      assertEquals(rolesDB.size(), rolesDBRes.size());
      for (int i=0; i<rolesDB.size(); i++) {
        HashMap rRes = rolesDBRes.get(i);
        R3RdRole rDB = rolesDB.get(i);
        assertEquals(rDB.getId().toString(), rRes.get("id").toString());
        assertEquals(rDB.getStatus().toString(), rRes.get("status").toString());
        assertEquals(rDB.getTitle(), rRes.get("title"));
        assertEquals(rDB.getRole(), rRes.get("role"));
      }
    } else if (statusCode == 403) {
      assertTrue(response.jsonPath().get("errorMessage").toString().contains(errorMessage));
    } else {
      assertTrue(response.jsonPath().get("message").toString().contains(errorMessage));
    }
  }
}
