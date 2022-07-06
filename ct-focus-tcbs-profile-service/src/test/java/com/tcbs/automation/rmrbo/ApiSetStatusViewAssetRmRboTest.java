package com.tcbs.automation.rmrbo;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsUserAdditionStatus;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.SET_VIEW_ASSET_STATUS;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.TCBS_USER_TOKEN;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/rmrbo/ApiSetStatusViewAssetRmRbo.csv", separator = '|')
public class ApiSetStatusViewAssetRmRboTest {

  @Getter
  private String testCaseName;
  private String viewAssetFund;
  private String viewAssetBond;
  private String actor;
  private int statusCode;
  private String erroMess;
  private String tcbsId;

  @Before
  public void before() {
    viewAssetFund = syncData(viewAssetFund);
    viewAssetBond = syncData(viewAssetBond);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api set status view asset permission")
  public void apiSetStatusViewAssetRMRBO() {
    System.out.println("Testcase Name: " + testCaseName);

    LinkedHashMap<String, Object> body = new LinkedHashMap<>();

    body.put("viewAssetBond", StringUtils.isNumeric(viewAssetBond) ?
      Integer.valueOf(viewAssetBond) : viewAssetBond);
    body.put("viewAssetFund", StringUtils.isNumeric(viewAssetFund) ?
      Integer.valueOf(viewAssetFund) : viewAssetFund);

    Response response = given()
      .baseUri(SET_VIEW_ASSET_STATUS.replace("#tcbsId", tcbsId))
      .header("Authorization", "Bearer " + TCBS_USER_TOKEN)
      .contentType("application/json")
      .body(body)
      .when()
      .post();

    assertThat(response.getStatusCode(), is(statusCode));
    System.out.println("Actual Status Code: " + response.getStatusCode());

    if (statusCode == 400) {
      assertEquals(erroMess, response.jsonPath().get("message"));
    } else if (statusCode == 200) {
      assertEquals("200", response.jsonPath().get("code"));
      assertThat(response.jsonPath().get("data"), is(true));

      //check invalid viewAssetFund/viewAssetBond cases returning status code 200 but database should NOT be updated with invalid value
      //getting data from database to validate
      if (testCaseName.contains("more than 1 number digit") || testCaseName.contains("containing negative number")) {
        TcbsUserAdditionStatus tcbsUserAdditionStatus = TcbsUserAdditionStatus.getUserByTcbsId(tcbsId);
        assertThat(tcbsUserAdditionStatus.getViewAssetFundStatus(), anyOf(is(new BigDecimal(1)), is(new BigDecimal(0))));
        assertThat(tcbsUserAdditionStatus.getViewAssetBondStatus(), anyOf(is(new BigDecimal(1)), is(new BigDecimal(0))));

      }
    }
  }
}
