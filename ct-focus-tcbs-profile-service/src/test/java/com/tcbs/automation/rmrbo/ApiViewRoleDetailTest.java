package com.tcbs.automation.rmrbo;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.R3RdUser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/rmrbo/ApiViewRoleDetail.csv", separator = '|')
public class ApiViewRoleDetailTest {

  @Getter
  private String testCaseName;
  private String id;
  private int statusCode;
  private String errorMessage;

@Before
  public void before() {
  if (id.equalsIgnoreCase("gen")) {
    id = R3RdUser.getAllList().get(0).getId().toString();
  } else {
    id = syncData(id);
  }
}

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api get role detail")
  public void apiViewRoleDetail() {
    System.out.println("Testcase Name: " + testCaseName);

    RequestSpecification requestSpecification = SerenityRest.given()
      .baseUri(VIEW_ROLE_DETAIL)
      .header("Authorization", "Bearer " + (testCaseName.contains("authorization is incorrect") ? TCBS_USER_TOKEN : RMRBO_CHECKER_AUTHORIZATION_KEY))
      .contentType("application/json");

    if (!testCaseName.contains("missing Id param")) {
      requestSpecification.param("id", id);
    }
    Response response = requestSpecification
      .when()
      .get();

    assertThat(response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      HashMap<String, Object> roleDetail = response.jsonPath().get("data");
      R3RdUser r3RdUser = R3RdUser.getById(id);
      assertThat(roleDetail.get("tcbsId"), is(r3RdUser.getTcbsId()));
      assertThat(roleDetail.get("custodyCode"), is(r3RdUser.getCustodyCode()));
      assertThat(roleDetail.get("username"), is(r3RdUser.getUsername()));
      assertThat(roleDetail.get("fullName"), is(r3RdUser.getFullName()));
      assertThat(roleDetail.get("email"), is(r3RdUser.getEmail()));
      assertThat(roleDetail.get("bankCode"), is(r3RdUser.getBankCode()));
      assertThat(roleDetail.get("branchCode"), is(r3RdUser.getBranchCode()));
      assertThat(roleDetail.get("status").toString(), is(r3RdUser.getStatus().toString()));
      assertThat(roleDetail.get("masterRole"), is(r3RdUser.getMasterRole()));
    } else if (statusCode == 400) {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }
}