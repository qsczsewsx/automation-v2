package com.tcbs.automation.rmrbo;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsRelation;
import com.tcbs.automation.login.LoginApi;
import com.tcbs.automation.login.TheUserInfo;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.GET_CUSTOMER_BY_RBO;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/rmrbo/ApiGetCustomerByRbo.csv", separator = '|')
public class ApiGetCustomerByRboTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String token;
  private String identifyCustodyCd;
  private String pageNumber;
  private String pageSize;

  @Before
  public void before() {
    Actor actor = Actor.named("vietdb");
    LoginApi.withCredentials(identifyCustodyCd, "abc123").performAs(actor);
    token = TheUserInfo.aboutLoginData().answeredBy(actor).getToken();
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify API get customer by RBO")
  public void apiGetCustomerByRbo() {
    System.out.println("Testcase Name : " + testCaseName);

    RequestSpecification requestSpecification = SerenityRest.given()
      .baseUri(GET_CUSTOMER_BY_RBO)
      .header("Authorization", "Bearer " + token)
      .contentType("application/json");

    Response response;
    String PAGE_NUMBER = "pageNumber";
    String PAGE_SIZE = "pageSize";
    String CUSTODY_CD = "identifyCustodyCd";

    if (testCaseName.contains("missing param pageSize")) {
      response = requestSpecification
        .param(PAGE_NUMBER, pageNumber)
        .param(CUSTODY_CD, identifyCustodyCd)
        .get();
    } else if (testCaseName.contains("missing param pageNumber")) {
      response = requestSpecification
        .param(PAGE_SIZE, pageSize)
        .param(CUSTODY_CD, identifyCustodyCd)
        .get();
    } else if (testCaseName.contains("missing param identifyCustodyCd")) {
      response = requestSpecification
        .param(PAGE_SIZE, pageSize)
        .param(PAGE_NUMBER, pageNumber)
        .get();
    } else {
      response = requestSpecification
        .param(PAGE_NUMBER, pageNumber)
        .param(PAGE_SIZE, pageSize)
        .param(CUSTODY_CD, identifyCustodyCd)
        .get();
    }

    assertThat("verify status", response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      if (!testCaseName.contains("greater than")) {
        List<TcbsRelation> rolesDB = TcbsRelation.getByIdentifyCustodyCd(identifyCustodyCd);
        List<HashMap> rolesDBRes = response.jsonPath().getList("data.items");
        assertEquals(rolesDB.size(), rolesDBRes.size());
        for (int i = 0; i < rolesDB.size(); i++) {
          HashMap rRes = rolesDBRes.get(i);
          TcbsRelation rDB = rolesDB.get(i);
          assertEquals(rDB.getTcbsId(), rRes.get("tcbsId").toString());
          assertEquals(rDB.getCustodyCd(), rRes.get("custodyCd").toString());
          assertEquals(rDB.getVipType(), rRes.get("vipType"));
          assertEquals(rDB.getIdentifyType(), rRes.get("identifyType"));
          assertEquals(rDB.getIdentifyCustodyCd(), rRes.get("identifyCustodyCd"));
        }
      }
    } else {
      assertTrue(response.jsonPath().get("message").toString().contains(errorMessage));
    }
  }
}
