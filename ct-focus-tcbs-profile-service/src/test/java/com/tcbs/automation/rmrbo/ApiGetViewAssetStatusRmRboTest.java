package com.tcbs.automation.rmrbo;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsRelation;
import com.tcbs.automation.cas.TcbsUser;
import com.tcbs.automation.cas.TcbsUserAdditionStatus;
import com.tcbs.automation.login.LoginApi;
import com.tcbs.automation.login.TheUserInfo;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.screenplay.Actor;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.GET_VIEW_ASSET_STATUS;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/rmrbo/ApiGetViewAssetStatusRmRbo.csv", separator = '|')
public class ApiGetViewAssetStatusRmRboTest {

  @Getter
  private String testCaseName;
  private String tcbsId;
  private int statusCode;
  private String erroMess;
  private String valid_tcbsId;

  private String jwt;

  @Before
  public void before() {
    List<TcbsUserAdditionStatus> userList = TcbsUserAdditionStatus.getListUserByFundStatus(0);
    valid_tcbsId = userList.get(0).getTcbsId();
    String userName = TcbsUser.getByTcbsId(valid_tcbsId).getUsername();

    Actor actor = Actor.named("Customer Login");
    if (testCaseName.contains("Valid TcbsId")) {
      LoginApi.withCredentials(userName, "abc123").performAs(actor);
    } else {
      LoginApi.withCredentials("105C313993", "abc123").performAs(actor);
    }
    jwt = TheUserInfo.aboutLoginData().answeredBy(actor).getToken();
  }


  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api get view asset permission status")
  public void apiGetViewAssetStatusRMRBO() {
    System.out.println("Testcase Name: " + testCaseName);

    Response response = given()
      .baseUri(tcbsId.contains("gen") ? GET_VIEW_ASSET_STATUS.replace("#tcbsId#", valid_tcbsId) : GET_VIEW_ASSET_STATUS.replace("#tcbsId#", tcbsId))
      .header("Authorization", "Bearer " + jwt)
      .param("fields", "userSetting")
      .when()
      .get();

    assertThat(response.getStatusCode(), is(statusCode));
    System.out.println("Actual Status Code: " + response.getStatusCode());

    if (statusCode == 200 && tcbsId.equalsIgnoreCase("gen")) {
      TcbsUserAdditionStatus tcbsUserAdditionStatus = TcbsUserAdditionStatus.getUserByTcbsId(valid_tcbsId);
      TcbsRelation tcbsRelation = TcbsRelation.getByCustodyCd(TcbsUser.getByTcbsId(valid_tcbsId).getUsername());

      assertEquals(tcbsUserAdditionStatus.getViewAssetFundStatus().toString(), response.jsonPath().get("userSetting.viewAssetFund").toString());
      assertEquals(tcbsUserAdditionStatus.getViewAssetBondStatus().toString(), response.jsonPath().get("userSetting.viewAssetBond").toString());

      //If customer share view for both Fund and Bond
      if (Integer.parseInt(response.jsonPath().get("userSetting.viewAssetBond").toString()) == 1 && Integer.parseInt(response.jsonPath().get("userSetting.viewAssetFund").toString()) == 1) {
        assertEquals(true, response.jsonPath().get("userSetting.hasRm"));
        assertThat(response.jsonPath().get("userSetting.rmName"), is(notNullValue()));

        //if customer does not share his asset view
      } else if (Integer.parseInt(response.jsonPath().get("userSetting.viewAssetBond").toString()) == 0 && Integer.parseInt(response.jsonPath().get("userSetting.viewAssetFund").toString()) == 0) {

        //customer has RM/RBO or account not found in Tcbs_relation
        if (tcbsRelation.getIdentifyCustodyCd() != null) {
          assertEquals(true, response.jsonPath().get("userSetting.hasRm"));
          TcbsUser tcbsUser = TcbsUser.getByUserName(tcbsRelation.getIdentifyCustodyCd());
          String fullName = tcbsUser.getLastname() + " " + tcbsUser.getFirstname();
          assertEquals(response.jsonPath().get("userSetting.rmName"), fullName);

          //customer does NOt have RM/RBO
        } else {
          assertThat(response.jsonPath().get("userSetting.rmName"), is(nullValue()));
          assertEquals(false, response.jsonPath().get("userSetting.hasRm"));
        }
      }
    } else if (statusCode == 400) {
      assertEquals(erroMess, response.jsonPath().get("message"));
    }
  }
}
