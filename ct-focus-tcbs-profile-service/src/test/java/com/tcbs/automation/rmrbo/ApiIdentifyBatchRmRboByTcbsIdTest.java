package com.tcbs.automation.rmrbo;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.cas.TcbsRelation;
import com.tcbs.automation.cas.TcbsUser;
import com.tcbs.automation.cas.TcbsUserAdditionStatus;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/*
  API Get identification information by multi TCBSID (customer tcbsId)
*/

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/rmrbo/ApiIdentifyBatchRmRboByTcbsId.csv", separator = '|')
public class ApiIdentifyBatchRmRboByTcbsIdTest {

  private final List<String> tcbsIds_asList = new ArrayList<>();
  @Getter
  private String testCaseName;
  private int statusCode;
  private String tcbsIds;
  private String errMess;
  private int items;

  @Before
  public void before() {
    // customer tcbsIds
    if (tcbsIds.equalsIgnoreCase("gen")) {
      List<TcbsRelation> list105Code = TcbsRelation.getByStatus(1);
      for (int i = 0; i < 10; i++) {
        tcbsIds_asList.add(list105Code.get(i).getTcbsId());
      }
    } else {
      tcbsIds_asList.add(tcbsIds);
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Api Get Identification Batch by tcbsid")
  public void apiIdentificationBatchByTcbsId() {
    System.out.println("Test case: " + testCaseName);

    Gson gson = new Gson();
    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    body.put("tcbsIds", tcbsIds.isEmpty() ? null : tcbsIds_asList);

    Response response = given()
      .baseUri(GET_IDENTIFICATION_BATCH_BY_TCBSID)
      .header("x-api-key", testCaseName.contains("invalid x-api-key") ? FMB_X_API_KEY : API_KEY)
      .header("Content-Type", "application/json")
      .body(gson.toJson(body))
      .when()
      .post();

    assertEquals(statusCode, response.getStatusCode());

    if (statusCode == 401) {
      assertEquals(errMess, response.jsonPath().get("message"));
    } else {
      if (items == 0) {
        assertThat(response.jsonPath().get("items"), is(nullValue()));
      } else if (items == 1) {
        //if account does not exist in TCBS_RELATION and TCBS_USER_ADDITION_STATUS
        //Bond and Fund return 1 as default
        assertEquals("1", response.jsonPath().get("items[0].userSetting.viewAssetBond"));
        assertEquals("1", response.jsonPath().get("items[0].userSetting.viewAssetFund"));
      } else {
        verifyDataRelationAndAdditionStatus(response);
      }
    }
  }

  private void verifyDataRelationAndAdditionStatus(Response response) {
    for (String s : tcbsIds_asList) {
      TcbsUser tcbsUser = TcbsUser.getByUserName(s);
      TcbsUserAdditionStatus tcbsAdditionStatus = TcbsUserAdditionStatus.getUserByTcbsId(tcbsUser.getTcbsid());
      TcbsRelation tcbsRelation = TcbsRelation.getByCustodyCd(tcbsUser.getTcbsid());

      //if account does not exist in table TCBS_USER_ADDITION_STATUS
      if (tcbsAdditionStatus == null) {
        assertEquals(0, Integer.parseInt(response.jsonPath().get("totalCount").toString()));
        assertThat(response.jsonPath().get("items"), is(nullValue()));
      }

      //if account does not exist in TCBS_RELATION but exists in TCBS_USER_ADDITION_STATUS
      //Bond and Fund return 1 as default
      else if (tcbsRelation == null) {
        assertEquals("1", response.jsonPath().get("items[:1].userSetting.viewAssetBond"));
        assertEquals("1", response.jsonPath().get("items[:1].userSetting.viewAssetFund"));
      }
    }
  }

}
