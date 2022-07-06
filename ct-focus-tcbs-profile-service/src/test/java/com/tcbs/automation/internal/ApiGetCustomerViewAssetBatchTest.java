package com.tcbs.automation.internal;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.cas.TcbsRelation;
import com.tcbs.automation.cas.TcbsUser;
import com.tcbs.automation.cas.TcbsUserAdditionStatus;
import com.tcbs.automation.tools.ConvertUtils;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.*;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.junit.Assert.assertEquals;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/ApiGetCustomerViewAssetBatch.csv", separator = '|')
public class ApiGetCustomerViewAssetBatchTest {

  @Getter
  private String testCaseName;
  private int statusCode;
  private String tcbsIds;
  private String errMess;

  private List<String> gen_tcbsIds = new ArrayList<>();

  @Before
  public void before() {
    List<TcbsUserAdditionStatus> gen_userList = TcbsUserAdditionStatus.getListUserByFundStatus(1);
    for (int i = 0; i < 2; i++) {
      gen_tcbsIds.add(gen_userList.get(i).getTcbsId());
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify Api get customer view asset batch")
  public void apiGetCustomerViewAssetBatch() {
    System.out.println("Test Case: " + testCaseName);

    Gson gson = new Gson();
    LinkedHashMap<String, Object> body = new LinkedHashMap<>();

    if (tcbsIds.equalsIgnoreCase("gen")) {
      body.put("tcbsIds", gen_tcbsIds);
    } else if (tcbsIds.equalsIgnoreCase("genDuplicated")) {
      body.put("tcbsIds", new ArrayList<>(Arrays.asList(gen_tcbsIds.get(0), gen_tcbsIds.get(0))));
    } else if (tcbsIds.isEmpty()) {
      body.put("tcbsIds", null);
    } else {
      if (testCaseName.contains("containing number")) {
        gen_tcbsIds = new ArrayList<>(Arrays.asList(tcbsIds.split(",")));
        body.put("tcbsIds", ConvertUtils.parseStringArrayToInt(gen_tcbsIds));
      } else {
        gen_tcbsIds = new ArrayList<>(Arrays.asList(tcbsIds.split(",")));
        body.put("tcbsIds", gen_tcbsIds);
      }
    }

    Response response = given()
      .baseUri(GET_CUSTOMER_VIEW_ASSET)
      .header("x-api-key", (statusCode == 401 ? FMB_X_API_KEY : API_KEY))
      .contentType("application/json")
      .body(gson.toJson(body))
      .when()
      .post();

    if (response.getStatusCode() == 401 || response.getStatusCode() == 400) {
      assertEquals(String.valueOf(statusCode), response.jsonPath().get("status").toString());
      assertEquals(errMess, response.jsonPath().get("message"));

    } else if (response.getStatusCode() == 200 && tcbsIds.equalsIgnoreCase("gen")) {
      Map<String, Object> itemList = response.jsonPath().getObject("items", new TypeRef<Map<String, Object>>() {
      });
      for (String gen_tcbsId : gen_tcbsIds) {
        TcbsUser user = TcbsUser.getByTcbsId(gen_tcbsId);
        TcbsRelation tcbsRelation = TcbsRelation.getByCustodyCd(user.getUsername());

        //if account does not exist in table TCBS_RELATION but exists in table TCBS_ADDITION_STATUS
        if (tcbsRelation == null) {

        }
      }
    }
  }
}
