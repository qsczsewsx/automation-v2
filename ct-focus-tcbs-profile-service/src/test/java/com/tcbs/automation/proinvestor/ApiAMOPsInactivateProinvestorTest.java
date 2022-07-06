package com.tcbs.automation.proinvestor;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsProInvestorDocument;
import com.tcbs.automation.cas.TcbsUser;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/ApiAMOPsInactivateProinvestor.csv", separator = '|')
public class ApiAMOPsInactivateProinvestorTest {

  @Getter
  private String testCaseName;

  private String username;
  private BigDecimal generated_proinvestorUserId;
  private String generated_105code;
  private String reasonRemove;
  private String reason;
  private String actorDeleting;
  private int statusCode;
  private String erroMsg;

  public void before() {
    username = syncData(username);
    reasonRemove = syncData(reasonRemove);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api AMOPs inactivates Proinvestor")
  public void verifyApiAMOPsDeactivateProinvestorTest() throws InterruptedException {
    System.out.println("testCaseName: " + testCaseName);

    LinkedHashMap<String, Object> body = new LinkedHashMap<>();

    if (username.equalsIgnoreCase("autoGenerate")) {

      generated_proinvestorUserId = TcbsProInvestorDocument.getListOfProInvestorByStatus("ACTIVE").get(0).getUserId();
      generated_105code = TcbsUser.getById(generated_proinvestorUserId).getUsername();
      body.put("username", generated_105code);
      body.put("reasonRemove", reasonRemove);

    } else {

      body.put("username", username.isEmpty() ? null : username);
      body.put("reasonRemove", StringUtils.isNumeric(reasonRemove) ?
        Integer.valueOf(reasonRemove) : reasonRemove);
    }


    Response response = given()
      .baseUri(AMOPS_DEACTIVATE_PRO_INVESTOR)
      .header("Authorization", "Bearer " +
        (testCaseName.contains("user has no permission") ? TCBSPROFILE_AUTHORIZATION : ASSIGN_TASK_TO_AMOPS_MAKER))
      .contentType("application/json")
      .body(body)
      .when()
      .put();

    assertEquals(statusCode, response.getStatusCode());

    if (response.getStatusCode() == 400) {
      System.out.println("Actual Message: " + response.jsonPath().get("message"));
    }


    if (response.getStatusCode() == 200) {

      TcbsProInvestorDocument proinvestorDocument = TcbsProInvestorDocument.getProinvestorByUserId(String.valueOf(generated_proinvestorUserId));
      assertEquals("INACTIVE", proinvestorDocument.getStatus());
      assertEquals(reasonRemove, proinvestorDocument.getReason());
      assertThat(response.jsonPath().get("code"), is("200"));
      assertThat(response.jsonPath().get("message"), is(erroMsg));

    } else if (response.getStatusCode() == 400) {

      String actualMes = response.jsonPath().get("message");
      assertEquals(erroMsg, actualMes);

    } else if (response.getStatusCode() == 403) {

      String actualMes = response.jsonPath().get("errorMessage");
      assertEquals(erroMsg, actualMes);
    }
  }
}
