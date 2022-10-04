package com.tcbs.automation.bauTool;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsBankIaaccount;
import com.tcbs.automation.cas.TcbsUser;
import common.CommonUtils;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.rest.RestRequests;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/bauTool/ApiSearchBankIaInfo.csv", separator = ',')
public class ApiSearchBankIaInfoTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String actionKey;
  private String username;
  private HashMap<String, Object> params;

  @Before
  public void before() {
    params = new HashMap<>();
    if (!testCaseName.contains("missing param")) {
      params.put("actionKey", actionKey);
      params.put("username", username);
    }
    System.out.println(params);
  }


  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api search bank ia info")
  public void verifySearchBankIaInfoTest() {
    System.out.println("TestcaseName: " + testCaseName);

    Response response = RestRequests.given()
      .baseUri(BAU_SEARCH_BANK_IA_INFO)
      .params(params)
      .header("Authorization", "Bearer " +
        (testCaseName.contains("authorization is invalid") ? TCBSPROFILE_AUTHORIZATION : BAU_AUTHORIZATION_TOKEN))
      .contentType("application/json").get();

    Assert.assertThat(response.getStatusCode(), is(statusCode));
    if (statusCode == 200) {
      List<TcbsBankIaaccount> dataDB = TcbsBankIaaccount.getListBanks(TcbsUser.getByUserName(username).getId().toString());
      List<HashMap> dataRes = response.jsonPath().getList("data");
      for (TcbsBankIaaccount bankIa : dataDB) {
        boolean hasBankIa = false;
        for (HashMap data : dataRes) {
          if (bankIa.getId().toString().equals(data.get("id").toString())) {
            hasBankIa = true;
            assertEquals(bankIa.getAccountNo(), data.get("bankAccountNo"));
            assertEquals(bankIa.getAccountName(), data.get("bankAccountName"));
            assertEquals(bankIa.getBankSource(), data.get("bankSource"));
            assertEquals(CommonUtils.getStatusBankIA(bankIa.getStatus().toString()), data.get("status"));
            assertEquals(bankIa.getAutoTransfer(), data.get("autoTransfer"));
            assertEquals(bankIa.getIsIaPaid(), data.get("isIaPaid"));
            break;
          }
        }
        assertTrue(hasBankIa);
      }
    } else if (statusCode == 403) {
      assertEquals(errorMessage, response.jsonPath().get("errorMessage"));
    } else {
      assertEquals(errorMessage, response.jsonPath().get("message"));
    }
  }
}